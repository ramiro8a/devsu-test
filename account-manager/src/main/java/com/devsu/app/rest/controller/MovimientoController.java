package com.devsu.app.rest.controller;

import com.devsu.app.rest.request.CuentaRequest;
import com.devsu.app.rest.request.MovimientoRequest;
import com.devsu.app.rest.response.CuentaResponse;
import com.devsu.app.rest.response.MovimientoResponse;
import com.devsu.domain.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/movimientos")
public class MovimientoController {
    private final MovimientoService movimientoService;

    @PostMapping("")
    public ResponseEntity<MovimientoResponse> crea(
            @Valid @RequestBody MovimientoRequest request
    ) {
        return new ResponseEntity<>(movimientoService.crear(request), HttpStatus.CREATED);
    }
}
