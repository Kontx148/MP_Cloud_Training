package edu.bbte.idde.vnim2413.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.vnim2413.model.UsedCarListing;
import edu.bbte.idde.vnim2413.service.CarListingServiceInterface;
import edu.bbte.idde.vnim2413.service.ServiceException;
import edu.bbte.idde.vnim2413.service.ServiceFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/usedCars")
public class UsedCarApiServlet extends HttpServlet {
    // JSON supporting object
    private final ObjectMapper mapper = new ObjectMapper();
    private final transient CarListingServiceInterface carListingService = ServiceFactory.getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String paramId = req.getParameter("id");

        if (paramId == null) {
            // No ID parameter -> returning all usedCars
            mapper.writeValue(resp.getWriter(), carListingService.getAllCarListings());
            return;
        }

        long id;
        try {
            id = Long.parseLong(paramId);
        } catch (NumberFormatException e) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return;
        }

        UsedCarListing usedCarListing;
        try {
            usedCarListing = carListingService.getById(id);
        } catch (ServiceException e) {
            sendJsonError(resp, e.getStatus(), e.getMessage());
            return;
        }

        mapper.writeValue(resp.getWriter(), usedCarListing);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            UsedCarListing newCar = mapper.readValue(req.getReader(), UsedCarListing.class);

            if (isInvalid(newCar)) {
                sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Error creating usedCarListing: "
                        + "Missing or invalid required parameters");
                return;
            }

            long createdId;
            try {
                createdId = carListingService.register(newCar);
            } catch (ServiceException e) {
                sendJsonError(resp, e.getStatus(), e.getMessage());
                return;
            }

            // Response will be 201 + the created listing id
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), Map.of("createdListingId", createdId));

        } catch (IOException e) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long id;
        try {
            id = validateId(req);
        } catch (ServletException e) {
            sendJsonError(resp, e.getStatus(), e.getMessage());
            return;
        }

        try {
            UsedCarListing updatedCarData = mapper.readValue(req.getReader(), UsedCarListing.class);

            // Check if parameters are valid for the obj
            if (isInvalid(updatedCarData)) {
                sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Error updating usedCarListing: "
                        + "Missing or invalid required parameters");
                return;
            }

            // Set the id from param
            updatedCarData.setId(id);

            try {
                carListingService.update(updatedCarData);
            } catch (ServiceException e) {
                sendJsonError(resp, e.getStatus(), e.getMessage());
                return;
            }

            mapper.writeValue(resp.getWriter(), updatedCarData);

        } catch (IOException e) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long id;
        try {
            id = validateId(req);
        } catch (ServletException e) {
            sendJsonError(resp, e.getStatus(), e.getMessage());
            return;
        }

        try {
            carListingService.delete(id);
        } catch (ServiceException e) {
            sendJsonError(resp, e.getStatus(), e.getMessage());
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private boolean isInvalid(UsedCarListing car) {
        return car.getMake() == null || car.getMake().isEmpty()
                || car.getModel() == null || car.getModel().isEmpty()
                || car.getUploadDate() == null || car.getUploadDate().isEmpty()
                || car.getFabricationYear() < 1885;
    }

    private long validateId(HttpServletRequest req) throws ServletException {
        // Helper function, checks if the parameter id is valid
        String paramId = req.getParameter("id");
        if (paramId == null) {
            throw new ServletException(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter 'id' for request");
        }
        long id;
        try {
            id = Long.parseLong(paramId);
        } catch (NumberFormatException e) {
            throw new ServletException(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }

        return id;
    }

    private void sendJsonError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), Map.of("errorValue", message));
    }

}
