package ru.kata.spring.boot_security.demo.configs;//*package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import javax.annotation.PostConstruct;
import java.util.Set;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);


        User user = new User("Andre", "Userov", 30, "user", passwordEncoder.encode("123"), Set.of(userRole));
        User admin = new User("Anatoly", "Adminov", 40, "admin", passwordEncoder.encode("admin"), Set.of(adminRole));
        userRepository.save(user);
        userRepository.save(admin);

    }
}