// src/main/java/com/goormthon/samsamejo/controller/ArchiveController.java
package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.dto.response.ArchiveEssayDetailResponse;
import com.goormthon.samsamejo.dto.response.ArchiveEssayFeedbackResponse;
import com.goormthon.samsamejo.dto.response.ArchiveResponse;
import com.goormthon.samsamejo.dto.response.ApiResponse;
import com.goormthon.samsamejo.service.ArchiveService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/archives")
public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ArchiveResponse>> getArchives(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "RECENT") String order,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId
    ) {
        Sort sort;
        switch (order.toUpperCase()) {
            case "OLDEST":
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            default: // RECENT
                sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        }

        PageRequest pageable = PageRequest.of(page - 1, 10, sort);
        ArchiveResponse response = archiveService.getArchives(userId, search, pageable);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{essayId}")
    public ResponseEntity<ApiResponse<ArchiveEssayDetailResponse>> getArchiveDetail(
            @PathVariable Long essayId
    ) {
        ArchiveEssayDetailResponse response = archiveService.getArchiveDetail(essayId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{essayId}/feedbacks")
    public ResponseEntity<ApiResponse<Map<String, List<ArchiveEssayFeedbackResponse>>>> getEssayFeedbacks(
            @PathVariable Long essayId
    ) {
        List<ArchiveEssayFeedbackResponse> feedbacks = archiveService.getEssayFeedbacks(essayId);
        Map<String, List<ArchiveEssayFeedbackResponse>> data = new HashMap<>();
        data.put("feedbacks", feedbacks);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
