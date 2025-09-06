package com.goormthon.samsamejo.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentEssayWriteRequest {
    private Long userRecruitmentId;     // 기존 자기소개서 ID (신규 작성이면 null)
    private List<EssayRequest> essays;  // 질문별 답변 리스트
}
