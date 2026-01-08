package data;

// factory for creating either student/homeowner based on a choice
public class UserFactory {
    public static User createUser(
            String choice,
            String username,
            String password

    ) {
        return switch (choice) {
            case "S" -> new Student(username, password);
            case "H" -> new HomeOwner(username, password);
            default -> null;
        };

    };
}
