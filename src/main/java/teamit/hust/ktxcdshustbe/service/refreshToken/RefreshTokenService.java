package teamit.hust.ktxcdshustbe.service.refreshToken;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import teamit.hust.ktxcdshustbe.entity.RefreshToken;
import teamit.hust.ktxcdshustbe.response.RefreshTokenResponse;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Integer userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(String  refreshToken);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}
