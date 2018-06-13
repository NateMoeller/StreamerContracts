package com.nicknathanjustin.streamercontracts.exampleapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class ExampleApiController {

    @NonNull private final ExampleService exampleService;

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping(path = "restricted", method = RequestMethod.GET)
    public String getRestrictedAPI() {
        return "wow! you hit a restricted endpoint. good job!";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getExample() {
        log.info("Sample log message to test logging. Check cloudwatch logs to verify this works, {}", System.nanoTime());
        System.out.println("test syso output " + System.nanoTime());
        return exampleService.getExampleValue(UUID.randomUUID());
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public String getExampleById(@PathVariable("id") @NonNull final UUID uuid) {
        return exampleService.getExampleValue(uuid);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createExampleAPI(@RequestBody CreateExampleModelRequest createExampleModelRequest) {
        return "Created: " + exampleService.createExampleValue(createExampleModelRequest.getExampleValue()).toString();
    }
}