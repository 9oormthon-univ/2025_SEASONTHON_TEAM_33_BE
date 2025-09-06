package com.goormthon.samsamejo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_fetch_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentFetchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "executed_at", nullable = false)
    private LocalDateTime executedAt;

    @Column(nullable = false, length = 20)
    private String status; // SUCCESS / FAILED

    @Column(nullable = false, length = 50)
    private String source; // API / CRAWLER

    @Column(name = "new_count")
    private int newCount;   // 신규 건수

    @Column(name = "updated_count")
    private int updatedCount; // 갱신 건수

    @Column(name = "failed_count")
    private int failedCount;  // 실패 건수

    @Column(columnDefinition = "TEXT")
    private String message; // 상세 메시지
}
