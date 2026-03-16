package com.devsu.app.rest.controller;

import com.devsu.app.rest.request.ClienteRequest;
import com.devsu.app.rest.response.ClienteResponse;
import com.devsu.commons.dto.ErrorResponse;
import com.devsu.domain.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clientes", description = "Gestión de clientes del sistema bancario")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/clientes")
public class ClientController {
    private final ClienteService clienteService;

    @Operation(
            summary = "Crear un nuevo cliente",
            description = "Registra un nuevo cliente en el sistema. La identificación debe ser única. " +
                    "Al crearse, se publica un evento Kafka para sincronizar con el microservicio de cuentas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o identificación duplicada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<ClienteResponse> crea(
            @Valid @RequestBody ClienteRequest request
    ) {
        return new ResponseEntity<>(clienteService.crea(request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar clientes con paginación",
            description = "Retorna una lista paginada de clientes ordenada por ID descendente. " +
                    "Soporta parámetros de paginación: page, size, sort."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    })
    @GetMapping("")
    public ResponseEntity<Page<ClienteResponse>> recuperarPaginado(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(clienteService.recuperarPaginado(pageable));
    }

    @Operation(
            summary = "Actualizar un cliente existente",
            description = "Actualiza todos los datos de un cliente por su ID. " +
                    "Publica un evento Kafka de actualización para sincronizar con el microservicio de cuentas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cliente no encontrado o datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualiza(
            @Parameter(description = "ID del cliente a actualizar", required = true)
            @PathVariable final Long id,
            @Valid @RequestBody ClienteRequest clienteRequest
    ) {
        return new ResponseEntity<>(clienteService.actualiza(id, clienteRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar un cliente",
            description = "Elimina un cliente por su ID. " +
                    "Publica un evento Kafka de eliminación para sincronizar con el microservicio de cuentas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Cliente no encontrado o tiene registros asociados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(
            @Parameter(description = "ID del cliente a eliminar", required = true)
            @PathVariable final Long id
    ) {
        clienteService.elimina(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
