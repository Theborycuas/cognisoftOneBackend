package com.cognisoftone.medicalRecord.controller;

import com.cognisoftone.medicalRecord.interfaces.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/medical-history")
@RequiredArgsConstructor
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;
    private final Logger log = Logger.getLogger(MedicalHistoryController.class.getName());

    @RequestMapping(
            value = "/patient/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getFullHistoryByPatient(@PathVariable Long id) {
        log.info("START GET FULL MEDICAL HISTORY FOR PATIENT " + id);
        var response = medicalHistoryService.getFullHistoryByPatientId(id);
        log.info("END GET FULL MEDICAL HISTORY FOR PATIENT");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
