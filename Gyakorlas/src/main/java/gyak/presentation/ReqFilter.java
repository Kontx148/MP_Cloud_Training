package gyak.presentation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class ReqFilter extends HttpFilter {

    private static final Logger logger = LoggerFactory.getLogger(HttpFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        logger.info("Got " + req.getMethod() + " for path : " + req.getRequestURI() + " with this: " + req.getQueryString() + " from: " + req.getRemoteAddr());

        chain.doFilter(req, res);

        logger.info("Got " + res.getStatus() + " content type : " + res.getContentType());
    }
}
