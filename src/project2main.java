import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


public class project2main {
    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer,Player> playerMap=new HashMap<>();
        PriorityQueue<Event> eventQueue=new PriorityQueue<>(new EventComparator()); //Priority queue for all events

        //Queues for training, physiotherapy and massage. They all have different event comparator
        PriorityQueue<Event> trainingQueue =new PriorityQueue<>(new EventTrainingComparator());
        PriorityQueue<Event> physioQueue =new PriorityQueue<>(new EventPhysioComparator());
        PriorityQueue<Event> massageQueue =new PriorityQueue<>(new EventMassageComparator());



        Locale.setDefault(new Locale("en", "US"));
        NumberFormat formatter = new DecimalFormat("#0.000");


        Simulator simulator= new Simulator();

        File inputFile=new File("D:\\Kullanıcılar\\canko\\OneDrive\\Masaüstü\\testcases\\input4.txt");

            Scanner scan = new Scanner(inputFile);

            //Player reading
            final int numOfPlayers=scan.nextInt();

            for (int i = 0; i < numOfPlayers; i++) {

                final Player player = new Player(scan.nextInt(),scan.nextInt());
                playerMap.put(i,player);
            }

            Statistics statistics=new Statistics(numOfPlayers);

            //Event Reading
            int numOfArrivalEvents=scan.nextInt();
            scan.nextLine();

            for (int i = 0; i < numOfArrivalEvents; i++) {
                String readLine= scan.nextLine();
                String[] lineArray=readLine.split(" ");

                char type=lineArray[0].charAt(0);
                int id=Integer.parseInt(lineArray[1]);
                double arrivalTime=Double.parseDouble(lineArray[2]);
                double duration=Double.parseDouble(lineArray[3]);
                Player player=playerMap.get(id);
                int skill=player.getSkill();

                Event arrivalEvent=new Event(type,id,arrivalTime,duration,skill);
                eventQueue.add(arrivalEvent);
            }


            //Creating Physiotherapists where service time is setting with the given input.
            String readLine= scan.nextLine();
            String[] lineArray=readLine.split(" ");
            int numOfPhysiT=Integer.parseInt(lineArray[0]);
            Physiotherapist[] physiotherapists=new Physiotherapist[numOfPhysiT];

        for (int i = 0; i < numOfPhysiT; i++) {
                Physiotherapist physiotherapist=new Physiotherapist(i);

                double serviceTimePhysT= Double.parseDouble(lineArray[i+1]);
                physiotherapist.setServiceTime(serviceTimePhysT);
                physiotherapists[i]=physiotherapist;
            }

        //Creating Training Coaches with service time of -1. This will be updated later.
            readLine= scan.nextLine();
            lineArray=readLine.split(" ");

            int numofTrainCoach=Integer.parseInt(lineArray[0]);
            TrainCoach[] trainingCoaches=new TrainCoach[numofTrainCoach];

            for (int i = 0; i < numofTrainCoach; i++) {
                TrainCoach trainCoach=new TrainCoach(i);
                trainingCoaches[i]=trainCoach;
            }

            //Creating Masseurs with service time of -1. This will be updated later.
            int numofMasseur=Integer.parseInt(lineArray[1]);
            Masseur[] masseurs=new Masseur[numofMasseur];

            for (int i = 0; i < numofMasseur; i++) {
                Masseur masseur=new Masseur(i);
                masseurs[i]=masseur;
            }
            scan.close();
            //Taking Input Part is finished.


