package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.users.UserModel;

public interface ContractService {

    /**
     * Creates the contract in the database.
     *
     * @param  proposer The UserModel that proposed the challenge.
     * @param  streamer The UserModel of the streamer.
     * @param  game The name of the game being streamed.
     * @param  description The description of the contract.
     */
    ContractModel createContract(UserModel proposer, UserModel streamer, String game, String description);
}
