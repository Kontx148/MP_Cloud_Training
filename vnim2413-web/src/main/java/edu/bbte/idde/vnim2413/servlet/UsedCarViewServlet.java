package edu.bbte.idde.vnim2413.servlet;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import edu.bbte.idde.vnim2413.service.CarListingServiceInterface;
import edu.bbte.idde.vnim2413.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/usedCars/view")
public class UsedCarViewServlet extends HttpServlet {

    private final transient Handlebars handlebars = new Handlebars();
    private transient CarListingServiceInterface carListingService;
    private transient Template usedCarsTemplate;

    @Override
    public void init() throws ServletException {
        super.init();
        carListingService = ServiceFactory.getUserService();

        try {
            usedCarsTemplate = handlebars.compile("templates/usedCars");
        } catch (IOException e) {
            throw new ServletException("Could not compile template", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        Map<String, Object> model = new ConcurrentHashMap<>();

        String contextPath = req.getContextPath();
        model.put("contextPath", contextPath);
        model.put("cars", carListingService.getAllCarListings());

        try {
            String html = usedCarsTemplate.apply(model);
            resp.getWriter().write(html);

        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not render used cars page");
        }
    }
}