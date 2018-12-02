package com.nicknathanjustin.streamercontracts.settings;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSettingsModelRepository extends CrudRepository<UserSettingsModel, UUID> {
    Optional<UserSettingsModel> findByUserId(UUID userId);
}

