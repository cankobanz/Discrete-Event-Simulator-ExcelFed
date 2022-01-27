import java.util.Comparator;
//Event comparator for massage queue.
public class EventMassageComparator implements Comparator<Event>{
    public int compare(Event e1, Event e2) {
        if (e1.getSkill() < e2.getSkill())
            return 1;
        else if (e1.getSkill() > e2.getSkill())
            return -1;
        else{ //If equal skill level, FIFO
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
        }
        return 0;
    }
}

