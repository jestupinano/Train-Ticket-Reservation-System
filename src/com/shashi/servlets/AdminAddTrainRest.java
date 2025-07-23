package com.shashi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shashi.beans.TrainBean;
import com.shashi.beans.TrainException;
import com.shashi.constant.ResponseCode;
import com.shashi.constant.UserRole;
import com.shashi.dto.ApiResponse;
import com.shashi.service.TrainService;
import com.shashi.service.impl.TrainServiceImpl;
import com.shashi.utility.JsonUtil;
import com.shashi.utility.TrainUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/trains")   // <-- endpoint REST
public class AdminAddTrainRest extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final TrainService trainService = new TrainServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Autorización igual que antes
       // TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

        try {
            // Esperando JSON desde Angular
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
            JsonUtil.write(resp, 422, new ApiResponse(false, te.getMessage()));
        } catch (Exception e) {
            JsonUtil.write(resp, 500, new ApiResponse(false, e.getMessage()));
        }
    }
}
