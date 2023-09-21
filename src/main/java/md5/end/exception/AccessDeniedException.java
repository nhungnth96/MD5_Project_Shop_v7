package md5.end.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedException implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorMessage = "You don't have permission to access this page.";
        String jsonError = objectMapper.writeValueAsString(errorMessage);
        response.getWriter().write(jsonError);
    }
    }

