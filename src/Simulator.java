public class Simulator {
    /*TC methods
    * isThereTC: Checks if there is training couch.
    * makeTCbusy: Makes training couch with the lowest ID busy.
    * makeTCidle: Makes training couch with the lowest ID idle.
    * howManyTC: Counts available training couches.
    * */
    public boolean isThereTC(TrainCoach[] trainCoaches){
        int availableTC=howManyTC(trainCoaches);
        return availableTC != 0;
    }

    public void makeTCbusy(TrainCoach[] trainCoaches){
        for (TrainCoach trainCoach : trainCoaches) {
            if (trainCoach.isIdle()) {
                trainCoach.setIdle(false);
                break;
            }
        }
    }

    public void makeTCidle(TrainCoach[] trainCoaches){
        for (TrainCoach trainCoach : trainCoaches) {
            if (!trainCoach.isIdle()) {
                trainCoach.setIdle(true);
                break;
            }
        }
    }

    private int howManyTC(TrainCoach[] trainCoaches){
        int availableTC=0;
        for (TrainCoach trainCoach : trainCoaches) {
            if (trainCoach.isIdle()) {
                availableTC++;
            }
        }
        return availableTC;
    }


    /*PT methods
     * isTherePT: Checks if there is physiotherapist.
     * makePTbusy: Makes physiotherapist with the lowest ID busy.
     * makePTidle: Makes physiotherapist with the lowest ID idle.
     * howManyPT: Counts available physiotherapist.
     * */
    public boolean isTherePT(Physiotherapist[] physiotherapists){
        int availablePT=howManyPT(physiotherapists);
        return availablePT != 0;
    }

    public int makePTbusy(Physiotherapist[] physiotherapists){
        int availablePTid=0;
        for (Physiotherapist physiotherapist : physiotherapists) {
            if (physiotherapist.isIdle()) {
                physiotherapist.setIdle(false);
                availablePTid=physiotherapist.getId();
                break;
            }
        }
        return availablePTid;
    }

    public void makePTidle(Physiotherapist[] physiotherapists, int id){
        physiotherapists[id].setIdle(true);
    }
    private int howManyPT(Physiotherapist[] physiotherapists){
        int availablePT=0;
        for (Physiotherapist physiotherapist : physiotherapists) {
            if (physiotherapist.isIdle()) {
                availablePT++;
            }
        }
        return availablePT;
    }
    /*M methods
     * isThereM: Checks if there is masseur.
     * makeMbusy: Makes masseur with the lowest ID busy.
     * makeMidle: Makes masseur with the lowest ID idle.
     * howManyM: Counts available masseurs.
     * */
    public boolean isThereM(Masseur[] masseurs){
        int availableM=howManyM(masseurs);
        return availableM != 0;
    }

    public void makeMbusy(Masseur[] masseurs){
        for (Masseur masseur : masseurs) {
            if (masseur.isIdle()) {
                masseur.setIdle(false);
                break;
            }
        }
    }

    public void makeMidle(Masseur[] masseurs){
        for (Masseur masseur : masseurs) {
            if (!masseur.isIdle()) {
                masseur.setIdle(true);
                break;
            }
        }
    }

    private int howManyM(Masseur[] masseurs){
        int availableM=0;
        for (Masseur masseur : masseurs) {
            if (masseur.isIdle()) {
                availableM++;
            }
        }
        return availableM;
    }


}
