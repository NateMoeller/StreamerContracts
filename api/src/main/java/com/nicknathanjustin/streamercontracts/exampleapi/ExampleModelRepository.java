package com.nicknathanjustin.streamercontracts.exampleapi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExampleModelRepository extends CrudRepository<ExampleModel, UUID> {
}
