package com.inspline.patient_api.patient;

import com.inspline.patient_api.patient.entity.PatientEntity;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {

    List<PatientEntity> findAll();
    Optional<PatientEntity> findById(String id);
    PatientEntity save(PatientEntity entity);
}
