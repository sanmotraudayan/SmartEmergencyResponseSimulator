
import java.io.Serializable;

public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int id;
    protected String name;
    protected int age;

    public Person(int id, String name, int age) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Person ID must be positive."
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Person name cannot be empty."
            );
        }

        if (age < 0 || age > 120) {
            throw new IllegalArgumentException(
                    "Age must be between 0 and 120."
            );
        }

        this.id = id;
        this.name = name.trim();
        this.age = age;
    }

    public abstract void displayDetails();
}
