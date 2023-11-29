package com.csye6225.cloudwebapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="submission")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID )
    @Column(name="id")
    private String id;

    @Column(name="assignment_id")
    private String assignment_id;

    @Column(name="submission_url")
    private String submission_url;

    @CreationTimestamp
    @Column(name="submission_date",
            updatable = false)
    private LocalDateTime submission_date;

    @UpdateTimestamp
    @Column(name="assignment_updated")
    private LocalDateTime assignment_updated;


}
