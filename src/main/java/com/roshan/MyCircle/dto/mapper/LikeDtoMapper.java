package com.roshan.MyCircle.dto.mapper;

import com.roshan.MyCircle.dto.LikeDto;
import com.roshan.MyCircle.dto.TwitDto;
import com.roshan.MyCircle.dto.UserDto;
import com.roshan.MyCircle.model.Like;
import com.roshan.MyCircle.model.User;

import java.util.ArrayList;
import java.util.List;

public class LikeDtoMapper {

    public static LikeDto toLikeDto(Like like, User reqUser){
        UserDto user = UserDtoMapper.toUserDto(like.getUser());
        UserDto reqUserDto = UserDtoMapper.toUserDto(reqUser);
        TwitDto twit = TweetDtoMapper.toTwitDto(like.getTwit(),reqUser);

        LikeDto likeDto = new LikeDto();
        likeDto.setId(like.getId());
        likeDto.setTwit(twit);
        likeDto.setUser(user);

        return likeDto;

    }
    public static List<LikeDto> toLikeDtos(List<Like>likes, User reqUser){
        List<LikeDto>likeDtos=new ArrayList<>();

        for (Like like : likes) {
            UserDto user = UserDtoMapper.toUserDto(like.getUser());
            TwitDto twit = TweetDtoMapper.toTwitDto(like.getTwit(),reqUser);

            LikeDto likeDto = new LikeDto();
            likeDto.setId(like.getId());
            likeDto.setTwit(twit);
            likeDto.setUser(user);
            likeDtos.add(likeDto);
        }
        return likeDtos;

    }
}
