package com.mysite.sbb.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.sbb.video.Video;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByVideoVideoNo(int videoNo);
}
