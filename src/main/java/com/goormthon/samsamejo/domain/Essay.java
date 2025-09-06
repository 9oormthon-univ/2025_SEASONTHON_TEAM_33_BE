package com.goormthon.samsamejo.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
    @Column(name = "essay_id")
    private Long id;   // 답변 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;   // 연결된 질문

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;          // 작성한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_recruitment_id", nullable = false)
    private UserRecruitment userRecruitment; // 속한 자기소개서(이력서)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 사용자가 작성한 자기소개서 답변

    // AI 피드백
    @Column(columnDefinition = "TEXT")
    private String good;

    @Column(columnDefinition = "TEXT")
    private String bad;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    @Column(columnDefinition = "TEXT")
    private String tags; // JSON 문자열로 저장되는 태그 목록

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ===== Lifecycle Callbacks =====
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Helper Methods =====
    /**
     * tags(JSON 문자열) → List<String> 변환
     */
    public List<String> getTagsAsList() {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(tags, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * tags 리스트 → JSON 문자열 저장
     */
    public void setTagsFromList(List<String> tagList) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.tags = mapper.writeValueAsString(tagList);
        } catch (Exception e) {
            this.tags = "[]";
        }
    }
}
