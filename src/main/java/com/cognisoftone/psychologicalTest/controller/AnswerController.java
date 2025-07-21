package com.cognisoftone.psychologicalTest.controller;

import com.cognisoftone.psychologicalTest.interfaces.AnswerService;
import com.cognisoftone.psychologicalTest.model.AnswerModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;

    @RequestMapping(
            value = "/submit",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> submit(@RequestBody List<AnswerModel> request) {
        log.info("START SUBMIT ANSWERS");
        var response = answerService.saveAnswers(request);
        log.info("END SUBMIT ANSWERS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/test/{testId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getByTestId(@PathVariable Long testId) {
        log.info("START GET ANSWERS BY TEST ID");
        var response = answerService.getAnswersByTestId(testId);
        log.info("END GET ANSWERS BY TEST ID");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(
            value = "/user/{userId}/test/{testId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getByUserAndTest(@PathVariable Long userId, @PathVariable Long testId) {
        log.info("START GET ANSWERS BY USER AND TEST");
        var response = answerService.getAnswersByUserAndTest(userId, testId);
        log.info("END GET ANSWERS BY USER AND TEST");
        return ResponseEntity.ok(response);
    }
}

