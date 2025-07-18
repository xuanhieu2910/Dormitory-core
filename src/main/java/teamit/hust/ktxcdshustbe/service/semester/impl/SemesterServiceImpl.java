package teamit.hust.ktxcdshustbe.service.semester.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.semester.FindAllSemesterDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Semester;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.repository.semester.SemesterRepository;
import teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.StudentRegisterRoomRepository;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepository;
import teamit.hust.ktxcdshustbe.request.semester.CreateSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.UpdateSemesterRequest;
import teamit.hust.ktxcdshustbe.response.semester.FindAllSemesterResponse;
import teamit.hust.ktxcdshustbe.service.semester.SemesterService;
import teamit.hust.ktxcdshustbe.utility.PageUtils;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private TimeHiredRepository timeHiredRepository;
    @Autowired
    private StudentRegisterRoomRepository studentRegisterRoomRepository;


    @Override
    public Page<FindAllSemesterResponse> findAllSemester(FindAllSemesterRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllSemesterDto> semesterDtos = semesterRepository.findAllSemester(request, pageable);
        return new PageImpl<>(convertToFindAllSemesterResponse(semesterDtos.getContent()), pageable, semesterDtos.getTotalElements());
    }

    @Override
    public Semester create(CreateSemesterRequest request) {
        if(semesterRepository.existsByTitle(request.getTitleSemester()) {
            throw new ExitsObjectException();
        }
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Semester semester = new Semester();
        semester.setTitle(request.getTitleSemester());
        semester.setStatus(request.getStatus());
        semester.setTimeCreated(new Date().getTime());
        semester.setIdUserCreated(ktxUser.getIdKtxUser());
        return semesterRepository.save(semester);
    }

    @Override
    public Semester update(UpdateSemesterRequest request) {
        return null;
    }

    @Override
    public void delete(String codeSemester) {

    }

    private List<FindAllSemesterResponse> convertToFindAllSemesterResponse(List<FindAllSemesterDto> content) {
        List<FindAllSemesterResponse> responses = new ArrayList<>();
        for (FindAllSemesterDto dto : content){
            FindAllSemesterResponse response = new FindAllSemesterResponse();
            response.setTitleSemester(dto.getTitleSemester());
            response.setCodeSemester(dto.getCodeSemester());
            response.setStatus(dto.getStatus());
            responses.add(response);
        }
        return responses;
    }
}
