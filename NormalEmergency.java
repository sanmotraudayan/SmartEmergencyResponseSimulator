
public class NormalEmergency extends Emergency {
    public NormalEmergency(int id, int patientId, String location) {
        super(id, patientId, location);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getType() {
        return "NORMAL";
    }
}
