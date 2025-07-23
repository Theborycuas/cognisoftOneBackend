package com.cognisoftone.psychologicalTest.controller;

import com.cognisoftone.psychologicalTest.interfaces.QuestionService;
import com.cognisoftone.psychologicalTest.model.QuestionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> register(@RequestBody QuestionModel request) {
        log.info("START QUESTION REGISTER");
        var response = questionService.createQuestion(request);
        log.info("END QUESTION REGISTER");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/test/{testId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getByTestId(@PathVariable Long testId) {
        log.info("START GET QUESTIONS BY TEST ID");
        var response = questionService.getQuestionsByTestId(testId);
        log.info("END GET QUESTIONS BY TEST ID");
        return ResponseEntity.ok(response);
    }
}
