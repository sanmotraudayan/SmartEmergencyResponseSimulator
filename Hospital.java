import java.io.Serializable;

public class Hospital implements Serializable {
    private static final long serialVersionUID = 1L;
    private int hospitalId;
    private String name;
    private String location;
    private int availableBeds;
    private final int totalBeds;

    public Hospital(int hospitalId, String name,
                    String location, int totalBeds) {
        if (totalBeds < 0) {
            throw new IllegalArgumentException(
                "Bed capacity cannot be negative."
            );
        }

        this.hospitalId = hospitalId;
        this.name = name;
        this.location = location;
        this.totalBeds = totalBeds;
        this.availableBeds = totalBeds;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public synchronized boolean admitPatient() {
        if (availableBeds <= 0) {
            return false;
        }

        availableBeds--;
        return true;
    }

    public synchronized void dischargePatient() {
        if (availableBeds < totalBeds) {
            availableBeds++;
        }
    }

    public void displayDetails() {
        System.out.println(
            "Hospital ID: " + hospitalId
            + " | Name: " + name
            + " | Location: " + location
            + " | Beds: " + availableBeds
            + "/" + totalBeds
        );
    }
}
