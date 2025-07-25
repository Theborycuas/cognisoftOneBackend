package com.cognisoftone.appointment.controller;

import com.cognisoftone.appointment.interfaces.AppointmentService;
import com.cognisoftone.appointment.request.CreateAppointmentRequest;
import com.cognisoftone.appointment.request.UpdateSessionNotesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final Logger log = Logger.getLogger(AppointmentController.class.getName());

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentRequest request) {
        log.info("START CREATE APPOINTMENT");
        var response = appointmentService.create(request);
        log.info("END CREATE APPOINTMENT");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/psychologist/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getByPsychologist(@PathVariable Long id) {
        log.info("START GET APPOINTMENTS BY PSYCHOLOGIST " + id);
        var response = appointmentService.getByPsychologistId(id);
        log.info("END GET APPOINTMENTS BY PSYCHOLOGIST");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/patient/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getByPatient(@PathVariable Long id) {
        log.info("START GET APPOINTMENTS BY PATIENT " + id);
        var response = appointmentService.getByPatientId(id);
        log.info("END GET APPOINTMENTS BY PATIENT");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{id}/notes",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateNotes(
            @PathVariable Long id,
            @RequestBody UpdateSessionNotesRequest request) {
        log.info("START UPDATE SESSION NOTES FOR APPOINTMENT ID " + id);
        var response = appointmentService.updateNotes(id, request);
        log.info("END UPDATE SESSION NOTES FOR APPOINTMENT");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/psychologist/{id}/range",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getByPsychologistAndRange(
            @PathVariable Long id,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        log.info("START GET APPOINTMENTS BY PSYCHOLOGIST AND RANGE");
        var response = appointmentService.getByPsychologistIdAndDateRange(id, from, to);
        log.info("END GET APPOINTMENTS BY PSYCHOLOGIST AND RANGE");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{id}/cancel",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        log.info("START CANCEL APPOINTMENT ID " + id);
        var response = appointmentService.cancelAppointment(id);
        log.info("END CANCEL APPOINTMENT");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}