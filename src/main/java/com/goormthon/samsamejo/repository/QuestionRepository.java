package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * 특정 채용 공고에 속한 질문들 조회
     * @param recruitmentId 채용 공고 ID
     * @return 질문 리스트
     */
    List<Question> findByRecruitment_Id(Long recruitmentId);
}
