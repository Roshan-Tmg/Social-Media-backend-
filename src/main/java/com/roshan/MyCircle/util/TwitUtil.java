package com.roshan.MyCircle.util;

import com.roshan.MyCircle.model.Like;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;

public class TwitUtil {

    public final static boolean isLikedByReqUser(User reqUser, Twit twit){

        for(Like like: twit.getLikes()){
            if (like.getUser().getId().equals(reqUser.getId())){
                return true;
            }
        }
        return false;

    }

    public final static boolean isRetwitedByReqUser(User reqUser, Twit twit){
        for (User user: twit.getRetwitsUser()){
            if(user.getId().equals(reqUser.getId())){
                return true;
            }
        }
        return false;
    }
}
