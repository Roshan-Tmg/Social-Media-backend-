package com.roshan.MyCircle.repository;

import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TwitRepository extends JpaRepository<Twit, Long> {

    List<Twit> findAllByIsTwitTrueOrderByCreatedAtDesc();

    List<Twit> findByRetwitsUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);

    List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);

//    List<Twit> findAllTwitByFeedRanking();

    @Query("SELECT t From Twit t JOIN t.likes L where L.user.id=:userId")
    List<Twit>findByLikesUser_id(Long userId);


}
