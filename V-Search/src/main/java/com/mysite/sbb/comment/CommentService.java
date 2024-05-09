package com.mysite.sbb.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.video.Video;

import java.sql.SQLException;

import org.hibernate.Session;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private EntityManager entityManager;

    @Autowired
    public CommentService(UserService userService, CommentRepository commentRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    // 댓글 생성
    @Transactional
    public Comment createComment(HttpServletRequest request, Video video) {
        // 폼 데이터에서 댓글 내용을 추출
        String content = request.getParameter("content");
        
        // 댓글 내용이 없는 경우 처리
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("댓글 내용이 없습니다.");
        }

        // 사용자 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        // 사용자 아이디를 기반으로 사용자 번호를 조회
        String username = authentication.getName();
        int userNo = userService.getUserNO(username);
        
        // Comment 객체 생성 및 설정
        Comment comment = new Comment();
        
        // 사용자 정보 설정
        SiteUser user = new SiteUser();
        user.setUserNo(userNo);
        comment.setUser(user);
        comment.setVideo(video);
        
     
        
        return commentRepository.save(comment);
    }
}
