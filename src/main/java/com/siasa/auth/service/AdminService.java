package com.siasa.auth.service;

import com.siasa.auth.dto.UserDto;
import com.siasa.auth.entity.Usuario;
import com.siasa.auth.enums.Role;
import com.siasa.auth.exception.MessageNotFoundException;
import com.siasa.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    private UserDto mapToDto(Usuario user) {
        TypeMap<Usuario, UserDto> typeMap = modelMapper.typeMap(Usuario.class, UserDto.class);
        typeMap.addMapping(Usuario::getRoles, UserDto::setRoles);
        return typeMap.map(user);
    }

    private UserDto mapToDtoWithOutPassword(Usuario user) {
        TypeMap<Usuario, UserDto> typeMap = modelMapper.typeMap(Usuario.class, UserDto.class);
        typeMap.addMapping(src -> "", UserDto::setPassword); // Excluir el campo de contraseña
        typeMap.addMapping(Usuario::getRoles, UserDto::setRoles);
        return typeMap.map(user);
    }

    private void validatePassword(String newPassword) {
        // Validar la contraseña utilizando una expresión regular
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newPassword);

        // Si la contraseña no cumple con los requisitos, lanzar una excepción
        if (!matcher.matches()) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un caracter especial.");
        }
    }

    @Transactional
    public ResponseEntity<List<UserDto>> findAllWithRoles() {
        List<Usuario> users = userRepository.findAllWithRoles();
        if (!users.isEmpty()) {
            List<UserDto> userDtos = users.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDtos, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla Usuarios");
            throw new MessageNotFoundException("No hay datos en la tabla Usuarios");
        }
    }

    @Transactional
    public ResponseEntity<UserDto> updateRoles(Long userId, List<String> newRoles) {
        Optional<Usuario> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            Set<Role> roles = newRoles.stream()
                    .map(String::toUpperCase) // Convertir a mayúsculas
                    .map(Role::valueOf) // Convertir los strings a roles enumerados
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            userRepository.save(user);
            UserDto userDto = mapToDto(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("Usuario no encontrado con id %s ", userId));
        }
    }

    @Transactional
    public ResponseEntity<UserDto> changePassword(Long userId, String newPassword) {
        // Validar la nueva contraseña
        validatePassword(newPassword);

        Optional<Usuario> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            String encodedPassword = passwordEncoder.encode(newPassword); // Codificar la nueva contraseña
            user.setPassword(encodedPassword);
            userRepository.save(user);
            UserDto userDto = mapToDto(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("Usuario no encontrado con id %s ", userId));
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteUser(Long userId) {
        Optional<Usuario> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        } else {
            throw new MessageNotFoundException(String.format("Usuario no encontrado con id %s ", userId));
        }
    }
}
