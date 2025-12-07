package com.roshan.MyCircle.controller;

import com.roshan.MyCircle.dto.TwitDto;
import com.roshan.MyCircle.dto.mapper.TweetDtoMapper;
import com.roshan.MyCircle.exception.TwitException;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.dto.TwitDto;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.request.TwitReplyRequest;
import com.roshan.MyCircle.response.ApiResponse;
import com.roshan.MyCircle.service.TwitService;
import com.roshan.MyCircle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/twits")
public class TwitController {

    @Autowired
    private TwitService twitService;

    @Autowired
    private UserService userService;

  @PostMapping("/create")
    public ResponseEntity<TwitDto> createTwit(@RequestBody Twit req,
                                              @RequestHeader("Authorization")String jwt)
                                throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        Twit twit = twitService.createTwit(req,user);

        TwitDto twitDto = TweetDtoMapper.toTwitDto(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.CREATED);

    }

    @PostMapping("/reply")
    public ResponseEntity<TwitDto> replyTwit(@RequestBody TwitReplyRequest req,
                                              @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        Twit twit = twitService.createdReply(req, user);

        TwitDto twitDto = TweetDtoMapper.toTwitDto(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.CREATED);

    }

    @PutMapping("/{twitId}/reply")
    public ResponseEntity<TwitDto> reTwit(@PathVariable Long twitId,
                                             @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        Twit twit = twitService.retwit(twitId,user);

        TwitDto twitDto = TweetDtoMapper.toTwitDto(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.OK);

    }
//find the tweet
    @GetMapping("/{twitId}")
    public ResponseEntity<TwitDto> findTwitById(@PathVariable Long twitId,
                                          @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        Twit twit = twitService.findById(twitId);

        TwitDto twitDto = TweetDtoMapper.toTwitDto(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.OK);

    }


    @DeleteMapping("/{twitId}")
    public ResponseEntity<ApiResponse> deleteTwit(@PathVariable Long twitId,
                                          @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        twitService.deleteTwitById(twitId,user.getId());

        ApiResponse res = new ApiResponse();
        res.setMessage("Tweet deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<List<TwitDto>> getAllTwit(
                                          @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        List <Twit> twit = twitService.findAllTwit();

        List<TwitDto> twitDto = TweetDtoMapper.toTwitDtos(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.OK);

    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<List<TwitDto>> getUsersAllTwit(@PathVariable Long userId,
            @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        List <Twit> twit = twitService.getUserTwit(user);

        List<TwitDto> twitDto = TweetDtoMapper.toTwitDtos(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.OK);

    }

    @PutMapping("/user/{userId}/likes")
    public ResponseEntity<List<TwitDto>> findTwitByLikesContainesUser(@PathVariable Long userId,
                                                         @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException{
        User user = userService.findUserProfileByJwt(jwt);

        List <Twit> twit = twitService.findByLikesContainsUser(user);

        List<TwitDto> twitDto = TweetDtoMapper.toTwitDtos(twit,user);
        return new ResponseEntity<>(twitDto, HttpStatus.OK);

    }








}
