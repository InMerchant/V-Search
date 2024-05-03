package com.mysite.sbb.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.video.Video;
import com.mysite.sbb.video.VideoService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(UserService userService, CommentRepository commentRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    // 댓글 생성
    public Comment createComment(HttpServletRequest request, Video videoNumber) {
        // 새로운 댓글 객체 생성

        // 폼 데이터에서 댓글 내용을 추출
        String content = request.getParameter("content");
        
        // 댓글 내용이 없는 경우 처리
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("댓글 내용이 없습니다.");
        }

        try {
            // 사용자 인증 상태 확인
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalStateException("인증되지 않은 사용자입니다.");
            }
            Comment comment = new Comment();
            // 사용자 아이디를 기반으로 사용자 번호를 조회
            String username = authentication.getName();
            int userNo = userService.getUserNO(username);

            // Comment 객체에 사용자 정보 설정
            SiteUser user = new SiteUser();
            user.setUserNo(userNo);

            // Comment 객체 생성 및 설정
            comment.setContent(content);
            comment.setUser(user);
            comment.setVideoNo(videoNumber);
            return commentRepository.save(comment);
            
        } catch (Exception e) {
            // 저장 과정에서 예외가 발생한 경우 처리
            e.printStackTrace(); // 더 적합한 로깅 방식으로 변경하는 것이 좋습니다.
            throw new RuntimeException("댓글 저장 중 오류가 발생했습니다.");
        }
    }
}
