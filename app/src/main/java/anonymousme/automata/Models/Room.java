package anonymousme.automata.Models;

/**
 * Created by affan on 20/8/17.
 */

public class Room {

    boolean lights;
    boolean fans;
    boolean isOccupied;
    double temperature;
    String location_id;

    public Room(boolean lights, boolean fans, boolean isOccupied, double temperature,String location_id) {
        this.lights = lights;
        this.fans = fans;
        this.isOccupied = isOccupied;
        this.temperature = temperature;
        this.location_id = location_id;
    }

    public boolean isLightsOn() {
        return lights;
    }

    public void setLightsOn(boolean lights) {
        this.lights = lights;
    }

    public boolean isFansOn() {
        return fans;
    }

    public void setFansOn(boolean fans) {
        this.fans = fans;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }
}
