package com.roshan.MyCircle.service;

import com.roshan.MyCircle.exception.TwitException;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.request.TwitReplyRequest;

import java.util.List;

public interface TwitService {


    public Twit createTwit(Twit req, User user) throws UserException;
    public List<Twit>findAllTwit();
    public Twit retwit(Long twitId, User user) throws UserException, TwitException;

    public Twit findById(Long TwitId) throws TwitException;

    public void deleteTwitById(Long twitID, Long userId) throws TwitException,UserException;

    public Twit removeFromRetwit(Long twitId, User user)throws TwitException,UserException;
    public Twit createdReply(TwitReplyRequest req, User user) throws TwitException,UserException;

    public List<Twit> getUserTwit(User user);

    public  List<Twit>findByLikesContainsUser(User user);







}
