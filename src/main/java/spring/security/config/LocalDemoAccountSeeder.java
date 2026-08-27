package spring.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import spring.security.entity.Roles;
import spring.security.entity.Users;
import spring.security.repository.RoleRepository;
import spring.security.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalDemoAccountSeeder implements CommandLineRunner {
    private static final String USER_EMAIL = "user@cinevault.demo";
    private static final String USER_PASSWORD = "User123!";
    private static final String ADMIN_EMAIL = "admin@cinevault.demo";
    private static final String ADMIN_PASSWORD = "Admin123!";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedAccount(USER_EMAIL, USER_PASSWORD, "USER");
        seedAccount(ADMIN_EMAIL, ADMIN_PASSWORD, "ADMIN");
        log.info("Local demo accounts are ready");
    }

    private void seedAccount(String email, String rawPassword, String roleName) {
        Roles role = roleRepository.findByNameAndDeletedFalse(roleName)
                .orElseThrow(() -> new IllegalStateException("Required role not found: " + roleName));

        Users user = userRepository.findByUsernameAndDeletedFalse(email)
                .orElseGet(() -> Users.builder()
                        .username(email)
                        .email(email)
                        .password(passwordEncoder.encode(rawPassword))
                        .roles(new HashSet<>())
                        .build());

        user.setEmail(email);
        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setDeleted(false);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        if (!user.getRoles().equals(Set.of(role))) {
            user.getRoles().clear();
            user.getRoles().add(role);
        }

        userRepository.save(user);
    }
}
