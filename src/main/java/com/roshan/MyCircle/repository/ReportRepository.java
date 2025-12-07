package com.roshan.MyCircle.repository;

import com.roshan.MyCircle.dto.ReportedPostDTO;
import com.roshan.MyCircle.model.Report;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReporterAndReportedTwit(User reporter, Twit twit);

    // Fetch all reports for posts that have been reported at least :count times
    @Query("SELECT r FROM Report r " +
            "WHERE r.reportedTwit.id IN (" +
            "SELECT r2.reportedTwit.id FROM Report r2 GROUP BY r2.reportedTwit HAVING COUNT(r2) >= :count" +
            ")")
    List<Report> findReportsForPostsWithMinCount(@Param("count") long count);

    List<Report> findByReportedTwit(Twit twit);

}
