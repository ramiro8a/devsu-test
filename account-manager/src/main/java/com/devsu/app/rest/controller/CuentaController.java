package com.devsu.app.rest.controller;

import com.devsu.app.rest.request.CuentaRequest;
import com.devsu.app.rest.request.CuentaUpdateRequest;
import com.devsu.app.rest.request.EstadoCuentaFilter;
import com.devsu.app.rest.response.CuentaResponse;
import com.devsu.app.rest.response.ReporteEstadoCuentaResponse;
import com.devsu.commons.dto.ErrorResponse;
import com.devsu.domain.service.CuentaService;
import com.devsu.domain.service.ReporteService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Cuentas", description = "Gestión de cuentas bancarias")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;
    private final ReporteService reporteService;

    @Operation(
            summary = "Crear una nueva cuenta",
            description = "Crea una cuenta bancaria asociada a un cliente existente. " +
                    "Se genera un número de cuenta UUID automáticamente. Si el saldo inicial es mayor a cero, " +
                    "se crea un movimiento de depósito inicial."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente",
                    content = @Content(schema = @Schema(implementation = CuentaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cliente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<CuentaResponse> crea(
            @Valid @RequestBody CuentaRequest request
    ) {
        return new ResponseEntity<>(cuentaService.crear(request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener cuenta por ID",
            description = "Retorna los datos de una cuenta bancaria incluyendo su historial de movimientos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada",
                    content = @Content(schema = @Schema(implementation = CuentaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cuenta no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> recuperarUnico(
            @Parameter(description = "ID de la cuenta", required = true)
            @PathVariable final Long id
    ) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Listar todas las cuentas",
            description = "Retorna la lista completa de cuentas registradas con su historial de movimientos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CuentaResponse.class))))
    })
    @GetMapping("")
    public ResponseEntity<List<CuentaResponse>> recuperarLista() {
        return ResponseEntity.ok(cuentaService.obtenerTodos());
    }

    @Operation(
            summary = "Actualizar cuenta",
            description = "Actualiza el tipo de cuenta y el estado de una cuenta existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = CuentaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cuenta no encontrada o datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualiza(
            @Parameter(description = "ID de la cuenta a actualizar", required = true)
            @PathVariable final Long id,
            @Valid @RequestBody CuentaUpdateRequest request
            ) {
        return new ResponseEntity<>(cuentaService.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(
            summary = "Generar reporte de estado de cuenta",
            description = "Genera un reporte de estado de cuenta para un cliente en un rango de fechas. " +
                    "Incluye el saldo de cada cuenta al inicio y fin del periodo, y el detalle de movimientos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
                    content = @Content(schema = @Schema(implementation = ReporteEstadoCuentaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cliente no encontrado o fechas inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/reportes")
    public ResponseEntity<ReporteEstadoCuentaResponse> reportes(
            @Valid EstadoCuentaFilter filter
    ) {
        return ResponseEntity.ok(reporteService.obtenerReporte(filter));
    }
}
