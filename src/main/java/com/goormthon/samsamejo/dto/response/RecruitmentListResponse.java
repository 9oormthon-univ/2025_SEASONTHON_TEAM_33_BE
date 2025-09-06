package com.goormthon.samsamejo.dto.response;

import com.goormthon.samsamejo.domain.Recruitment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class RecruitmentListResponse {

    private Long recruitmentId;   // 채용 아이디
    private String title;         // 채용명
    private String company;       // 채용 기업
    private String location;      // 채용 지역
    private List<String> careers; // 경력
    private List<String> skills;  // 사용 기술
    private LocalDate deadline;   // 채용 마감일자

    public static RecruitmentListResponse fromEntity(Recruitment recruitment) {
        return RecruitmentListResponse.builder()
                .recruitmentId(recruitment.getId())
                .title(recruitment.getTitle())
                .company(recruitment.getCompanyName())
                .location(recruitment.getLocation())
                .careers(toKoreanCareers(recruitment.getExperienceLevel()))
                .skills(extractSkills(recruitment.getDescription()))
                .deadline(recruitment.getDeadline())
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