package com.ferraz.subscription.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findBySystemUserId(Long systemUserId) {
        return find("systemUser.id = ?1", systemUserId).firstResultOptional();
    }
}