package com.csye6225.cloudwebapp.entity;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @CreationTimestamp
    @Column(name="account_created")
    private LocalDateTime accountCreated;

    @UpdateTimestamp
    @Column(name="account_updated")
    private LocalDateTime accountUpdated;

    @Column(name="role")
    private String role;

//    @OneToMany(mappedBy = "user",
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH})
//    private List<Assignment> assignments;
//
//    public void add(Assignment assi){
//        if(assignments == null){
//            assignments = new ArrayList<>();
//        }
//        assignments.add(assi);
//        assi.setUser(this);
    }

