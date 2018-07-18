package com.nicknathanjustin.streamercontracts.exampleapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ExampleApiController {

    @Value("${frontEndUrl}")
    private String frontEndUrl;

    @NonNull private final ExampleService exampleService;

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping(path = "restricted", method = RequestMethod.GET)
    public String getRestrictedAPI() {
        return "wow! you hit a restricted endpoint. good job!";
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public String getExampleById(@PathVariable("id") @NonNull final UUID uuid) {
        return exampleService.getExampleValue(uuid);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createExampleAPI(@RequestBody CreateExampleModelRequest createExampleModelRequest) {
        return "Created: " + exampleService.createExampleValue(createExampleModelRequest.getExampleValue()).toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(frontEndUrl + "/profile");
    }
}
