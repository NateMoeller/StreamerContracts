package com.nicknathanjustin.streamercontracts.emails;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailModelRepository extends CrudRepository<EmailModel, UUID> {
}
