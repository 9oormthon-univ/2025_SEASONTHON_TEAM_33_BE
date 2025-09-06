package com.goormthon.samsamejo.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentQuestionsResponse {

    private List<QuestionResponse> questions; // 질문 리스트
}
