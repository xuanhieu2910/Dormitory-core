package teamit.hust.ktxcdshustbe.config;

import com.azure.core.http.HttpMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import teamit.hust.ktxcdshustbe.service.logger.LoggingService;

@Configuration
public class InterceptLog implements HandlerInterceptor {


    @Autowired
    LoggingService loggingService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals(HttpMethod.GET.name())
                || request.getMethod().equals(HttpMethod.POST.name())
                || request.getMethod().equals(HttpMethod.PUT.name())
                || request.getMethod().equals(HttpMethod.DELETE.name())
                || request.getMethod().equals(HttpMethod.OPTIONS.name()))    {
            loggingService.displayReq(request,null);
        }
        return true;
    }
}
