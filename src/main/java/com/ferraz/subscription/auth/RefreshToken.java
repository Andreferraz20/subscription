package com.ferraz.subscription.auth;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_user_id", nullable = false)
    public SystemUser systemUser;

    @Column(name = "family_id", nullable = false)
    public UUID familyId;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    public String tokenHash;

    @Column(name = "issued_at", nullable = false)
    public LocalDateTime issuedAt = LocalDateTime.now();

    @Column(name = "expires_at", nullable = false)
    public LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    public LocalDateTime revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by_id")
    public RefreshToken replacedBy;

    @Column(name = "user_agent", length = 255)
    public String userAgent;

    @Column(name = "ip_address", length = 45)
    public String ipAddress;
}
