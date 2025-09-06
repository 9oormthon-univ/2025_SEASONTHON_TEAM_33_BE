package com.goormthon.samsamejo.validator;

import com.goormthon.samsamejo.annotation.ValidFile;
import com.goormthon.samsamejo.util.FileUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return (file != null)
                && !file.isEmpty()
                && FileUtil.getFileExtension(file.getOriginalFilename()).equals("pdf");
    }
}
