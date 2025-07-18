package teamit.hust.ktxcdshustbe.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.service.jwt.JwtTokenService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.service.user.impl.KtxUserServiceImpl;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    public static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private KtxUserService ktxUserService;

    @Autowired
    private JwtTokenService jwtTokenUtil;



    /**
     *  Filter supported for OAuth and OAuth2
     * */

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken)){
            handleOAuthFilter(request, response, filterChain);
        } else {
            setContextHolder();
//            hasCapability(request);
            filterChain.doFilter(request, response);
        }
    }

    private void setContextHolder() {
        OidcUser oidcUser = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KtxUser ktxUser = oidcUser.getUserInfo().getClaim(WebSecurityConfig.INFORMATION_USER);
        UserDetails userDetails = this.ktxUserService.loadUserByUsername(ktxUser.getUsername());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleOAuthFilter(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtTokenUtil.getJwtFromCookies(request);
        final String authHeader = request.getHeader(HEADER_STRING);
        if ((jwt == null && (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX))) || request.getRequestURI().contains("/api/v1/user/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (jwt == null && authHeader.startsWith(TOKEN_PREFIX)) {
            jwt = authHeader.replace(TOKEN_PREFIX, "");
        }
        final String userName = jwtTokenUtil.getUserNameFromToken(jwt);
        if (StringUtils.isNotEmpty(userName)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.ktxUserService.loadUserByUsername(userName);
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                //update the spring security context by adding a new UsernamePasswordAuthenticationToken
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
//        hasCapability(request);
        filterChain.doFilter(request, response);
    }


    private void hasCapability(HttpServletRequest request) throws ServletException {
        ktxUserService.hasCapability(request.getServletPath(), request.getMethod());
    }
}
