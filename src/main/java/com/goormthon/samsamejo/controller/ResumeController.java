package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.dto.request.ResumeUpdateRequest;
import com.goormthon.samsamejo.dto.response.ApiResponse;
import com.goormthon.samsamejo.dto.response.ResumeDetailResponse;
import com.goormthon.samsamejo.dto.response.ResumeListResponse;
import com.goormthon.samsamejo.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-recruitments")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    /**
     * 1. 자기소개서 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ResumeListResponse>> getUserResumes(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        ResumeListResponse data = resumeService.getUserResumes(userId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 2. 특정 자기소개서 상세 조회
     */
    @GetMapping("/{userRecruitmentId}")
    public ResponseEntity<ApiResponse<ResumeDetailResponse>> getUserResumeDetail(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long userRecruitmentId
    ) {
        ResumeDetailResponse data = resumeService.getUserResumeDetail(userId, userRecruitmentId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 3. 자기소개서 수정/저장
     */
    @PutMapping("/{userRecruitmentId}")
    public ResponseEntity<ApiResponse<Void>> updateUserResume(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long userRecruitmentId,
            @RequestBody ResumeUpdateRequest request
    ) {
        resumeService.updateUserResume(userId, userRecruitmentId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
