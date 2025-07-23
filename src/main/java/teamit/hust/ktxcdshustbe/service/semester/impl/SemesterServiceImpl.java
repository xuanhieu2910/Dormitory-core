package teamit.hust.ktxcdshustbe.service.semester.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.semester.FindAllSemesterDto;
import teamit.hust.ktxcdshustbe.dto.semester.FindSemesterDetailDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Semester;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.IsBlankException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistration.BatchesRegistrationRepository;
import teamit.hust.ktxcdshustbe.repository.semester.SemesterRepository;
import teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.StudentRegisterRoomRepository;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepository;
import teamit.hust.ktxcdshustbe.request.semester.CreateSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.UpdateSemesterRequest;
import teamit.hust.ktxcdshustbe.response.semester.DetailSemesterResponse;
import teamit.hust.ktxcdshustbe.response.semester.FindAllSemesterResponse;
import teamit.hust.ktxcdshustbe.service.semester.SemesterService;
import teamit.hust.ktxcdshustbe.utility.PageUtils;


import java.sql.Timestamp;
import java.util.*;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;
//    @Autowired
//    private TimeHiredRepository timeHiredRepository;
//    @Autowired
//    private StudentRegisterRoomRepository studentRegisterRoomRepository;

    @Autowired
    private BatchesRegistrationRepository batchesRegistrationRepository;


    @Override
    public Page<FindAllSemesterResponse> findAllSemester(FindAllSemesterRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllSemesterDto> semesterDtos = semesterRepository.findAllSemester(request, pageable);
        return new PageImpl<>(convertToFindAllSemesterResponse(semesterDtos.getContent()), pageable, semesterDtos.getTotalElements());
    }

    @Override
    public Semester create(CreateSemesterRequest request) {
        verifyCreateSemesterRequest(request);
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Semester semester = initializeSemester(request, ktxUser);
        return semesterRepository.save(semester);

    }
    private void verifyCreateSemesterRequest(CreateSemesterRequest request) {
        if(StringUtils.isBlank(request.getTitleSemester())) {
            throw new IsBlankException();
        }
        if(semesterRepository.existsByTitle(request.getTitleSemester())) {
            throw new ExitsObjectException();
        }
    }
    private Semester initializeSemester(CreateSemesterRequest request, KtxUser ktxUser) {
        Semester semester = new Semester();
        semester.setTitle(request.getTitleSemester());
        semester.setStatus(request.getStatus());
        semester.setTimeCreated(new Date().getTime());
        semester.setIdUserCreated(ktxUser.getIdKtxUser());
        semester.setIdUserModified(ktxUser.getIdUserModified());
        semester.setCodeSemester(UUID.nameUUIDFromBytes(request.getTitleSemester().getBytes()).toString());
        semester.setNote(request.getNote());
        return semester;
    }

    @Override
    public Semester update(UpdateSemesterRequest request) {
        verifyUpdateSemesterRequest(request);
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Semester semester = updateSemester(request, ktxUser);
        return semesterRepository.save(semester);
    }
    private void verifyUpdateSemesterRequest(UpdateSemesterRequest request) {
        if(StringUtils.isBlank(request.getTitleSemester())) {
            throw new IsBlankException();
        }
        Optional<Semester> optionalSemester = semesterRepository.findByCodeSemester(request.getCodeSemester());
        if(optionalSemester.isEmpty()) {
            throw new NotFoundException();
        }
    }
    private Semester updateSemester(UpdateSemesterRequest request, KtxUser ktxUser) {
        Optional<Semester> optionalSemester = semesterRepository.findByCodeSemester(request.getCodeSemester());
        Semester semester = optionalSemester.get();
        semester.setTitle(request.getTitleSemester());
        semester.setTimeModified(new Date().getTime());
        //semester.setStatus(request.getStatus());
        semester.setNote(request.getNote());
        semester.setIdUserModified(ktxUser.getIdKtxUser());
        return semester;
    }

    @Override
    public void delete(String codeSemester) {
        Optional<Semester> optionalSemester = semesterRepository.findByCodeSemester(codeSemester);
        if(optionalSemester.isEmpty()) {
            throw new NotFoundException();
        }
        semesterRepository.delete(optionalSemester.get());
        batchesRegistrationRepository.deleteIdSemester(optionalSemester.get().getIdSemester());
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

    @Override
    public DetailSemesterResponse findSemesterDetailByCode(String codeSemester) {
        if(StringUtils.isBlank(codeSemester)) {
            throw new IsBlankException();
        }
        FindSemesterDetailDto semesterDetailDto = semesterRepository.findSemesterDetailByCode(codeSemester);
        return convertToDetailSemesterResponse(semesterDetailDto);
    }

    @Override
    public Semester findSemesterByCode(String codeSemester) {
        Optional<Semester> semester = semesterRepository.findByCodeSemester(codeSemester);
        if (semester.isEmpty()){
            throw new NotFoundException();
        }
        return semester.get();
    }

    private DetailSemesterResponse convertToDetailSemesterResponse(FindSemesterDetailDto findSemesterDetailDto) {
        DetailSemesterResponse response = new DetailSemesterResponse();
        response.setCodeSemester(findSemesterDetailDto.getCodeSemester());
        response.setTitle(findSemesterDetailDto.getTitleSemester());
        response.setStatus(findSemesterDetailDto.getStatus().toString());
        //tra them
        return response;
    }

}
