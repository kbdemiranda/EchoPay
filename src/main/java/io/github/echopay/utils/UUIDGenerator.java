package io.github.echopay.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator {

    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
