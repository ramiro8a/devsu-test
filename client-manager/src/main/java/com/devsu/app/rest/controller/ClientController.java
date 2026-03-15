package com.devsu.app.rest.controller;

import com.devsu.app.rest.request.ClienteRequest;
import com.devsu.app.rest.response.ClienteResponse;
import com.devsu.domain.service.ClienteService;
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

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/clientes")
public class ClientController {
    private final ClienteService clienteService;

    @PostMapping("")
    public ResponseEntity<ClienteResponse> crea(
            @Valid @RequestBody ClienteRequest request
    ) {
        return new ResponseEntity<>(clienteService.crea(request), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<ClienteResponse>> recuperarPaginado(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(clienteService.recuperarPaginado(pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualiza(
            @PathVariable final Long id,
            @Valid @RequestBody ClienteRequest clienteRequest
    ) {
        return new ResponseEntity<>(clienteService.actualiza(id, clienteRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(
            @PathVariable final Long id
    ) {
        clienteService.elimina(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
