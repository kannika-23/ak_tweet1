package com.tweetapp.demo.controllers;

import com.tweetapp.demo.dtomodels.UserDto;
import com.tweetapp.demo.dtomodels.UserLoginDto;
import com.tweetapp.demo.dtomodels.UserPasswordDto;
import com.tweetapp.demo.exceptions.ParametersMismatchException;
//import com.tweetapp.demo.kafka.KafkaSender;
//import com.tweetapp.demo.kafka.KafkaProducer;
//import com.tweetapp.demo.kafka.KafkaProducer;
import com.tweetapp.demo.models.User;
import com.tweetapp.demo.security.JwtTokenUtil;
import com.tweetapp.demo.security.JwtUserDetailsService;
import com.tweetapp.demo.services.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid @NotNull UserLoginDto userLoginDto, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            log.debug("inside pensionerDetail - add Pensioner details - hasError");
            throw new ParametersMismatchException("Entered invalid parameters");
        }
        log.info("In User Controller - login");
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(userLoginDto.getEmail());
        String username = userService.loginCheck(userLoginDto);
        final String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("token","Bearer "+token);
        res.put("username",username);

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
        @PostMapping("/register")
        public ResponseEntity<Object> addUser(@RequestBody @Valid @NotNull UserDto user,  BindingResult result) throws ParametersMismatchException {
            if(result.hasErrors()) {
                log.info("inside pensionerDetail - add Pensioner details - hasError");
                System.out.println(result.getFieldError().toString());
                throw new ParametersMismatchException("Entered invalid parameters");
            }
            userService.addUser(user);
            return ResponseEntity.ok(HttpStatus.CREATED);
        }
    @PostMapping("/forgetPassword")
    public ResponseEntity<Object> forgetPassword(@RequestBody @Valid @NotNull UserPasswordDto userPasswordDto, BindingResult result) throws ParametersMismatchException {
        if(result.hasErrors()) {
            log.debug("inside forgotPassword - hasError");
            System.out.println(result.getFieldError().toString());
            throw new ParametersMismatchException("Entered invalid parameters");
        }
        userService.updatePassword(userPasswordDto);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/check")
    public ResponseEntity<Object> check(){
        return ResponseEntity.ok("tweets");
    }

    @GetMapping(value = "/users/all")
    public List<User> getAllUsers(){
        log.info("In getAll Users");
        return userService.getAllUsers();
    }

    @GetMapping(value = "user/search/{username}")
    public User getUserDetails(@PathVariable String username){
        log.info("In User Details");
        return userService.getUsersDetails(username);
    }
}
