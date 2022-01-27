public class Event{
    private char type;
    private int playerID;
    private double happeningTime;
    private double duration;
    private int skill;

    public Event(char type, int playerID, double happeningTime, double duration,int skill) {
        this.type = type;
        this.playerID = playerID;
        this.happeningTime = happeningTime;
        this.duration = duration;
        this.skill=skill;
    }

    public int getSkill() {
        return skill;
    }

    public char getType() {
        return type;
    }

    public int getPlayerID() {
        return playerID;
    }

    public double getHappeningTime() {
        return happeningTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double departureTimer(){
        return happeningTime + duration;
    }
}
