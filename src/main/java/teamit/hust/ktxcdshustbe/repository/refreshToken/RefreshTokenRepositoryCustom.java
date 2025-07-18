package teamit.hust.ktxcdshustbe.repository.refreshToken;

import teamit.hust.ktxcdshustbe.entity.RefreshToken;

import java.util.Optional;

public interface  RefreshTokenRepositoryCustom {
    Optional<RefreshToken> findByToken(String refreshToken);
}
