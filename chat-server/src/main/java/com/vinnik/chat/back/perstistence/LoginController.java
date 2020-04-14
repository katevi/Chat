package com.vinnik.chat.back.perstistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login/users/")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    @SendTo("/response")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            UserValidator userValidator = new UserValidator();
            userValidator.validateUser(userService, user);
            return new ResponseEntity<>("Authorization completed successful", HttpStatus.OK);
        } catch (IncorrectLoginOrPasswordException e) {
            return new ResponseEntity<>("Incorrect login or password", HttpStatus.FORBIDDEN);
        }
    }
}
