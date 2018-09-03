package com.nicknathanjustin.streamercontracts.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserModelRepository extends CrudRepository<UserModel, UUID> {
    List<UserModel> findByTwitchUsername(String twitchUsername);
}
