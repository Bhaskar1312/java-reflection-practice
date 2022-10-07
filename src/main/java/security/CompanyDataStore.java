package security;

import security.internal.Annotations;
import security.internal.OperationType;
import security.internal.Role;
import security.internal.User;

@Annotations.Permissions(role = Role.CLERK, allowed = OperationType.READ)
@Annotations.Permissions(role = Role.MANAGER, allowed = {OperationType.READ, OperationType.WRITE})
@Annotations.Permissions(role = Role.SUPPORT_ENGINEER, allowed = {OperationType.READ, OperationType.WRITE, OperationType.DELETE})
public class CompanyDataStore {
    private User user;

    public void CompanyDataStore(User user) {
        this.user = user;
    }

    // Different Database access operations
}