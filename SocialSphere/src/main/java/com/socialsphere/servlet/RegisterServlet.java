package com.socialsphere.servlet;

import com.socialsphere.dao.UserDAO;
import com.socialsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        try {
            userDAO = new UserDAO();
        } catch (SQLException e) {
            // Log the exception and re-throw as ServletException to indicate initialization failure
            System.err.println("Failed to initialize UserDAO: " + e.getMessage());
            throw new ServletException("Database initialization failed.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        response.setContentType("text/html"); // Set content type for HTML response

        try {
            // Check if user already exists
            if (userDAO.userExists(username)) {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Registration Failed</h2>");
                response.getWriter().println("<p>Username '" + username + "' already exists. Please choose a different username.</p>");
                response.getWriter().println("<a href='register.html'>Back to Registration</a>");
                response.getWriter().println("</body></html>");
                return; // Stop further processing
            }

            // Check if email already exists
            if (userDAO.emailExists(email)) {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Registration Failed</h2>");
                response.getWriter().println("<p>Email '" + email + "' is already registered. Please use a different email or login.</p>");
                response.getWriter().println("<a href='register.html'>Back to Registration</a>");
                response.getWriter().println("</body></html>");
                return; // Stop further processing
            }

            // If username and email are unique, proceed with registration
            User newUser = new User(username, password, email);
            if (userDAO.registerUser(newUser)) {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Registration Successful</h2>");
                response.getWriter().println("<p>Welcome, " + username + "! Your account has been created.</p>");
                response.getWriter().println("<p><a href='login.html'>Login now</a></p>");
                response.getWriter().println("</body></html>");
            } else {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Registration Failed</h2>");
                response.getWriter().println("<p>An unexpected error occurred during registration. Please try again later.</p>");
                response.getWriter().println("<a href='register.html'>Back to Registration</a>");
                response.getWriter().println("</body></html>");
            }

        } catch (SQLException e) {
            // Log the exception for debugging purposes
            System.err.println("Database error during registration: " + e.getMessage());
            e.printStackTrace();

            // Inform the user about the error
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2>Registration Failed</h2>");
            response.getWriter().println("<p>A database error occurred. Please try again later or contact support.</p>");
            response.getWriter().println("<a href='register.html'>Back to Registration</a>");
            response.getWriter().println("</body></html>");
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();

            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2>Registration Failed</h2>");
            response.getWriter().println("<p>An unexpected error occurred. Please try again later.</p>");
            response.getWriter().println("<a href='register.html'>Back to Registration</a>");
            response.getWriter().println("</body></html>");
        }
    }
}
