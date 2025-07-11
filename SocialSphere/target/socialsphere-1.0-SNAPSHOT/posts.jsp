<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.socialsphere.model.User" %>
<%@ page import="com.socialsphere.model.Post" %>
<%@ page import="com.socialsphere.model.Comment" %>
<%@ page import="com.socialsphere.dao.PostDAO" %>
<%@ page import="com.socialsphere.dao.CommentDAO" %>
<%@ page import="java.util.List" %>
<%
    // Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }

    // Get all posts
    PostDAO postDAO = new PostDAO();
    CommentDAO commentDAO = new CommentDAO();
    List<Post> posts = postDAO.getAllPosts();
%>
<!DOCTYPE html>
<html>
<head>
    <title>All Posts - SocialSphere</title>
</head>
<body>
    <h1>All Posts</h1>
    <p>Welcome, <%= user.getUsername() %>! | <a href="dashboard.jsp">Dashboard</a> | <a href="logout">Logout</a></p>

    <hr>

    <% if (posts.isEmpty()) { %>
        <p>No posts yet. Be the first to create one!</p>
    <% } else { %>
        <% for (Post post : posts) { %>
            <div style="border: 1px solid #ccc; margin: 10px 0; padding: 10px;">
                <h3><%= post.getTitle() %></h3>
                <p><strong>By:</strong> <%= post.getUsername() %> |
                   <strong>Date:</strong> <%= post.getCreatedAt() %> |
                   <strong>Comments:</strong> <%= post.getCommentCount() %></p>
                <p><%= post.getContent() %></p>

                <h4>Comments:</h4>
                <%
                    List<Comment> comments = commentDAO.getCommentsByPostId(post.getPostId());
                    if (comments.isEmpty()) {
                %>
                    <p><em>No comments yet.</em></p>
                <% } else { %>
                    <% for (Comment comment : comments) { %>
                        <div style="border-left: 3px solid #ddd; padding-left: 10px; margin: 5px 0;">
                            <p><strong><%= comment.getUsername() %></strong> - <%= comment.getCreatedAt() %></p>
                            <p><%= comment.getContent() %></p>
                        </div>
                    <% } %>
                <% } %>

                <h4>Add Comment:</h4>
                <form action="comment" method="post">
                    <input type="hidden" name="postId" value="<%= post.getPostId() %>">
                    <textarea name="content" required rows="3" cols="50"></textarea>
                    <br>
                    <input type="submit" value="Add Comment">
                </form>
            </div>
            <hr>
        <% } %>
    <% } %>

    <p><a href="dashboard.jsp">Back to Dashboard</a></p>
</body>
</html>