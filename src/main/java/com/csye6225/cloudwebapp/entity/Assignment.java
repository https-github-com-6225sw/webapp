package com.csye6225.cloudwebapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="points")
    private int points;

    @Column(name="num_of_attemps")
    private int numOfAttemps;

    @Column(name="deadline")
    private LocalDateTime deadline;

    @CreationTimestamp
    @Column(name="assignment_created",
            updatable = false)
    private LocalDateTime assignmentCreated;

    @UpdateTimestamp
    @Column(name="assignment_updated")
    private LocalDateTime assignmentUpdated;

//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                          CascadeType.DETACH, CascadeType.REFRESH})
    @Column(name="user_id")
    private int user;





}
