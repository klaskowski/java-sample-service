package com.example.talandemo;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestFilter implements Filter {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
      ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    // Process the request before it's handled by the controller
    String url = request.getRequestURL().toString();

    // Continue the filter chain
    filterChain.doFilter(request, response);

    // Process the response before it's returned to the client
    rabbitTemplate.convertAndSend("http-traces-exchange", "http-traces", response.getStatus() + " " + url);
  }
}
