package com.nicknathanjustin.streamercontracts.contracts;

import java.util.UUID;

public interface ContractService {

    /**
     * Creates the contract in the database.
     *
     * @param  proposerId The UUID of the user that proposed the challenge.
     * @param  streamerId The UUID of the streamer.
     * @param  game The name of the game being streamed.
     * @param  description The description of the contract.
     */
    ContractModel createContract(UUID proposerId, UUID streamerId, String game, String description);
}
