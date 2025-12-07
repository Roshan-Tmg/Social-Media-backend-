package com.roshan.MyCircle.controller;

import com.roshan.MyCircle.dto.UserDto;
import com.roshan.MyCircle.dto.mapper.UserDtoMapper;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.service.UserService;
import com.roshan.MyCircle.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String jwt)
        throws UserException{

        User user = userService.findUserProfileByJwt(jwt);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setReq_user(true);

        return new ResponseEntity<UserDto>(userDto,HttpStatus.ACCEPTED);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId,
            @RequestHeader("Authorization") String jwt)
            throws UserException{

        User reqUser = userService.findUserProfileByJwt(jwt);
        User user = userService.findUserById(userId);

        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setReq_user(UserUtil.isRegUser(reqUser,user));
        userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser,user));

        return new ResponseEntity<UserDto>(userDto,HttpStatus.ACCEPTED);

    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUser(@RequestParam String query, @RequestHeader("Authorization") String jwt)
            throws UserException{

        User reqUser = userService.findUserProfileByJwt(jwt);
        List<User> user = userService.searchUser(query);

        List<UserDto> userDtos = UserDtoMapper.toUserDtos(user);

        return new ResponseEntity<>(userDtos,HttpStatus.ACCEPTED);

    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody User req, @RequestHeader("Authorization") String jwt)
            throws UserException{

        User reqUser = userService.findUserProfileByJwt(jwt);
        User user = userService.updateUser(reqUser.getId(),req);

        UserDto userDto = UserDtoMapper.toUserDto(user);

        return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);

    }


    @PutMapping("/{userId}/follow")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @RequestHeader("Authorization") String jwt)
            throws UserException{

        User reqUser = userService.findUserProfileByJwt(jwt);
        User user = userService.followUser(userId, reqUser);

        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser,user));

        return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);

    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<List<UserDto>> recommendFriends(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "2") int depth) throws UserException {

        List<User> recommendedUsers = userService.recommendFriends(userId, depth);

        List<UserDto> dtoList = recommendedUsers.stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/searchUser")
    public ResponseEntity<List<UserDto>> searchUser(@RequestParam("query") String query) throws UserException {
        List<User> users = userService.searchUsersByName(query); // fetch users from DB
        List<UserDto> userDtos = users.stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }
//    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
//        try {
//            List<User> users = userService.searchUsersByUsername(query);
//            return ResponseEntity.ok(users);
//        } catch (UserException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
//        }
//    }





}
