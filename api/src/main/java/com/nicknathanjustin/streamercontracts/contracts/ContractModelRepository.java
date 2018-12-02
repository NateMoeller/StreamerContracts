package com.nicknathanjustin.streamercontracts.contracts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ContractModelRepository extends CrudRepository<ContractModel, UUID> {

    @Query("SELECT contractModel " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.settlesAt < :currentTimestamp " +
           "AND contractModel.state = 'ACCEPTED'")
    Set<ContractModel> findAllSettleableContracts(@Param("currentTimestamp") Timestamp now);
}
