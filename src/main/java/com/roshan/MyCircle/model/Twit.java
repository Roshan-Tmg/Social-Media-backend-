package com.roshan.MyCircle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Twit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne

    private User user;

    private String content;
    private String image;
    private String video;

    @OneToMany(mappedBy = "twit", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "replyFor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Twit> replyTwits = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<User> retwitsUser = new ArrayList<>();

    @ManyToOne
    @JsonBackReference
    private Twit replyFor;

    @Transient
    private int feedScore;

    private boolean isReply;
    private boolean isTwit;

    private LocalDateTime createdAt;
}


