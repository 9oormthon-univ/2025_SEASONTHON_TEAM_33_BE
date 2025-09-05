package com.goormthon.samsamejo.dto.request.auth;

import org.springframework.web.multipart.MultipartFile;

public record CareerFileRequest(
        MultipartFile resume,
        MultipartFile portfolio
) {
}
