// src/main/java/com/goormthon/samsamejo/dto/response/ArchiveEssayResponse.java
package com.goormthon.samsamejo.dto.response;

import com.goormthon.samsamejo.domain.Essay;
import com.goormthon.samsamejo.repository.EssayRepository;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ArchiveEssayResponse {
    private Long recruitmentId;
    private Long questionId;
    private Long essayId;
    private String questionCategory;
    private int reuseCount;
    private String currentUseDate;
    private String questionContent;
    private String essayContent;
    private List<String> applyCompanies;
    private List<String> tags;

    public static ArchiveEssayResponse fromEntity(Essay essay, EssayRepository essayRepository) {
        // ===== 날짜 포맷 =====
        String currentUseDate = null;
        if (essay.getUpdatedAt() != null) {
            currentUseDate = essay.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        // ===== 재사용 횟수 =====
        int reuseCount = essayRepository.countByQuestion_IdAndContent(
                essay.getQuestion().getId(),
                essay.getContent()
        );

        // ===== 재사용 기업 목록 =====
        List<String> applyCompanies = essayRepository.findAllByQuestion_IdAndContent(
                        essay.getQuestion().getId(),
                        essay.getContent()
                ).stream()
                .map(e -> e.getUserRecruitment().getRecruitment().getCompanyName())
                .distinct()
                .collect(Collectors.toList());

        // ===== 임시 질문 카테고리 =====
        String questionCategory = mapCategory(essay.getQuestion().getContent());

        return ArchiveEssayResponse.builder()
                .recruitmentId(essay.getQuestion().getRecruitment().getId())
                .questionId(essay.getQuestion().getId())
                .essayId(essay.getId())
                .questionCategory(questionCategory)
                .reuseCount(reuseCount)
                .currentUseDate(currentUseDate)
                .questionContent(essay.getQuestion().getContent())
                .essayContent(essay.getContent())
                .applyCompanies(applyCompanies)
                .tags(essay.getTagsAsList())
                .build();
    }

    /**
     * 임시 카테고리 매핑 로직 (content 기반 키워드 매칭)
     */
    private static String mapCategory(String content) {
        if (content.contains("지원동기")) return "지원동기";
        if (content.contains("경험") || content.contains("성과")) return "경험/역량";
        if (content.contains("성격") || content.contains("장단점")) return "성격/장단점";
        return "기타";
    }
}
