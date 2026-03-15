package com.devsu.domain.service;

import com.devsu.app.rest.request.ClienteRequest;
import com.devsu.app.rest.response.ClienteResponse;
import com.devsu.domain.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponse crea(ClienteRequest request);
    ClienteResponse actualiza(Long id, ClienteRequest request);
    ClienteResponse buscaPorId(Long id);
    Page<ClienteResponse> recuperarPaginado(Pageable pageable);
    void elimina(Long id);
    Cliente buscaClientePorId(Long id);
}
