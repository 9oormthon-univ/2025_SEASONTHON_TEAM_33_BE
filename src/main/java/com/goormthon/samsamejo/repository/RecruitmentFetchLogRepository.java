package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.RecruitmentFetchLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentFetchLogRepository extends JpaRepository<RecruitmentFetchLog, Long> {

    // 가장 최근 실행 로그 1건 가져오기
    Optional<RecruitmentFetchLog> findTopByOrderByExecutedAtDesc();
}
