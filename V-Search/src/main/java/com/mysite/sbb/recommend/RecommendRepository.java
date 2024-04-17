package com.mysite.sbb.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Recommend findByVideoNoAndUserNo(int videoNo, int userNo); // videoNo의 타입 수정

    int countByVideoNoAndRecommendCheck(int videoNo, boolean recommendCheck); // videoNo의 타입 수정

    int countByVideoNoAndUserNoAndRecommendCheck(int videoNo, int userNo, boolean recommendCheck); // videoNo의 타입 수정
}