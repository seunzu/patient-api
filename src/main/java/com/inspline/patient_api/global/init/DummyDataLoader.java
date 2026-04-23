package com.inspline.patient_api.global.init;

import com.inspline.patient_api.patient.PatientRepository;
import com.inspline.patient_api.patient.entity.PatientEntity;
import com.inspline.patient_api.user.UserRepository;
import com.inspline.patient_api.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataLoader {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public void load() {
        insertDummyUsers();
        insertDummyPatients();
    }

    private void insertDummyUsers() {
        if (userRepository.findByUsername("admin").isPresent()) {
            log.info("[DummyDataLoader] Dummy users already exist. Skipping.");
            return;
        }

        List.of(
                UserEntity.builder()
                        .username("admin")
                        .password("password123")
                        .build(),
                UserEntity.builder()
                        .username("user1")
                        .password("password123")
                        .build()
        ).forEach(userRepository::save);

        log.info("[DummyDataLoader] Dummy users inserted.");
    }

    private void insertDummyPatients() {
        if (patientRepository.findById("patient-001").isPresent()) {
            log.info("[DummyDataLoader] Dummy patients already exist. Skipping.");
            return;
        }

        List.of(
                PatientEntity.builder()
                        .id("patient-001")
                        .name("홍길동")
                        .dateOfBirth("1990-01-15")
                        .phone("010-1234-5678")
                        .insuranceNumber("INS-001")
                        .build(),
                PatientEntity.builder()
                        .id("patient-002")
                        .name("김철수")
                        .dateOfBirth("1985-03-22")
                        .phone("010-9876-5432")
                        .insuranceNumber("INS-002")
                        .build(),
                PatientEntity.builder()
                        .id("patient-003")
                        .name("이영희")
                        .dateOfBirth("1995-07-08")
                        .phone("010-5555-1234")
                        .insuranceNumber("INS-003")
                        .build()
        ).forEach(patientRepository::save);

        log.info("[DummyDataLoader] Dummy patients inserted.");
    }
}
