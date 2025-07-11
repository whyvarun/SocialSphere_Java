package com.socialsphere.model;

import java.sql.Timestamp;

public class Post {
    private int postId;
    private int userId;
    private String username;
    private String title;
    private String content;
    private Timestamp createdAt;
    private int commentCount;

    // Constructors
    public Post() {}

    public Post(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}