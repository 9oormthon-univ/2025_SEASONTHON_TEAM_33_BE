package com.goormthon.samsamejo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthon.samsamejo.domain.Recruitment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class RecruitmentDetailResponse {

    private Long recruitmentId;       // 채용 공고 ID
    private String title;             // 채용 공고명
    private String company;           // 기업명
    private String location;          // 근무지
    private List<String> careers;     // 경력 (["신입"], ["경력"], ["신입","경력"])
    private String employmentType;    // 채용 유형 (정규직, 인턴 등)
    private String description;       // 상세 설명
    private List<String> skills;      // 기술 스택
    private LocalDate deadline;       // 마감일자
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;  // 생성일
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;  // 수정일
    private List<String> questions;   // 자기소개서 문항

    public static RecruitmentDetailResponse fromEntity(Recruitment recruitment) {
        return RecruitmentDetailResponse.builder()
                .recruitmentId(recruitment.getId())
                .title(recruitment.getTitle())
                .company(recruitment.getCompanyName())
                .location(recruitment.getLocation())
                .careers(toKoreanCareers(recruitment.getExperienceLevel()))
                .employmentType(recruitment.getEmploymentType() != null
                        ? recruitment.getEmploymentType()
                        : "기타")
                .description(recruitment.getDescription())
                .skills(extractSkills(recruitment.getDescription()))
                .deadline(recruitment.getDeadline())
                .createdAt(recruitment.getCreatedAt())
                .updatedAt(recruitment.getUpdatedAt())
                .questions(recruitment.getQuestions() != null
                        ? recruitment.getQuestions().stream()
                        .map(q -> q.getContent())
                        .toList()
                        : Collections.emptyList()
                )
                .build();
    }

    private static List<String> toKoreanCareers(Recruitment.ExperienceLevel level) {
        if (level == null) return List.of("미정");
        return switch (level) {
            case NEW -> List.of("신입");
            case EXPERIENCED -> List.of("경력");
            case BOTH -> List.of("신입", "경력");
        };
    }

    /**
     * description에서 특정 키워드를 찾아서 skills 리스트로 반환
     */
    private static List<String> extractSkills(String description) {
        if (description == null || description.isBlank()) {
            return List.of("미정");
        }

        // 기본 키워드 사전
        String lower = description.toLowerCase();
        List<String> skills = new java.util.ArrayList<>();

        if (lower.contains("java")) skills.add("Java");
        if (lower.contains("spring")) skills.add("Spring Boot");
        if (lower.contains("python")) skills.add("Python");
        if (lower.contains("django")) skills.add("Django");
        if (lower.contains("javascript")) skills.add("JavaScript");
        if (lower.contains("react")) skills.add("React");
        if (lower.contains("node")) skills.add("Node.js");
        if (lower.contains("mysql")) skills.add("MySQL");
        if (lower.contains("postgres")) skills.add("PostgreSQL");
        if (lower.contains("aws")) skills.add("AWS");
        if (lower.contains("docker")) skills.add("Docker");
        if (lower.contains("kubernetes")) skills.add("Kubernetes");

        return skills.isEmpty() ? List.of("미정") : skills;
    }
}
