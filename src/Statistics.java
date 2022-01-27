import java.util.*;

public class Statistics {

    private int maxLenTrainQ=0;
    private int maxLenPhysioQ=0;
    private int maxLenMassageQ=0;

    private double totalWaitingT=0;
    private int successfulT =0;
    private double totalTrainingTime=0;

    private double totalWaitingP=0;
    private int successfulP =0;
    private double totalPhysioTime=0;
    private double[] waitingSumsP;

    private double totalWaitingM=0;
    private int successfulM =0;
    private double totalMassageTime=0;
    private double[] waitingSumsM;

    private int[] specialIds;


    private int invalidAttempts=0; //For massage
    private int canceledAttempts=0; //For Schrodinger's Cat

    private double globalTime=0.000;

    public Statistics(int numOfPlayers) {
        this.waitingSumsP = new double[numOfPlayers];
        this.waitingSumsM=new double[numOfPlayers];
        this.specialIds=new int[numOfPlayers];
    }

    //Maximum Length Methods
    public void maxLenQueue(Queue<Event> queue){
        int temp;
        temp=queue.size();
        assert queue.peek() != null;
        char queueType=queue.peek().getType();
        if(maxLenTrainQ<temp && queueType=='t') {
            maxLenTrainQ = temp;
        }
        else if(maxLenPhysioQ<temp && queueType=='p') {
            maxLenPhysioQ = temp;
        }
        else if(maxLenMassageQ<temp && queueType=='m') {
            maxLenMassageQ = temp;
        }
    }

    public int getMaxLenTrainQ() {
        return maxLenTrainQ;
    }

    public int getMaxLenPhysioQ() {
        return maxLenPhysioQ;
    }

    public int getMaxLenMassageQ() {
        return maxLenMassageQ;
    }

    //Average Waiting Time Methods
    //Training Event
    public void setTotalWaitingT(Event currentEvent, Event nextEvent){ //This method can be applied to all
        double  waitingTime=currentEvent.getHappeningTime()- nextEvent.getHappeningTime();  //Departure time of the event-Waiting event in the queue arrival time to the queue
        totalWaitingT+=waitingTime;
    }

    public void incSuccessfulT(){
        successfulT++;
    }
    public double getAverageWaitingT(){
        if(totalWaitingT==0) {
            return 0;
        }
        return totalWaitingT/ successfulT;
    }

    public void setTotalTrainingTime(Event event){ //This method can be applied to all
        totalTrainingTime+=event.getDuration();
    }
    public double getAverageTrainingTime(){
        if(totalTrainingTime==0) {
            return 0;
        }
        return totalTrainingTime/ successfulT;
    }

    //Physiotherapy Event
    public void setTotalWaitingP(Event currentEvent, Event nextEvent){ //This method can be applied to all
        double  waitingTime=currentEvent.getHappeningTime()- nextEvent.getHappeningTime();  //Departure time of the event-Waiting event in the queue arrival time to the queue

        totalWaitingP+=waitingTime;
    }

    public void incSuccessfulP(){
        successfulP++;
    }
    public double getAverageWaitingP(){
        if(totalWaitingP==0) {
            return 0;
        }
        return totalWaitingP/ successfulP;
    }
    public void setTotalPhysioTime(Event event){ //This method can be applied to all
        totalPhysioTime+=event.getDuration();
    }
    public double getAveragePhysioTime(){
        if(totalPhysioTime==0) {
            return 0;
        }
        return totalPhysioTime / successfulP;
    }
    public void setMostWaitedPlayerP(Event currentEvent,Event nextEvent,Player player){

        int id=player.getId();
        double  waitingTime=currentEvent.getHappeningTime()- nextEvent.getHappeningTime();

        waitingSumsP[id]+=waitingTime;
    }

    public int getIdMostWaitingPlayerP() {

        return findMostWaitedPlayerP();
    }

    public double getMostWaitingTimeP() {
        return waitingSumsP[findMostWaitedPlayerP()];
    }

    private int findMostWaitedPlayerP(){
        double maxWait=0;
        int maxId=0;
        boolean flag=true;

        for (int i = 0; i < waitingSumsP.length; i++) {
            if(waitingSumsP[i]>maxWait){
                maxWait=waitingSumsP[i];
                maxId=i;
                flag=false;
            }
        }
        if (flag){  //If no one waited in the queue, return the lower id with zero waiting.
            for (int i = 0; i < waitingSumsP.length; i++) {
                if(waitingSumsP[i]==0){
                    maxId=i;
                    break;
                }
            }
        }
        return maxId;
    }

    //Massage Event
    public void setTotalWaitingM(Event currentEvent, Event nextEvent){ //This method can be applied to all
        double  waitingTime=currentEvent.getHappeningTime()- nextEvent.getHappeningTime();  //Departure time of the event-Waiting event in the queue arrival time to the queue
        totalWaitingM+=waitingTime;
    }

    public void setLeastWaitedPlayerM(Event currentEvent,Event nextEvent,Player player) {

        int id = player.getId();
        double waitingTime = currentEvent.getHappeningTime() - nextEvent.getHappeningTime();

        waitingSumsM[id] += waitingTime;
    }

    public void findThreeAttemptedIds(Map<Integer,Player> playerMap){
        for (Player player : playerMap.values()) {
            if(player.getAttemptToMassage()==3){
                int id=player.getId();
                specialIds[id]++;
            }
        }

    }

    private int findLeastWaitedPlayerM(){
        double leastWait=Double.MAX_VALUE;
        int leastId=0;
        boolean flag=true;

        for (int i = 0; i < specialIds.length; i++) {
            if(specialIds[i]==1){
                if(waitingSumsM[i]<leastWait){
                    leastWait=waitingSumsM[i];
                    leastId=i;
                    flag=false;
                }
            }
        }
        if (flag){  //If no one takes 3 massage service, return -1.
            waitingSumsM[0]=-1;
            return -1;
        }
        return leastId;
    }
    public int getIdLeastWaitingPlayerM(){
        return findLeastWaitedPlayerM();
    }
    public double getLeastWaitingPlayerM(){
        int id=findLeastWaitedPlayerM();
        if(id==-1){
            return -1;
        }
        else return waitingSumsM[id];
    }

    public void incSuccessfulM(){
        successfulM++;
    }
    public double getAverageWaitingM(){
        if(totalWaitingM==0) {
            return 0;
        }
        return totalWaitingM/ successfulM;
    }
    public void setTotalMassageTime(Event event){ //This method can be applied to all
        totalMassageTime+=event.getDuration();
    }
    public double getAverageMassageTime(){
        if(totalMassageTime==0) {
            return 0;
        }
        return totalMassageTime/ successfulM;
    }

    //Turn Around Time
    public double getAverageTurnAround() {
        double totalTurnAround= totalTrainingTime+totalWaitingT+totalPhysioTime+totalWaitingP;
        return totalTurnAround/successfulT;
    }

    //Invalid and Cancelled Attempts
    public void incInvalidAttempts() {
        invalidAttempts++;
    }

    public int getInvalidAttempts() {
        return invalidAttempts;
    }
    public void incCanceledAttempts() {
        canceledAttempts++;
    }
    public int getCanceledAttempts() {
        return canceledAttempts;
    }
    //Global Time
    public void setGlobalTime(double globalTime) {
        this.globalTime = globalTime;
    }
    public double getGlobalTime() {
        return globalTime;
    }
}
