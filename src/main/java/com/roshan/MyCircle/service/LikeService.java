package com.roshan.MyCircle.service;

import com.roshan.MyCircle.exception.TwitException;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.Like;
import com.roshan.MyCircle.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LikeService {

    public Like likeTwit(Long twitId , User user) throws UserException, TwitException;

    public List<Like>getAllLikes(Long twitId) throws TwitException;


}
