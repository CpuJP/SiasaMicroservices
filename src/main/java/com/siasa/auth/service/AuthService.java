package com.siasa.auth.service;

import com.siasa.auth.config.JwtProvider;
import com.siasa.auth.dto.TokenDto;
import com.siasa.auth.dto.UserDto;
import com.siasa.auth.dto.UserDtoLogin;
import com.siasa.auth.entity.User;
import com.siasa.auth.enums.Role;
import com.siasa.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtProvider = jwtProvider;
    }

    private UserDto mapToDto(User user) {
        TypeMap<User, UserDto> typeMap = modelMapper.typeMap(User.class, UserDto.class);
        typeMap.addMapping(User::getRoles, UserDto::setRoles);
        return typeMap.map(user);
    }

    private UserDto mapToDtoWithOutPassword(User user) {
        TypeMap<User, UserDto> typeMap = modelMapper.typeMap(User.class, UserDto.class);
        typeMap.addMapping(src -> "", UserDto::setPassword); // Excluir el campo de contraseña
        typeMap.addMapping(User::getRoles, UserDto::setRoles);
        return typeMap.map(user);
    }

    @Transactional
    public ResponseEntity<UserDto> save(UserDto userDto) {
        if (StringUtils.isBlank(userDto.getName()) || StringUtils.isBlank(userDto.getEmail()) || StringUtils.isBlank(userDto.getPassword())) {
            throw new IllegalArgumentException("Nombre, correo electrónico y contraseña son obligatorios");
        }

        Optional<User> existingUser = userRepository.findByName(userDto.getName());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("El usuario %s ya se encuentra registrado", userDto.getName()));
        }

        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("El email %s ya se encuentra registrado", userDto.getEmail()));
        }

        Role userRole = Role.USER;
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setRoles(roles);

        User savedUser = userRepository.save(newUser);

        userDto = mapToDto(savedUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<TokenDto> login(UserDtoLogin userDto) {
        User user = userRepository.findByName(userDto.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            String token = jwtProvider.createToken(user.getName());
            TokenDto tokenDto = new TokenDto(token);
            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
    }

    @Transactional
    public ResponseEntity<TokenDto> validate(String token) {
        jwtProvider.validate(token);
        String userName = jwtProvider.getUsernameFromToken(token);
        userName = userName.replace("{", "").replace("}", "");
        String[] split = userName.split(",");
        String subValue = split[0].replace("sub=", "");
        if (userRepository.findByName(subValue).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado");
        }
        TokenDto tokenDto = new TokenDto(token);
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Set<Role>> getRolesFromToken(String token) {
        // Primero, validamos el token utilizando el método existente
        jwtProvider.validate(token);

        // Si la validación fue exitosa, extraemos el nombre de usuario del token
        String userName = jwtProvider.getUsernameFromToken(token);
        userName = userName.replace("{", "").replace("}", "");
        String[] split = userName.split(",");
        String subValue = split[0].replace("sub=", "");
        Optional<User> userOptionalId = userRepository.findByName(subValue);

        // Buscamos el usuario en la base de datos utilizando el nombre de usuario
        Optional<User> userOptional = userRepository.findByIdWithRoles(userOptionalId.get().getId());

        // Si el usuario existe, devolvemos los roles asociados a dicho usuario
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
        } else {
            // En caso de que el usuario no exista, puedes manejar este caso como mejor convenga a tu aplicación
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario asociado al token no encontrado");
        }
    }

}
