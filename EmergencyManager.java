
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Patient> patients = new ArrayList<>();
    private final List<Emergency> emergencies = new ArrayList<>();
    private final List<Ambulance> ambulances = new ArrayList<>();
    private final List<Hospital> hospitals = new ArrayList<>();
    private final List<DispatchRecord> history = new ArrayList<>();

    private final Map<Integer, DispatchRecord> activeDispatches =
            new HashMap<>();

    private long emergencyVersion = 0;

    public synchronized void addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        if (patients.stream().anyMatch(p -> p.getId() == patient.getId())) {
            throw new IllegalArgumentException("Patient ID already exists.");
        }

        patients.add(patient);
    }

    public synchronized void addEmergency(Emergency emergency) {
        if (emergency == null) {
            throw new IllegalArgumentException("Emergency cannot be null.");
        }

        boolean patientExists = patients.stream()
                .anyMatch(p -> p.getId() == emergency.getPatientId());

        if (!patientExists) {
            throw new IllegalArgumentException(
                    "Register the patient before adding an emergency."
            );
        }

        if (emergencies.stream().anyMatch(
                e -> e.getEmergencyId() == emergency.getEmergencyId())) {
            throw new IllegalArgumentException(
                    "Emergency ID already exists."
            );
        }

        emergencies.add(emergency);

        // Notify the waiting monitor that emergency data has changed.
        emergencyVersion++;
        notifyAll();
    }

    public synchronized long getEmergencyVersion() {
        return emergencyVersion;
    }

    public synchronized long waitForEmergencyChange(long lastSeenVersion)
            throws InterruptedException {

        while (emergencyVersion <= lastSeenVersion) {
            wait();
        }

        return emergencyVersion;
    }

    public synchronized String getHighestPriorityPendingSummary() {
        Emergency emergency = emergencies.stream()
                .filter(e -> "PENDING".equals(e.getStatus()))
                .max(Comparator.comparingInt(Emergency::getPriority)
                        .thenComparingInt(e -> -e.getEmergencyId()))
                .orElse(null);

        if (emergency == null) {
            return "No pending emergencies remain.";
        }

        return "Highest-priority pending emergency: ID "
                + emergency.getEmergencyId()
                + ", Type: " + emergency.getType()
                + ", Priority: " + emergency.getPriority()
                + ", Location: " + emergency.getLocation();
    }

    public synchronized void addAmbulance(Ambulance ambulance) {
        if (ambulance == null) {
            throw new IllegalArgumentException("Ambulance cannot be null.");
        }

        if (ambulances.stream().anyMatch(
                a -> a.getAmbulanceId() == ambulance.getAmbulanceId())) {
            throw new IllegalArgumentException(
                    "Ambulance ID already exists."
            );
        }

        ambulances.add(ambulance);
    }

    public synchronized void addHospital(Hospital hospital) {
        if (hospital == null) {
            throw new IllegalArgumentException("Hospital cannot be null.");
        }

        if (hospitals.stream().anyMatch(
                h -> h.getHospitalId() == hospital.getHospitalId())) {
            throw new IllegalArgumentException(
                    "Hospital ID already exists."
            );
        }

        hospitals.add(hospital);
    }

    public synchronized int getNextPatientId() {
        return patients.stream()
                .mapToInt(Patient::getId)
                .max()
                .orElse(0) + 1;
    }

    public synchronized int getNextEmergencyId() {
        return emergencies.stream()
                .mapToInt(Emergency::getEmergencyId)
                .max()
                .orElse(0) + 1;
    }

    public synchronized void displayPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }

        for (Patient patient : patients) {
            patient.displayDetails();
        }
    }

    public synchronized void displayEmergencies() {
        if (emergencies.isEmpty()) {
            System.out.println("No emergencies registered.");
            return;
        }

        emergencies.stream()
                .sorted(Comparator.comparingInt(Emergency::getPriority)
                        .reversed()
                        .thenComparingInt(Emergency::getEmergencyId))
                .forEach(Emergency::displayDetails);
    }

    public synchronized void displayAmbulances() {
        if (ambulances.isEmpty()) {
            System.out.println("No ambulances registered.");
            return;
        }

        for (Ambulance ambulance : ambulances) {
            ambulance.displayDetails();
        }
    }

    public synchronized void displayHospitals() {
        if (hospitals.isEmpty()) {
            System.out.println("No hospitals registered.");
            return;
        }

        for (Hospital hospital : hospitals) {
            hospital.displayDetails();
        }
    }

    public synchronized void dispatchNextEmergency() {
        Emergency selected = emergencies.stream()
                .filter(e -> "PENDING".equals(e.getStatus()))
                .max(Comparator.comparingInt(Emergency::getPriority)
                        .thenComparingInt(e -> -e.getEmergencyId()))
                .orElse(null);

        if (selected == null) {
            System.out.println("No pending emergencies.");
            return;
        }

        Ambulance ambulance = ambulances.stream()
                .filter(Ambulance::isAvailable)
                .findFirst()
                .orElse(null);

        if (ambulance == null) {
            System.out.println("No ambulance is currently available.");
            return;
        }

        Hospital hospital = hospitals.stream()
                .filter(h -> h.getAvailableBeds() > 0)
                .findFirst()
                .orElse(null);

        if (hospital == null) {
            System.out.println("No hospital beds are currently available.");
            return;
        }

        try {
            ambulance.dispatch();

            if (!hospital.admitPatient()) {
                ambulance.release();
                System.out.println("Hospital admission failed.");
                return;
            }

            DispatchRecord record = new DispatchRecord(
                    selected.getEmergencyId(),
                    ambulance.getAmbulanceId(),
                    hospital.getHospitalId()
            );

            selected.setStatus("DISPATCHED");
            activeDispatches.put(selected.getEmergencyId(), record);
            history.add(record);

            System.out.println("Emergency dispatched successfully.");
            System.out.println("Emergency ID: " + selected.getEmergencyId());
            System.out.println("Ambulance ID: " + ambulance.getAmbulanceId());
            System.out.println("Hospital: " + hospital.getName());

        } catch (IllegalStateException e) {
            System.out.println("Dispatch error: " + e.getMessage());
        }
    }

    public synchronized void completeEmergency(int emergencyId)
            throws EmergencyNotFoundException {

        Emergency emergency = emergencies.stream()
                .filter(e -> e.getEmergencyId() == emergencyId)
                .findFirst()
                .orElse(null);

        if (emergency == null) {
            throw new EmergencyNotFoundException(
                    "Emergency ID " + emergencyId + " was not found."
            );
        }

        DispatchRecord record = activeDispatches.get(emergencyId);

        if (record == null ||
                !"DISPATCHED".equals(emergency.getStatus())) {
            System.out.println("This emergency has no active dispatch.");
            return;
        }

        Ambulance ambulance = ambulances.stream()
                .filter(a -> a.getAmbulanceId() == record.getAmbulanceId())
                .findFirst()
                .orElse(null);

        Hospital hospital = hospitals.stream()
                .filter(h -> h.getHospitalId() == record.getHospitalId())
                .findFirst()
                .orElse(null);

        if (ambulance == null || hospital == null) {
            System.out.println("Assigned resources could not be found.");
            return;
        }

        emergency.setStatus("COMPLETED");
        record.complete();
        ambulance.release();
        hospital.dischargePatient();
        activeDispatches.remove(emergencyId);

        System.out.println(
                "Emergency " + emergencyId + " completed successfully."
        );
    }

    public synchronized void displayHistory() {
        if (history.isEmpty()) {
            System.out.println("No dispatch history available.");
            return;
        }

        for (DispatchRecord record : history) {
            record.displayDetails();
        }
    }

    public synchronized String createReport() {
        long pending = emergencies.stream()
                .filter(e -> "PENDING".equals(e.getStatus())).count();

        long dispatched = emergencies.stream()
                .filter(e -> "DISPATCHED".equals(e.getStatus())).count();

        long completed = emergencies.stream()
                .filter(e -> "COMPLETED".equals(e.getStatus())).count();

        return "SMART EMERGENCY RESPONSE REPORT\n"
                + "================================\n"
                + "Total Patients: " + patients.size() + "\n"
                + "Total Emergencies: " + emergencies.size() + "\n"
                + "Pending Emergencies: " + pending + "\n"
                + "Active Emergencies: " + dispatched + "\n"
                + "Completed Emergencies: " + completed + "\n"
                + "Total Ambulances: " + ambulances.size() + "\n"
                + "Total Hospitals: " + hospitals.size() + "\n"
                + "Dispatch Records: " + history.size() + "\n";
    }

    public synchronized String createHistoryReport() {
        StringBuilder report = new StringBuilder();

        report.append("DISPATCH HISTORY\n");
        report.append("================\n");

        if (history.isEmpty()) {
            report.append("No dispatch records available.\n");
        } else {
            for (DispatchRecord record : history) {
                report.append("Emergency ID: ")
                        .append(record.getEmergencyId())
                        .append(", Ambulance ID: ")
                        .append(record.getAmbulanceId())
                        .append(", Hospital ID: ")
                        .append(record.getHospitalId())
                        .append(", Status: ")
                        .append(record.getStatus())
                        .append("\n");
            }
        }

        return report.toString();
    }
}
