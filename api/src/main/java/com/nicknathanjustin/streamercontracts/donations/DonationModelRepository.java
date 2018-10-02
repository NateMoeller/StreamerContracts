package com.nicknathanjustin.streamercontracts.donations;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DonationModelRepository extends CrudRepository<DonationModel, UUID> {
}
