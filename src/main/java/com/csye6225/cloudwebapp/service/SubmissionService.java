package com.csye6225.cloudwebapp.service;

import com.csye6225.cloudwebapp.entity.Submission;

import java.util.List;

public interface SubmissionService {
    Submission save(Submission theSubmission);

    Submission findById(String theId);

    List<Submission> findByAssignmentId(String theAssingmenmtId);
}
