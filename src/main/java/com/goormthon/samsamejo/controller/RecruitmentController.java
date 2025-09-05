package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.domain.Recruitment;
import com.goormthon.samsamejo.dto.response.PageResponse;
import com.goormthon.samsamejo.dto.response.RecruitmentListResponse;
import com.goormthon.samsamejo.dto.response.RecruitmentDetailResponse;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.service.RecruitmentService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

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
    public ResponseEntity<String> fetchRecruitmentsFromApi() {
        int savedCount = recruitmentService.fetchAndSaveFromApi();
        return ResponseEntity.ok("저장된 공고 개수: " + savedCount);
    }
}
