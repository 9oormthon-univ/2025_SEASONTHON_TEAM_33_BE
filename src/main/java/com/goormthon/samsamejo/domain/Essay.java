package com.goormthon.samsamejo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "essays")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Essay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // essayId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;   // 연결된 질문

    // TODO: User 연동 시 활성화
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 사용자가 작성한 자기소개서 답변

    // AI 피드백
    @Column(columnDefinition = "TEXT")
    private String good;

    @Column(columnDefinition = "TEXT")
    private String bad;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    private String tags;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
