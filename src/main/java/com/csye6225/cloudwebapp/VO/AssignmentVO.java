package com.csye6225.cloudwebapp.VO;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class AssignmentVO {
    private String id;
    private String name;
    private String points;
    private String numOfAttempts;
    private LocalDateTime deadline;

    @CreationTimestamp
    private LocalDateTime assignmentCreated;

    @UpdateTimestamp
    private LocalDateTime assignmentUpdated;
}
