package com.ferraz.subscription.auth;

import com.ferraz.subscription.auth.dto.SignupRequest;
import com.ferraz.subscription.user.User;
import com.ferraz.subscription.user.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {

    final SystemUserRepository systemUserRepository;
    final UserRepository userRepository;

    @Inject
    public AuthService(SystemUserRepository systemUserRepository, UserRepository userRepository) {
        this.systemUserRepository = systemUserRepository;
        this.userRepository = userRepository;
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
        user.birthDate = request.birthDate();
        user.document = request.document();
        user.phone = request.phone();
        userRepository.persist(user);
    }
}
