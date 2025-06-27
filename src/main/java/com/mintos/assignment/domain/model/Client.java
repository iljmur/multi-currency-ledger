package com.mintos.assignment.domain.model;

import java.util.UUID;

public class Client {
    private final UUID id;
    private final String name;
    private final String email;

    public Client(UUID id, String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Client name must not be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Client email must be valid.");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
