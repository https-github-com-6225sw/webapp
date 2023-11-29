package com.csye6225.cloudwebapp.dao;

import com.csye6225.cloudwebapp.entity.Submission;

import java.util.List;

public interface SubmissionDao {

    Submission save(Submission theSubmission);

    Submission findById(String theId);

    List<Submission> findByAssignmentId(String assignmentId);
}
