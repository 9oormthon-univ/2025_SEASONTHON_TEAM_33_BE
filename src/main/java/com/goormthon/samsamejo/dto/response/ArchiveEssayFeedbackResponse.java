package com.goormthon.samsamejo.dto.response;

import com.goormthon.samsamejo.domain.Essay;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveEssayFeedbackResponse {
    private int sequence;             // 피드백 순번
    private String questionContent;   // 질문 내용
    private String essayContent;      // 답변 내용
    private String goodTitle;         // 장점 타이틀
    private String goodContent;       // 장점 내용
    private String badTitle;          // 단점 타이틀
    private String badContent;        // 단점 내용
    private String suggestionTitle;   // 제안 타이틀
    private String suggestionContent; // 제안 내용

    public static ArchiveEssayFeedbackResponse fromEntity(Essay essay) {
        return ArchiveEssayFeedbackResponse.builder()
                .sequence(1) // 단일 feedback → 항상 1, 추후 확장 가능
                .questionContent(essay.getQuestion().getContent())
                .essayContent(essay.getContent())
                .goodTitle("잘한 점") // 필요하면 AI가 따로 세분화
                .goodContent(essay.getGood())
                .badTitle("아쉬운 점")
                .badContent(essay.getBad())
                .suggestionTitle("개선 제안")
                .suggestionContent(essay.getSuggestion())
                .build();
    }
}
