package spring.security.security.user;

import org.junit.jupiter.api.Test;
import spring.security.entity.Permissions;
import spring.security.entity.Roles;
import spring.security.entity.Users;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsTest {
    @Test
    void mapsRolesAndPermissionsToSpringAuthorities() {
        Permissions permission = new Permissions();
        permission.setName("USER_READ");
        Roles role = new Roles();
        role.setName("ADMIN");
        role.setPermissions(Set.of(permission));
        Users user = enabledUser();
        user.setRoles(Set.of(role));

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_ADMIN", "USER_READ");
    }

    @Test
    void softDeletedUserIsNotEnabled() {
        Users user = enabledUser();
        user.setDeleted(true);

        assertThat(new CustomUserDetails(user).isEnabled()).isFalse();
    }

    @Test
    void ignoresSoftDeletedRolesAndPermissions() {
        Permissions deletedPermission = new Permissions();
        deletedPermission.setName("USER_DELETE");
        deletedPermission.setDeleted(true);
        Roles activeRole = new Roles();
        activeRole.setName("USER");
        activeRole.setDeleted(false);
        activeRole.setPermissions(Set.of(deletedPermission));

        Roles deletedRole = new Roles();
        deletedRole.setName("ADMIN");
        deletedRole.setDeleted(true);
        Users user = enabledUser();
        user.setRoles(Set.of(activeRole, deletedRole));

        assertThat(new CustomUserDetails(user).getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }

    private Users enabledUser() {
        Users user = Users.builder()
                .username("user@example.com")
                .password("encoded")
                .build();
        user.setDeleted(false);
        return user;
    }
}
