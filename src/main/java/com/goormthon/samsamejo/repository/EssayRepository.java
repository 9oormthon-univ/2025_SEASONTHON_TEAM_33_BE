package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EssayRepository extends JpaRepository<Essay, Long> {

    // TODO: User 연동 시 활성화
    // Optional<Essay> findByUser_IdAndQuestion_Id(Long userId, Long questionId);
}
