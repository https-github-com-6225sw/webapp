package com.csye6225.cloudwebapp.VO;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class SubmissionVO {
    private String id;
    private String assignment_id;
    private String submission_url;
    @CreationTimestamp
    private LocalDateTime submission_date;
    @UpdateTimestamp
    private LocalDateTime assignment_updated;
}
