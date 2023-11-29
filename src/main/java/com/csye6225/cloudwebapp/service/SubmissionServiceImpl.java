package com.csye6225.cloudwebapp.service;

import com.csye6225.cloudwebapp.dao.SubmissionDao;
import com.csye6225.cloudwebapp.entity.Submission;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService{
    private SubmissionDao submissionDao;

    @Autowired
    public SubmissionServiceImpl (SubmissionDao thesubmissionDao){
        submissionDao = thesubmissionDao;
    }

    @Transactional
    @Override
    public Submission save(Submission theSubmission) {
        return submissionDao.save(theSubmission);
    }

    @Override
    public Submission findById(String theId) {
        return submissionDao.findById(theId);
    }

    @Override
    public List<Submission> findByAssignmentId(String theAssingmenmtId) {
        return submissionDao.findByAssignmentId(theAssingmenmtId);
    }


}
