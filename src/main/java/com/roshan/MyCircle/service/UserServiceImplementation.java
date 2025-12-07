package com.roshan.MyCircle.service;

import com.roshan.MyCircle.config.JwtProvider;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImplementation implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;




    @Override
    public User findUserById(Long userId) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(()
                ->new UserException("User not found with id"+ userId));
        return user;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email= jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if (user==null){
            throw new UserException("User not found with email"+email);

        }
        return user;
    }

    @Override
    public User updateUser(Long userId, User req) throws UserException {

        User user = findUserById(userId);
        if(req.getFullName()!=null){
            user.setFullName(req.getFullName());

        }
        if(req.getImage()!=null){
            user.setImage(req.getImage());
        }
        if(req.getBackgroundImage()!=null){
            user.setBackgroundImage(req.getBackgroundImage());
        }
        if(req.getBirthDate()!=null){
            user.setBirthDate(req.getBirthDate());
        }
        if(req.getLocation()!=null){
            user.setLocation(req.getLocation());
        }
        if(req.getBio()!=null){
            user.setBio(req.getBio());
        }
        if(req.getWebsite()!=null) {
            user.setWebsite(req.getWebsite());

        }

        return userRepository.save(user);
    }

    @Override
    public User followUser(Long userId, User user) throws UserException {
        User followToUser = findUserById(userId);

        if(user.getFollowing().contains(followToUser) && followToUser.getFollowers().contains(user)){
            user.getFollowing().remove(followToUser);
            followToUser.getFollowers().remove(user);

        }
        else {
            user.getFollowing().add(followToUser);
            followToUser.getFollowers().add(user);
        }
        userRepository.save(followToUser);
        userRepository.save(user);

        return followToUser;
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }

//    @Override
//    public List<User> recommendFriends(Long userId, int depth) throws UserException {
//        User startUser = findUserById(userId);
//
//        Queue<User> queue = new LinkedList<>();
//        Set<Long> visited = new HashSet<>();
//        List<User> recommendations = new ArrayList<>();
//
//        queue.add(startUser);
//        visited.add(startUser.getId());
//
//        int currentDepth = 0;
//
//        while (!queue.isEmpty() && currentDepth < depth) {
//            int size = queue.size();
//            for (int i = 0; i < size; i++) {
//                User current = queue.poll();
//                for (User neighbor : current.getFollowing()) {
//                    if (!visited.contains(neighbor.getId())) {
//                        visited.add(neighbor.getId());
//                        queue.add(neighbor);
//
//                        // recommendation: not already a direct friend of startUser
//                        if (!startUser.getFollowing().contains(neighbor) && !neighbor.equals(startUser)) {
//                            recommendations.add(neighbor);
//                        }
//                    }
//                }
//            }
//            currentDepth++;
//        }
//
//        return recommendations;
//    }
@Override
public List<User> recommendFriends(Long userId, int depth) throws UserException {
    User startUser = findUserById(userId);

    Queue<User> queue = new LinkedList<>();
    Set<Long> visited = new HashSet<>();
    List<User> recommendations = new ArrayList<>();

    queue.add(startUser);
    visited.add(startUser.getId());

    int currentDepth = 0;

    while (!queue.isEmpty() && currentDepth < depth) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            User current = queue.poll();

            // Explore both followings and followers
            List<User> neighbors = new ArrayList<>();
            neighbors.addAll(current.getFollowing());
            neighbors.addAll(current.getFollowers());

            for (User neighbor : neighbors) {
                if (!visited.contains(neighbor.getId())) {
                    visited.add(neighbor.getId());
                    queue.add(neighbor);


                    if (!startUser.getFollowing().contains(neighbor) && !neighbor.equals(startUser)) {
                        recommendations.add(neighbor);
                    }
                }
            }
        }
        currentDepth++;
    }

    return recommendations;
}
    @Override
    public List<User> searchUsersByName(String fullName) throws UserException {
        List<User> users = userRepository.findByfullNameContainingIgnoreCase(fullName);

        if (users.isEmpty()) {
            throw new UserException("No users found with username containing: " + fullName);
        }

        return users;
    }


}
