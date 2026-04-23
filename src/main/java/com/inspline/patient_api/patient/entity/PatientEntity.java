package com.inspline.patient_api.patient.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class PatientEntity {

    private String id;
    private String name;
    private String dateOfBirth;
    private String phone;
    private String insuranceNumber;

    @DynamoDbPartitionKey
    public String getId() { return id; }
}