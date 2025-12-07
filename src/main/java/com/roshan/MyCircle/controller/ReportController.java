package com.roshan.MyCircle.controller;

import com.roshan.MyCircle.exception.TwitException;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.Report;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.repository.ReportRepository;
import com.roshan.MyCircle.repository.TwitRepository;
import com.roshan.MyCircle.repository.UserRepository;
import com.roshan.MyCircle.service.TwitService;
import com.roshan.MyCircle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TwitService twitService;
    @Autowired
    private UserService userService;

    @PostMapping("/{twitId}")
    public ResponseEntity<String> reportPost(@PathVariable Long twitId, @RequestParam Long userId) throws TwitException, UserException {
        Twit twit = twitService.findById(twitId);

        // Fetch the full User entity from your UserService
        User reporter = userService.findUserById(userId);

        // Prevent same user from reporting same post twice
        if(reportRepository.existsByReporterAndReportedTwit(reporter, twit)) {
            return ResponseEntity.badRequest().body("You already reported this post.");
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedTwit(twit);
        reportRepository.save(report);

        return ResponseEntity.ok("Post reported successfully");
    }

}
