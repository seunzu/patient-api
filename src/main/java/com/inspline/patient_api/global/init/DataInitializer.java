package com.inspline.patient_api.global.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("local")
public class DataInitializer implements ApplicationRunner {

    private final DynamoDbClient dynamoDbClient;
    private final DummyDataLoader dummyDataLoader;

    @Value("${dynamodb.table.users}")
    private String usersTableName;

    @Value("${dynamodb.table.patients}")
    private String patientsTableName;

    @Override
    public void run(ApplicationArguments args) {
        createTables();
        dummyDataLoader.load();
    }

    private void createTables() {
        createTableIfNotExists(usersTableName, "username");
        createTableIfNotExists(patientsTableName, "id");
    }

    private void createTableIfNotExists(String tableName, String partitionKey) {
        try {
            dynamoDbClient.describeTable(r -> r.tableName(tableName));
            log.info("[DataInitializer] Table '{}' already exists.", tableName);
        } catch (ResourceNotFoundException e) {
            dynamoDbClient.createTable(r -> r
                    .tableName(tableName)
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName(partitionKey)
                                    .attributeType(ScalarAttributeType.S)
                                    .build())
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName(partitionKey)
                                    .keyType(KeyType.HASH)
                                    .build()));

            dynamoDbClient.waiter()
                    .waitUntilTableExists(r -> r.tableName(tableName));

            log.info("[DataInitializer] Table '{}' created.", tableName);
        }
    }
}