import java.io.Serializable;

public abstract class Emergency implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int emergencyId;
    protected int patientId;
    protected String location;
    protected String status;

    public Emergency(int emergencyId, int patientId, String location) {
        if (emergencyId <= 0) {
            throw new IllegalArgumentException(
                    "Emergency ID must be positive."
            );
        }

        if (patientId <= 0) {
            throw new IllegalArgumentException(
                    "Patient ID must be positive."
            );
        }

        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Emergency location cannot be empty."
            );
        }

        this.emergencyId = emergencyId;
        this.patientId = patientId;
        this.location = location.trim();
        this.status = "PENDING";
    }

    public abstract int getPriority();

    public abstract String getType();

    public int getEmergencyId() {
        return emergencyId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null ||
                !(status.equals("PENDING")
                || status.equals("DISPATCHED")
                || status.equals("COMPLETED"))) {
            throw new IllegalArgumentException(
                    "Invalid emergency status."
            );
        }

        this.status = status;
    }

    public void displayDetails() {
        System.out.println("Emergency ID: " + emergencyId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Type: " + getType());
        System.out.println("Priority: " + getPriority());
        System.out.println("Location: " + location);
        System.out.println("Status: " + status);
        System.out.println("--------------------------------");
    }
}
