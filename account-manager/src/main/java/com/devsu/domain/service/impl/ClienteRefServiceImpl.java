package com.devsu.domain.service.impl;

import com.devsu.app.messaging.dto.ClienteSincronizadoEvent;
import com.devsu.commons.enums.ErrorCode;
import com.devsu.commons.exceptions.AppException;
import com.devsu.domain.entity.ClienteRef;
import com.devsu.domain.repository.ClienteRefRepository;
import com.devsu.domain.service.ClienteRefService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClienteRefServiceImpl implements ClienteRefService {
    private final ClienteRefRepository clienteRefRepository;

    @Override
    public void crea(ClienteSincronizadoEvent data) {
        ClienteRef cliente = ClienteRef.builder()
                .id(data.clienteId())
                .identificacion(data.identificacion())
                .nombres(data.nombres())
                .estado(data.estado())
                .build();
        clienteRefRepository.save(cliente);
    }

    @Override
    public void actualiza(ClienteSincronizadoEvent data) {
        ClienteRef clienteRef = buscaPorId(data.clienteId());
        clienteRef.setIdentificacion(data.identificacion());
        clienteRef.setNombres(data.nombres());
        clienteRef.setEstado(data.estado());
        clienteRefRepository.save(clienteRef);
    }

    @Override
    public void elimina(ClienteSincronizadoEvent data) {
        try {
            clienteRefRepository.deleteById(data.clienteId());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.NO_SE_PUEDE_ELIMINAR);
        }
    }

    @Override
    public ClienteRef buscaPorId(Long id) {
        return clienteRefRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NO_EXISTE_CLIENTE));
    }
}
