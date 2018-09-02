package com.nicknathanjustin.streamercontracts.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModelRepository extends CrudRepository<UserModel, Long> {
    Long countByTwitchUsername(String twitchUsername);
    UserModel findByTwitchUsername(String twitchUsername);
}
