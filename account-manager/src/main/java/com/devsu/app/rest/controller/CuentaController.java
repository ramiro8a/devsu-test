package com.devsu.app.rest.controller;

import com.devsu.app.rest.request.CuentaRequest;
import com.devsu.app.rest.request.CuentaUpdateRequest;
import com.devsu.app.rest.response.CuentaResponse;
import com.devsu.domain.service.CuentaService;
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

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    @PostMapping("")
    public ResponseEntity<CuentaResponse> crea(
            @Valid @RequestBody CuentaRequest request
    ) {
        return new ResponseEntity<>(cuentaService.crear(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> recuperarUnico(
            @PathVariable final Long id
    ) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @GetMapping("")
    public ResponseEntity<List<CuentaResponse>> recuperarLista() {
        return ResponseEntity.ok(cuentaService.obtenerTodos());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualiza(
            @PathVariable final Long id,
            @Valid @RequestBody CuentaUpdateRequest request
            ) {
        return new ResponseEntity<>(cuentaService.actualizar(id, request), HttpStatus.OK);
    }
}
