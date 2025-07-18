package teamit.hust.ktxcdshustbe.repository.user.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.Capabilities;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.repository.user.KtxUserRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.user.FindAllStudentsRequest;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.user.FindAllStudentsResponse;
import teamit.hust.ktxcdshustbe.response.user.InformationStudentHiredResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;


public class KtxUserRepositoryImpl implements KtxUserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Optional<KtxUser> loadUserByUsername(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "       ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "       ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "       ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "       ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "       ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "       ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "       ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "       ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "       ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "       ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "       ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "       ktxUser.code_user,  " +
                "       role.id_role, role.title, role.status, role.content,   " +
                "       role.short_name, role.description, role.time_created,role.time_modified,  " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "       capabilities.component  " +
                "from ktx_user ktxUser  " +
                "         inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user  " +
                "         inner join role role on userRole.id_role = role.id_role  " +
                "         inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role  " +
                "         inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability  " +
                "    where ktxUser.user_name = :userName  " +
                "      and role.status = 1 and roleCapabilities.permission = 1 and capabilities.status = 1  " +
                "      and userRole.picked = :isPicked  " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "         ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "         ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "         ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "         ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "         ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "         ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "         ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "         ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "         ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "         ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "         ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "         ktxUser.code_user,  " +
                "         role.id_role, role.title, role.status,role.content,   " +
                "         role.short_name, role.description, role.time_created, role.time_modified,  " +
                "         capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "         capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "         capabilities.component ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userName", username);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            KtxUser userDetails = setCustomUserDetailsLoadByUserName(result.get(0));
            userDetails.setRole(getRolesLoadByUserName(result));
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    @Override
    public Boolean exitsByUserName(String userName) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id_ktx_user, user_name from ktx_user where user_name = :userName ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userName", userName);
        List<Object[]> result = query.getResultList();
        return !CollectionUtils.isEmpty(result) ? true : false;
    }

    @Override
    public Optional<KtxUser> findByKtxUserId(Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "       ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "       ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "       ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "       ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "       ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "       ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "       ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "       ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "       ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "       ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "       ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "       ktxUser.code_user,  " +
                "       role.id_role, role.title, role.status, role.content,   " +
                "       role.short_name, role.description, role.time_created,role.time_modified,  " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "       capabilities.component  " +
                "from ktx_user ktxUser  " +
                "         inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user  " +
                "         inner join role role on userRole.id_role = role.id_role  " +
                "         inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role  " +
                "         inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability  " +
                "    where ktxUser.id_ktx_user = :idUser  " +
                "      and role.status = 1 and roleCapabilities.permission = 1 and capabilities.status = 1  " +
                "      and userRole.picked = :isPicked  " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "         ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "         ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "         ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "         ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "         ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "         ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "         ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "         ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "         ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "         ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "         ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "         ktxUser.code_user,  " +
                "         role.id_role, role.title, role.status,role.content,   " +
                "         role.short_name, role.description, role.time_created, role.time_modified,  " +
                "         capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "         capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "         capabilities.component ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idUser", userId);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            KtxUser userDetails = setCustomUserDetailsLoadByUserName(result.get(0));
            userDetails.setRole(getRolesLoadByUserName(result));
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationStudentHiredResponse> searchInformationStudentHiredRoomByNumberStudent(String numberStudent) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ktxUser.code_user            , " +
                "       ktxUser.full_name      fullName, " +
                "       ktxUser.number_student numberStudent, " +
                "       ro.code_room                 , " +
                "       ro.title               titleRoom, " +
                "       de.code_department                 , " +
                "       de.title               titleDepartment, " +
                "       ktxUser.sex " +
                "from ktx_user ktxUser " +
                "         inner join (select * from student_room studentRoom where studentRoom.status = 1) studentRoom " +
                "                    on ktxUser.id_ktx_user = studentRoom.id_user " +
                "         inner join room ro on studentRoom.id_room = ro.id_room " +
                "         inner join department de on ro.id_department = de.id_department " +
                "where ktxUser.number_student = :numberStudent ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("numberStudent", numberStudent);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj: result){
                InformationStudentHiredResponse response = new InformationStudentHiredResponse();
                response.setCodeUser(ValueUtil.getStringByObject(obj[0]));
                response.setFullName(ValueUtil.getStringByObject(obj[1]));
                response.setNumberStudent(ValueUtil.getStringByObject(obj[2]));
                response.setCodeRoom(ValueUtil.getStringByObject(obj[3]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[4]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[5]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[6]));
                response.setSex(ValueUtil.getStringByObject(obj[7]));
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }

    @Modifying
    @Transactional
    @Override
    public int updateStatusIsActive(Integer userId, Integer statusIsActive) {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE ktx_user ktxUser " +
                "SET ktxUser.is_actived = :isActived " +
                "WHERE ktxUser.id_ktx_user = :userId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("isActived", statusIsActive);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    @Modifying
    @Transactional
    @Override
    public int updateStatusRegisterRoom(Integer userId, Integer statusRegisterRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE ktx_user ktxUser " +
                "SET ktxUser.status_register_room = :statusRegisterRoom " +
                "WHERE ktxUser.id_ktx_user = :userId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("statusRegisterRoom", statusRegisterRoom);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    @Override
    public Page<FindAllStudentsResponse> findAllStudent(FindAllStudentsRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ktxUser.id_ktx_user, " +
                "       ktxUser.user_name, " +
                "       ktxUser.full_name, " +
                "       ktxUser.number_student, " +
                "       ktxUser.phone_number, " +
                "       ktxUser.title_major, " +
                "       ktxUser.status_register_room, " +
                "       if(studentRoom.id_user is not null, 1, -1) statusHiredRoom " +
                "from ktx_user ktxUser " +
                "         inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user " +
                "         inner join role roles on userRole.id_role = roles.id_role " +
                "         left join (select * " +
                "                    from student_room studentRoom " +
                "                    where studentRoom.status = 1) " +
                "             studentRoom on ktxUser.id_ktx_user = studentRoom.id_user " +
                "where 1 = 1 " +
                "  and roles.title = 'STUDENT' ");
        setConditionFindAllStudents(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllStudents(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllStudentsResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object [] obj: result){
                FindAllStudentsResponse response = new FindAllStudentsResponse();
                response.setCodeUser(ValueUtil.getStringByObject(obj[0]));
                response.setUserName(ValueUtil.getStringByObject(obj[1]));
                response.setFullName(ValueUtil.getStringByObject(obj[2]));
                response.setNumberStudent(ValueUtil.getStringByObject(obj[3]));
                response.setPhoneNumber(ValueUtil.getStringByObject(obj[4]));
                response.setTitleMajor(ValueUtil.getStringByObject(obj[5]));
                response.setStatusDeclareInformation(ValueUtil.getIntegerByObject(obj[6]));
                response.setStatusHireRoom(ValueUtil.getIntegerByObject(obj[7]));
                responses.add(response);
            }
        }
        return new PageImpl<>(responses, pageable, countFindAllStudents(request));
    }

    @Override
    public Optional<KtxUser> findByKtxUserByUserName(String userName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "       ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "       ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "       ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "       ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "       ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "       ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "       ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "       ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "       ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "       ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "       ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "       ktxUser.code_user,  " +
                "       role.id_role, role.title, role.status, role.content,   " +
                "       role.short_name, role.description, role.time_created,role.time_modified,  " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "       capabilities.component  " +
                "from ktx_user ktxUser  " +
                "         inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user  " +
                "         inner join role role on userRole.id_role = role.id_role  " +
                "         inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role  " +
                "         inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability  " +
                "    where ktxUser.user_name = :userName  " +
                "      and role.status = 1 and roleCapabilities.permission = 1 and capabilities.status = 1  " +
                "      and userRole.picked = :isPicked  " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "         ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "         ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "         ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "         ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "         ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "         ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "         ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "         ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "         ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "         ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "         ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "         ktxUser.code_user,  " +
                "         role.id_role, role.title, role.status,role.content,   " +
                "         role.short_name, role.description, role.time_created, role.time_modified,  " +
                "         capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "         capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "         capabilities.component ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userName", userName);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            KtxUser userDetails = setCustomUserDetailsLoadByUserName(result.get(0));
            userDetails.setRole(getRolesLoadByUserName(result));
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    @Override
    public Optional<KtxUser> findByKtxUserCode(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "       ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "       ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "       ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "       ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "       ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "       ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "       ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "       ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "       ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "       ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "       ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "       ktxUser.code_user,  " +
                "       role.id_role, role.title, role.status, role.content,   " +
                "       role.short_name, role.description, role.time_created,role.time_modified,  " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "       capabilities.component  " +
                "from ktx_user ktxUser  " +
                "         inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user  " +
                "         inner join role role on userRole.id_role = role.id_role  " +
                "         inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role  " +
                "         inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability  " +
                "    where ktxUser.code_user = :codeUser  " +
                "      and role.status = 1 and roleCapabilities.permission = 1 and capabilities.status = 1  " +
                "      and userRole.picked = :isPicked  " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.address,  " +
                "         ktxUser.address_contact, ktxUser.cccd, ktxUser.class_user,  " +
                "         ktxUser.date_of_birth, ktxUser.faculty, ktxUser.district,  " +
                "         ktxUser.email_contact, ktxUser.full_name, ktxUser.is_actived,  " +
                "         ktxUser.number_student, ktxUser.password, ktxUser.path_avatar,  " +
                "         ktxUser.phone_number, ktxUser.province, ktxUser.school, ktxUser.sex,  " +
                "         ktxUser.time_created, ktxUser.time_modified, ktxUser.wards,  " +
                "         ktxUser.year_grade, ktxUser.status_register_room, ktxUser.religion,  " +
                "         ktxUser.code_major, ktxUser.title_major, ktxUser.type_login,  " +
                "         ktxUser.nation, ktxUser.area, ktxUser.name_father, ktxUser.year_father,  " +
                "         ktxUser.phone_number_father, ktxUser.address_father, ktxUser.name_mother,  " +
                "         ktxUser.year_mother, ktxUser.phone_number_mother, ktxUser.address_mother,  " +
                "         ktxUser.code_user,  " +
                "         role.id_role, role.title, role.status,role.content,   " +
                "         role.short_name, role.description, role.time_created, role.time_modified,  " +
                "         capabilities.id_capability, capabilities.name, capabilities.cap_type,  " +
                "         capabilities.status, capabilities.time_created, capabilities.time_modified,  " +
                "         capabilities.component ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            KtxUser userDetails = setCustomUserDetailsLoadByUserName(result.get(0));
            userDetails.setRole(getRolesLoadByUserName(result));
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    private void setParameterFindAllStudents(Query query, FindAllStudentsRequest request) {
        if (StringUtils.isNotBlank(request.getKeyword())){
            query.setParameter("keyword", request.getKeyword());
        }
    }

    private void setConditionFindAllStudents(FindAllStudentsRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append("   and ((ktxUser.full_name like '%' + :keyword + '%') OR " +
                    "       (ktxUser.number_student REGEXP '[' + :keyword + ']') OR " +
                    "       (ktxUser.phone_number REGEXP '[' + :keyword + ']') OR " +
                    "       (ktxUser.user_name REGEXP '[' + :keyword + ']')) ");
        }
    }


    private long countFindAllStudents(FindAllStudentsRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) as count " +
                "from ktx_user ktxUser " +
                "         inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user " +
                "         inner join role roles on userRole.id_role = roles.id_role " +
                "         left join (select * " +
                "                    from student_room studentRoom " +
                "                    where studentRoom.status = 1) " +
                "             studentRoom on ktxUser.id_ktx_user = studentRoom.id_user " +
                "where 1 = 1 " +
                "  and roles.title = 'STUDENT' ");
        setConditionFindAllStudents(request,sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllStudents(query, request);
        return ValueUtil.getLongByObject(query.getSingleResult()).longValue();
    }




    private KtxUser setCustomUserDetailsLoadByUserName(Object[] obj){
        KtxUser ktxUser = new KtxUser();
        ktxUser.setIdKtxUser(ValueUtil.getIntegerByObject(ValueUtil.getIntegerByObject(obj[0])));
        ktxUser.setUserName(ValueUtil.getStringByObject(obj[1]));
        ktxUser.setAddress(ValueUtil.getStringByObject(obj[2]));
        ktxUser.setAddressContact(ValueUtil.getStringByObject(obj[3]));
        ktxUser.setCccd(ValueUtil.getStringByObject(obj[4]));
        ktxUser.setClassUser(ValueUtil.getStringByObject(obj[5]));
        ktxUser.setDateOfBirth(ValueUtil.getStringByObject(obj[6]));
        ktxUser.setFaculty(ValueUtil.getStringByObject(obj[7]));
        ktxUser.setDistrict(ValueUtil.getStringByObject(obj[8]));
        ktxUser.setEmailContact(ValueUtil.getStringByObject(obj[9]));
        ktxUser.setFullName(ValueUtil.getStringByObject(obj[10]));
        ktxUser.setIsActived(ValueUtil.getIntegerByObject(obj[11]));
        ktxUser.setNumberStudent(ValueUtil.getStringByObject(obj[12]));
        ktxUser.setPassword(ValueUtil.getStringByObject(obj[13]));
        ktxUser.setPathAvatar(ValueUtil.getStringByObject(obj[14]));
        ktxUser.setPhoneNumber(ValueUtil.getStringByObject(obj[15]));
        ktxUser.setProvince(ValueUtil.getStringByObject(obj[16]));
        ktxUser.setSchool(ValueUtil.getStringByObject(obj[17]));
        ktxUser.setSex(ValueUtil.getIntegerByObject(obj[18]));
        ktxUser.setTimeCreated(ValueUtil.getLongByObject(obj[19]));
        ktxUser.setTimeModified(ValueUtil.getLongByObject(obj[20]));
        ktxUser.setWards(ValueUtil.getStringByObject(obj[21]));
        ktxUser.setYearGrade(ValueUtil.getIntegerByObject(obj[22]));
        ktxUser.setStatusRegisterRoom(ValueUtil.getIntegerByObject(obj[23]));
        ktxUser.setReligion(ValueUtil.getStringByObject(obj[24]));
        ktxUser.setCodeMajor(ValueUtil.getStringByObject(obj[25]));
        ktxUser.setTitleMajor(ValueUtil.getStringByObject(obj[26]));
        ktxUser.setTypeLogin(ValueUtil.getStringByObject(obj[27]));
        ktxUser.setNation(ValueUtil.getStringByObject(obj[28]));
        ktxUser.setArea(ValueUtil.getStringByObject(obj[29]));
        ktxUser.setNameFather(ValueUtil.getStringByObject(obj[30]));
        ktxUser.setYearFather(ValueUtil.getIntegerByObject(obj[31]));
        ktxUser.setPhoneNumberFather(ValueUtil.getStringByObject(obj[32]));
        ktxUser.setAddressFather(ValueUtil.getStringByObject(obj[33]));
        ktxUser.setNameMother(ValueUtil.getStringByObject(obj[34]));
        ktxUser.setYearMother(ValueUtil.getIntegerByObject(obj[35]));
        ktxUser.setPhoneNumberMother(ValueUtil.getStringByObject(obj[36]));
        ktxUser.setAddressMother(ValueUtil.getStringByObject(obj[37]));
        ktxUser.setCodeUser(ValueUtil.getStringByObject(obj[38]));
        return ktxUser;
    }

    private Collection<Role> getRolesLoadByUserName(List<Object[]> result) {
        List<Role> roles = new ArrayList<>();
        Role role = createRole(result.get(0));
        role.setCapabilities(createCapabilities(result));
        roles.add(role);
        return roles;
    }

    private Role createRole(Object[] obj) {
        Role role = new Role();
        role.setIdRole(ValueUtil.getIntegerByObject(obj[39]));
        role.setTitle(ValueUtil.getStringByObject(obj[40]));
        role.setStatus(ValueUtil.getIntegerByObject(obj[41]));
        role.setContent(ValueUtil.getStringByObject(obj[42]));
        role.setShortName(ValueUtil.getStringByObject(obj[43]));
        role.setDescription(ValueUtil.getStringByObject(obj[44]));
        role.setTimeCreated(ValueUtil.getLongByObject(obj[45]));
        role.setTimeModified(ValueUtil.getLongByObject(obj[46]));
        return role;
    }

    private Set<Capabilities> createCapabilities(List<Object[]> result) {
        Set<Capabilities> capabilities = new HashSet<>();
        for (Object[] obj : result){
            Capabilities capa = new Capabilities();
            capa.setIdCapability(ValueUtil.getIntegerByObject(obj[47]));
            capa.setName(ValueUtil.getStringByObject(obj[48]));
            capa.setCapType(ValueUtil.getStringByObject(obj[49]));
            capa.setComponent(ValueUtil.getStringByObject(obj[53]));

            capabilities.add(capa);
        }
        return capabilities;
    }
}
