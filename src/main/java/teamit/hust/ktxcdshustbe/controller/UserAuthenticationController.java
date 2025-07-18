package teamit.hust.ktxcdshustbe.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamit.hust.ktxcdshustbe.dto.ApiResponseDto;
import teamit.hust.ktxcdshustbe.dto.user.AuthenticationDto;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.StrongPasswordException;
import teamit.hust.ktxcdshustbe.request.auth.AuthenticationRequest;
import teamit.hust.ktxcdshustbe.request.auth.UserRegisterAccountStudentRequest;
import teamit.hust.ktxcdshustbe.response.RefreshTokenResponse;
import teamit.hust.ktxcdshustbe.service.auth.AuthenticationService;
import teamit.hust.ktxcdshustbe.service.jwt.JwtTokenService;
import teamit.hust.ktxcdshustbe.service.refreshToken.RefreshTokenService;


@Tag(name = "Authentication User", description = "The Authentication User API. Contains operations like register, login" +
        ", logout, refresh-token etc.")
@RestController
@RequestMapping("/api/v1/user/auth")
@SecurityRequirements()
public class UserAuthenticationController {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserRegisterAccountStudentRequest request){
        try {
            AuthenticationDto authenticationDto = authenticationService.register(request);
            ResponseCookie jwtCookie = jwtTokenService.generateJwtCookie(authenticationDto.getAccessToken());
            ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(authenticationDto.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString())
                    .body(authenticationService.convertToAuthenticationResponse(authenticationDto));
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (StrongPasswordException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseDto.createdWithMessage(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationDto authenticationDto = authenticationService.authenticate(request);
            ResponseCookie jwtCookie = jwtTokenService.generateJwtCookie(authenticationDto.getAccessToken());
            ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(authenticationDto.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString())
                    .body(authenticationService.convertToAuthenticationResponse(authenticationDto));
        } catch (NotFoundException e){
            return ApiResponseDto.createdWithErrors(e.toErrorsDetails(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?>refreshToken(HttpServletRequest request){
        try {
            RefreshTokenResponse response =
                    refreshTokenService.generateNewToken(refreshTokenService.getRefreshTokenFromCookies(request));
            ResponseCookie jwtCookie = jwtTokenService.generateJwtCookie(response.getAccessToken());
            ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(response.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString())
                    .body("Refresh token success!");
        }catch (Exception e){
            return ApiResponseDto.createdWithMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String refreshToken = refreshTokenService.getRefreshTokenFromCookies(request);
        if(StringUtils.isNotBlank(refreshToken)) {
            refreshTokenService.deleteByToken(refreshToken.trim());
        }
        ResponseCookie jwtCookie = jwtTokenService.getCleanJwtCookie();
        ResponseCookie refreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie();
        jwtTokenService.deleteJSessionId();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString())
                .build();

    }
}
