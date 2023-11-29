package com.csye6225.cloudwebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID )
    @Column(name="id")
    private String id;

    @Column(name="name")
//    @NotNull
//   @NotBlank
    private String name;

    @Column(name="points")
//    @NotNull
    private String points;

    @Column(name="num_of_attempts")
//    @NotNull
    private String numOfAttempts;


    @Column(name="deadline")
//    @NotNull
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
    private String user;

    @Column(name="attempts_used")
    private String attemptsUsed;







}
