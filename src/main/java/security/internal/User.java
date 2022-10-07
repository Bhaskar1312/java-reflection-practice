package security.internal;

public class User {
    private final String name;
    private final Role role;

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}