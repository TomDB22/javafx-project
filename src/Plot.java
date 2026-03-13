public class Plot {
    private boolean occupied = false;
    private boolean ready = false;
    private String plantType = "";

    public boolean isOccupied() { return occupied; }
    public boolean isReady() { return ready; }
    public String getPlantType() { return plantType; }

    public void plant(String type) {
        this.occupied = true;
        this.ready = false;
        this.plantType = type;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setEmpty() {
        this.occupied = false;
        this.ready = false;
        this.plantType = "";
    }
}