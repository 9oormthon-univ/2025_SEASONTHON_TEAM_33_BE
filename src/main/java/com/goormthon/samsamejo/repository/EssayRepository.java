package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssayRepository extends JpaRepository<Essay, Long> {

    /**
     * 특정 유저 + 질문 조합으로 작성한 답변 조회
     */
    Optional<Essay> findByUser_IdAndQuestion_Id(Long userId, Long questionId);

    /**
     * 특정 자기소개서(UserRecruitment)에 속한 모든 답변 조회
     */
    List<Essay> findAllByUserRecruitment_Id(Long userRecruitmentId);

    /**
     * 특정 유저가 특정 자기소개서(UserRecruitment) 안에서 작성한 모든 답변 조회
     */
    List<Essay> findAllByUser_IdAndUserRecruitment_Id(Long userId, Long userRecruitmentId);
}
