package com.mysite.sbb.comment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.mysite.sbb.User.UserService;

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
    public Comment createComment(Comment comment) {
        // 사용자 아이디를 기반으로 사용자 번호를 조회
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int userNo = userService.getUserNO(username);
        
        // Comment 객체에 사용자 번호 설정
        comment.getUser().setUserNo(userNo);
        
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
}
