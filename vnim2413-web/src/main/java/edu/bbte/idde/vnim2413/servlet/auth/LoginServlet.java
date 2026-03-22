package edu.bbte.idde.vnim2413.servlet.auth;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import edu.bbte.idde.vnim2413.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private final transient Handlebars handlebars = new Handlebars();

    private transient Template loginTemplate;

    @Override
    public void init() {
        try {
            loginTemplate = handlebars.compile("templates/login");
        } catch (IOException e) {
            throw new ServletException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not compile login template");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        Map<String, Object> model = new ConcurrentHashMap<>();
        String error = (String) req.getSession().getAttribute("loginError");
        model.put("contextPath", req.getContextPath());
        model.put("error", error != null ? error : "");
        req.getSession().removeAttribute("loginError");

        try {
            resp.getWriter().write(loginTemplate.apply(model));
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not render login page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("loggedIn", true);
            resp.sendRedirect(req.getContextPath() + "/usedCars/view");
        } else {
            req.getSession().setAttribute("loginError", "Invalid username or password");
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}