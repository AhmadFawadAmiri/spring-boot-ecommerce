package com.project.ecommerce.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;//(USER/ADMIN)
    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Cart cart;
}
