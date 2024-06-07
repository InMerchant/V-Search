package com.mysite.sbb.agechk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoChkRepository extends JpaRepository<VideoChk, Long> {
	List<VideoChk> findByVideoId(Long videoId);

    @Query("SELECT v.scriptimo, COUNT(*) FROM VIDEO_INFO v WHERE v.VIDEO_NO = :videoNo GROUP BY v.scriptimo")
    List<Object[]> countScriptimoByVideoNo(Long videoNo);

    @Query("SELECT v.nude, COUNT(*) FROM VIDEO_INFO v WHERE v.VIDEO_NO = :videoNo GROUP BY v.nude")
    List<Object[]> countNudeByVideoNo(Long videoNo);
}
