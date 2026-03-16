package com.devsu.app.rest.controller;

import com.devsu.app.rest.request.MovimientoRequest;
import com.devsu.app.rest.response.MovimientoResponse;
import com.devsu.commons.dto.ErrorResponse;
import com.devsu.domain.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Movimientos", description = "Gestión de movimientos bancarios (depósitos y retiros)")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/movimientos")
public class MovimientoController {
    private final MovimientoService movimientoService;

    @Operation(
            summary = "Registrar un movimiento",
            description = "Registra un depósito o retiro en una cuenta bancaria. " +
                    "Para retiros, valida que exista saldo suficiente. " +
                    "Los valores negativos se convierten automáticamente a positivos. " +
                    "Un valor de cero es rechazado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = MovimientoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cuenta no encontrada, saldo insuficiente, valor o tipo de movimiento inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<MovimientoResponse> crea(
            @Valid @RequestBody MovimientoRequest request
    ) {
        return new ResponseEntity<>(movimientoService.crear(request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Consultar movimientos por número de cuenta",
            description = "Retorna el historial completo de movimientos de una cuenta bancaria, " +
                    "ordenados del más reciente al más antiguo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos obtenidos exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MovimientoResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Cuenta no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{nroCuenta}")
    public ResponseEntity<List<MovimientoResponse>> buscaPorNroCuenta(
            @Parameter(description = "Número de cuenta (UUID generado al crear la cuenta)", required = true)
            @PathVariable final String nroCuenta
    ) {
        return ResponseEntity.ok(movimientoService.buscarPorNroCuenta(nroCuenta));
    }
}
