package teamit.hust.ktxcdshustbe.repository.user.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
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
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "       ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "       ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "       ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value,   " +
                "       role.id_role, role.title, role.status, role.content,     " +
                "       role.short_name, role.description, role.time_created,role.time_modified,    " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,    " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,    " +
                "       capabilities.component    " +
                "from ktx_user ktxUser    " +
                "    inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user    " +
                "    inner join role role on userRole.id_role = role.id_role    " +
                "    inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role    " +
                "    inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability    " +
                "where ktxUser.user_name = :userName    " +
                "and role.status = :statusRole and roleCapabilities.permission = :permission and capabilities.status = :statusCapabilities    " +
                "and userRole.picked = :isPicked    " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "  ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "  ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "  ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value, " +
                "  role.id_role, role.title, role.status, role.content, " +
                "  role.short_name, role.description, role.time_created,role.time_modified, " +
                "  capabilities.id_capability, capabilities.name, capabilities.cap_type, " +
                "  capabilities.status, capabilities.time_created, capabilities.time_modified, " +
                "  capabilities.component    ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userName", username);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        query.setParameter("statusRole", Constants.STATUS_ROLE_ACTIVE);
        query.setParameter("permission", Constants.ROLE_CAPABILITIES_PERMISSION);
        query.setParameter("statusCapabilities", Constants.ROLE_CAPABILITIES_ACTIVE);
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
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "       ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "       ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "       ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value,   " +
                "       role.id_role, role.title, role.status, role.content,     " +
                "       role.short_name, role.description, role.time_created,role.time_modified,    " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,    " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,    " +
                "       capabilities.component    " +
                "from ktx_user ktxUser    " +
                "    inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user    " +
                "    inner join role role on userRole.id_role = role.id_role    " +
                "    inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role    " +
                "    inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability    " +
                "where ktxUser.id_ktx_user = :idUser " +
                "and role.status = :statusRole and roleCapabilities.permission = :permission and capabilities.status = :statusCapabilities " +
                "and userRole.picked = :isPicked " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "  ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "  ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "  ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value, " +
                "  role.id_role, role.title, role.status, role.content, " +
                "  role.short_name, role.description, role.time_created,role.time_modified, " +
                "  capabilities.id_capability, capabilities.name, capabilities.cap_type, " +
                "  capabilities.status, capabilities.time_created, capabilities.time_modified, " +
                "  capabilities.component    ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idUser", userId);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        query.setParameter("statusRole", Constants.STATUS_ROLE_ACTIVE);
        query.setParameter("permission", Constants.ROLE_CAPABILITIES_PERMISSION);
        query.setParameter("statusCapabilities", Constants.ROLE_CAPABILITIES_ACTIVE);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            KtxUser userDetails = setCustomUserDetailsLoadByUserName(result.get(0));
            userDetails.setRole(getRolesLoadByUserName(result));
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationStudentHiredResponse> searchInformationStudentHiredRoomByNumberStudent(String codeStudent) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ktxUser.code_user            , " +
                "       ktxUser.value      , " +
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
                "where ktxUser.code_user = :codeStudent ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeStudent", codeStudent);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj: result){
                InformationStudentHiredResponse response = new InformationStudentHiredResponse();
                response.setCodeUser(ValueUtil.getStringByObject(obj[0]));
                response.setValue(ValueUtil.getStringByObject(obj[1]));
                response.setCodeRoom(ValueUtil.getStringByObject(obj[2]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[3]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[4]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[5]));
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
        sb.append("SELECT   " +
                "    ktxUser.id_ktx_user,    " +
                "    ktxUser.user_name,   " +
                "    ktxUser.code_user,   " +
                "    ktxUser.value,   " +
                "    ktxUser.sex    " +
                "FROM   " +
                "    ktx_user ktxUser    " +
                "    INNER JOIN user_role userRole ON ktxUser.id_ktx_user = userRole.id_user    " +
                "    INNER JOIN role roles ON userRole.id_role = roles.id_role    " +
                "WHERE   " +
                "    roles.title = 'STUDENT'  " +
                "    AND NOT EXISTS (  " +
                "        SELECT 1 FROM student_room sr   " +
                "        WHERE sr.id_user = ktxUser.id_ktx_user   " +
                "          AND sr.status = :statusStudent  " +
                "    )");
        setConditionFindAllStudents(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllStudents(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllStudentsResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object [] obj: result){
                FindAllStudentsResponse response = new FindAllStudentsResponse();
                response.setUserName(ValueUtil.getStringByObject(obj[1]));
                response.setCodeUser(ValueUtil.getStringByObject(obj[2]));
                response.setValue(ValueUtil.getStringByObject(obj[3]));
                response.setSex(ValueUtil.getIntegerByObject(obj[4]));
                responses.add(response);
            }
        }
        return new PageImpl<>(responses, pageable, countFindAllStudents(request));
    }

    @Override
    public Optional<KtxUser> findByKtxUserByUserName(String userName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "       ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "       ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "       ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value,   " +
                "       role.id_role, role.title, role.status, role.content,     " +
                "       role.short_name, role.description, role.time_created,role.time_modified,    " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,    " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,    " +
                "       capabilities.component    " +
                "from ktx_user ktxUser    " +
                "    inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user    " +
                "    inner join role role on userRole.id_role = role.id_role    " +
                "    inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role    " +
                "    inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability    " +
                "where ktxUser.user_name = :userName " +
                "and role.status = :statusRole and roleCapabilities.permission = :permission and capabilities.status = :statusCapabilities " +
                "and userRole.picked = :isPicked " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "  ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "  ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "  ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value, " +
                "  role.id_role, role.title, role.status, role.content, " +
                "  role.short_name, role.description, role.time_created,role.time_modified, " +
                "  capabilities.id_capability, capabilities.name, capabilities.cap_type, " +
                "  capabilities.status, capabilities.time_created, capabilities.time_modified, " +
                "  capabilities.component    ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userName", userName);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        query.setParameter("statusRole", Constants.STATUS_ROLE_ACTIVE);
        query.setParameter("permission", Constants.ROLE_CAPABILITIES_PERMISSION);
        query.setParameter("statusCapabilities", Constants.ROLE_CAPABILITIES_ACTIVE);
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
        sb.append(" select ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "       ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "       ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "       ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value,   " +
                "       role.id_role, role.title, role.status, role.content,     " +
                "       role.short_name, role.description, role.time_created,role.time_modified,    " +
                "       capabilities.id_capability, capabilities.name, capabilities.cap_type,    " +
                "       capabilities.status, capabilities.time_created, capabilities.time_modified,    " +
                "       capabilities.component    " +
                "from ktx_user ktxUser    " +
                "    inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user    " +
                "    inner join role role on userRole.id_role = role.id_role    " +
                "    inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role    " +
                "    inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability    " +
                "where ktxUser.code_user = :codeUser " +
                "and role.status = :statusRole and roleCapabilities.permission = :permission and capabilities.status = :statusCapabilities " +
                "and userRole.picked = :isPicked " +
                "group by ktxUser.id_ktx_user, ktxUser.user_name, ktxUser.id_user_created, " +
                "  ktxUser.id_user_modified, ktxUser.id_year_group, ktxUser.id_priority_group, " +
                "  ktxUser.password, ktxUser.sex, ktxUser.time_created, ktxUser.time_modified, " +
                "  ktxUser.is_actived, ktxUser.type_login, ktxUser.code_user, ktxUser.value, " +
                "  role.id_role, role.title, role.status, role.content, " +
                "  role.short_name, role.description, role.time_created,role.time_modified, " +
                "  capabilities.id_capability, capabilities.name, capabilities.cap_type, " +
                "  capabilities.status, capabilities.time_created, capabilities.time_modified, " +
                "  capabilities.component    ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        query.setParameter("statusRole", Constants.STATUS_ROLE_ACTIVE);
        query.setParameter("permission", Constants.ROLE_CAPABILITIES_PERMISSION);
        query.setParameter("statusCapabilities", Constants.ROLE_CAPABILITIES_ACTIVE);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            KtxUser userDetails = setCustomUserDetailsLoadByUserName(result.get(0));
            userDetails.setRole(getRolesLoadByUserName(result));
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    private void setParameterFindAllStudents(Query query, FindAllStudentsRequest request) {
        query.setParameter("statusStudent", Constants.STATUS_STUDENT_HIRING_ROOM);
        if (StringUtils.isNotBlank(request.getKeyword())){
            query.setParameter("keyword", request.getKeyword());
        }
        if(ObjectUtils.isNotEmpty(request.getStatus())){
           query.setParameter("status", request.getStatus());
        }
    }

    private void setConditionFindAllStudents(FindAllStudentsRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append("   and (ktxUser.value REGEXP '[' + :keyword + ']') OR " +
                    "       (ktxUser.user_name REGEXP '[' + :keyword + ']')) ");
        }
        if(ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append("student_room.status = :status");
        }
    }


    private long countFindAllStudents(FindAllStudentsRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) as count " +
                "FROM   " +
                "    ktx_user ktxUser    " +
                "    INNER JOIN user_role userRole ON ktxUser.id_ktx_user = userRole.id_user    " +
                "    INNER JOIN role roles ON userRole.id_role = roles.id_role    " +
                "WHERE   " +
                "    roles.title = 'STUDENT'  " +
                "    AND NOT EXISTS (  " +
                "        SELECT 1 FROM student_room sr   " +
                "        WHERE sr.id_user = ktxUser.id_ktx_user   " +
                "          AND sr.status = :statusStudent  " +
                "    )");
        setConditionFindAllStudents(request,sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllStudents(query, request);
        return ValueUtil.getLongByObject(query.getSingleResult()).longValue();
    }




    private KtxUser setCustomUserDetailsLoadByUserName(Object[] obj){
        KtxUser ktxUser = new KtxUser();
        ktxUser.setIdKtxUser(ValueUtil.getIntegerByObject(obj[0]));
        ktxUser.setUserName(ValueUtil.getStringByObject(obj[1]));
        ktxUser.setIdUserCreated(ValueUtil.getIntegerByObject(obj[2]));
        ktxUser.setIdUserModified(ValueUtil.getIntegerByObject(obj[3]));
        ktxUser.setIdYearGroup(ValueUtil.getIntegerByObject(obj[4]));
        ktxUser.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[5]));
        ktxUser.setPassword(ValueUtil.getStringByObject(obj[6]));
        ktxUser.setSex(ValueUtil.getIntegerByObject(obj[7]));
        ktxUser.setTimeCreated(ValueUtil.getLongByObject(obj[8]));
        ktxUser.setTimeModified(ValueUtil.getLongByObject(obj[9]));
        ktxUser.setIsActived(ValueUtil.getIntegerByObject(obj[10]));
        ktxUser.setTypeLogin(ValueUtil.getStringByObject(obj[11]));
        ktxUser.setCodeUser(ValueUtil.getStringByObject(obj[12]));
        ktxUser.setValue(ValueUtil.getStringByObject(obj[13]));
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
        role.setIdRole(ValueUtil.getIntegerByObject(obj[14]));
        role.setTitle(ValueUtil.getStringByObject(obj[15]));
        role.setStatus(ValueUtil.getIntegerByObject(obj[16]));
        role.setContent(ValueUtil.getStringByObject(obj[17]));
        role.setShortName(ValueUtil.getStringByObject(obj[18]));
        role.setDescription(ValueUtil.getStringByObject(obj[19]));
        role.setTimeCreated(ValueUtil.getLongByObject(obj[20]));
        role.setTimeModified(ValueUtil.getLongByObject(obj[21]));
        return role;
    }

    private Set<Capabilities> createCapabilities(List<Object[]> result) {
        Set<Capabilities> capabilities = new HashSet<>();
        for (Object[] obj : result){
            Capabilities capa = new Capabilities();
            capa.setIdCapability(ValueUtil.getIntegerByObject(obj[22]));
            capa.setName(ValueUtil.getStringByObject(obj[23]));
            capa.setCapType(ValueUtil.getStringByObject(obj[24]));
            capa.setStatus(ValueUtil.getIntegerByObject(obj[25]));
            capa.setTimeCreated(ValueUtil.getLongByObject(obj[26]));
            capa.setTimeModified(ValueUtil.getLongByObject(obj[27]));
            capa.setComponent(ValueUtil.getStringByObject(obj[28]));
            capabilities.add(capa);
        }
        return capabilities;
    }
}
