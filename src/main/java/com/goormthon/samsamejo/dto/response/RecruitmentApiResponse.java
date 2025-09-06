package com.goormthon.samsamejo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RecruitmentApiResponse {
    private int resultCode;
    private String resultMsg;
    private int totalCount;
    private List<Item> result;

    @Getter
    @Setter
    public static class Item {
        @JsonProperty("recrutPblntSn")
        private Long announcementId; // 공고 식별자

        @JsonProperty("instNm")
        private String companyName;  // 기관명

        @JsonProperty("recrutPbancTtl")
        private String title;        // 공고 제목

        @JsonProperty("hireTypeNmLst")
        private String employmentType;     // 채용 분야

        @JsonProperty("scrnprcdrMthdExpln")
        private String description;  // 공고 설명 (전형 절차 요약)

        @JsonProperty("aplyQlfcCn")
        private String requirements; // 지원 자격

        @JsonProperty("workRgnNmLst")
        private String location;     // 근무지

        @JsonProperty("recrutSeNm")
        private String career;       // 경력 구분 (신입/경력)

        @JsonProperty("pbancEndYmd")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        private LocalDate deadline;  // 마감일

        @JsonProperty("srcUrl")
        private String url;          // 원본 공고 URL

        @JsonProperty("ncsCdNmLst")
        private String ncsCdNmLst;   // NCS 분류명 (예: 보건.의료, 정보통신)
    }
}
