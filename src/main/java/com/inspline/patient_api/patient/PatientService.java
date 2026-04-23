package com.inspline.patient_api.patient;

import com.inspline.patient_api.patient.dto.CreatePatientRequest;
import com.inspline.patient_api.patient.dto.PatientDetail;
import com.inspline.patient_api.patient.dto.PatientSummary;

import java.util.List;

public interface PatientService {

    List<PatientSummary> findAll();
    PatientDetail findById(String id);
    PatientDetail create(CreatePatientRequest request);
}
