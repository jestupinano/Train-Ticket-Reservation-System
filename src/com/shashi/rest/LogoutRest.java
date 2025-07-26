package com.shashi.rest;

import com.shashi.utility.TrainUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/logout")
public class LogoutRest extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TrainUtil.logout(response);

        // Invalida sesión actual también
        request.getSession().invalidate();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Logout successful");
    }
}
