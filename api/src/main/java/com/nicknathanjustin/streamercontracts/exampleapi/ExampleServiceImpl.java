package com.nicknathanjustin.streamercontracts.exampleapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService{

    @NonNull final ExampleModelRepository exampleModelRepository;

    @Override
    public String getExampleValue(@NonNull final UUID uuid) {
        final Optional<ExampleModel> exampleModel = exampleModelRepository.findById(uuid);
        return exampleModel.map(exampleModel1 -> "ExampleValue with uuid: " + uuid + " is: " + exampleModel1.getExampleValue())
                .orElseGet(() -> "No ExampleValue found with uuid: " + uuid);
    }

    @Override
    public ExampleModel createExampleValue(@NonNull final String exampleValue) {
        return exampleModelRepository.save(ExampleModel.builder().exampleValue(exampleValue).build());
    }
}