package com.labtech.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false)
    private Long id;

    @Size(max = 500)
    @NotNull
    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Size(max = 500)
    @NotNull
    @Column(name = "link", nullable = false, length = 500)
    private String link;

    @Column(name = "creator_ip")
    private String creatorIp;

    @ColumnDefault("0")
    @Column(name = "access_count", nullable = false)
    private Integer accessCount;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false,updatable = false)
    private Instant createdAt;


    @Column(name = "last_access")
    private Instant lastAccess;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;
}
