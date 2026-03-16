package com.devsu.domain.service.impl;

import com.devsu.app.rest.request.ClienteRequest;
import com.devsu.app.rest.request.ClienteRequestFixture;
import com.devsu.app.rest.response.ClienteResponse;
import com.devsu.domain.entity.Cliente;
import com.devsu.domain.entity.ClienteFixture;
import com.devsu.domain.provider.ClienteEventProducer;
import com.devsu.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteEventProducer clienteEventProducer;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void debeCrearClienteCorrectamente() {
        ClienteRequest request = ClienteRequestFixture.withDefault();

        Cliente clienteGuardado = ClienteFixture.withDefault();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        ClienteResponse response = clienteService.crea(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Jose Lema", response.nombres());
        assertEquals("0102030405", response.identificacion());
        assertEquals(1, response.estado());
    }

}