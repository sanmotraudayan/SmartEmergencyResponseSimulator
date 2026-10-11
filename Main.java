
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "[Error] Please enter a whole number."
                );
            }
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);

            if (value > 0) {
                return value;
            }

            System.out.println(
                    "[Error] Value must be greater than zero."
            );
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println(
                    "[Error] Input cannot be empty."
            );
        }
    }

    private static int readAge() {
        while (true) {
            int age = readInt("Enter patient age: ");

            if (age >= 0 && age <= 120) {
                return age;
            }

            System.out.println(
                    "[Error] Age must be between 0 and 120."
            );
        }
    }

    private static int readEmergencyType() {
        while (true) {
            System.out.println("\n1. Critical");
            System.out.println("2. Moderate");
            System.out.println("3. Normal");

            int type = readInt("Choose emergency type: ");

            if (type >= 1 && type <= 3) {
                return type;
            }

            System.out.println(
                    "[Error] Choose 1, 2, or 3."
            );
        }
    }

    private static EmergencyManager createNewManager() {
        EmergencyManager manager = new EmergencyManager();

        manager.addAmbulance(new Ambulance(101, "Central"));
        manager.addAmbulance(new Ambulance(102, "North"));
        manager.addAmbulance(new Ambulance(103, "South"));

        manager.addHospital(
                new Hospital(
                        201,
                        "City General Hospital",
                        "Central",
                        10
                )
        );

        manager.addHospital(
                new Hospital(
                        202,
                        "North Medical Centre",
                        "North",
                        5
                )
        );

        return manager;
    }

    public static void main(String[] args) {

        FileManager fileManager = new FileManager();

        System.out.println("\n========================================");
        System.out.println("   SMART EMERGENCY RESPONSE SIMULATOR");
        System.out.println("========================================");

        EmergencyManager manager = fileManager.loadData();

        if (manager == null) {
            manager = createNewManager();

            System.out.println(
                    "[System] New application session initialized successfully."
            );
        }

        int nextPatientId = manager.getNextPatientId();
        int nextEmergencyId = manager.getNextEmergencyId();

        EmergencyMonitor monitor = new EmergencyMonitor(manager);

        Thread monitorThread = new Thread(
                monitor,
                "Emergency-Monitor"
        );

        monitor.setMonitorThread(monitorThread);
        monitorThread.setDaemon(true);
        monitorThread.start();

        boolean running = true;

        try {
            while (running) {

                System.out.println("\n----------------------------------------");
                System.out.println("                 MAIN MENU");
                System.out.println("----------------------------------------");
                System.out.println("1. Register Patient and Emergency");
                System.out.println("2. View All Patients");
                System.out.println("3. View All Emergencies");
                System.out.println("4. View Ambulances");
                System.out.println("5. View Hospitals");
                System.out.println("6. Dispatch Next Emergency");
                System.out.println("7. Complete Emergency");
                System.out.println("8. View Dispatch History");
                System.out.println("9. View System Report");
                System.out.println("10. Save Data and Reports");
                System.out.println("11. Exit");
                System.out.println("----------------------------------------");

                int choice = readInt("Enter your choice: ");

                try {
                    switch (choice) {

                        case 1: {
                            String name =
                                    readText("Enter patient name: ");

                            int age = readAge();

                            String condition =
                                    readText("Enter patient condition: ");

                            String location =
                                    readText("Enter emergency location: ");

                            int type = readEmergencyType();

                            Patient patient = new Patient(
                                    nextPatientId,
                                    name,
                                    age,
                                    condition
                            );

                            Emergency emergency;

                            switch (type) {
                                case 1:
                                    emergency = new CriticalEmergency(
                                            nextEmergencyId,
                                            nextPatientId,
                                            location
                                    );
                                    break;

                                case 2:
                                    emergency = new ModerateEmergency(
                                            nextEmergencyId,
                                            nextPatientId,
                                            location
                                    );
                                    break;

                                default:
                                    emergency = new NormalEmergency(
                                            nextEmergencyId,
                                            nextPatientId,
                                            location
                                    );
                            }

                            manager.addPatient(patient);
                            manager.addEmergency(emergency);

                            nextPatientId++;
                            nextEmergencyId++;

                            System.out.println(
                                    "\n[System] Patient and emergency registered successfully."
                            );
                            System.out.println(
                                    "Patient ID: " + patient.getId()
                            );
                            System.out.println(
                                    "Emergency ID: "
                                            + emergency.getEmergencyId()
                            );
                            System.out.println(
                                    "Type: " + emergency.getType()
                            );
                            System.out.println(
                                    "Priority: " + emergency.getPriority()
                            );

                            fileManager.saveData(manager);
                            break;
                        }

                        case 2:
                            manager.displayPatients();
                            break;

                        case 3:
                            manager.displayEmergencies();
                            break;

                        case 4:
                            manager.displayAmbulances();
                            break;

                        case 5:
                            manager.displayHospitals();
                            break;

                        case 6:
                            manager.dispatchNextEmergency();
                            fileManager.saveData(manager);
                            break;

                        case 7: {
                            int emergencyId = readPositiveInt(
                                    "Enter emergency ID to complete: "
                            );

                            try {
                                manager.completeEmergency(emergencyId);
                                fileManager.saveData(manager);

                            } catch (EmergencyNotFoundException e) {
                                System.out.println(
                                        "[Error] " + e.getMessage()
                                );
                            }

                            break;
                        }

                        case 8:
                            manager.displayHistory();
                            break;

                        case 9:
                            System.out.println(manager.createReport());
                            break;

                        case 10:
                            fileManager.saveData(manager);
                            fileManager.saveReport(manager);
                            break;

                        case 11:
                            running = false;
                            break;

                        default:
                            System.out.println(
                                    "[Error] Invalid choice. Enter 1 to 11."
                            );
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "[Error] Invalid operation: " + e.getMessage()
                    );
                }
            }

        } finally {
            monitor.stopMonitoring();

            try {
                monitorThread.join(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                System.out.println(
                        "[Error] Interrupted while stopping the monitor."
                );
            }

            System.out.println(
                    "\n[System] Saving final application state..."
            );

            fileManager.saveData(manager);
            fileManager.saveReport(manager);

            sc.close();

            System.out.println();
            System.out.println(
                    "Thank you for using the Smart Emergency Response Simulator."
            );
            System.out.println("Application closed successfully.");
        }
    }
}
