package com.shashi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shashi.beans.TrainBean;
import com.shashi.beans.TrainException;
import com.shashi.constant.ResponseCode;
import com.shashi.dto.ApiResponse;
import com.shashi.service.TrainService;
import com.shashi.service.impl.TrainServiceImpl;
import com.shashi.utility.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TrainApiServlet", urlPatterns = "/api/trains/*")
public class AdminAddTrainRest extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int HTTP_NOT_FOUND = 404;

    /** Java 8 no trae esta constante */
    private static final int HTTP_UNPROCESSABLE_ENTITY = 422;

    private final TrainService trainService = new TrainServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    /** POST /api/trains  -> crear */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            TrainBean train = mapper.readValue(req.getInputStream(), TrainBean.class);
            String result = trainService.addTrain(train);

            if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(result)) {
                JsonUtil.write(resp, HttpServletResponse.SC_CREATED,
                        new ApiResponse(true, "Train added successfully"));
            } else {
                JsonUtil.write(resp, HttpServletResponse.SC_BAD_REQUEST,
                        new ApiResponse(false, "Error adding train: " + result));
            }

        } catch (TrainException te) {
            JsonUtil.write(resp, HTTP_UNPROCESSABLE_ENTITY, new ApiResponse(false, te.getMessage()));
        } catch (Exception e) {
            JsonUtil.write(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ApiResponse(false, e.getMessage()));
        }
    }

    /** GET /api/trains/{id} -> obtener */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = extractId(req, resp);
        if (id == null) return;

        try {
            TrainBean train = trainService.getTrainById(id);
            if (train == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Train not found");
                return;
            }
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), train);
        } catch (TrainException e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        }
    }

    /** PUT /api/trains/{id} -> actualizar */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = extractId(req, resp);
        if (id == null) return;

        try {
            TrainBean body = mapper.readValue(req.getInputStream(), TrainBean.class);
            body.setTr_no(id); // asegurar coherencia

            String result = trainService.updateTrain(body);

            if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(result)) {
                JsonUtil.write(resp, HttpServletResponse.SC_OK,
                        new ApiResponse(true, "Updated"));
            } else {
                JsonUtil.write(resp, HTTP_UNPROCESSABLE_ENTITY,
                        new ApiResponse(false, result));
            }
        } catch (TrainException te) {
            JsonUtil.write(resp, te.getStatusCode(), new ApiResponse(false, te.getMessage()));
        } catch (Exception e) {
            JsonUtil.write(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ApiResponse(false, e.getMessage()));
        }
    }

    /** DELETE /api/trains/{id} -> eliminar */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = extractId(req, resp);
        if (id == null) return;

        // (opcional) validar rol
        // TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

        try {
            String result = trainService.deleteTrainById(String.valueOf(id));

            if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(result)) {
                JsonUtil.write(resp, HttpServletResponse.SC_OK,
                        new ApiResponse(true, "Deleted"));
            } else if (ResponseCode.NO_CONTENT.toString().equalsIgnoreCase(result)) {
                // tu servicio podría devolver NO_CONTENT si no encontró el tren
                JsonUtil.write(resp, HTTP_NOT_FOUND,
                        new ApiResponse(false, "Train not found"));
            } else {
                JsonUtil.write(resp, HTTP_UNPROCESSABLE_ENTITY,
                        new ApiResponse(false, result));
            }
        } catch (TrainException te) {
            JsonUtil.write(resp, te.getStatusCode(), new ApiResponse(false, te.getMessage()));
        } catch (Exception e) {
            JsonUtil.write(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ApiResponse(false, e.getMessage()));
        }
    }
    /** Helper: saca el id del path /api/trains/{id} */
    private Long extractId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // e.g. "/1234"
        if (path == null || "/".equals(path)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Train id required");
            return null;
        }
        try {
            return Long.parseLong(path.substring(1));
        } catch (NumberFormatException nfe) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid train id");
            return null;
        }
    }
}
