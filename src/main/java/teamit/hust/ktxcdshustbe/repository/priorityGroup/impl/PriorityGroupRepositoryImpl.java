package teamit.hust.ktxcdshustbe.repository.priorityGroup.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.repository.priorityGroup.PriorityGroupRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindAllPriorityGroupDto;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindPriorityGroupDetailDto;
import teamit.hust.ktxcdshustbe.dto.semester.FindAllSemesterDto;
import teamit.hust.ktxcdshustbe.repository.priorityGroup.PriorityGroupRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.priorityGroup.FindAllPriorityGroupRequest;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PriorityGroupRepositoryImpl implements PriorityGroupRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Optional<PriorityGroup> findPriorityGroupByTitle(String titlePriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select priority_group.id_priority_group, priority_group_code,  " +
                "       title, description, time_created, " +
                "       time_modified, id_user_created, id_user_modified " +
                "from priority_group where priority_group.title = :titlePriorityGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titlePriorityGroup", titlePriorityGroup);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                return Optional.of(writeDataPriorityGroup(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<PriorityGroup> findPriorityGroupByIdPriorityGroup(Integer idPriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select priority_group.id_priority_group, priority_group_code,  " +
                "       title, description, time_created, " +
                "       time_modified, id_user_created, id_user_modified " +
                "from priority_group where priority_group.id_priority_group = :idPriorityGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idPriorityGroup", idPriorityGroup);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                return Optional.of(writeDataPriorityGroup(obj));
            }
        }
        return Optional.empty();
    }

    private PriorityGroup writeDataPriorityGroup(Object[] obj) {
        PriorityGroup priorityGroup = new PriorityGroup();
        priorityGroup.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[0]));
        priorityGroup.setPriorityGroupCode(ValueUtil.getStringByObject(obj[1]));
        priorityGroup.setTitle(ValueUtil.getStringByObject(obj[2]));
        priorityGroup.setDescription(ValueUtil.getStringByObject(obj[3]));
        priorityGroup.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        priorityGroup.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        priorityGroup.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        priorityGroup.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        return priorityGroup;
    }
    @Override
    public Page<FindAllPriorityGroupDto> findAllPriorityGroup(FindAllPriorityGroupRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select pg.id_priority_group, pg.priority_group_code,
                pg.title, pg.description, pg.time_created, pg.time_modified,
                pg.id_user_created, pg.id_user_modified,
                ku_created.user_name, ku_modified.user_name
                from priority_group pg
                    inner join ktx_user ku_created on pg.id_user_created = ku_created.id_ktx_user
                    inner join ktx_user ku_modified on pg.id_user_modified = ku_modified.id_ktx_user
                where 1 =1
                """);
        setConditionFindAllPriorityGroupDto(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllPriorityGroupDto(request, query);
        PageUtils.buildQuery(pageable, query);
        List<FindAllPriorityGroupDto> priorityGroupDtos = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if(!CollectionUtils.isEmpty(result)){
            for(Object[] obj : result){
                FindAllPriorityGroupDto findAllPriorityGroupDto = new FindAllPriorityGroupDto();
                findAllPriorityGroupDto.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[0]));
                findAllPriorityGroupDto.setPriorityGroupCode(ValueUtil.getStringByObject(obj[1]));
                findAllPriorityGroupDto.setTitlePriorityGroup(ValueUtil.getStringByObject(obj[2]));
                findAllPriorityGroupDto.setDescription(ValueUtil.getStringByObject(obj[3]));
                findAllPriorityGroupDto.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
                findAllPriorityGroupDto.setTimeModified(ValueUtil.getLongByObject(obj[5]));
                findAllPriorityGroupDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                findAllPriorityGroupDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                findAllPriorityGroupDto.setUserNameCreated(ValueUtil.getStringByObject(obj[8]));
                findAllPriorityGroupDto.setUserNameModified(ValueUtil.getStringByObject(obj[9]));
                priorityGroupDtos.add(findAllPriorityGroupDto);
            }
        }
        return new PageImpl<>(priorityGroupDtos, pageable, countFindAllPriorityGroup(request) );
    }
    private Long countFindAllPriorityGroup(FindAllPriorityGroupRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select count(*)
                from priority_group pg
                    inner join ktx_user ku_created on pg.id_user_created = ku_created.id_ktx_user
                    inner join ktx_user ku_modified on pg.id_user_modified = ku_modified.id_ktx_user
                where 1 = 1    
                """);
        setConditionFindAllPriorityGroupDto(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllPriorityGroupDto(request, query);
        return ValueUtil.getLongByObject(query.getSingleResult());
    }

    private  void setParameterFindAllPriorityGroupDto(FindAllPriorityGroupRequest request, Query query) {
        if(StringUtils.isNotBlank(request.getTitlePriorityGroup())){
            query.setParameter("titlePriorityGroup", request.getTitlePriorityGroup());
        }
        if (StringUtils.isNotBlank(request.getKeyword())){
            query.setParameter("keyword", request.getKeyword());
        }
    }

    private void setConditionFindAllPriorityGroupDto(FindAllPriorityGroupRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append("   and (pg.title REGEXP  :keyword )  " );
        }
        if(StringUtils.isNotBlank(request.getTitlePriorityGroup())){
            sb.append(" and pg.title REGEXP :titlePriorityGroup ");
        }
        sb.append(" order by pg.id_priority_group desc ");
    }


    @Override
    public FindPriorityGroupDetailDto getPriorityGroupDetailByPriorityGroupCode(String priorityGroupCode){
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select pg.id_priority_group, pg.priority_group_code,
                pg.title, pg.description, pg.time_created, pg.time_modified,
                pg.id_user_created, pg.id_user_modified,
                ku_created.user_name, ku_modified.user_name
                from priority_group pg
                    inner join ktx_user ku_created on pg.id_user_created = ku_created.id_ktx_user
                    inner join ktx_user ku_modified on pg.id_user_modified = ku_modified.id_ktx_user
                where pg.priority_group_code=:priorityGroupCode
                """);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("priorityGroupCode", priorityGroupCode);
        List<Object[]> result = query.getResultList();
        if(!CollectionUtils.isEmpty(result)){
            Object[] obj = result.get(0);

            FindPriorityGroupDetailDto findPriorityGroupDetailDto = new FindPriorityGroupDetailDto();
            findPriorityGroupDetailDto.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[0]));
            findPriorityGroupDetailDto.setPriorityGroupCode(ValueUtil.getStringByObject(obj[1]));
            findPriorityGroupDetailDto.setTitlePriorityGroup(ValueUtil.getStringByObject(obj[2]));
            findPriorityGroupDetailDto.setDescription(ValueUtil.getStringByObject(obj[3]));
            findPriorityGroupDetailDto.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
            findPriorityGroupDetailDto.setTimeModified(ValueUtil.getLongByObject(obj[5]));
            findPriorityGroupDetailDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
            findPriorityGroupDetailDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
            findPriorityGroupDetailDto.setUserNameCreated(ValueUtil.getStringByObject(obj[8]));
            findPriorityGroupDetailDto.setUserNameModified(ValueUtil.getStringByObject(obj[9]));
            return findPriorityGroupDetailDto;
        }
        return null;
    }

    @Override
    public Optional<List<PriorityGroup>> findPriorityGroupByListPriorityGroupCode(List<String> codesPriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_priority_group, priority_group_code,   " +
                "       title, description, time_created,   " +
                "       time_modified, id_user_created, id_user_modified  " +
                "from priority_group  " +
                "where priority_group_code in (:priorityCodes) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("priorityCodes", codesPriorityGroup);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)){
            List<PriorityGroup> priorityGroups = new ArrayList<>();
            for (Object[] obj : results){
                priorityGroups.add(writeDataPriorityGroup(obj));
            }
            return Optional.of(priorityGroups);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<PriorityGroup>> findPriorityGroupByIdsPriorityGroup(List<Integer> idsPriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_priority_group, priority_group_code,   " +
                "       title, description, time_created,   " +
                "       time_modified, id_user_created, id_user_modified  " +
                "from priority_group  " +
                "where id_priority_group in (:idsPriorityGroup) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idsPriorityGroup", idsPriorityGroup);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)){
            List<PriorityGroup> priorityGroups = new ArrayList<>();
            for (Object[] obj : results){
                priorityGroups.add(writeDataPriorityGroup(obj));
            }
            return Optional.of(priorityGroups);
        }
        return Optional.empty();
    }

    @Override
    public Optional<PriorityGroup> findByPriorityGroupCode(String priorityGroupCode){
        StringBuilder sb = new StringBuilder();
        sb.append("""
                 select pg.id_priority_group, pg.priority_group_code,
                pg.title, pg.description, pg.time_created, pg.time_modified,
                pg.id_user_created, pg.id_user_modified
                from priority_group pg
                where pg.priority_group_code=:priorityGroupCode
                """);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("priorityGroupCode", priorityGroupCode);
        List<Object[]> result = query.getResultList();
        if(!CollectionUtils.isEmpty(result)){
            for(Object[] obj : result){
                PriorityGroup priorityGroup = new PriorityGroup();
                priorityGroup.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[0]));
                priorityGroup.setPriorityGroupCode(ValueUtil.getStringByObject(obj[1]));
                priorityGroup.setTitle(ValueUtil.getStringByObject(obj[2]));
                priorityGroup.setDescription(ValueUtil.getStringByObject(obj[3]));
                priorityGroup.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
                priorityGroup.setTimeModified(ValueUtil.getLongByObject(obj[5]));
                priorityGroup.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                priorityGroup.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                return Optional.of(priorityGroup);
            }
        }
        return Optional.empty();
    }

//    @Override
//    public boolean existsByPriorityGroupCode(String priorityGroupCode) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("""
//                    select pg.id_priority_group
//                    from priority_group pg
//                    where pg.priority_group_code=:priorityGroupCode
//                    """);
//            Query query = entityManager.createNativeQuery(sb.toString());
//            query.setParameter("priorityGroupCode", priorityGroupCode);
//            return !CollectionUtils.isEmpty(query.getResultList());
//    }
    @Override
    public boolean existsByTitlePriorityGroup(String titlePriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select pg.title
                from priority_group pg
                where pg.title=:titlePriorityGroup
                """);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titlePriorityGroup", titlePriorityGroup);
        return !CollectionUtils.isEmpty(query.getResultList());
    }

    @Override
    public Optional<List<PriorityGroup>> findListAllPriorityGroup() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_priority_group, priority_group_code,   " +
                "       title, description, time_created,   " +
                "       time_modified, id_user_created, id_user_modified  " +
                "from priority_group  " +
                "where 1=1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)){
            List<PriorityGroup> priorityGroups = new ArrayList<>();
            for (Object[] obj : results){
                priorityGroups.add(writeDataPriorityGroup(obj));
            }
            return Optional.of(priorityGroups);
        }
        return Optional.empty();
    }
}
