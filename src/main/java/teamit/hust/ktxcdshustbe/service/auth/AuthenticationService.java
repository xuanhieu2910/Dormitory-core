package teamit.hust.ktxcdshustbe.service.auth;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import teamit.hust.ktxcdshustbe.dto.user.AuthenticationDto;
import teamit.hust.ktxcdshustbe.request.auth.AuthenticationRequest;
import teamit.hust.ktxcdshustbe.request.auth.UserRegisterAccountStudentRequest;
import teamit.hust.ktxcdshustbe.response.auth.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationDto register(UserRegisterAccountStudentRequest request);
    AuthenticationDto authenticate(AuthenticationRequest request) throws Exception;
    AuthenticationResponse convertToAuthenticationResponse(AuthenticationDto authenticationDto);
    AuthenticationDto getOAuthentication2ByUserName(OidcUser principal);

}
