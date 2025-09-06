package com.goormthon.samsamejo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecruitmentStatusResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastRun;

    private String status;   // SUCCESS / FAILED

    private int newJobs;
    private int updatedJobs;
    private int failedJobs;
}
