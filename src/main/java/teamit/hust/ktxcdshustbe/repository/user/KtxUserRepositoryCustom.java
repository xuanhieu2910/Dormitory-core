package teamit.hust.ktxcdshustbe.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.request.user.FindAllStudentsRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.user.FindAllStudentsResponse;
import teamit.hust.ktxcdshustbe.response.user.InformationStudentHiredResponse;

import java.util.Optional;

public interface KtxUserRepositoryCustom {

    Optional<KtxUser> loadUserByUsername(String username);

    Boolean exitsByUserName(String userName);

    Optional<KtxUser> findByKtxUserId(Integer userId);

    Optional<InformationStudentHiredResponse> searchInformationStudentHiredRoomByNumberStudent(String numberStudent);

    int updateStatusIsActive(Integer userId, Integer statusIsActive);
    int updateStatusRegisterRoom(Integer userId, Integer statusRegisterRoom);

    Page<FindAllStudentsResponse> findAllStudent(FindAllStudentsRequest request, Pageable pageable);

    Optional<KtxUser> findByKtxUserCode(String codeUser);
    Optional<KtxUser> findByKtxUserByUserName(String userName);
}
