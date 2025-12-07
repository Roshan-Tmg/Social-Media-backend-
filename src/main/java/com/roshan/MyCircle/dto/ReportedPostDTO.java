package com.roshan.MyCircle.dto;

import com.roshan.MyCircle.model.Report;
import com.roshan.MyCircle.repository.ReportRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data

public class ReportedPostDTO {

    ReportRepository reportRepository;

    private Long twitId;
    private String twitContent;
    private String ownerName;        // post owner
    private List<String> reporters;  // all reporters
    private Long reportCount;

    public ReportedPostDTO(Long twitId, String twitContent, String ownerName, List<String> reporters, Long reportCount) {
        this.twitId = twitId;
        this.twitContent = twitContent;
        this.ownerName = ownerName;
        this.reporters = reporters;
        this.reportCount = reportCount;
    }




}

