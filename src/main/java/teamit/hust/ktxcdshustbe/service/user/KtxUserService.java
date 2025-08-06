package teamit.hust.ktxcdshustbe.service.user;

import jakarta.servlet.ServletException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.request.user.FindAllStudentsRequest;
import teamit.hust.ktxcdshustbe.request.user.UpdateProfileUserRequest;
import teamit.hust.ktxcdshustbe.response.user.DetailInformationUserResponse;
import teamit.hust.ktxcdshustbe.response.user.FindAllStudentsResponse;
import teamit.hust.ktxcdshustbe.response.user.InformationStudentHiredResponse;

import java.sql.SQLException;
import java.util.List;

public interface KtxUserService extends UserDetailsService {

    Boolean exitsByUserName(String userName);
    KtxUser save(KtxUser ktxUser);
    KtxUser findKtxUserByKtxUserId(Integer idUser);
    DetailInformationUserResponse getDetailInformationUser();
    DetailInformationUserResponse getDetailInformationUserHiredRoomIdByHiredRoomId(Integer studentRoomId);
    InformationStudentHiredResponse searchInformationStudentByNumberStudent(String numberStudent);

    void uploadFileAccountStudent(MultipartFile file);
    Page<FindAllStudentsResponse> findAllStudentRequest(FindAllStudentsRequest request);
    DetailInformationUserResponse getDetailInformationUserByCodeUser(String userName);
    void hasCapability(String servletPath, String method) throws ServletException;
    KtxUser findKtxUserByCodeUser(String codeUser);
    int updateStatusRegisterRoom(Integer userId, Integer statusUserRegisterRoom);

    void updateUserProfile(UpdateProfileUserRequest request);

    KtxUser findKtxUserByUserName(String lowerCase);

    void saveAllValue(List<KtxUser> customUserDetails);
}
