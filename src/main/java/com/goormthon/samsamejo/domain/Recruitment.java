package com.goormthon.samsamejo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recruitments",
        indexes = {
            @Index(name = "ux_recruitment_external_id", columnList = "external_id", unique = true)
        }
)
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

    @Column(name = "external_id", nullable = false, unique = true, length = 50)
    private String externalId; // 공고 고유 ID

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;   // 기업/기관명

    @Column(nullable = false, length = 200)
    private String title;         // 공고 제목 (ex. 2025년도 하반기 채용 공고)

    @Column(name = "employment_type", nullable = false, length = 100)
    private String employmentType;      // 채용 유형 (정규직, 인턴 등)

    @Column(columnDefinition = "TEXT")
    private String description;  // 공고 설명 (세부 요약/업무 내용)

    @Column(columnDefinition = "TEXT")
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

    @ElementCollection(fetch = FetchType.LAZY)
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

    /**
     * 기존 엔티티를 새로운 데이터로 갱신 (externalId 제외)
     */
    public void updateFrom(Recruitment other) {
        if (other.getCompanyName() != null) this.companyName = other.getCompanyName();
        if (other.getTitle() != null) this.title = other.getTitle();
        if (other.getEmploymentType() != null) this.employmentType = other.getEmploymentType();
        if (other.getDescription() != null) this.description = other.getDescription();
        if (other.getRequirements() != null) this.requirements = other.getRequirements();
        if (other.getLocation() != null) this.location = other.getLocation();
        if (other.getExperienceLevel() != null) this.experienceLevel = other.getExperienceLevel();
        if (other.getDeadline() != null) this.deadline = other.getDeadline();
        if (other.getUrl() != null) this.url = other.getUrl();
        if (other.getCategory() != null) this.category = other.getCategory();
        if (other.getSkills() != null) this.skills = other.getSkills();
    }
}
