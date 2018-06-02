package com.nicknathanjustin.streamercontracts.exampleapi;

import java.util.UUID;

public interface ExampleService {
    String getExampleValue(UUID uuid);

    ExampleModel createExampleValue(String exampleValue);
}
