package com.cognisoftone.medicalRecord.controller;

import com.cognisoftone.medicalRecord.interfaces.MedicalRecordService;
import com.cognisoftone.medicalRecord.request.CreateMedicalRecordRequest;
import com.cognisoftone.medicalRecord.response.MedicalRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final Logger log = Logger.getLogger(MedicalRecordController.class.getName());

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MedicalRecordResponse> create(@RequestBody CreateMedicalRecordRequest request) {
        log.info("START CREATE MEDICAL RECORD");
        var response = medicalRecordService.create(request);
        log.info("END CREATE MEDICAL RECORD");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/patient/{patientId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(@PathVariable Long patientId) {
        log.info("START GET MEDICAL RECORDS BY PATIENT " + patientId);
        var response = medicalRecordService.getByPatientId(patientId);
        log.info("END GET MEDICAL RECORDS BY PATIENT");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/psychologist/{psychologistId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<MedicalRecordResponse>> getByPsychologist(@PathVariable Long psychologistId) {
        log.info("START GET MEDICAL RECORDS BY PSYCHOLOGIST " + psychologistId);
        var response = medicalRecordService.getByPsychologistId(psychologistId);
        log.info("END GET MEDICAL RECORDS BY PSYCHOLOGIST");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/patient/{patientId}/range",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<MedicalRecordResponse>> getByDateRange(
            @PathVariable Long patientId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        log.info("START GET MEDICAL RECORDS BY DATE RANGE for patient " + patientId);
        var response = medicalRecordService.getByPatientAndDateRange(patientId, from, to);
        log.info("END GET MEDICAL RECORDS BY DATE RANGE");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/patient/{patientId}/test/{testId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<MedicalRecordResponse>> getByTest(
            @PathVariable Long patientId,
            @PathVariable Long testId) {

        log.info("START GET MEDICAL RECORDS BY TEST for patient " + patientId);
        var response = medicalRecordService.getByPatientAndTest(patientId, testId);
        log.info("END GET MEDICAL RECORDS BY TEST");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}