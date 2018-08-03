package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersApiController {

    @NonNull private final UserService userService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest.getTwitchUsername()).toString();
    }
}