        /*Simulation starts.Event types according to char:
        * 't':Training Arrival
        * 'r':Training Departure
        * 'p':Physiotherapy Arrival
        * 'h':Physiotherapy Departure
        * 'm':Massage Arrival
        * 'a':Massage Departure
        * */
            while(!eventQueue.isEmpty()) {

                Event currentEvent;
                currentEvent = eventQueue.poll();
                char eventType = currentEvent.getType();

                int id=currentEvent.getPlayerID();
                Player player = playerMap.get(id);


                statistics.setGlobalTime(currentEvent.getHappeningTime());

                /*If player is not busy, player become busy.
                * If there is training couch, one of them is chosen and training departure event is created.
                * Departure event is added to base eventQueue.
                * If there is not training couch, currentEvent added to training queue.
                * */
                if (eventType == 't') {

                    if(player.isBusy()){
                        statistics.incCanceledAttempts();
                        continue;
                    }
                    else{
                        player.makeBusy();
                    }

                    statistics.incSuccessfulT(); //Only successful training events can be added. Attention: Whether player goes to queue or direct to training he or she counted as successful
                    statistics.setTotalTrainingTime(currentEvent);

                    boolean thereTC = simulator.isThereTC(trainingCoaches);
                    if (thereTC) {
                        simulator.makeTCbusy(trainingCoaches);

                        double departureTime = currentEvent.getHappeningTime() + currentEvent.getDuration();
//                        r is for departure event of training remembering it from second letter of t(r)aining
                        Event departureTrain = new Event('r', currentEvent.getPlayerID(), departureTime, currentEvent.getDuration(),player.getSkill());
                        eventQueue.add(departureTrain);
                    } else {
                        trainingQueue.add(currentEvent);
                        statistics.maxLenQueue(trainingQueue);
                    }
                }

                /*Departure of training starts with making idle one of the training coaches.
                * Departure of training event means creation of physiotherapy event for the same player.
                * Physiotherapy arrival is created and added to eventQueue.
                * If there is a waiting player in the physiotherapy queue, it can be polled instead of departed player.
                * Departure event for event that is polled from physiotherapy queue is created and added to eventQueue.
                * Attention: Player is not made unbusy because he or she directly goes to physiotherapy event.
                * */
                else if (eventType == 'r' ) {

                    simulator.makeTCidle(trainingCoaches);

                    Event arrivalPhysioEvent = new Event('p', currentEvent.getPlayerID(), currentEvent.getHappeningTime(), currentEvent.getDuration(),player.getSkill());//duration depends on physiotherapist
                    eventQueue.add(arrivalPhysioEvent);

                    if(!trainingQueue.isEmpty()) {
                        Event nextEvent = trainingQueue.poll();   //next event type: t

                        simulator.makeTCbusy(trainingCoaches);

                        double departureTime = currentEvent.getHappeningTime() + nextEvent.getDuration(); //Finished event time + next event duration
                        Player nextPlayer=playerMap.get(nextEvent.getPlayerID());

                        Event departureTrain = new Event('r', nextEvent.getPlayerID(), departureTime, nextEvent.getDuration(),nextPlayer.getSkill());
                        eventQueue.add(departureTrain);

                        statistics.setTotalWaitingT(currentEvent, nextEvent);
                    }
                }

                /*Physiotherapy event starts with controlling available physiotherapists.
                * If there is one, it is assigned to the player with id number.
                * Duration of event is updated with related service time of physiotherapist.
                * Departure event for physiotherapy is created and added to eventQueue.
                * If there is no available physiotherapist, event is added to physiotherapy queue.
                * Attention: Cancelled attempt is not checked because player directly comes from training.
                * */
                else if (eventType == 'p' ) {

                    statistics.incSuccessfulP();//Only successful physiotherapy events can be added

                    boolean therePT = simulator.isTherePT(physiotherapists);
                    if (therePT) {
                        int physiotherapistId = simulator.makePTbusy(physiotherapists); //Return the service time of related PT.
                        double serviceTime=physiotherapists[physiotherapistId].getServiceTime();
                        currentEvent.setDuration(serviceTime);
                        player.setPhysiotherapistId(physiotherapistId);

                        statistics.setTotalPhysioTime(currentEvent);

                        double departureTime = currentEvent.departureTimer();

                        Event departurePhysio = new Event('h', currentEvent.getPlayerID(), departureTime, currentEvent.getDuration(),player.getSkill());
                        eventQueue.add(departurePhysio);
                    } else {
                        physioQueue.add(currentEvent); //Service time of the physiotherapist has not assigned yet
                        statistics.maxLenQueue(physioQueue);
                    }
                }

                /* Departure of physiotherapy starts with making player unbusy.
                 * Then, making related physiotherapist idle.
                 * If physiotherapy queue is not empty, nextEvent is polled from physiotherapy queue.
                 * Then same steps are actioned for physiotherapy event.
                 * Departure of nextEvent is created and added to eventQueue.
                 * */
                else if (eventType == 'h') {
                    player.makeUnbusy();
                    int physiotherapistId=player.getPhysiotherapistId();
                    simulator.makePTidle(physiotherapists,physiotherapistId);

                    if(!physioQueue.isEmpty()){
                        Event nextEvent = physioQueue.poll();   //next event type: p

                        int ptId = simulator.makePTbusy(physiotherapists); //Return the service time of related PT.
                        double serviceTime=physiotherapists[ptId].getServiceTime();
                        currentEvent.setDuration(serviceTime);

                        nextEvent.setDuration(serviceTime); //set nextEvent duration with service time of related PT

                        statistics.setTotalPhysioTime(nextEvent);

                        double departureTime = currentEvent.getHappeningTime() + nextEvent.getDuration(); //Finished event time + next event duration
                        Player nextPlayer=playerMap.get(nextEvent.getPlayerID());
                        nextPlayer.setPhysiotherapistId(ptId);

                        Event departurePhysio = new Event('h', nextEvent.getPlayerID(), departureTime, nextEvent.getDuration(),nextPlayer.getSkill()); //goes with duration of service time of related PT
                        eventQueue.add(departurePhysio);

                        statistics.setTotalWaitingP(currentEvent,nextEvent);
                        statistics.setMostWaitedPlayerP(currentEvent,nextEvent,nextPlayer);

                    }
                }

                /*To start massage event, player should not be busy or attempt more than 3.
                * First three if() checks these situations. If they are passed, player become busy.
                * If there is available masseur, make one of them busy.
                * Then, create departure event for massage and add it to eventQueue.
                * If there is no available masseur, create special massage event and add it to massage queue.
                * Massage event is different from normal event because it includes skill level of player.
                * */
                else if(eventType=='m'){

                    int temp=player.getAttemptToMassage();
                    temp++;
                    if(player.isBusy() && temp>3){
                        statistics.incInvalidAttempts();
                        continue;
                    }
                    else if(player.isBusy() && !(temp>3)){
                        statistics.incCanceledAttempts();
                        continue;
                    }
                    else if(!player.isBusy()&& temp>3 ){
                        statistics.incInvalidAttempts();
                        continue;
                    }

                    player.incAttemptToMassage();
                    player.makeBusy();

                    statistics.incSuccessfulM(); //Only successful massage events can be added
                    statistics.setTotalMassageTime(currentEvent);

                    boolean thereM = simulator.isThereM(masseurs);
                    if (thereM) {
                        simulator.makeMbusy(masseurs);

                        double departureTime = currentEvent.getHappeningTime() + currentEvent.getDuration();

                        //a is for departure event of massage remembering it from second letter of m(a)ssage
                        Event departureMassage = new Event('a', currentEvent.getPlayerID(), departureTime, currentEvent.getDuration(),player.getSkill());
                        eventQueue.add(departureMassage);
                    } else {
                        char type=currentEvent.getType();
                        double arrivalTime=currentEvent.getHappeningTime();
                        double duration=currentEvent.getDuration();
                        int skill=playerMap.get(id).getSkill();

                        Event massageEvent=new Event(type,id,arrivalTime,duration,skill);
                        massageQueue.add(massageEvent);

                        statistics.maxLenQueue(massageQueue);
                    }
                }

                /* Departure of massage event starts with making player unbusy and masseur idle.
                * If there is event in massage queue poll it and make one of the masseurs busy.
                * Create departure event for polled event.
                * */
                else if (eventType=='a'){
                    player.makeUnbusy();
                    simulator.makeMidle(masseurs);

                    if(!massageQueue.isEmpty()) {
                        Event nextEvent = massageQueue.poll();   //next event type: m

                        simulator.makeMbusy(masseurs);

                        double departureTime = currentEvent.getHappeningTime() + nextEvent.getDuration(); //Finished event time + next event duration
                        Player nextPlayer=playerMap.get(nextEvent.getPlayerID());

                        Event departureMassage = new Event('a', nextEvent.getPlayerID(), departureTime, nextEvent.getDuration(),nextPlayer.getSkill());
                        eventQueue.add(departureMassage);

                        statistics.setTotalWaitingM(currentEvent,nextEvent);
                        statistics.setLeastWaitedPlayerM(currentEvent,nextEvent,nextPlayer);
                    }
                }
            }
            statistics.findThreeAttemptedIds(playerMap);
        //Simulation finishes

