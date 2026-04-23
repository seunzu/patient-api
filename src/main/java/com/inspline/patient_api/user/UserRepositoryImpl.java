package com.inspline.patient_api.user;

import com.inspline.patient_api.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${dynamodb.table.users}")
    private String tableName;

    private DynamoDbTable<UserEntity> table() {
        return dynamoDbEnhancedClient.table(
                tableName,
                TableSchema.fromBean(UserEntity.class));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        UserEntity entity = table().getItem(
                Key.builder()
                        .partitionValue(username)
                        .build());
        return Optional.ofNullable(entity);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        table().putItem(entity);
        return entity;
    }
}
