package com.devsu.domain.entity;

public class ClienteFixture {
    public static Cliente withDefault() {
        return Cliente.builder()
                .id(1L)
                .nombres("Jose Lema")
                .genero("M")
                .edad(30)
                .identificacion("0102030405")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .password("1234")
                .estado(1)
                .build();
    }
}