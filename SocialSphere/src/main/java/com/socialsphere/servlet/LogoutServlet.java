package com.socialsphere.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.invalidate();

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Logout Successful</h2>");
        response.getWriter().println("<p>You have been logged out successfully.</p>");
        response.getWriter().println("<a href='index.html'>Home</a>");
        response.getWriter().println("</body></html>");
    }
}