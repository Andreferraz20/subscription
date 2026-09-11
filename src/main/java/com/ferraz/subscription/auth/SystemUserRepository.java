package com.ferraz.subscription.auth;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SystemUserRepository implements PanacheRepository<SystemUser> {

    public Optional<SystemUser> findByEmail(String email) {
        return find("lower(email) = ?1", email.toLowerCase())
                .firstResultOptional();
    }
}