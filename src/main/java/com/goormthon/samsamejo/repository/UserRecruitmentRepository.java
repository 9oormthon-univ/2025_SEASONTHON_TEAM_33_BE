package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.UserRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRecruitmentRepository extends JpaRepository<UserRecruitment, Long> {
    List<UserRecruitment> findAllByUser_Id(Long userId);
}
