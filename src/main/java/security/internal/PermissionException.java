package security.internal;

public class PermissionException extends RuntimeException {
    public PermissionException() {
        super("User does not have the right permissions for the operation");
    }
}
