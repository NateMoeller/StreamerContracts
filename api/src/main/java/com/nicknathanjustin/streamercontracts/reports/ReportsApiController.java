package com.nicknathanjustin.streamercontracts.reports;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractService;
import com.nicknathanjustin.streamercontracts.reports.requests.CreateReportRequest;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Slf4j
public class ReportsApiController {

    @NonNull private final ContractService contractService;
    @NonNull private final ReportService reportService;
    @NonNull private final SecurityService securityService;
    @NonNull private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> createReport(@NonNull final HttpServletRequest httpServletRequest,
                                       @RequestBody @NonNull final CreateReportRequest createReportRequest) {

        final UserModel reportingUser = securityService.isAnonymousRequest(httpServletRequest) ?
                null :
                userService.getUserModelFromRequest(httpServletRequest);

        final UUID reportedContractId = createReportRequest.getReportedContractId();
        final ContractModel reportedContract = reportedContractId != null ? contractService.getContract(reportedContractId).orElse(null) : null;
        if (reportedContract == null) {
            log.warn("Received report for non existent contractID: {}", reportedContractId);
        }

        reportService.createReport(reportingUser,
                reportedContract,
                createReportRequest.getReport(),
                createReportRequest.getContactEmail());
        return ResponseEntity.ok().build();
    }
}
