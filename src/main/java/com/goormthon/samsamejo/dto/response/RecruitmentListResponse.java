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
                .skills(Collections.emptyList()) // 아직 스킬 필드 없으므로 빈 배열
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
}