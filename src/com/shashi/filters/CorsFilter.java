package com.shashi.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/*")
public class CorsFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;

    String origin = req.getHeader("Origin");
    if (origin != null && !origin.isEmpty()) {
        resp.setHeader("Access-Control-Allow-Origin", origin);
    }
    resp.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
    resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    resp.setHeader("Access-Control-Allow-Credentials", "true");

    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
	// TODO Auto-generated method stub
	
  }

  @Override
  public void destroy() {
	// TODO Auto-generated method stub
	
  }
}
