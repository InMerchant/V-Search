package com.mysite.sbb.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findByVideoNameContainingIgnoreCase(String videoName);
    List<Search> findByVideoNumber(int videoNumber); 
    Page<Search> findByVideoNameContaining(String keyword, Pageable pageable);

}

