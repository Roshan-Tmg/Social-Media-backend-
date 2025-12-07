package com.roshan.MyCircle.controller;

import com.roshan.MyCircle.dto.ReportedPostDTO;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.repository.ReportRepository;
import com.roshan.MyCircle.repository.TwitRepository;
import com.roshan.MyCircle.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TwitRepository twitRepository;

    @Autowired
    ReportService reportService;

    // Show posts with more than 4 reports
    @GetMapping("/reports")
    public List<ReportedPostDTO> getReportedPosts() {
        long minReports = 1; // Minimum reports to consider
        return reportService.getReportedPostsWithMinReports(minReports);
    }

    // Delete a post
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        Twit twit = twitRepository.findById(id).orElse(null);

        if (twit == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        // Count reports for this post
        int reportCount = reportRepository.findByReportedTwit(twit).size();

        if (reportCount < 4) {
            System.out.println("Post " + id + " has less than 4 reports: " + reportCount);
        }

        twitRepository.deleteById(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}

