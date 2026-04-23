package com.inspline.patient_api.patient;

import com.inspline.patient_api.global.exception.ApplicationException;
import com.inspline.patient_api.patient.dto.CreatePatientRequest;
import com.inspline.patient_api.patient.dto.PatientDetail;
import com.inspline.patient_api.patient.dto.PatientSummary;
import com.inspline.patient_api.patient.entity.PatientEntity;
import com.inspline.patient_api.patient.exception.PatientErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public List<PatientSummary> findAll() {
        return patientRepository.findAll().stream()
                .map(entity -> new PatientSummary(
                        entity.getId(),
                        entity.getName(),
                        entity.getDateOfBirth()))
                .toList();
    }

    @Override
    public PatientDetail findById(String id) {
        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PatientErrorCode.PATIENT_NOT_FOUND));

        return new PatientDetail(
                entity.getId(),
                entity.getName(),
                entity.getDateOfBirth(),
                entity.getPhone(),
                entity.getInsuranceNumber());
    }

    @Override
    public PatientDetail create(CreatePatientRequest request) {
        PatientEntity entity = PatientEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .dateOfBirth(request.dateOfBirth())
                .phone(request.phone())
                .insuranceNumber(request.insuranceNumber())
                .build();

        PatientEntity saved = patientRepository.save(entity);

        return new PatientDetail(
                saved.getId(),
                saved.getName(),
                saved.getDateOfBirth(),
                saved.getPhone(),
                saved.getInsuranceNumber());
    }
}