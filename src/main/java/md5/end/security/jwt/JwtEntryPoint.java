package md5.end.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// xử lý ngoại lệ khi xác thực
@Component
@Slf4j
public class JwtEntryPoint implements AuthenticationEntryPoint {
    public final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class); // ghi log ở class mình đang đứng

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Error-> Authentication: "+authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorMessage = "You must login first to access this page!";
        String jsonError = objectMapper.writeValueAsString(errorMessage);
        response.getWriter().write(jsonError);

    }
}
