public class Player {
    private final int id;
    private final int skill;
    private boolean busy;
    private int attemptToMassage;
    private int physiotherapistId;

    public Player(int id, int skill) {
        this.id = id;
        this.skill = skill;
        this.busy=false;    //Initially all players are not bus (Not started to do event)
        this.attemptToMassage=0; //Initially none of the players did not attempt to any massage.
        this.physiotherapistId=-1; //Which physiotherapist is serving to the player currently.
    }

    public boolean isBusy() {
        return busy;
    }
    public void makeBusy() {
        this.busy = true;
    }
    public void makeUnbusy() {
        this.busy = false;
    }

    public void incAttemptToMassage() {
        attemptToMassage++;
    }

    public int getAttemptToMassage() {
        return attemptToMassage;
    }

    public int getId() {
        return id;
    }

    public int getSkill() {
        return skill;
    }

    public int getPhysiotherapistId() {
        return physiotherapistId;
    }

    public void setPhysiotherapistId(int physiotherapistId) {
        this.physiotherapistId = physiotherapistId;
    }
}

