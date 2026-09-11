package com.ferraz.subscription.auth;

import com.ferraz.subscription.auth.enums.SystemUserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_users")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 255)
    public String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    public String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public SystemUserStatus status = SystemUserStatus.ACTIVE;

    @Column(name = "failed_login_attempts", nullable = false)
    public int failedLoginAttempts = 0;

    @Column(name = "locked_until")
    public LocalDateTime lockedUntil;

    @Column(name = "last_login_at")
    public LocalDateTime lastLoginAt;

    @Column(name = "password_changed_at")
    public LocalDateTime passwordChangedAt;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
