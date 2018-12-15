package com.nicknathanjustin.streamercontracts.reports;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportModelRepository extends CrudRepository<ReportModel, UUID> {
}
