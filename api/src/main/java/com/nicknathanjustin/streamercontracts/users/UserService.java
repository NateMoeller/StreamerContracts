package com.nicknathanjustin.streamercontracts.users;

public interface UserService {
    UserModel createUser(String twitchUsername);

    boolean userExists(String twitchUsername);

    void login(String twitchUsername);
}
