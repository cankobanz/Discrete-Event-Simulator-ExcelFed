/*This class describes general properties of server objects: training coaches, physiotherapists and masseurs.
* */
abstract class Server {
    private final int id;
    private double serviceTime;
    private boolean idle;

    public Server(int id, double serviceTime) {
        this.id = id;
        this.serviceTime = serviceTime;
        this.idle = true;
    }
    public void setIdle(boolean idle) {
        this.idle = idle;
    }
    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }
    public boolean isIdle() {
        return idle;
    }

    public int getId() {
        return id;
    }

    public double getServiceTime() {
        return serviceTime;
    }
}
