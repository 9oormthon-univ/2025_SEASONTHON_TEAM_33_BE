package com.goormthon.samsamejo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "test_value")
    private String testValue;

    @Builder
    private Test(Long id, String testValue) {
        this.id = id;
        this.testValue = testValue;
    }

    // 도메인 메서드
    public String updateValue(String testValue) {
        return this.testValue = testValue;
    }
}
