
public class CriticalEmergency extends Emergency {
    public CriticalEmergency(int id, int patientId, String location) {
        super(id, patientId, location);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public String getType() {
        return "CRITICAL";
    }
}
