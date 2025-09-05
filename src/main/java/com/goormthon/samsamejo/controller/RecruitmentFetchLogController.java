package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.dto.response.RecruitmentStatusResponse;
import com.goormthon.samsamejo.service.RecruitmentFetchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
public class RecruitmentFetchLogController {

    private final RecruitmentFetchLogService fetchLogService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<RecruitmentStatusResponse> getLastFetchStatus() {
        return fetchLogService.getLastLog()
                .map(log -> RecruitmentStatusResponse.builder()
                        .lastRun(log.getExecutedAt())
                        .status(log.getStatus())
                        .newJobs(log.getNewCount())
                        .updatedJobs(log.getUpdatedCount())
                        .failedJobs(log.getFailedCount())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(
                        RecruitmentStatusResponse.builder()
                                .lastRun(null)
                                .status("NO_LOG")
                                .newJobs(0)
                                .updatedJobs(0)
                                .failedJobs(0)
                                .build()
                ));
    }
}
