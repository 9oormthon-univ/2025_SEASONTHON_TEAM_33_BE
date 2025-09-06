package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.domain.Recruitment;
import com.goormthon.samsamejo.dto.request.RecruitmentEssayWriteRequest;
import com.goormthon.samsamejo.dto.response.*;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.service.EssayService;
import com.goormthon.samsamejo.service.RecruitmentService;
import com.goormthon.samsamejo.service.QuestionService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;
    private final QuestionService questionService;
    private final EssayService essayService;

    /**
     * 채용 공고 목록 조회
     */
    @GetMapping
    public ResponseEntity<PageResponse<RecruitmentListResponse>> getRecruitments(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String career,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(0) int size
    ) {
        Page<Recruitment> recruitments = recruitmentService.getRecruitments(
                keyword, order, location, career, category, page, size
        );

        List<RecruitmentListResponse> content = recruitments.getContent().stream()
                .map(RecruitmentListResponse::fromEntity)
                .toList();

        PageResponse.PageInfo pageInfo = new PageResponse.PageInfo(
                recruitments.getNumber() + 1,   // 0-index → 1-index 변환
                recruitments.getSize(),
                recruitments.getTotalElements(),
                recruitments.getTotalPages()
        );

        return ResponseEntity.ok(new PageResponse<>(content, pageInfo));
    }

    /**
     * 채용 공고 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentDetailResponse> getRecruitment(@PathVariable @Min(0) Long id) {
        return recruitmentService.getRecruitmentById(id)
                .map(RecruitmentDetailResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_RECRUITMENT));
    }

    /**
     * 채용 공고 API 연동 (공공데이터포털 → DB 저장)
     */
    @PostMapping("/fetch")
    public ResponseEntity<Map<String, Object>> fetchRecruitmentsFromApi() {
        int savedCount = recruitmentService.fetchAndSaveFromApi();

        Map<String, Object> response = new HashMap<>();
        response.put("savedCount", savedCount);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    /**
     * 채용 공고 질문 불러오기
     */
    @GetMapping("/{recruitmentId}/write")
    public ResponseEntity<ApiResponse<RecruitmentQuestionsResponse>> getRecruitmentQuestions(
            @PathVariable @Min(0) Long recruitmentId) {

        RecruitmentQuestionsResponse response = questionService.getQuestions(recruitmentId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * 채용 공고 자기소개서 작성
     */
    @PostMapping("/{recruitmentId}/write")
    public ResponseEntity<ApiResponse<Void>> writeEssays(
            @PathVariable @Min(0) Long recruitmentId,
            @RequestBody RecruitmentEssayWriteRequest request
    ) {
        essayService.writeEssays(recruitmentId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
