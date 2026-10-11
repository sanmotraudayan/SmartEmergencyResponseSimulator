
public class Patient extends Person {

    private String condition;

    public Patient(int id, String name, int age, String condition) {
        super(id, name, age);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Patient ID must be positive."
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Patient name cannot be empty."
            );
        }

        if (age < 0 || age > 120) {
            throw new IllegalArgumentException(
                    "Patient age must be between 0 and 120."
            );
        }

        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Patient condition cannot be empty."
            );
        }

        this.condition = condition.trim();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public void displayDetails() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Condition: " + condition);
        System.out.println("--------------------------------");
    }
}
