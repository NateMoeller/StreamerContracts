package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper=true)
@Value
public class PublicContract extends Contract {

    public PublicContract(ContractModel contract) {
        super(contract);
    }

}
