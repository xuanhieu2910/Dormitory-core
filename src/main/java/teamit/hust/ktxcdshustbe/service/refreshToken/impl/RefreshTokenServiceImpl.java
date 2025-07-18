package teamit.hust.ktxcdshustbe.service.refreshToken.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import teamit.hust.ktxcdshustbe.config.JwtAuthenticationFilter;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.RefreshToken;
import teamit.hust.ktxcdshustbe.exception.IsNullException;
import teamit.hust.ktxcdshustbe.exception.TokenException;
import teamit.hust.ktxcdshustbe.repository.refreshToken.RefreshTokenRepository;
import teamit.hust.ktxcdshustbe.response.RefreshTokenResponse;
import teamit.hust.ktxcdshustbe.service.jwt.JwtTokenService;
import teamit.hust.ktxcdshustbe.service.refreshToken.RefreshTokenService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {


    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    @Autowired
    KtxUserService ktxUserService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public RefreshToken createRefreshToken(Integer userId) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MILLISECOND, (int) refreshExpiration);
        Timestamp later = new Timestamp(cal.getTime().getTime());


        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(false);
        refreshToken.setIdUser(userId);
        refreshToken.setToken(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        refreshToken.setExpiryDate(later);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null){
            log.error("Token is null");
            throw new TokenException();
        }
        if (token.getExpiryDate().compareTo(new Timestamp(new Date().getTime())) < 0){
            refreshTokenRepository.delete(token);
            throw new TokenException();
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public RefreshTokenResponse generateNewToken(String refreshTokenRequest) {
        if (Objects.isNull(refreshTokenRequest)){
            throw new IsNullException();
        }
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest);
        validateRefreshToken(refreshToken,refreshTokenRequest);
        verifyExpiration(refreshToken.get());
        KtxUser ktxUser = ktxUserService.findKtxUserByKtxUserId(refreshToken.get().getIdUser());
        String token = jwtTokenService.generateToken(ktxUser);
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.get().getToken())
                .tokenType(JwtAuthenticationFilter.TOKEN_PREFIX)
                .build();
    }

    private void validateRefreshToken(Optional<RefreshToken> refreshToken,String tokenRequest){
        if (!refreshToken.isPresent()){
            throw new TokenException();
        }
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration/1000) // 15 days in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }

    @Override
    public void deleteByToken(String token) {
        if (token == null){
            log.error("Token is null");
            throw new TokenException();
        }
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        validateRefreshToken(refreshToken,token);
        refreshTokenRepository.delete(refreshToken.get());
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }
}
