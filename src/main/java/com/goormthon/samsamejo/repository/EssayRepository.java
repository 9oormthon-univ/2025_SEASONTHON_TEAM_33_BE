package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.Essay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssayRepository extends JpaRepository<Essay, Long> {

    // ================= 기본 조회 =================

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

    // ================= 아카이브 조회 =================

    /**
     * 특정 유저의 전체 답변 (페이징)
     */
    Page<Essay> findAllByUser_Id(Long userId, Pageable pageable);

    /**
     * 특정 유저의 답변 + 검색 (content OR question.content)
     */
    Page<Essay> findByUser_IdAndContentContainingOrUser_IdAndQuestion_ContentContaining(
            Long userId1, String content,
            Long userId2, String questionContent,
            Pageable pageable
    );

    /**
     * 같은 질문 + 같은 답변 내용을 가진 에세이 개수 (재사용 횟수 계산용)
     */
    int countByQuestion_IdAndContent(Long questionId, String content);

    /**
     * 같은 질문 + 같은 답변 내용을 가진 모든 에세이 (재사용 기업 목록 조회용)
     */
    List<Essay> findAllByQuestion_IdAndContent(Long questionId, String content);
}
