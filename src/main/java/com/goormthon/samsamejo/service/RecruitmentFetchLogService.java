package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.RecruitmentFetchLog;
import com.goormthon.samsamejo.repository.RecruitmentFetchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitmentFetchLogService {

    private final RecruitmentFetchLogRepository logRepository;

    /**
     * 수집 실행 로그 저장
     */
    public RecruitmentFetchLog saveLog(String status, String source,
                                       int newCount, int updatedCount, int failedCount,
                                       String message) {
        RecruitmentFetchLog log = RecruitmentFetchLog.builder()
                .executedAt(LocalDateTime.now())
                .status(status)
                .source(source)
                .newCount(newCount)
                .updatedCount(updatedCount)
                .failedCount(failedCount)
                .message(message)
                .build();
        return logRepository.save(log);
    }

    /**
     * 가장 최근 실행 로그 조회
     */
    public Optional<RecruitmentFetchLog> getLastLog() {
        return logRepository.findTopByOrderByExecutedAtDesc();
    }
}
