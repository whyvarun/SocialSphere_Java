package com.socialsphere.servlet;

import com.socialsphere.dao.PostDAO;
import com.socialsphere.model.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class PostServlet extends HttpServlet {
    private PostDAO postDAO;

    @Override
    public void init() throws ServletException {
        try {
            postDAO = new PostDAO();
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

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        try {
            Post post = new Post(userId, title, content);
            boolean success = postDAO.createPost(post);

            if (success) {
                response.sendRedirect("posts.jsp");
            } else {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Post Creation Failed</h2>");
                response.getWriter().println("<p>Something went wrong. Please try again.</p>");
                response.getWriter().println("<a href='dashboard.jsp'>Back to Dashboard</a>");
                response.getWriter().println("</body></html>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2>Database Error</h2>");
            response.getWriter().println("<p>Please try again later.</p>");
            response.getWriter().println("<a href='dashboard.jsp'>Back to Dashboard</a>");
            response.getWriter().println("</body></html>");
        }
    }
}