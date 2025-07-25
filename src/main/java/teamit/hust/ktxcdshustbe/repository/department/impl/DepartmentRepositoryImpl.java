package teamit.hust.ktxcdshustbe.repository.department.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentByCodeAndVisibleDto;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.department.FindDepartmentStatisticDetailDto;
import teamit.hust.ktxcdshustbe.dto.department.StudentSearchDepartmentDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.repository.department.DepartmentRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.StudentSearchDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentDetailsResponse;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Override
    public Page<FindAllDepartmentDto> findAllDepartment(Pageable pageable, FindAllDepartmentRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("WITH RECURSIVE cte_department as ( " +
                "    select department.id_department,department.title, " +
                "           department.code_department, department.short_name, " +
                "           department.description, department.parent, " +
                "            department.time_created,department.status, " +
                "           department.time_modified, " +
                "           1 as depth,   CAST(department.id_department as NCHAR ) as path ," +
                "       NULL AS code_parent_department " +
                "    from department " +
                "    where department.parent is null " +
                "    union all " +
                "    select department.id_department,department.title, " +
                "           department.code_department, department.short_name, " +
                "           department.description, department.parent, " +
                "           department.time_created,department.status, " +
                "           department.time_modified, " +
                "           cte.depth + 1 as depth, " +
                "concat_ws('/',cte.path,CAST(department.id_department as NCHAR)) as path," +
                "  cte.code_department AS code_parent_department " +
                "from department" +
                "             INNER JOIN cte_department cte ON department.parent = cte.id_department " +
                ") " +
                "select cte.id_department, cte.title,  " +
                "       cte.code_department, cte.description,  " +
                "       cte.parent,  " +
                "        cte.time_created, cte.time_modified,  " +
                "       cte.depth, cte.status, cte.path , cte.short_name,cte.code_parent_department " +
                "from cte_department cte  " +
                "where 1 = 1 and cte.status = :status ");
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
                departmentDto.setCodeDepartment(ValueUtil.getStringByObject(obj[2]));
                departmentDto.setDescription(ValueUtil.getStringByObject(obj[3]));
                departmentDto.setParent(ValueUtil.getIntegerByObject(obj[4]));
                departmentDto.setTimeCreated(ValueUtil.getStringByObject(obj[5]));
                departmentDto.setTimeModified(ValueUtil.getStringByObject(obj[6]));
                departmentDto.setDepth(ValueUtil.getIntegerByObject(obj[7]));
                departmentDto.setStatus(ValueUtil.getIntegerByObject(obj[8]));
                departmentDto.setPath(ValueUtil.getStringByObject(obj[9]));
                departmentDto.setShortName(ValueUtil.getStringByObject(obj[10]));
                departmentDto.setCodeParentDepartment(ValueUtil.getStringByObject(obj[11]));
                departmentDtos.add(departmentDto);
            }
        }
        return new PageImpl<>(departmentDtos, pageable, countFindAllDepartment(request));
    }

    private long countFindAllDepartment(FindAllDepartmentRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("WITH RECURSIVE cte_department as ( " +
                "    select department.id_department,department.title, " +
                "           department.code_department, department.short_name, " +
                "           department.description, department.parent, " +
                "            department.time_created,department.status, " +
                "           department.time_modified, " +
                "           1 as depth,   CAST(department.id_department as NCHAR ) as path ," +
                "       NULL AS code_parent_department " +
                "    from department " +
                "    where department.parent is null " +
                "    union all " +
                "    select department.id_department,department.title, " +
                "           department.code_department, department.short_name, " +
                "           department.description, department.parent, " +
                "           department.time_created,department.status, " +
                "           department.time_modified, " +
                "           cte.depth + 1 as depth, " +
                "concat_ws('/',cte.path,CAST(department.id_department as NCHAR)) as path," +
                "  cte.code_department AS code_parent_department " +
                "from department" +
                "             INNER JOIN cte_department cte ON department.parent = cte.id_department " +
                ") " +
                "    select count(cte.id_department) count  " +
                "from cte_department cte  " +
                "where 1 = 1 and cte.status = :status ");
        setConditionFindAllDepartment(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllDepartment(request, query);
        return ValueUtil.getIntegerByObject(query.getSingleResult());
    }

    private void setParameterFindAllDepartment(FindAllDepartmentRequest request, Query query) {
        query.setParameter("status", Constants.DEPARTMENT_ACTIVE_STATUS);
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
        sb.append(" order by path  ");
    }


    @Override
    public Optional<Department> findDepartmentById(Integer departmentId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.title, de.time_created, " +
                "       de.time_modified, de.status, de.id_user_created, " +
                "       de.id_user_modified, de.code_department, de.parent," +
                "       de.description,de.short_name " +
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
                "       de.id_user_modified, de.code_department, " +
                "       de.parent,de.description,de.short_name " +
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
                "where de.code_department = :codeDepartment  " +
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
                "       de.id_user_modified, de.code_department, de.parent," +
                "       de.description,de.short_name " +
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

    @Override
    public List<FindAllDepartmentByCodeAndVisibleDto> findAllStructDepartmentByIdDepartment(Integer idDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append(" WITH RECURSIVE cte_department as (     " +
                "       select department.id_department,department.title,     " +
                "              department.code_department,    " +
                "               department.parent,     " +
                "               department.time_created,department.status,     " +
                "              department.time_modified,     " +
                "              1 as depth,   CAST(department.id_department as NCHAR ) as path     " +
                "       from department     " +
                "       where department.id_department = :idDepartment  " +
                "       union all     " +
                "       select department.id_department,department.title,     " +
                "              department.code_department, department.parent,     " +
                "              department.time_created,department.status,     " +
                "              department.time_modified,     " +
                "              cte.depth + 1 as depth,     " +
                "   concat_ws('/',cte.path,CAST(department.id_department as NCHAR)) as path     " +
                "   from department    " +
                "                INNER JOIN cte_department cte ON department.parent = cte.id_department     " +
                "   )     " +
                "select cte.id_department, cte.title,  " +
                "       cte.code_department,  " +
                "       cte.parent,  " +
                "       cte.time_created, cte.time_modified,  " +
                "       cte.depth, cte.status, cte.path  " +
                "from cte_department cte  " +
                "where cte.status = :status ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idDepartment", idDepartment);
        query.setParameter("status", Constants.DEPARTMENT_ACTIVE_STATUS);
        List<Object[]> result = query.getResultList();
        List<FindAllDepartmentByCodeAndVisibleDto> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                FindAllDepartmentByCodeAndVisibleDto dto= new FindAllDepartmentByCodeAndVisibleDto();
                dto.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
                dto.setName(ValueUtil.getStringByObject(obj[1]));
                dto.setCodeDepartment(ValueUtil.getStringByObject(obj[2]));
                dto.setParent(ValueUtil.getIntegerByObject(obj[3]));
                dto.setTimeCreated(ValueUtil.getStringByObject(obj[4]));
                dto.setTimeModified(ValueUtil.getStringByObject(obj[5]));
                dto.setDepth(ValueUtil.getIntegerByObject(obj[6]));
                dto.setStatus(ValueUtil.getIntegerByObject(obj[7]));
                dto.setPath(ValueUtil.getStringByObject(obj[8]));
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Override
    public boolean checkExitsDepartmentByTitleOrShortName(String title, String shortName) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * " +
                "from department de " +
                "where 1 = 1 ");
        if (StringUtils.isNotBlank(title)){
            sb.append(" or de.title = :title ");
        }

        if (StringUtils.isNotBlank(shortName)){
            sb.append(" or de.short_name = :shortName ");
        }
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(title)){
            query.setParameter("title", title);
        }
        if (StringUtils.isNotBlank(shortName)){
            query.setParameter("shortName", shortName);
        }
        List<Object[]> result = query.getResultList();
        return CollectionUtils.isEmpty(result);
    }

    @Override
    public boolean isExitsRoomByIdDepartment(Integer idDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select room.id_room " +
                "from department de " +
                "    inner join room on de.id_department = room.id_department " +
                "where de.id_department = :idDepartment LIMIT 1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idDepartment", idDepartment);
        return !CollectionUtils.isEmpty(query.getResultList());
    }

    @Override
    public Optional<DepartmentDetailsResponse> findDepartmentDetailsByCode(String codeDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.title, de.code_department, " +
                "       de.short_name, de.description, de.parent,de.status,de.id_user_created, " +
                "       de.id_user_modified,de.time_created, de.time_modified,de_parent.title,de_parent.code_department " +
                "from department de left join department de_parent on de_parent.id_department =de.parent " +
                "where de.code_department = :codeDepartment ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeDepartment", codeDepartment);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                DepartmentDetailsResponse department = new DepartmentDetailsResponse();
                department.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
                department.setTitle(ValueUtil.getStringByObject(obj[1]));
                department.setCodeDepartment(ValueUtil.getStringByObject(obj[2]));
                department.setShortname(ValueUtil.getStringByObject(obj[3]));
                department.setDescription(ValueUtil.getStringByObject(obj[4]));
                department.setIdParent(ValueUtil.getIntegerByObject(obj[5]));
                department.setStatus(ValueUtil.getIntegerByObject(obj[6]));
                department.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
                department.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
                department.setTimeCreated(ValueUtil.getStringByObject(obj[9]));
                department.setTimeModified(ValueUtil.getStringByObject(obj[10]));
                department.setNameParent(ValueUtil.getStringByObject(obj[11]));
                department.setCodeParent(ValueUtil.getStringByObject(obj[12]));
                return Optional.of(department);
            }
        }
        return Optional.empty();
    }

    @Override
    public Page<StudentSearchDepartmentDto> findAllStudentSearchDepartment(Pageable pageable, StudentSearchDepartmentRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select de.id_department, de.code_department, de.title
                 from batches_registration br
                     inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration 
                     inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration
                     inner join priority_group pg on brs.id_priority_group = pg.id_priority_group
                     inner join batches_year_group_registration bygr on br.id_batches_registration = bygr.id_batches_registration
                     inner join year_group yg on bygr.id_year_group = yg.id_year_group
                     inner join room ro on brr.id_room = ro.id_room
                     inner join department de on ro.id_department = de.id_department
                 where :currentTime  between brs.registration_start_time and brs.registration_end_time
                 and pg.id_priority_group = :idPriorityGroup
                 and yg.id_year_group = :idYearGroup
                 and ro.sex_room = :sexRoom
                """);
        setConditionFindAllStudentSearchDepartment(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllStudentSearchDepartment(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<StudentSearchDepartmentDto> studentSearchDepartmentDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                StudentSearchDepartmentDto studentSearchDepartmentDto  = new StudentSearchDepartmentDto();
                studentSearchDepartmentDto.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
                studentSearchDepartmentDto.setCodeDepartment(ValueUtil.getStringByObject(obj[1]));
                studentSearchDepartmentDto.setTitleDepartment(ValueUtil.getStringByObject(obj[2]));
                studentSearchDepartmentDtos.add(studentSearchDepartmentDto);
            }
        }
        return new PageImpl<>(studentSearchDepartmentDtos, pageable, countFindAllStudentSearchDepartment(request));
    }

    private long countFindAllStudentSearchDepartment(StudentSearchDepartmentRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select count(0)
                 from batches_registration br 
                     inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration  
                     inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration 
                     inner join priority_group pg on brs.id_priority_group = pg.id_priority_group 
                     inner join batches_year_group_registration bygr on br.id_batches_registration = bygr.id_batches_registration 
                     inner join year_group yg on bygr.id_year_group = yg.id_year_group 
                     inner join room ro on brr.id_room = ro.id_room 
                     inner join department de on ro.id_department = de.id_department 
                 where :currentTime  between brs.registration_start_time and brs.registration_end_time
                 and pg.id_priority_group = :idPriorityGroup 
                 and yg.id_year_group = :idYearGroup
                 and ro.sex_room = :sexRoom
                """);
        setConditionFindAllStudentSearchDepartment(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllStudentSearchDepartment(query, request);
        return ValueUtil.getIntegerByObject(query.getSingleResult());
    }

    private void setParameterFindAllStudentSearchDepartment(Query query, StudentSearchDepartmentRequest request) {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("sexRoom", ktxUser.getSex());
    }

    private void setConditionFindAllStudentSearchDepartment(StringBuilder sb, StudentSearchDepartmentRequest request) {
        sb.append(" group by de.id_department, de.code_department, de.title ");
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
        department.setCodeDepartment(ValueUtil.getStringByObject(obj[7]));
        department.setParent(ValueUtil.getIntegerByObject(obj[8]));
        department.setDescription(ValueUtil.getStringByObject(obj[9]));
        department.setShortName(ValueUtil.getStringByObject(obj[10]));
        return department;
    }
}
