package com.vinnik.chat.back.perstistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("registration/users")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/{nickname}")
    public User getUserByUserNickname(@PathVariable("nickname") String nickname) {
        return userService.findByNickname(nickname);
    }

    @GetMapping(value = "/")
    public List<User> getAllStudents() {
        return userService.findAll();
    }

    @PostMapping(path="/", consumes="application/json", produces="application/json")
    @SendTo("/response")
    public String[] saveOrUpdateUser(@RequestBody User user) {
        try {
            userValidator.validateNewUser(userService, user);

            String[] userData = new String[2];
            userData[0] = user.getUserName();
            userData[1] = user.getPassword();
            System.out.println("User = " + user.getUserName() + " " + user.getAvatar() + "!!!" + user.getPassword());
            userService.saveOrUpdateUser(user);
            System.out.println("User = " + user.getUserName() + " " + user.getAvatar() + "!!!" + user.getPassword());
            return userData;
        } catch (NicknameAlreadyExistsException e) {
            e.printStackTrace();
            return null;
        }
    }



    @PostMapping("/avatar/{nickname}")
    public ResponseEntity<?> setAvatar(@PathVariable String nickname, @RequestParam("avatar") MultipartFile file) {
        System.out.println(file.getName() + "<- it is filename");
        try {
            System.out.print(file.getName() + " " + file.getBytes());
            User user = userService.findByNickname(nickname);
            user.setAvatar(file.getBytes());
            userService.saveOrUpdateUser(user);
            System.out.println("Received avatar");
            return new ResponseEntity("User's avatar added successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{nickname}")
    public ResponseEntity<?> deleteUser(@PathVariable("nickname") String nickname) {
        userService.deleteUser(nickname);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Allows browsers to make preflight requests.
     */
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
