package com.goormthon.samsamejo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResumeDetailResponse {
    private Long recruitmentId;
    private List<QuestionWithEssay> questions;

    @Getter
    @Builder
    public static class QuestionWithEssay {
        private Long questionId;
        private String questionContent;
        private Integer minLength;
        private Integer maxLength;
        private Boolean required;
        private Long essayId;
        private String essayContent;
        private List<String> tags;
    }
}
