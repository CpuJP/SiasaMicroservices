package com.siasa.auth.controller;

import com.siasa.auth.dto.ErrorResponseDto;
import com.siasa.auth.dto.UserDto;
import com.siasa.auth.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Manejo usuarios API", description = "Operaciones relacionadas con el control de los Usuarios ya registrados en SIASA")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @Operation(summary = "Get all Users",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<List<UserDto>> findAll() {
        return adminService.findAllWithRoles();
    }

    @PatchMapping("/updateroles/{userId}")
    @Operation(summary = "Update Role in User",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Usuario encontrado y permisos actualizados",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Usuario no encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<UserDto> updateRoles(
            @Parameter(name = "userId", description = "ID del usuario a actualizar",
                in = ParameterIn.PATH, example = "2", schema = @Schema(type = "integer"))
            @PathVariable Long userId,
            @Parameter(name = "newRoles", description = "Lista de roles para cambiar en el usuario",
                in = ParameterIn.QUERY, example = "ADMIN, ROOT, USER, BIENESTAR,  BIBLIOTECA, COMPUTO, LAB, CAMPUS",
                    schema = @Schema(type = "array",
                            implementation = String.class,
                            example = "ADMIN", enumAsRef = true))
            @RequestParam List<String> newRoles) {
        return adminService.updateRoles(userId, newRoles);
    }

    @PatchMapping("/changepassword/{userId}")
    @Operation(summary = "Update Password un User",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Usuario encontrado y contraseña actualizada",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = UserDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - Usuario no encontrado",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<UserDto> changePassword(
            @Parameter(name = "userId", description = "ID del usuario a actualizar",
                    in = ParameterIn.PATH, example = "2", schema = @Schema(type = "integer"))
            @PathVariable Long userId,
            @Parameter(name = "newPassword", description = "Nueva contraseña",
                    in = ParameterIn.QUERY, example = "Prueba12345*",
                    schema = @Schema(type = "string"))
            @RequestParam String newPassword) {
        return adminService.changePassword(userId, newPassword);
    }

    @DeleteMapping("/deleteuser/{userId}")
    @Operation(summary = "Delete User",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Usuario encontrado y eliminado"),
                @ApiResponse(responseCode = "404", description = "Not Found - Usuario no encontrado",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<Void> deleteUser(
            @Parameter(name = "userId", description = "ID del usuario a eliminar",
                    in = ParameterIn.PATH, example = "2", schema = @Schema(type = "integer"))
            @PathVariable Long userId) {
        return adminService.deleteUser(userId);
    }
}
