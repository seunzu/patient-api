package com.inspline.patient_api.patient;

import com.inspline.patient_api.patient.entity.PatientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${dynamodb.table.patients}")
    private String tableName;

    private DynamoDbTable<PatientEntity> table() {
        return dynamoDbEnhancedClient.table(
                tableName,
                TableSchema.fromBean(PatientEntity.class));
    }

    @Override
    public List<PatientEntity> findAll() {
        return table().scan().items().stream().toList();
    }

    @Override
    public Optional<PatientEntity> findById(String id) {
        PatientEntity entity = table().getItem(
                Key.builder()
                        .partitionValue(id)
                        .build());
        return Optional.ofNullable(entity);
    }

    @Override
    public PatientEntity save(PatientEntity entity) {
        table().putItem(entity);
        return entity;
    }
}
