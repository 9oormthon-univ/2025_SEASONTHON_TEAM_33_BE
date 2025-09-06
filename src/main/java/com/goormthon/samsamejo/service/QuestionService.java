package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.Question;
import com.goormthon.samsamejo.dto.response.QuestionResponse;
import com.goormthon.samsamejo.dto.response.RecruitmentQuestionsResponse;
import com.goormthon.samsamejo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public RecruitmentQuestionsResponse getQuestions(Long recruitmentId) {
        List<Question> questions = questionRepository.findByRecruitment_Id(recruitmentId);

        List<QuestionResponse> questionDtos = questions.stream()
                .map(q -> QuestionResponse.builder()
                        .questionId(q.getId())
                        .content(q.getContent())
                        .minLength(q.getMinLength())
                        .maxLength(q.getMaxLength())
                        .required(q.getRequired())
                        .build())
                .collect(Collectors.toList());

        return RecruitmentQuestionsResponse.builder()
                .questions(questionDtos)
                .build();
    }
}
