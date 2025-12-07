package com.roshan.MyCircle.controller;

import com.roshan.MyCircle.dto.LikeDto;
import com.roshan.MyCircle.dto.mapper.LikeDtoMapper;
import com.roshan.MyCircle.exception.TwitException;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.Like;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.service.LikeService;
import com.roshan.MyCircle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;


    @PostMapping("/{twitId}/likes")
    public ResponseEntity<LikeDto>likeTwit(@PathVariable Long twitId,
                                           @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException
    {
        User user = userService.findUserProfileByJwt(jwt);
        Like like = likeService.likeTwit(twitId,user);

        LikeDto likeDto = LikeDtoMapper.toLikeDto(like,user);

        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }

    @PostMapping("/twit/{twitId}")
    public ResponseEntity<List <LikeDto>>getAllLikes(@PathVariable Long twitId,
                                                   @RequestHeader("Authorization")String jwt)
            throws UserException, TwitException
    {
        User user = userService.findUserProfileByJwt(jwt);
        List<Like> like = likeService.getAllLikes(twitId);

        List<LikeDto> likeDtos = LikeDtoMapper.toLikeDtos(like,user);

        return new ResponseEntity<>(likeDtos, HttpStatus.CREATED);
    }

}
