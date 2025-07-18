package teamit.hust.ktxcdshustbe.service.upload;

import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.request.studentRegister.DeletePathAvatarStudentRegisterRequest;

import java.io.IOException;

public interface FilesStorageService { ;

     String saveAndReturnPath(MultipartFile file, String folderName) throws IOException;

     void deleteByPathFile(DeletePathAvatarStudentRegisterRequest pathFile) throws IOException, InterruptedException;
}
