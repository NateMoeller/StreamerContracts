package com.nicknathanjustin.streamercontracts.votes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteModelRepository extends CrudRepository<VoteModel, UUID> {
    Optional<VoteModel> findByContractIdAndVoterId(UUID contractId, UUID voterId);
}
