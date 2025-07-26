package com.cognisoftone.users.controller;


import com.cognisoftone.users.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;


    @RequestMapping(
            value = "/psychologist/{id}/patients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPatientsOfPsychologist(@PathVariable Long id) {
        log.info("START GET PATIENTS OF PSYCHOLOGIST {}", id);
        var response = userService.getPatientsOfPsychologist(id);
        log.info("END GET PATIENTS OF PSYCHOLOGIST {}", id);
        return ResponseEntity.ok(response);
    }

}
