package edu.bbte.idde.vnim2413.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/usedCars/view")
public class UsedCarsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {

        Boolean loggedIn = (Boolean) request.getSession().getAttribute("loggedIn");

        if (loggedIn != null && loggedIn) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}