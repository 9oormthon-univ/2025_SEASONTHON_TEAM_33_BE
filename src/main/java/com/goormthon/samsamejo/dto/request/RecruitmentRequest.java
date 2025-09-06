package com.goormthon.samsamejo.dto.request;

import com.goormthon.samsamejo.domain.Recruitment;
import lombok.*;

import java.time.LocalDate;

/**
 * 채용 공고 등록/수정 요청 DTO
 * (프론트 요청 → 엔티티 변환)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentRequest {

    private String companyName;
    private String employmentType;
    private String description;
    private String requirements;
    private String location;
    private String experienceLevel; // 문자열 ("신입", "경력", "신입+경력")
    private LocalDate deadline;
    private String url;

    public Recruitment toEntity() {
        return Recruitment.builder()
                .companyName(companyName)
                .employmentType(employmentType)
                .description(description)
                .requirements(requirements)
                .location(location)
                .experienceLevel(convertExperienceLevel(experienceLevel))
                .deadline(deadline)
                .url(url)
                .build();
    }

    /**
     * 문자열 → Enum 변환
     */
    private Recruitment.ExperienceLevel convertExperienceLevel(String level) {
        if (level == null) return null;
        return switch (level) {
            case "신입" -> Recruitment.ExperienceLevel.NEW;
            case "경력" -> Recruitment.ExperienceLevel.EXPERIENCED;
            case "신입+경력" -> Recruitment.ExperienceLevel.BOTH;
            default -> throw new IllegalArgumentException("잘못된 경력 구분 값: " + level);
        };
    }
}
