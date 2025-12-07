package com.roshan.MyCircle.dto.mapper;

import com.roshan.MyCircle.dto.TwitDto;
import com.roshan.MyCircle.dto.UserDto;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.util.TwitUtil;

import java.util.ArrayList;
import java.util.List;

public class TweetDtoMapper {

    public static TwitDto toTwitDto(Twit twit, User reqUser){

        UserDto user = UserDtoMapper.toUserDto(twit.getUser());

        boolean isLiked = TwitUtil.isLikedByReqUser(reqUser, twit);
        boolean isRetwited = TwitUtil.isRetwitedByReqUser(reqUser, twit);

        List<Long> retwitUserId = new ArrayList<>();

        for (User user1: twit.getRetwitsUser()){
            retwitUserId.add(user1.getId());
        }

        TwitDto twitDto = new TwitDto();
        twitDto.setId(twit.getId());
        twitDto.setContent(twit.getContent());
        twitDto.setCreatedAt(twit.getCreatedAt());
        twitDto.setImage(twit.getImage());
        twitDto.setTotalLikes(twit.getLikes().size());
        twitDto.setTotalReplies(twit.getReplyTwits().size());
        twitDto.setTotalRetweets(twit.getRetwitsUser().size());
        twitDto.setUser(user);
        twitDto.setLiked(isLiked);
        twitDto.setRetwit(isRetwited);
        twitDto.setRetwitUserId(retwitUserId);
        twitDto.setReplyTwits(toTwitDtos(twit.getReplyTwits(), reqUser));
        twitDto.setVideo(twit.getVideo());


        return twitDto;
    }

    public static List<TwitDto> toTwitDtos(List<Twit> twits, User reqUser){
        List<TwitDto> twitDtos = new ArrayList<>();

        for (Twit twit: twits){
            TwitDto twitDto = toReplyDto(twit, reqUser);
            twitDtos.add(twitDto);
        }
        return twitDtos;
    }

    private static TwitDto toReplyDto(Twit twit, User reqUser) {

        UserDto user = UserDtoMapper.toUserDto(twit.getUser());

        boolean isLiked = TwitUtil.isLikedByReqUser(reqUser, twit);
        boolean isRetwited = TwitUtil.isRetwitedByReqUser(reqUser, twit);

        List<Long> retwitUserId = new ArrayList<>();

        for (User user1: twit.getRetwitsUser()){
            retwitUserId.add(user1.getId());
        }


        TwitDto twitDto = new TwitDto();
        twitDto.setId(twit.getId());
        twitDto.setContent(twit.getContent());
        twitDto.setCreatedAt(twit.getCreatedAt());
        twitDto.setImage(twit.getImage());
        twitDto.setTotalLikes(twit.getLikes().size());
        twitDto.setTotalReplies(twit.getReplyTwits().size());
        twitDto.setTotalRetweets(twit.getRetwitsUser().size());
        twitDto.setUser(user);
        twitDto.setLiked(isLiked);
        twitDto.setRetwit(isRetwited);
        twitDto.setRetwitUserId(retwitUserId);

        twitDto.setVideo(twit.getVideo());

        if (twit.getUser() == null) {
            System.out.println("WARNING: Twit ID " + twit.getId() + " has null user.");
        }



        return twitDto;
    }
}
