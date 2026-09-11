package com.ferraz.subscription.auth;

import com.ferraz.subscription.auth.dto.LoginRequest;
import com.ferraz.subscription.auth.dto.SignupRequest;
import com.ferraz.subscription.auth.dto.TokenResponse;
import com.ferraz.subscription.auth.enums.SystemUserStatus;
import com.ferraz.subscription.common.exception.ForbiddenException;
import com.ferraz.subscription.common.exception.UnauthorizedException;
import com.ferraz.subscription.user.User;
import com.ferraz.subscription.user.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    final SystemUserRepository systemUserRepository;
    final UserRepository userRepository;
    final RefreshTokenRepository refreshTokenRepository;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    final Integer EXPIRES_IN = 900;

    @Inject
    public AuthService(SystemUserRepository systemUserRepository, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.systemUserRepository = systemUserRepository;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void signup(final SignupRequest request) {

        SystemUser systemUser = new SystemUser();
        systemUser.email = request.email().toLowerCase();
        systemUser.passwordHash = BcryptUtil.bcryptHash(request.password());
        systemUserRepository.persist(systemUser);

        User user = new User();
        user.systemUser = systemUser;
        user.firstName = request.firstName();
        user.lastName = request.lastName();
        user.birthDate = request.birthDate();
        user.document = request.document();
        user.phone = request.phone();
        userRepository.persist(user);
    }

    @Transactional(dontRollbackOn = UnauthorizedException.class)
    public TokenResponse login(LoginRequest loginRequest) {
        final SystemUser systemUser = systemUserRepository.findByEmail(loginRequest.email()).orElseThrow(UnauthorizedException::new);

        if(systemUser.status == SystemUserStatus.DISABLED) {
            throw new ForbiddenException();
        }

        if(systemUser.lockedUntil != null && systemUser.lockedUntil.isAfter(LocalDateTime.now())) {
            throw new UnauthorizedException();
        }

        if(!(BcryptUtil.matches(loginRequest.password(), systemUser.passwordHash))) {
            systemUser.failedLoginAttempts++;
            if( systemUser.failedLoginAttempts >= 5) {
                systemUser.lockedUntil = LocalDateTime.now().plusMinutes(15);
            }
            throw new UnauthorizedException();
        }

        systemUser.failedLoginAttempts = 0;
        systemUser.lastLoginAt = LocalDateTime.now();

        return this.issueTokens(systemUser);
    }

    private TokenResponse issueTokens(SystemUser systemUser) {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String rawRefreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        byte[] hashBytes;
        try {
            hashBytes = MessageDigest.getInstance("SHA-256").digest(rawRefreshToken.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String tokenHash = HexFormat.of().formatHex(hashBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.systemUser = systemUser;
        refreshToken.familyId = UUID.randomUUID();
        refreshToken.tokenHash = tokenHash;
        refreshToken.expiresAt = LocalDateTime.now().plusDays(30);
        refreshTokenRepository.persist(refreshToken);

        String accessToken = Jwt.issuer(issuer)
                .subject(systemUser.id.toString())
                .expiresIn(EXPIRES_IN)
                .sign();

        return new TokenResponse(accessToken, rawRefreshToken, EXPIRES_IN);
    }
}
