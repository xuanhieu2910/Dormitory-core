package teamit.hust.ktxcdshustbe.service.auth.impl;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.config.JwtAuthenticationFilter;
import teamit.hust.ktxcdshustbe.dto.user.AuthenticationDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.UserRole;
import teamit.hust.ktxcdshustbe.enums.RolePattern;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.role.RoleRepository;
import teamit.hust.ktxcdshustbe.request.auth.AuthenticationRequest;
import teamit.hust.ktxcdshustbe.request.auth.UserRegisterAccountStudentRequest;
import teamit.hust.ktxcdshustbe.response.auth.AuthenticationResponse;
import teamit.hust.ktxcdshustbe.service.auth.AuthenticationService;
import teamit.hust.ktxcdshustbe.service.jwt.JwtTokenService;
import teamit.hust.ktxcdshustbe.service.refreshToken.RefreshTokenService;
import teamit.hust.ktxcdshustbe.service.role.RoleService;
import teamit.hust.ktxcdshustbe.service.user.impl.KtxUserServiceImpl;
import teamit.hust.ktxcdshustbe.service.userRole.UserRoleService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.RoleUtils;
import teamit.hust.ktxcdshustbe.utility.ValidationUtility;
import java.sql.Timestamp;
import java.util.*;


@Log4j2
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    KtxUserServiceImpl ktxUserService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    RefreshTokenService refreshTokenService;



    @Override
    @Transactional
    public AuthenticationDto register(UserRegisterAccountStudentRequest request){
        if (ktxUserService.exitsByUserName(request.getUsername())) {
            throw new ExitsObjectException();
        }
        Role role = roleService.findRoleByTitleRole(RolePattern.STUDENT.name());
        ValidationUtility.validateStrongPassword(request.getPassword());
        KtxUser ktxUser = createKtxUserByRegisterAccount(request);
        ktxUserService.save(ktxUser);
        userRoleService.saveUserRole(createUserRoleByRegisterAccount(ktxUser.getIdKtxUser(), role.getIdRole()));
        return convertToAuthenticationDto(ktxUser,role);
    }

    private AuthenticationDto convertToAuthenticationDto(KtxUser ktxUser, Role role ){
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setRoles(RoleUtils.convertToRoleResponse(List.of(role)));
        authenticationDto.setUserName(ktxUser.getUsername());
        authenticationDto.setTokenType(JwtAuthenticationFilter.TOKEN_PREFIX);
        authenticationDto.setCodeUser(ktxUser.getCodeUser());
        authenticationDto.setIsActive(ktxUser.getIsActived());
        authenticationDto.setIsInitialize(ktxUser.getIsInitialize());
        return authenticationDto;
    }

    @Override
    public AuthenticationDto authenticate(AuthenticationRequest request) throws Exception {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        KtxUser user = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> role = user.getRole().stream().toList();
        String jwt = jwtTokenService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getIdKtxUser()).getToken();
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setAccessToken(jwt);
        authenticationDto.setCodeUser(user.getCodeUser());
        authenticationDto.setRoles(RoleUtils.convertToRoleResponse(role));
        authenticationDto.setUserName(user.getUsername());
        authenticationDto.setRefreshToken(refreshToken);
        authenticationDto.setTokenType(JwtAuthenticationFilter.TOKEN_PREFIX);
        authenticationDto.setIsActive(user.getIsActived());
        authenticationDto.setIsInitialize(user.getIsInitialize());
        return authenticationDto;
    }

    public AuthenticationResponse convertToAuthenticationResponse(AuthenticationDto authenticationDto) {
        return AuthenticationResponse.builder()
                .codeUser(authenticationDto.getCodeUser())
                .userName(authenticationDto.getUserName())
                .roles(authenticationDto.getRoles())
                .isInitialize(authenticationDto.getIsInitialize())
                .build();
    }

    @Override
    public AuthenticationDto getOAuthentication2ByUserName() {
        log.debug("Redirect front end success!");
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KtxUser ktxUser1 = ktxUserService.findKtxUserByCodeUser(ktxUser.getCodeUser());
        List<Role> role = ktxUser1.getRole().stream().toList();
        AuthenticationDto authenticationDto = new AuthenticationDto();
        authenticationDto.setRoles(RoleUtils.convertToRoleResponse(role));
        authenticationDto.setUserName(ktxUser1.getUsername());
        authenticationDto.setCodeUser(ktxUser1.getCodeUser());
        authenticationDto.setIsActive(ktxUser1.getIsActived());
        authenticationDto.setIsInitialize(ktxUser1.getIsInitialize());
        return authenticationDto;
    }

    private KtxUser createKtxUserByRegisterAccount(UserRegisterAccountStudentRequest request){
        Long timeCurrently = new Date().getTime();
        KtxUser ktxUser = new KtxUser();
        ktxUser.setUserName(request.getUsername());
        ktxUser.setPassword(passwordEncoder.encode(request.getPassword()));
        ktxUser.setTimeCreated(timeCurrently);
        ktxUser.setTimeModified(timeCurrently);
        ktxUser.setIsActived(Constants.ACCOUNT_IS_UN_ACTIVED);
        ktxUser.setIsInitialize(Constants.NOT_YET_IS_INITIALIZE);
        return ktxUser;
    }

    private UserRole createUserRoleByRegisterAccount(Integer idUser, Integer idRole){
        Long timeCurrently =new Date().getTime();
        UserRole userRole = new UserRole();
        userRole.setIdUser(idUser);
        userRole.setIdRole(idRole);
        userRole.setTimeCreated(timeCurrently);
        userRole.setTimeModified(timeCurrently);
        userRole.setPicked(Constants.ROLE_USER_PICKED);
        return userRole;
    }

}
