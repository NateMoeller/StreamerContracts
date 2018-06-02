package com.nicknathanjustin.streamercontracts.exampleapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ExampleApiController {

    @NonNull private final ExampleService exampleService;

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public String getExampleAPI(@PathVariable("id") @NonNull final UUID uuid) {
        return exampleService.getExampleValue(uuid);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createExampleAPI(@RequestBody CreateExampleModelRequest createExampleModelRequest) {
        return "Created: " + exampleService.createExampleValue(createExampleModelRequest.getExampleValue()).toString();
    }
}