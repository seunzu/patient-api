package com.inspline.patient_api.user;

import com.inspline.patient_api.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByUsername(String username);
    UserEntity save(UserEntity entity);
}