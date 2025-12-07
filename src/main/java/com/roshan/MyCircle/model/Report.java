package com.roshan.MyCircle.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User reporter; // who reported

    @ManyToOne
    private Twit reportedTwit; // which post

    private LocalDateTime reportedAt = LocalDateTime.now();
}
