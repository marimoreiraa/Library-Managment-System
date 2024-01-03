package br.com.marianadmoreira.bibliotecaob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.marianadmoreira.bibliotecaob.model.Role;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;
import br.com.marianadmoreira.bibliotecaob.repository.IRoleRepository;
import br.com.marianadmoreira.bibliotecaob.repository.IUserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");
    }

    private void createRoleIfNotExists(String roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
    }

    private void initializeUsers() {
        createUserIfNotExists("admin", "admin@example.com", "Admin User", "senha", "ROLE_ADMIN");
        createUserIfNotExists("user", "user@example.com", "Regular User", "senha", "ROLE_USER");
    }

    private void createUserIfNotExists(String username, String email, String name, String password, String roleName) {
        UserModel existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            UserModel user = new UserModel();
            user.setUsername(username);
            user.setEmail(email);
            user.setName(name);
            user.setPassword(passwordEncoder().encode(password));

            Role role = roleRepository.findByRoleName(roleName);
            user.setRole(role);

            userRepository.save(user);
        }
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
