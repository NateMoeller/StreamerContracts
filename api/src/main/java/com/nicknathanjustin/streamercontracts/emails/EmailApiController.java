package com.nicknathanjustin.streamercontracts.emails;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailApiController {

    @NonNull private final EmailService emailService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addEmail(@RequestBody @NonNull final CreateEmailRequest createEmailRequest) {
        emailService.createEmail(createEmailRequest.getName(), createEmailRequest.getEmail());

        return ResponseEntity.ok().build();
    }
}
