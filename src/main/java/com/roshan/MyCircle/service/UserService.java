package com.roshan.MyCircle.service;

import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.User;

import java.util.List;

public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;

    public User updateUser(Long userId, User user) throws UserException;

    public User followUser(Long userId, User user) throws UserException;

    public List<User> searchUser(String query);

    List<User> recommendFriends(Long userId, int depth) throws UserException;

    public List<User> searchUsersByName(String fullName) throws UserException;
}
