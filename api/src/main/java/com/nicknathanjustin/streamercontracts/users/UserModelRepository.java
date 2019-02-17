package com.nicknathanjustin.streamercontracts.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserModelRepository extends CrudRepository<UserModel, UUID> {

    Page<UserModel> findAll(Pageable pageable);

    UserModel findByTwitchUsername(String twitchUsername);
}
