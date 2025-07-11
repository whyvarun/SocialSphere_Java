package com.socialsphere.servlet;

import com.socialsphere.dao.CommentDAO;
import com.socialsphere.model.Comment;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class CommentServlet extends HttpServlet {
    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        try {
            commentDAO = new CommentDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        String postIdStr = request.getParameter("postId");
        String content = request.getParameter("content");

        try {
            int postId = Integer.parseInt(postIdStr);
            Comment comment = new Comment(postId, userId, content);
            boolean success = commentDAO.createComment(comment);

            if (success) {
                response.sendRedirect("posts.jsp");
            } else {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Comment Failed</h2>");
                response.getWriter().println("<p>Something went wrong. Please try again.</p>");
                response.getWriter().println("<a href='posts.jsp'>Back to Posts</a>");
                response.getWriter().println("</body></html>");
            }

        }         catch (SQLException | NumberFormatException e) {
        e.printStackTrace();
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Error</h2>");
        response.getWriter().println("<p>An error occurred while processing your comment. Please ensure the post ID is valid and try again.</p>");
        response.getWriter().println("<a href='posts.jsp'>Back to Posts</a>");
        response.getWriter().println("</body></html>");
    }
}
}
