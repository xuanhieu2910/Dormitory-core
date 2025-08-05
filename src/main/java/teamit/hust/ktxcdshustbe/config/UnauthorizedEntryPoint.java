package teamit.hust.ktxcdshustbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import teamit.hust.ktxcdshustbe.response.ErrorResponse;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Slf4j
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthenticated error: {}",authException.getMessage());
        Set<String> allowOriginals = new HashSet<>();
        allowOriginals.add(WebSecurityConfig.DOMAIN_FPT_PAYMENT);
        allowOriginals.add(WebSecurityConfig.DOMAIN_FE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String uriCurrent = request.getRequestURI();
        log.info("Uri current un-author {}", uriCurrent);
        if (allowOriginals.contains(uriCurrent)) {
            log.info("Allow uri: {}", uriCurrent);
            response.setHeader("Access-Control-Allow-Origin", uriCurrent);
        }
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthenticated error")
                .timestamp(Instant.now())
                .message("Token JWT đã hết hạn hoặc không hợp lệ!")
                .path(request.getServletPath())
                .build();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        mapper.writeValue(response.getOutputStream(), body);
    }
}