        //Output file writing
        File outputFile=new File("D:\\Kullanıcılar\\canko\\OneDrive\\Masaüstü\\output.txt");
        try {
            FileWriter writer=new FileWriter(outputFile);
            PrintWriter output=new PrintWriter(writer);

            output.write(statistics.getMaxLenTrainQ()+"\n");
            output.write(statistics.getMaxLenPhysioQ()+"\n");
            output.write(statistics.getMaxLenMassageQ()+"\n");

            output.write(formatter.format(statistics.getAverageWaitingT())+"\n");
            output.write(formatter.format(statistics.getAverageWaitingP())+"\n");
            output.write(formatter.format(statistics.getAverageWaitingM())+"\n");

            output.write(formatter.format(statistics.getAverageTrainingTime())+"\n");
            output.write(formatter.format(statistics.getAveragePhysioTime())+"\n");
            output.write(formatter.format(statistics.getAverageMassageTime())+"\n");
            output.write(formatter.format(statistics.getAverageTurnAround())+"\n");

            output.write(statistics.getIdMostWaitingPlayerP()+" "+formatter.format(statistics.getMostWaitingTimeP())+"\n");
            output.write(statistics.getIdLeastWaitingPlayerM()+" "+formatter.format(statistics.getLeastWaitingPlayerM())+"\n");

            output.write(statistics.getInvalidAttempts()+"\n");
            output.write(statistics.getCanceledAttempts()+"\n");
            output.write(formatter.format(statistics.getGlobalTime())+"\n");

            output.close();

        } catch (IOException e){
            System.out.println("Catch - An error occurred.");
            e.printStackTrace();
        }
    }
}
