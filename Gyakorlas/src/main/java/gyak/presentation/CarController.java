package gyak.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import gyak.model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gyak.repository.jdbc.CarRepositoryJdbc;

import java.io.IOException;
import java.util.Collection;

@WebServlet("/api/cars")
public class CarController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarRepositoryJdbc repository = new CarRepositoryJdbc();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        logger.info("Starting car servlet....");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Requesting all cars");
        resp.setHeader("Content-Type", "application/json");

        Collection<Car> cars;
        try {
            int min = Integer.parseInt(req.getParameter("min"));
            int max = Integer.parseInt(req.getParameter("max"));
            cars = repository.filterByYear(min, max);
        } catch (NumberFormatException e) {
            cars = repository.getAllCars();
        }

        try {
            //objectMapper.writeValue(resp.getWriter(), cars);
            req.setAttribute("cars", cars);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception exception) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "asd xdd");
        }

    }

    @Override
    public void destroy() {
        logger.info("Destroying car servlet...");
    }
}
