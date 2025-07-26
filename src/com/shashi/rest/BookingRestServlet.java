package com.shashi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shashi.beans.HistoryBean;
import com.shashi.beans.TrainException;
import com.shashi.constant.ResponseCode;
import com.shashi.dto.ApiResponse;
import com.shashi.service.BookingService;
import com.shashi.service.impl.BookingServiceImpl;
import com.shashi.utility.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookingApiServlet", urlPatterns = "/api/bookings/*")
public class BookingRestServlet extends HttpServlet {
	

    private static final long serialVersionUID = 1L;

    private static final int HTTP_UNPROCESSABLE_ENTITY = 422;

    private final BookingService bookingService = new BookingServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    /** POST /api/bookings → crear nueva reserva */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            HistoryBean booking = mapper.readValue(req.getInputStream(), HistoryBean.class);
            HistoryBean created = bookingService.createHistory(booking);

            JsonUtil.write(resp, HttpServletResponse.SC_CREATED,
                    new ApiResponse(true, "Booking created successfully"));

        } catch (TrainException te) {
            JsonUtil.write(resp, HTTP_UNPROCESSABLE_ENTITY, new ApiResponse(false, te.getMessage()));
        } catch (Exception e) {
            JsonUtil.write(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ApiResponse(false, e.getMessage()));
        }
    }

    /** GET /api/bookings/{email} → obtener historial por email */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = extractEmail(req, resp);
        if (email == null) return;

        try {
            List<HistoryBean> bookings = bookingService.getAllBookingsByCustomerId(email);
            if (bookings == null || bookings.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No bookings found");
                return;
            }

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), bookings);

        } catch (TrainException e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        }
    }

    /** Helper: saca el email del path /api/bookings/{email} */
    private String extractEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // e.g. "/cliente@email.com"
        if (path == null || "/".equals(path)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer email required");
            return null;
        }

        return path.substring(1); // remove leading "/"
    }

}
