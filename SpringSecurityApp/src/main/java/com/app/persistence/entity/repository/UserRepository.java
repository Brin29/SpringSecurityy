package com.app.persistence.entity.repository;

import com.app.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    // Querys Methods
    // Optional examina si el usuario existe o no existe
    Optional<UserEntity> findUserEntityByUsername(String username);

    /*
    @Query("SELECT u FROM UserEntity u WHERE u.username = ?")
    Optional<UserEntity> findUser(String username);
    */
}
