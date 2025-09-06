// src/main/java/com/goormthon/samsamejo/controller/RecruitmentFetchLogController.java
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

    private static final String NO_LOG_STATUS = "NO_LOG";

    private final RecruitmentFetchLogService fetchLogService;

    /**
     * 최근 채용 공고 수집 실행 상태 조회 (관리자 전용)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<RecruitmentStatusResponse> getLastFetchStatus() {
        RecruitmentStatusResponse response = fetchLogService.getLastLog()
                .map(log -> RecruitmentStatusResponse.builder()
                        .lastRun(log.getExecutedAt())
                        .status(log.getStatus())
                        .newJobs(log.getNewCount())
                        .updatedJobs(log.getUpdatedCount())
                        .failedJobs(log.getFailedCount())
                        .build())
                .orElseGet(() -> RecruitmentStatusResponse.builder()
                        .lastRun(null)
                        .status(NO_LOG_STATUS)
                        .newJobs(0)
                        .updatedJobs(0)
                        .failedJobs(0)
                        .build());

        return ResponseEntity.ok(response);
    }
}
