package com.siasa.auth.controller;


import com.siasa.auth.dto.ErrorResponseDto;
import com.siasa.auth.dto.TokenDto;
import com.siasa.auth.dto.UserDto;
import com.siasa.auth.dto.UserDtoLogin;
import com.siasa.auth.enums.Role;
import com.siasa.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autentificación API", description = "Operaciones realionadas con el control de acceso de los usuarios a SIASA")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login a User",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Ingreso correcto, Token generado"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - Credenciales inválidas",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<TokenDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a ingresar",
                required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoLogin.class)))
            @RequestBody UserDtoLogin userDto) {
        return authService.login(userDto);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate a Token",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Token Validado"),
                @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - Token Inválido",
                        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<TokenDto> validate(
            @Parameter(name = "token", description = "Token a validar en el sistema",
                in = ParameterIn.QUERY, example = "Bearer Token", schema = @Schema(type = "string"))
            @RequestParam String token) {
        return authService.validate(token);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new User in SIASA",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Usuario creado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - Nombre de usuario o Email ya se encuentran en uso",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - La contraseña no cumple con el requsito mínimo (8 caracteres, una mayúscula, un número y un caracter especial)",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<UserDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de usuario a registrar",
                required = true,
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)))
            @RequestBody UserDto userDto) {
        return authService.save(userDto);
    }

    @GetMapping("/getroles")
    @Operation(summary = "Get all the roles a user has based on the Token",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista con roles recuperada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Not Found - El Token no es válido",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<Set<Role>> getRolesFromToken(
            @Parameter(name = "token", description = "Token para extraer los roles",
                    in = ParameterIn.QUERY, example = "Bearer Token", schema = @Schema(type = "string"))
            @RequestParam String token) {
        return authService.getRolesFromToken(token);
    }

}
