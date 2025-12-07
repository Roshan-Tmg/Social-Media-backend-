package com.roshan.MyCircle.service;

import com.roshan.MyCircle.dto.ReportedPostDTO;
import com.roshan.MyCircle.model.Report;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.repository.ReportRepository;
import com.roshan.MyCircle.repository.TwitRepository;
import com.roshan.MyCircle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<ReportedPostDTO> getReportedPostsWithMinReports(long minCount) {
        // Fetch all reports where reportedTwit has at least minCount reports
        List<Report> reports = reportRepository.findReportsForPostsWithMinCount(minCount);

        // Group reports by the reported post
        Map<Twit, List<Report>> reportsByPost = reports.stream()
                .filter(r -> r.getReportedTwit() != null) // safety check
                .collect(Collectors.groupingBy(Report::getReportedTwit));

        List<ReportedPostDTO> result = new ArrayList<>();

        for (Map.Entry<Twit, List<Report>> entry : reportsByPost.entrySet()) {
            Twit post = entry.getKey();
            List<Report> postReports = entry.getValue();

            // Collect reporter names safely (skip null users)
            List<String> reporters = postReports.stream()
                    .map(Report::getReporter)
                    .filter(Objects::nonNull)
                    .map(User::getFullName)
                    .collect(Collectors.toList());

            String ownerName = post.getUser() != null ? post.getUser().getFullName() : "Unknown";

            result.add(new ReportedPostDTO(
                    post.getId(),
                    post.getContent(),
                    ownerName,
                    reporters,
                    (long) postReports.size()
            ));
        }

        return result;
    }

}

