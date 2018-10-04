package com.nicknathanjustin.streamercontracts.contracts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractModelRepository extends CrudRepository<ContractModel, UUID> {
}
