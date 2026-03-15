package com.devsu.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "ER001"),
    HTTP_MSG_NOT_READABLE(HttpStatus.BAD_REQUEST, "No es posible leer el mensaje", "ER002"),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "El argumento del método no es válido", "ER003"),
    MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "Falta el encabezado de la solicitud", "ER004"),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "No se encontró el recurso", "ER005"),
    DATA_ACCESS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Error de acceso a datos", "ER006"),
    NO_EXISTE_CLIENTE(HttpStatus.BAD_REQUEST, "No existe el cliente", "ER007"),
    NO_SE_PUEDE_ELIMINAR(HttpStatus.BAD_REQUEST,"No se puede eliminar porque tiene registros asociados", "ER008"),
    NO_EXISTE_CUENTA(HttpStatus.BAD_REQUEST, "No existe la cuenta", "ER009"),
    SALDO_INSUFICIENTE(HttpStatus.BAD_REQUEST, "Saldo insuficiente", "ER010"),
    TIPO_MOVIMIENTO_INVALIDO(HttpStatus.BAD_REQUEST, "Tipo de movimiento inválido", "ER011"),
    VALOR_MOVIMIENTO_INVALIDO(HttpStatus.BAD_REQUEST, "Valor del movimiento inválido", "ER012"),
    ;
    private final HttpStatus status;
    private final String message;
    private final String code;
}
