package com.devsu.domain.service.impl;

import com.devsu.app.rest.request.ClienteRequest;
import com.devsu.app.rest.response.ClienteResponse;
import com.devsu.commons.enums.ErrorCode;
import com.devsu.commons.exceptions.AppException;
import com.devsu.domain.entity.Cliente;
import com.devsu.domain.provider.ClienteEventProducer;
import com.devsu.domain.provider.dto.ClienteSincronizadoEvent;
import com.devsu.domain.repository.ClienteRepository;
import com.devsu.domain.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteEventProducer clienteEventProducer;

    @Transactional
    @Override
    public ClienteResponse crea(ClienteRequest request) {
        if(clienteRepository.existsByIdentificacion(request.identificacion())){
            throw new AppException(ErrorCode.LA_IDENTIFICAION_YA_EXISTE);
        }
        Cliente cliente = Cliente.builder()
                .nombres(request.nombres())
                .genero(request.genero())
                .edad(request.edad())
                .identificacion(request.identificacion())
                .direccion(request.direccion())
                .telefono(request.telefono())
                .estado(request.estado())
                .password(request.password())
                .build();
        cliente = clienteRepository.save(cliente);
        clienteEventProducer.publicarClienteCreado(cliente.toSincEvent("CLIENTE_CREADO"));
        return cliente.toResponse();
    }

    @Transactional
    @Override
    public ClienteResponse actualiza(Long id, ClienteRequest request) {
        Cliente cliente = buscaClientePorId(id);
        cliente.setNombres(request.nombres());
        cliente.setGenero(request.genero());
        cliente.setEdad(request.edad());
        cliente.setIdentificacion(request.identificacion());
        cliente.setDireccion(request.direccion());
        cliente.setTelefono(request.telefono());
        cliente.setEstado(request.estado());
        cliente.setPassword(request.password());
        cliente = clienteRepository.save(cliente);
        clienteEventProducer.publicarClienteActualizado(cliente.toSincEvent("CLIENTE_ACTUALIZADO"));
        return cliente.toResponse();
    }

    @Override
    public ClienteResponse buscaPorId(Long id) {
        return (buscaClientePorId(id)).toResponse();
    }

    @Override
    public Page<ClienteResponse> recuperarPaginado(Pageable pageable) {
        Page<Cliente> invitadosPage = clienteRepository.findAll(pageable);
        return invitadosPage.map(Cliente::toResponse);
    }

    @Transactional
    @Override
    public void elimina(Long id) {
        Cliente cliente = buscaClientePorId(id);
        ClienteSincronizadoEvent eventData = cliente.toSincEvent("CLIENTE_ELIMINADO");
        clienteRepository.deleteById(id);
        clienteEventProducer.publicarClienteEliminado(eventData);
    }

    @Override
    public Cliente buscaClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.NO_EXISTE_CLIENTE));
    }
}
