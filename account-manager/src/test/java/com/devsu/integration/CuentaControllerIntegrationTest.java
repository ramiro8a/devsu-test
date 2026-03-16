package com.devsu.integration;

import com.devsu.domain.entity.ClienteRef;
import com.devsu.domain.repository.ClienteRefRepository;
import com.devsu.domain.repository.CuentaRepository;
import com.devsu.domain.repository.MovimientoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
class CuentaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private ClienteRefRepository clienteRefRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    private ClienteRef clienteRef;

    @BeforeEach
    void setUp() {
        movimientoRepository.deleteAll();
        cuentaRepository.deleteAll();
        clienteRefRepository.deleteAll();

        clienteRef = ClienteRef.builder()
                .id(1L)
                .identificacion("1234567890")
                .nombres("Juan Perez")
                .estado(0)
                .build();
        clienteRefRepository.save(clienteRef);
    }

    @Nested
    @DisplayName("POST /cuentas - Crear cuenta")
    class CrearCuenta {

        @Test
        @DisplayName("Debe crear una cuenta exitosamente con saldo inicial")
        void crearCuenta_conSaldoInicial_retorna201() throws Exception {
            var request = Map.of(
                    "clientId", 1L,
                    "tipoCuenta", 0,
                    "saldo", 1000.00,
                    "estado", 0
            );

            mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.nroCuenta").isNotEmpty())
                    .andExpect(jsonPath("$.tipoCuenta").value(0))
                    .andExpect(jsonPath("$.tipoCuentaDesc").value("Ahorro"))
                    .andExpect(jsonPath("$.saldoInicial").value(1000.00))
                    .andExpect(jsonPath("$.saldoDisponible").value(1000.00))
                    .andExpect(jsonPath("$.estado").value(0))
                    .andExpect(jsonPath("$.estadoDesc").value("Activo"))
                    .andExpect(jsonPath("$.movimientos", hasSize(1)));
        }

        @Test
        @DisplayName("Debe crear una cuenta con saldo cero sin movimientos")
        void crearCuenta_conSaldoCero_sinMovimientos() throws Exception {
            var request = Map.of(
                    "clientId", 1L,
                    "tipoCuenta", 1,
                    "saldo", 0.00,
                    "estado", 0
            );

            mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoCuenta").value(1))
                    .andExpect(jsonPath("$.tipoCuentaDesc").value("Corriente"))
                    .andExpect(jsonPath("$.saldoDisponible").value(0.00))
                    .andExpect(jsonPath("$.movimientos", hasSize(0)));
        }

        @Test
        @DisplayName("Debe fallar al crear cuenta con cliente inexistente")
        void crearCuenta_clienteInexistente_retorna400() throws Exception {
            var request = Map.of(
                    "clientId", 999L,
                    "tipoCuenta", 0,
                    "saldo", 500.00,
                    "estado", 0
            );

            mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("ER007"))
                    .andExpect(jsonPath("$.message").value("No existe el cliente"));
        }

        @Test
        @DisplayName("Debe fallar con validación cuando faltan campos requeridos")
        void crearCuenta_sinCamposRequeridos_retorna400() throws Exception {
            var request = Map.of("tipoCuenta", 0);

            mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("ER003"));
        }

        @Test
        @DisplayName("Debe fallar con tipo de cuenta inválido")
        void crearCuenta_tipoCuentaInvalido_retorna400() throws Exception {
            var request = Map.of(
                    "clientId", 1L,
                    "tipoCuenta", 5,
                    "saldo", 100.00,
                    "estado", 0
            );

            mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("ER003"));
        }
    }

    @Nested
    @DisplayName("GET /cuentas/{id} - Obtener cuenta por ID")
    class ObtenerCuentaPorId {

        @Test
        @DisplayName("Debe retornar cuenta existente")
        void obtenerCuenta_existente_retorna200() throws Exception {
            // Crear cuenta primero
            var request = Map.of(
                    "clientId", 1L,
                    "tipoCuenta", 0,
                    "saldo", 500.00,
                    "estado", 0
            );

            String response = mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andReturn().getResponse().getContentAsString();

            Long cuentaId = objectMapper.readTree(response).get("id").asLong();

            // Consultar la cuenta
            mockMvc.perform(get("/cuentas/{id}", cuentaId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(cuentaId))
                    .andExpect(jsonPath("$.saldoInicial").value(500.00))
                    .andExpect(jsonPath("$.saldoDisponible").value(500.00));
        }

        @Test
        @DisplayName("Debe retornar error cuando la cuenta no existe")
        void obtenerCuenta_noExiste_retorna400() throws Exception {
            mockMvc.perform(get("/cuentas/{id}", 9999L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("ER009"))
                    .andExpect(jsonPath("$.message").value("No existe la cuenta"));
        }
    }

    @Nested
    @DisplayName("GET /cuentas - Listar todas las cuentas")
    class ListarCuentas {

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay cuentas")
        void listarCuentas_sinCuentas_retornaListaVacia() throws Exception {
            mockMvc.perform(get("/cuentas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Debe retornar todas las cuentas creadas")
        void listarCuentas_conCuentas_retornaLista() throws Exception {
            // Crear dos cuentas
            var request1 = Map.of("clientId", 1L, "tipoCuenta", 0, "saldo", 100.00, "estado", 0);
            var request2 = Map.of("clientId", 1L, "tipoCuenta", 1, "saldo", 200.00, "estado", 0);

            mockMvc.perform(post("/cuentas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request1)));

            mockMvc.perform(post("/cuentas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request2)));

            mockMvc.perform(get("/cuentas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }
    }

    @Nested
    @DisplayName("PATCH /cuentas/{id} - Actualizar cuenta")
    class ActualizarCuenta {

        @Test
        @DisplayName("Debe actualizar tipo y estado de la cuenta")
        void actualizarCuenta_exitosamente() throws Exception {
            // Crear cuenta
            var createReq = Map.of("clientId", 1L, "tipoCuenta", 0, "saldo", 100.00, "estado", 0);
            String response = mockMvc.perform(post("/cuentas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReq)))
                    .andReturn().getResponse().getContentAsString();

            Long cuentaId = objectMapper.readTree(response).get("id").asLong();

            // Actualizar
            var updateReq = Map.of("tipoCuenta", 1, "estado", 1);

            mockMvc.perform(patch("/cuentas/{id}", cuentaId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReq)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoCuenta").value(1))
                    .andExpect(jsonPath("$.tipoCuentaDesc").value("Corriente"))
                    .andExpect(jsonPath("$.estado").value(1))
                    .andExpect(jsonPath("$.estadoDesc").value("Inactivo"));
        }

        @Test
        @DisplayName("Debe fallar al actualizar cuenta inexistente")
        void actualizarCuenta_noExiste_retorna400() throws Exception {
            var updateReq = Map.of("tipoCuenta", 1, "estado", 0);

            mockMvc.perform(patch("/cuentas/{id}", 9999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReq)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("ER009"));
        }
    }
}
