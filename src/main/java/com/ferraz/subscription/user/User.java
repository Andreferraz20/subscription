package com.ferraz.subscription.user;

import com.ferraz.subscription.auth.SystemUser;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_user_id", nullable = false, unique = true)
    public SystemUser systemUser;

    @Column(name = "first_name", nullable = false, length = 150)
    public String firstName;

    @Column(name = "last_name", nullable = false, length = 150)
    public String lastName;

    @Column(length = 20)
    public String document;

    @Column(length = 20)
    public String phone;

    @Column(name = "birth_date")
    public LocalDate birthDate;

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