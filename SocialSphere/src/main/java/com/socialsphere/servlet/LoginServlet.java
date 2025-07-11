package com.socialsphere.servlet;

import com.socialsphere.dao.UserDAO;
import com.socialsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        try {
            userDAO = new UserDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.loginUser(username, password);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());

                // Redirect to dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Login Failed</h2>");
                response.getWriter().println("<p>Invalid username or password!</p>");
                response.getWriter().println("<a href='login.html'>Try Again</a>");
                response.getWriter().println("</body></html>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2>Database Error</h2>");
            response.getWriter().println("<p>Please try again later.</p>");
            response.getWriter().println("<a href='login.html'>Try Again</a>");
            response.getWriter().println("</body></html>");
        }
    }
}