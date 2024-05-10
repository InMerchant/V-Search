package com.mysite.sbb.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.User.SiteUser;
import com.mysite.sbb.User.UserService;
import com.mysite.sbb.video.Video;

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

    // 현재 사용자 정보 가져오기
    public SiteUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }
        String username = authentication.getName();
        int userNo = userService.getUserNO(username);
        SiteUser user = new SiteUser();
        user.setUserNo(userNo);
        return user;
    }

    // 댓글 생성
    public Comment createComment(HttpServletRequest request, Video video) {
        String content = request.getParameter("content");
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("댓글 내용이 없습니다.");
        }
        
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(getCurrentUser()); // 현재 사용자 정보 설정
        comment.setVideo(video);
        return commentRepository.save(comment);
    }
    public void deleteComment(int commentId) {
        // 댓글 ID를 사용하여 댓글을 가져옴
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글을 찾을 수 없습니다."));
        
        // 현재 로그인한 사용자와 댓글을 작성한 사용자가 동일한지 확인
        SiteUser currentUser = getCurrentUser();
        if (comment.getUser().getUserNo() == currentUser.getUserNo()) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }
    }
 
    public List<Comment> getCommentsByVideoNumber(int videoNo) {
        return commentRepository.findByVideoVideoNo(videoNo);
    }
  
}
