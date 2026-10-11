import java.time.LocalDateTime;
import java.io.Serializable;

public class DispatchRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int emergencyId;
    private final int ambulanceId;
    private final int hospitalId;
    private final LocalDateTime dispatchTime;
    private LocalDateTime completionTime;
    private String status;

    public DispatchRecord(int emergencyId, int ambulanceId,
                          int hospitalId) {
        this.emergencyId = emergencyId;
        this.ambulanceId = ambulanceId;
        this.hospitalId = hospitalId;
        this.dispatchTime = LocalDateTime.now();
        this.status = "DISPATCHED";
    }

    public int getEmergencyId() {
        return emergencyId;
    }

    public int getAmbulanceId() {
        return ambulanceId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public String getStatus() {
        return status;
    }

    public void complete() {
        status = "COMPLETED";
        completionTime = LocalDateTime.now();
    }

    public void displayDetails() {
        System.out.println(
            "Emergency: " + emergencyId
            + " | Ambulance: " + ambulanceId
            + " | Hospital: " + hospitalId
            + " | Status: " + status
            + " | Dispatched: " + dispatchTime
        );

        if (completionTime != null) {
            System.out.println("Completed: " + completionTime);
        }
    }
}
