package com.mysite.sbb.agechk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoChkRepository extends JpaRepository<VideoChk, Integer> {
	List<VideoChk> findByVideoNo(int videoNo);

    @Query("SELECT v.scriptimo, COUNT(*) FROM VideoChk v WHERE v.videoNo = :videoNo GROUP BY v.scriptimo")
    List<Object[]> countScriptimoByVideoNo(int videoNo);

    @Query("SELECT v.nude, COUNT(*) FROM VideoChk v WHERE v.videoNo = :videoNo GROUP BY v.nude")
    List<Object[]> countNudeByVideoNo(int videoNo);
}
