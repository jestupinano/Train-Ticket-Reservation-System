package com.shashi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shashi.beans.LoginRequest;
import com.shashi.constant.UserRole;
import com.shashi.utility.TrainUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/login")
public class LoginRest extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    ObjectMapper mapper = new ObjectMapper();
	    LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);

	    if (loginRequest.username == null || loginRequest.password == null || loginRequest.role == null) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing login fields");
	        return;
	    }

	    UserRole userRole;
	    try {
	        userRole = UserRole.valueOf(loginRequest.role.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user role");
	        return;
	    }

	    String result = TrainUtil.login(request, response, userRole, loginRequest.username, loginRequest.password);

	    if (result.equalsIgnoreCase("SUCCESS")) {
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().write("Login successful");
	    } else {
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login failed: " + result);
	    }
	}
}
