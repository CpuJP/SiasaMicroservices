package com.siasa.auth.config;

import com.siasa.auth.entity.Usuario;
import com.siasa.auth.enums.Role;
import com.siasa.auth.repository.UserRepository;
import com.siasa.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class DefaultUserInitializer implements ApplicationRunner {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Value("${root.name}")
    private String name;

    @Value("${root.email}")
    private String email;

    @Value("${root.password}")
    private String password;

    public DefaultUserInitializer(AuthService authService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Verificar si el usuario ya está registrado
        Optional<Usuario> existingUser = userRepository.findByName(name);
        if (existingUser.isPresent()) {
            // Si el usuario ya está presente, envía un registro de alerta y termina
            log.warn("El usuario {} ya está registrado. No se creará otro.", name);
            return;
        }
        Optional<Usuario> existingEmail = userRepository.findByEmail(email);
        if (existingEmail.isPresent()) {
            log.warn("El Email {} ya está registrado. No se creará otro.", email);
            return;
        }

        // Si el usuario no está presente, procede a crearlo
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        roles.add(Role.ROOT);

        String encodedPassword = passwordEncoder.encode(password);

        Usuario newUser = new Usuario();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        newUser.setRoles(roles);

        userRepository.save(newUser);
        log.info("Usuario ROOT creado exitosamente");
    }
}
