package com.tallerarte.service;

// El mensaje es una CLAVE de messages_reservas*.properties, asi los errores salen en el idioma activo
public class ReservaException extends RuntimeException {

    public ReservaException(String claveMensaje) {
        super(claveMensaje);
    }
}