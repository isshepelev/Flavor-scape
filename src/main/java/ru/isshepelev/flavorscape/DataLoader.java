package ru.isshepelev.flavorscape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.Role;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.RoleRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        Role role1 = new Role();
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");
        roleRepository.save(role1);
        roleRepository.save(role2);

        User user = new User();
        user.setUsername("1");
        user.setPassword(passwordEncoder.encode("1"));
        user.setRoles(Set.of(role1));

        User user2 = new User();
        user2.setUsername("2");
        user2.setPassword(passwordEncoder.encode("2"));
        user2.setRoles(Set.of(role2));

        userRepository.save(user);
        userRepository.save(user2);
    }
}
