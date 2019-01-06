package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;

import lombok.Value;

@Value
public class PublicContract extends Contract {

    public PublicContract(ContractModel contract) {
        super(contract);
    }

}
