package com.cognisoftone.psychologicalTest.controller;

import com.cognisoftone.psychologicalTest.interfaces.TestService;
import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.psychologicalTest.request.AssignTestRequest;
import com.cognisoftone.psychologicalTest.request.SubmitTestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/psycologicalTests")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final TestService testService;

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> register(@RequestBody TestModel request) {
        log.info("START TEST REGISTER");
        var response = testService.createTest(request);
        log.info("END TEST REGISTER");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("START TEST GET BY ID");
        var response = testService.getTestById(id);
        log.info("END TEST GET BY ID");
        return ResponseEntity.of(response);
    }

    @RequestMapping(
            value = "/active",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllActive() {
        log.info("START GET ALL ACTIVE TESTS");
        var response = testService.getAllActiveTests();
        log.info("END GET ALL ACTIVE TESTS");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(
            value = "/assign",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> assign(@RequestBody AssignTestRequest request) {
        log.info("START ASSIGN TEST");
        var response = testService.assignTest(request);
        log.info("END ASSIGN TEST");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/link/{token}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTestByToken(@PathVariable String token) {
        log.info("START GET TEST BY TOKEN");
        var response = testService.getTestByToken(token);
        log.info("END GET TEST BY TOKEN");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(
            value = "/link/{token}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> submitTest(
            @PathVariable String token,
            @RequestBody SubmitTestRequest request
    ) {
        log.info("START SUBMIT TEST");
        testService.submitTestResponse(token, request);
        log.info("END SUBMIT TEST");
        return ResponseEntity.ok(Map.of("message", "Test submitted successfully"));
    }

}
