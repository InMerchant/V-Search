package com.mysite.sbb.comment;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final VideoService videoService; // VideoService 추가


    @Autowired
    public CommentService(UserService userService, CommentRepository commentRepository, VideoService videoService) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.videoService = videoService; 
    }

   
 // 댓글 생성
    public Comment createComment(HttpServletRequest request, int videoNumber) {
        // 폼 데이터에서 댓글 내용을 추출
        String content = request.getParameter("content");

        // 사용자 아이디를 기반으로 사용자 번호를 조회
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userNo = userService.getUserNO(username);

        // Comment 객체에 사용자 정보 설정
        SiteUser user = new SiteUser();
        user.setUserNo(userNo);

        // 비디오 번호를 사용하여 비디오를 데이터베이스에서 가져옴
        Video video = videoService.getVideoByNo(videoNumber);

        // Comment 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setVideoNo(video);

        return commentRepository.save(comment);
    }



    // 댓글 조회
    public Comment getCommentById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    // 모든 댓글 조회
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // 댓글 수정
    public Comment updateComment(int id, Comment updatedComment) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        comment.setContent(updatedComment.getContent()); // 댓글 내용 수정 등 필요한 로직 추가

        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(int id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        commentRepository.delete(comment);
    }
    
    private int extractVideoNumber(String url) {
        // URL에서 "/video/" 이후의 숫자 부분 추출
        String[] parts = url.split("/");
        String lastPart = parts[parts.length - 1];
        // 숫자 부분 추출
        String videoNumberString = lastPart.replaceAll("[^0-9]", "");
        // 추출한 문자열을 정수로 변환하여 반환
        return Integer.parseInt(videoNumberString);
    }
    
}
