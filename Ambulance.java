import java.io.Serializable;

public class Ambulance implements Serializable {
    private static final long serialVersionUID = 1L;
    private int ambulanceId;
    private String location;
    private boolean available;

    public Ambulance(int ambulanceId, String location) {
        this.ambulanceId = ambulanceId;
        this.location = location;
        this.available = true;
    }

    public int getAmbulanceId() {
        return ambulanceId;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void dispatch() {
        if (!available) {
            throw new IllegalStateException(
                "Ambulance is already dispatched."
            );
        }
        available = false;
    }

    public void release() {
        available = true;
    }

    public void displayDetails() {
        System.out.println(
            "Ambulance ID: " + ambulanceId
            + " | Location: " + location
            + " | Status: "
            + (available ? "AVAILABLE" : "DISPATCHED")
        );
    }
}
