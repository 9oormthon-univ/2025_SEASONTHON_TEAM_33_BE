package com.goormthon.samsamejo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recruitments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;   // 기업/기관명

    @Column(nullable = false, length = 200)
    private String title;         // 공고 제목 (ex. 2025년도 하반기 채용 공고)

    @Column(name = "employment_type", nullable = false, length = 100)
    private String employmentType;      // 채용 유형 (정규직, 인턴 등)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;   // 공고 설명 (세부 요약/업무 내용)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requirements;  // 지원 자격

    @Column(length = 100)
    private String location;      // 근무 지역

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", length = 20)
    private ExperienceLevel experienceLevel; // 신입/경력/둘 다

    @Column(nullable = false)
    private LocalDate deadline;   // 마감일

    @Column(columnDefinition = "TEXT")
    private String url;           // 원본 공고 URL

    @Column(name = "category", length = 100)
    private String category;      // 직무 분류 (NCS 분류명)

    @ElementCollection
    @CollectionTable(name = "recruitment_skills", joinColumns = @JoinColumn(name = "recruitment_id"))
    @Column(name = "skill")
    private List<String> skills;  // 기술 스택 (사용자 입력)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum ExperienceLevel {
        NEW,
        EXPERIENCED,
        BOTH
    }
}
