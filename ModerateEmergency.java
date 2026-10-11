
public class ModerateEmergency extends Emergency {
    public ModerateEmergency(int id, int patientId, String location) {
        super(id, patientId, location);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public String getType() {
        return "MODERATE";
    }
}
