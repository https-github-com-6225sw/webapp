package com.csye6225.cloudwebapp.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;


@Data
public class Message {
    String email;
    String assignment_id;
    String submission_id;

    String num_of_attempts;
    String submission_url;
    String submission_date;

    String update_date;

    public Message(String email, String assignment_id, String submission_id, String submission_url,
                   String submission_date, String update_date, String num_of_attempts) {
        this.email = email;
        this.assignment_id = assignment_id;
        this.submission_id = submission_id;
        this.submission_url = submission_url;
        this.submission_date = submission_date;
        this.update_date = update_date;
        this.num_of_attempts = num_of_attempts;
    }
}
