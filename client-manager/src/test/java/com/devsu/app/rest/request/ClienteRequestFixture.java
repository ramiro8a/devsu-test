package com.devsu.app.rest.request;

public class ClienteRequestFixture {
    public static ClienteRequest withDefault() {
        return new ClienteRequest(
                "Tux",
                "M",
                24,
                "12312314",
                "Las alturas",
                "123123123",
                0,
                "h4ckm3"
        );
    }
}