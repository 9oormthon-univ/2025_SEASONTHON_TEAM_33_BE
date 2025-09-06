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
public class ArchiveEssayDetailResponse {
    private Long questionId;          // 질문 ID
    private String content;           // 질문 본문
    private String questionCategory;  // 질문 카테고리 (임시 매핑)
    private String createdAt;         // 최초 작성일
    private String currentUseDate;    // 최근 사용일
    private int useCount;             // 사용 횟수
    private int length;               // 글자 수
    private Long essayId;             // 답변 ID
    private String essayContent;      // 답변 본문
    private List<String> companies;   // 재사용된 기업명
    private List<String> tags;        // 태그 목록

    public static ArchiveEssayDetailResponse fromEntity(Essay essay, EssayRepository essayRepository) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 사용 횟수 = 같은 질문 + 동일 답변 내용 에세이 개수
        int useCount = essayRepository.countByQuestion_IdAndContent(
                essay.getQuestion().getId(),
                essay.getContent()
        );

        // 사용 기업 목록
        List<String> companies = essayRepository.findAllByQuestion_IdAndContent(
                        essay.getQuestion().getId(),
                        essay.getContent()
                ).stream()
                .map(e -> e.getUserRecruitment().getRecruitment().getCompanyName())
                .distinct()
                .collect(Collectors.toList());

        // 카테고리 임시 매핑
        String questionCategory = mapCategory(essay.getQuestion().getContent());

        return ArchiveEssayDetailResponse.builder()
                .questionId(essay.getQuestion().getId())
                .content(essay.getQuestion().getContent())
                .questionCategory(questionCategory)
                .createdAt(essay.getCreatedAt() != null ? essay.getCreatedAt().format(formatter) : null)
                .currentUseDate(essay.getUpdatedAt() != null ? essay.getUpdatedAt().format(formatter) : null)
                .useCount(useCount)
                .length(essay.getContent() != null ? essay.getContent().length() : 0)
                .essayId(essay.getId())
                .essayContent(essay.getContent())
                .companies(companies)
                .tags(essay.getTagsAsList())
                .build();
    }

    private static String mapCategory(String content) {
        if (content.contains("지원동기")) return "지원동기";
        if (content.contains("경험") || content.contains("성과")) return "경험/역량";
        if (content.contains("성격") || content.contains("장단점")) return "성격/장단점";
        return "기타";
    }
}
