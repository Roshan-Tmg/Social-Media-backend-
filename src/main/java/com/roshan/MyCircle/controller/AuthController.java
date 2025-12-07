package com.roshan.MyCircle.controller;

import com.roshan.MyCircle.config.JwtProvider;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.model.Varification;
import com.roshan.MyCircle.repository.UserRepository;
import com.roshan.MyCircle.response.AuthResponse;
import com.roshan.MyCircle.service.CustomUserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsServiceImplementation customUserDetails;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody User user) throws UserException {

        System.out.println("user"+user);

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String birthDate = user.getBirthDate();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist!= null){
            throw  new UserException("Email is already used by another account");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setBirthDate(birthDate);
        createdUser.setVerification(new Varification());

        User savedUser = userRepository.save(createdUser);



        String token = jwtProvider.generateToken(createdUser);

        AuthResponse res = new AuthResponse(token, true, "USER");


        return new ResponseEntity<AuthResponse>(res, HttpStatus.CREATED);
    }
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User user){
        String username = user.getEmail();
        String password = user.getPassword();

        // Authenticate user with username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get your user from database instead of casting authentication principal
        User loggedInUser = userRepository.findByEmail(username)
                ;

        // Generate token with your JwtProvider using your User model
        String token = jwtProvider.generateToken(loggedInUser);

        AuthResponse res = new AuthResponse(token, true, loggedInUser.getRole());

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);


//        AuthResponse res = new AuthResponse(token, true);
//
//
//        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        if (userDetails == null){
            System.out.println("user detail is null" + username);
            throw new BadCredentialsException("Invalid username");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            System.out.println("Password does not match"+ username);
            throw new BadCredentialsException("Invalid username or password");
        }
        System.out.println("Authentication sucessfull");
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
    }
}
