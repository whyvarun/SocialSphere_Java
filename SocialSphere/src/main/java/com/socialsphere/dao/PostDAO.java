package com.socialsphere.dao;

import com.socialsphere.model.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    private Connection connection;

    public PostDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean createPost(Post post) throws SQLException {
        String query = "INSERT INTO posts (user_id, title, content) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getTitle());
            pstmt.setString(3, post.getContent());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        String query = """
            SELECT p.post_id, p.user_id, p.title, p.content, p.created_at, 
                   u.username, COUNT(c.comment_id) as comment_count
            FROM posts p 
            JOIN users u ON p.user_id = u.user_id 
            LEFT JOIN comments c ON p.post_id = c.post_id
            GROUP BY p.post_id, p.user_id, p.title, p.content, p.created_at, u.username
            ORDER BY p.created_at DESC
        """;

        List<Post> posts = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setCommentCount(rs.getInt("comment_count"));
                posts.add(post);
            }
        }
        return posts;
    }

    public Post getPostById(int postId) throws SQLException {
        String query = """
            SELECT p.post_id, p.user_id, p.title, p.content, p.created_at, u.username
            FROM posts p 
            JOIN users u ON p.user_id = u.user_id 
            WHERE p.post_id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                return post;
            }
        }
        return null;
    }
}