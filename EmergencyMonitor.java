
public class EmergencyMonitor implements Runnable {

    private final EmergencyManager manager;
    private volatile boolean running = true;
    private volatile Thread monitorThread;

    public EmergencyMonitor(EmergencyManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException(
                    "Emergency manager cannot be null."
            );
        }

        this.manager = manager;
    }

    public void setMonitorThread(Thread monitorThread) {
        this.monitorThread = monitorThread;
    }

    @Override
    public void run() {
        monitorThread = Thread.currentThread();

        System.out.println(
                "[Monitor] Background emergency monitor started."
        );

        long lastSeenVersion = manager.getEmergencyVersion();

        try {
            while (running) {
                lastSeenVersion =
                        manager.waitForEmergencyChange(lastSeenVersion);

                if (!running) {
                    break;
                }

                String summary =
                        manager.getHighestPriorityPendingSummary();

                System.out.println(
                        "\n[Monitor] Emergency activity detected."
                );
                System.out.println("[Monitor] " + summary);
                System.out.print("Enter your choice: ");
            }

        } catch (InterruptedException e) {
            // Interruption is used to request a clean shutdown.
            Thread.currentThread().interrupt();

        } finally {
            System.out.println(
                    "[Monitor] Background emergency monitor stopped."
            );
        }
    }

    public void stopMonitoring() {
        running = false;

        Thread thread = monitorThread;

        if (thread != null) {
            thread.interrupt();
        }
    }
}
