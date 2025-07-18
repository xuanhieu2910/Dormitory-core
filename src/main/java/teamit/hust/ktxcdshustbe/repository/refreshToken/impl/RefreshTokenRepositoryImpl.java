package teamit.hust.ktxcdshustbe.repository.refreshToken.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.RefreshToken;
import teamit.hust.ktxcdshustbe.repository.refreshToken.RefreshTokenRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.List;
import java.util.Optional;


public class RefreshTokenRepositoryImpl implements RefreshTokenRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<RefreshToken> findByToken(String refreshToken) {
        StringBuilder sb = new StringBuilder();
        sb.append("select refreshToken.id_refresh_token, refreshToken.id_user, " +
                "       refreshToken.token, refreshToken.expiry_date, " +
                "       refreshToken.revoked " +
                "from refresh_token refreshToken " +
                "where refreshToken.token = :refreshToken ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("refreshToken", refreshToken);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for(Object[] obj: result){
                RefreshToken response = new RefreshToken();
                response.setIdRefreshToken(ValueUtil.getIntegerByObject(obj[0]));
                response.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                response.setToken(ValueUtil.getStringByObject(obj[2]));
                response.setExpiryDate(ValueUtil.getTimestampByObject(obj[3]));
                response.setRevoked(ValueUtil.getBooleanByObject(obj[4]));
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }
}
