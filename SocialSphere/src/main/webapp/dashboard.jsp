<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.socialsphere.model.User" %>
<%@ page import="com.socialsphere.dao.UserDAO" %>
<%
    // Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }

    // Get user statistics
    UserDAO userDAO = new UserDAO();
    int postCount = userDAO.getUserPostCount(user.getUserId());
    int commentCount = userDAO.getUserCommentCount(user.getUserId());
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - SocialSphere</title>
</head>
<body>
    <h1>Welcome to SocialSphere, <%= user.getUsername() %>!</h1>

    <h2>Your Statistics</h2>
    <p>Posts: <%= postCount %></p>
    <p>Comments: <%= commentCount %></p>
    <p>Member since: <%= user.getCreatedAt() %></p>

    <h2>Create New Post</h2>
    <form action="posts" method="post">
        <table>
            <tr>
                <td>Title:</td>
                <td><input type="text" name="title" required size="50"></td>
            </tr>
            <tr>
                <td>Content:</td>
                <td><textarea name="content" required rows="5" cols="50"></textarea></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Create Post">
                    <input type="reset" value="Clear">
                </td>
            </tr>
        </table>
    </form>

    <h2>Navigation</h2>
    <p><a href="posts.jsp">View All Posts</a></p>
    <p><a href="logout">Logout</a></p>
</body>
</html>