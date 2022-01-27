import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        if (e1.getHappeningTime() < e2.getHappeningTime())
            return -1;
        else if (e1.getHappeningTime() > e2.getHappeningTime())
            return 1;
        else{//If two event are arrived at the same time, prioritize according to event types.
            if(e1.getType()==e2.getType()) {
                if (e1.getType() == 't') { //If two training event are arrived at the same time, check playerID.
                    if (e1.getPlayerID() < e2.getPlayerID())
                        return -1;
                    else if (e1.getPlayerID() > e2.getPlayerID())
                        return 1;
                }
                else if(e1.getType()=='p'){//If two physiotherapy event are arrived at the same time, check duration first and then player id.
                    if (e1.getDuration() < e2.getDuration())
                        return 1;
                    else if (e1.getDuration() > e2.getDuration())
                        return -1;
                    else{
                        if (e1.getPlayerID() < e2.getPlayerID())
                            return -1;
                        else if (e1.getPlayerID() > e2.getPlayerID())
                            return 1;
                    }
                }
                else if (e1.getType()=='m'){//If two massage event are arrived at the same time, check skill first then player id.
                    if (e1.getSkill() < e2.getSkill())
                        return 1;
                    else if (e1.getSkill() > e2.getSkill())
                        return -1;
                    else{
                        if (e1.getPlayerID() < e2.getPlayerID())
                            return -1;
                        else if (e1.getPlayerID() > e2.getPlayerID())
                            return 1;
                    }
                }
                else{
                    if (e1.getPlayerID() < e2.getPlayerID())
                        return -1;
                    else if (e1.getPlayerID() > e2.getPlayerID())
                        return 1;
                }
            }
            //This condition provides if two event physiotherapy and massage event arrives, physiotherapy event occur first because player should be in physiotherapy.
            else if((e1.getType()=='p' && e2.getType()=='m') ||(e2.getType()=='p' && e1.getType()=='m')){
                if(e1.getType()=='p'||e2.getType()=='p')
                    return -1;
                else
                    return 1;
            }
            else{
                if (e1.getPlayerID() < e2.getPlayerID())
                    return -1;
                else if (e1.getPlayerID() > e2.getPlayerID())
                    return 1;
            }

        }
        return 0;
    }
}
