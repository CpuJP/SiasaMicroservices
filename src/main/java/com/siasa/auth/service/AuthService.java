package com.siasa.auth.service;

import com.siasa.auth.config.JwtProvider;
import com.siasa.auth.dto.TokenDto;
import com.siasa.auth.dto.UserDto;
import com.siasa.auth.entity.User;
import com.siasa.auth.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
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

    public UserDto save(UserDto userDto) {
        Optional<User> user = userRepository.findByName(userDto.getName());
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("El usuario %s ya se encuentra registrado", userDto.getName()));
        }
        Optional<User> userEmail = userRepository.findByEmail(userDto.getEmail());
        if (userEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("El email %s ya se encuentra registrado", userDto.getEmail()));
        }
        User userSave = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        userRepository.save(userSave);
        return modelMapper.map(userSave, UserDto.class);
    }

    public TokenDto login(UserDto userDto) {
        User user = userRepository.findByName(userDto.getName()).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return new TokenDto(jwtProvider.createToken(user));
        } else  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public TokenDto validate(String token) {
        jwtProvider.validate(token);
        String userName = jwtProvider.getUsernameFromToken(token);
        userName = userName.replace("{", "").replace("}", "");
        String[] split = userName.split(",");
        String subValue = split[0].replace("sub=", "");
        if (userRepository.findByName(subValue).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new TokenDto(token);
    }
}
