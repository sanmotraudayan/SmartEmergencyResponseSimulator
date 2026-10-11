
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {

    private final Path dataDirectory = Paths.get("data");
    private final Path dataFile =
            dataDirectory.resolve("simulator_data.ser");

    // Save application data using serialization
    public void saveData(EmergencyManager manager) {

        if (manager == null) {
            System.out.println(
                    "[Error] Cannot save application data."
            );
            return;
        }

        try {
            Files.createDirectories(dataDirectory);

            try (ObjectOutputStream out =
                    new ObjectOutputStream(
                            new BufferedOutputStream(
                                    new FileOutputStream(
                                            dataFile.toFile())))) {

                out.writeObject(manager);
            }

            System.out.println(
                    "[System] Application data saved successfully."
            );

        } catch (IOException e) {
            System.out.println(
                    "[Error] Unable to save application data: "
                            + e.getMessage()
            );
        }
    }

    // Load previously saved application data
    public EmergencyManager loadData() {

        if (!Files.exists(dataFile)) {
            return null;
        }

        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(dataFile.toFile()))) {

            Object object = in.readObject();

            if (object instanceof EmergencyManager) {
                System.out.println(
                        "[System] Saved application data loaded successfully."
                );

                return (EmergencyManager) object;
            }

            System.out.println(
                    "[Error] Saved data has an unexpected format."
            );

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                    "[Error] Unable to load saved data: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // Generate and save system report and dispatch history
    public void saveReport(EmergencyManager manager) {

        if (manager == null) {
            System.out.println(
                    "[Error] Cannot generate reports without application data."
            );
            return;
        }

        try {
            Files.createDirectories(dataDirectory);

            writeFile(
                    dataDirectory.resolve("system_report.txt"),
                    manager.createReport()
            );

            System.out.println(
                    "[System] System report saved successfully."
            );

            writeFile(
                    dataDirectory.resolve("dispatch_history.txt"),
                    manager.createHistoryReport()
            );

            System.out.println(
                    "[System] Dispatch history saved successfully."
            );

        } catch (IOException e) {
            System.out.println(
                    "[Error] Unable to save reports: "
                            + e.getMessage()
            );
        }
    }

    // Write text content to a file
    private void writeFile(Path path, String content)
            throws IOException {

        Files.writeString(
                path,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }
}
