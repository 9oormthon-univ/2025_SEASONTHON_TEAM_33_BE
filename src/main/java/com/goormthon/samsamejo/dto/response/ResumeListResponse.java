package com.goormthon.samsamejo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeListResponse {

    private List<ResumeSummary> totalUserRecruitments;
    private List<ResumeSummary> completeUserRecruitments;
    private List<ResumeSummary> progressingUserRecruitments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumeSummary {
        private Long userRecruitmentId;
        private String company;
        private String title;
        private String writeStatus;
        private int progress;
        private int essayCount;
        private int questionCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate modifiedAt;
    }
}
