package com.libraryAdmin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String contact;
    private String designation;
    private Boolean isActive;

    @CreationTimestamp
    private Instant createdTime;
    @UpdateTimestamp
    private String updatedTime;
    private Boolean isSuperAdmin;
    private Long createdBy;

}
