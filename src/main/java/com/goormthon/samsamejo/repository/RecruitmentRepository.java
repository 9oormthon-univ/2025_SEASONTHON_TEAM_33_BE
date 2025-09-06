package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query(value =
            "SELECT * FROM recruitments r " +
                    "WHERE (:keyword IS NULL " +
                    "   OR r.company_name LIKE CONCAT('%', :keyword, '%') " +
                    "   OR r.employment_type LIKE CONCAT('%', :keyword, '%') " +
                    "   OR r.description LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:location IS NULL OR r.location = :location) " +
                    "AND (:careersSize = 0 OR r.experience_level IN (:careers)) " +
                    "AND (:category IS NULL OR r.category = :category) ",
            countQuery =
                    "SELECT count(*) FROM recruitments r " +
                            "WHERE (:keyword IS NULL " +
                            "   OR r.company_name LIKE CONCAT('%', :keyword, '%') " +
                            "   OR r.employment_type LIKE CONCAT('%', :keyword, '%') " +
                            "   OR r.description LIKE CONCAT('%', :keyword, '%')) " +
                            "AND (:location IS NULL OR r.location = :location) " +
                            "AND (:careersSize = 0 OR r.experience_level IN (:careers)) " +
                            "AND (:category IS NULL OR r.category = :category)",
            nativeQuery = true)
    Page<Recruitment> findByConditionsNative(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("careers") List<String> careers,
            @Param("careersSize") int careersSize,
            @Param("category") String category,
            Pageable pageable
    );

    /**
     * 외부 공고 식별자(externalId)로 조회
     */
    Optional<Recruitment> findByExternalId(String externalId);
}
