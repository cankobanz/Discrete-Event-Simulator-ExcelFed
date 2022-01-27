import java.util.Comparator;
//Event comparator for training queue.
public class EventTrainingComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        if (e1.getHappeningTime() < e2.getHappeningTime())
            return -1;
        else if (e1.getHappeningTime() > e2.getHappeningTime())
            return 1;
        else{       //If equal FIFO, lower ID gets priority
            if (e1.getPlayerID() < e2.getPlayerID())
                return -1;
            else if (e1.getPlayerID() > e2.getPlayerID())
                return 1;
        }
        return 0;
    }
}

