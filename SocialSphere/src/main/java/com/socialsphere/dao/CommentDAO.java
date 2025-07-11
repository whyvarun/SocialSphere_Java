package com.socialsphere.dao;

import com.socialsphere.model.Comment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    private Connection connection;

    public CommentDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean createComment(Comment comment) throws SQLException {
        String query = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, comment.getPostId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Comment> getCommentsByPostId(int postId) throws SQLException {
        String query = """
            SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.username
            FROM comments c 
            JOIN users u ON c.user_id = u.user_id 
            WHERE c.post_id = ? 
            ORDER BY c.created_at ASC
        """;

        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setPostId(rs.getInt("post_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setUsername(rs.getString("username"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comments.add(comment);
            }
        }
        return comments;
    }
}