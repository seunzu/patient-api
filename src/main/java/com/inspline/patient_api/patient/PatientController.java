package com.inspline.patient_api.patient;

import com.inspline.patient_api.global.response.ApiResponse;
import com.inspline.patient_api.patient.dto.CreatePatientRequest;
import com.inspline.patient_api.patient.dto.PatientDetail;
import com.inspline.patient_api.patient.dto.PatientSummary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientSummary>>> getPatients() {
        return ResponseEntity.ok(ApiResponse.ok(patientService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDetail>> getPatient(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PatientDetail>> createPatient(
            @Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(patientService.create(request)));
    }
}