package teamit.hust.ktxcdshustbe.repository.department.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.department.FindDepartmentStatisticDetailDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.repository.department.DepartmentRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Override
    public Page<FindAllDepartmentDto> findAllDepartment(Pageable pageable, FindAllDepartmentRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("select dep.id_department, dep.title, dep.time_created,  " +
                "       dep.time_modified, dep.status, dep.code_department,  " +
                "       ku_created.user_name, ku_created.full_name,  " +
                "       ku_modified.user_name, ku_modified.full_name,  " +
                "       ku_manager.user_name, ku_manager.full_name  " +
                "from department dep  " +
                "    inner join ktx_user ku_created on dep.id_user_created = ku_created.id_ktx_user  " +
                "    inner join ktx_user ku_modified on dep.id_user_modified = ku_modified.id_ktx_user  " +
                "    inner join ktx_user ku_manager on dep.id_user_managed = ku_manager.id_ktx_user  " +
                "where 1 = 1 ");
        setConditionFindAllDepartment(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllDepartment(request, query);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllDepartmentDto> departmentDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                FindAllDepartmentDto departmentDto = new FindAllDepartmentDto();
                departmentDto.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
                departmentDto.setTitle(ValueUtil.getStringByObject(obj[1]));
                departmentDto.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
                departmentDto.setTimeModified(ValueUtil.getLongByObject(obj[3]));
                departmentDto.setStatus(ValueUtil.getIntegerByObject(obj[4]));
                departmentDto.setCodeDepartment(ValueUtil.getStringByObject(obj[5]));
                departmentDto.setUserNameCreated(ValueUtil.getStringByObject(obj[6]));
                departmentDto.setFullNameCreated(ValueUtil.getStringByObject(obj[7]));
                departmentDto.setUserNameModified(ValueUtil.getStringByObject(obj[8]));
                departmentDto.setFullNameModified(ValueUtil.getStringByObject(obj[9]));
                departmentDto.setUserNameManaged(ValueUtil.getStringByObject(obj[10]));
                departmentDto.setFullNameManaged(ValueUtil.getStringByObject(obj[11]));
                departmentDtos.add(departmentDto);
            }
        }
        return new PageImpl<>(departmentDtos, pageable, countFindAllDepartment(request));
    }

    private long countFindAllDepartment(FindAllDepartmentRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(0) " +
                "from department dep  " +
                "    inner join ktx_user ku_created on dep.id_user_created = ku_created.id_ktx_user  " +
                "    inner join ktx_user ku_modified on dep.id_user_modified = ku_modified.id_ktx_user  " +
                "    inner join ktx_user ku_manager on dep.id_user_managed = ku_manager.id_ktx_user  " +
                "where 1 = 1 ");
        setConditionFindAllDepartment(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllDepartment(request, query);
        return ValueUtil.getIntegerByObject(query.getSingleResult());
    }

    private void setParameterFindAllDepartment(FindAllDepartmentRequest request, Query query) {
        if (StringUtils.isNotBlank(request.getTitleDepartment())){
            query.setParameter("titleDepartment", request.getTitleDepartment());
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            query.setParameter("status", request.getStatus());
        }
    }

    private void setConditionFindAllDepartment(FindAllDepartmentRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getTitleDepartment())){
            sb.append(" and dep.title REGEXP :titleDepartment ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and dep.status = :status ");
        }
        sb.append(" order by dep.id_department desc  ");
    }


    @Override
    public Optional<Department> findDepartmentById(Integer departmentId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.title, de.time_created, " +
                "       de.time_modified, de.status, de.id_user_created, " +
                "       de.id_user_modified, de.id_user_managed, de.code_department " +
                "from department de " +
                "where de.id_department = :departmentId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("departmentId", departmentId);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                return Optional.of(writeDataDepartment(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Department> findDepartmentByTitle(String titleDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.title, de.time_created, " +
                "       de.time_modified, de.status, de.id_user_created, " +
                "       de.id_user_modified, de.id_user_managed, " +
                "       de.code_department " +
                "from department de   " +
                "where de.title = :titleDepartment  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleDepartment", titleDepartment);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                return Optional.of(writeDataDepartment(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public FindDepartmentStatisticDetailDto findDepartmentStatisticDetailsByCodeDepartment(String codeDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append("select de.id_department, de.title,  " +
                "       sum(case when ro.id_room is not null then 1 else 0 end)  totalRoom,  " +
                "       sum(case when ro.is_active = 1 then 1 else 0 end)  totalRoomActive,  " +
                "       sum(case when ro.is_active = -1 then 1 else 0 end)  totalRoomUnActive,  " +
                "       sum(case when studentRoom.id_user is not null then 1 else 0 end)   totalStudentHiring,  " +
                "       sum(case when studentRegisterRoom.id_user is not null then 1 else 0 end)  totalStudentRegister,  " +
                "       sum(case when studentRegisterRoom.status = 2 then 1 else 0 end)  totalStudentNotPayment,  " +
                "       sum(case when studentRegisterRoom.status = 3 then 1 else 0 end) totalStudentPayment,  " +
                "       de.status  " +
                "from department de  " +
                "         inner join room ro on de.id_department = ro.id_department  " +
                "         left join (select studentRoom.*  " +
                "                    from student_room studentRoom  " +
                "                    where studentRoom.status = :statusStudentRoom) studentRoom  " +
                "                    on ro.id_room = studentRoom.id_room  " +
                "         left join (select studentRegisterRoom.*  " +
                "                    from student_register_room studentRegisterRoom  " +
                "                    where studentRegisterRoom.status in (:statusHoldRoom, :statusPayment)  " +
                "                             ) studentRegisterRoom  on ro.id_room = studentRegisterRoom.id_room  " +
                "where de.title = :codeDepartment  " +
                "group by de.id_department, de.title, de.status ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeDepartment", codeDepartment);
        query.setParameter("statusStudentRoom", Constants.STATUS_STUDENT_HIRING_ROOM);
        query.setParameter("statusHoldRoom", Constants.STATUS_HOLD_STUDENT_ROOM_REGISTER);
        query.setParameter("statusPayment", Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER);
        FindDepartmentStatisticDetailDto detailDto = new FindDepartmentStatisticDetailDto();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result) {
                detailDto.setCodeDepartment(ValueUtil.getStringByObject(obj[0]));
                detailDto.setTitleDepartment(ValueUtil.getStringByObject(obj[1]));
                detailDto.setTotalRoom(ValueUtil.getIntegerByObject(obj[2]));
                detailDto.setTotalRoomActive(ValueUtil.getIntegerByObject(obj[3]));
                detailDto.setTotalRoomUnActive(ValueUtil.getIntegerByObject(obj[4]));
                detailDto.setTotalStudentHiring(ValueUtil.getIntegerByObject(obj[5]));
                detailDto.setTotalStudentRegister(ValueUtil.getIntegerByObject(obj[6]));
                detailDto.setTotalStudentRegisterNotPayment(ValueUtil.getIntegerByObject(obj[7]));
                detailDto.setTotalStudentRegisterPayment(ValueUtil.getIntegerByObject(obj[8]));
                detailDto.setStatus(ValueUtil.getIntegerByObject(obj[9]));
            }
        }
        return detailDto;
    }

    @Override
    public Optional<Department> findDepartmentByCode(String codeDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.title, de.time_created,  " +
                "       de.time_modified, de.status, de.id_user_created,  " +
                "       de.id_user_modified, de.id_user_managed, de.code_department  " +
                "from department de  " +
                "where de.code_department = :codeDepartment ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeDepartment", codeDepartment);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataDepartment(obj));
            }
        }
        return Optional.empty();
    }

    private Department writeDataDepartment(Object[] obj) {
        Department department = new Department();
        department.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
        department.setTitle(ValueUtil.getStringByObject(obj[1]));
        department.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
        department.setTimeModified(ValueUtil.getLongByObject(obj[3]));
        department.setStatus(ValueUtil.getIntegerByObject(obj[4]));
        department.setIdUserCreated(ValueUtil.getIntegerByObject(obj[5]));
        department.setIdUserModified(ValueUtil.getIntegerByObject(obj[6]));
        department.setIdUserManaged(ValueUtil.getIntegerByObject(obj[7]));
        department.setCodeDepartment(ValueUtil.getStringByObject(obj[8]));
        return department;
    }
}
