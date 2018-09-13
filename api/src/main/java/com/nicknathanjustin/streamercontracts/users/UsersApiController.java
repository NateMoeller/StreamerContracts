package com.nicknathanjustin.streamercontracts.users;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersApiController {

    @RequestMapping(method = RequestMethod.GET)
    public Principal user(@Nullable final Principal principal) {
        return principal;
    }

}
