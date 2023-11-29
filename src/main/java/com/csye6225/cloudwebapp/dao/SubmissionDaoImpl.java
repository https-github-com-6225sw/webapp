package com.csye6225.cloudwebapp.dao;


import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.entity.Submission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubmissionDaoImpl implements SubmissionDao {
    private EntityManager entityManager;

    @Autowired
    public SubmissionDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public Submission save(Submission theSubmission) {
        Submission dbSubmission = entityManager.merge(theSubmission);
        return dbSubmission;
    }

    @Override
    public Submission findById(String theId) {
        Submission theSubmisson = entityManager.find(Submission.class, theId);
        return theSubmisson;
    }

    @Override
    public List<Submission> findByAssignmentId(String assignmentId) {
        TypedQuery<Submission> theQuery = entityManager.createQuery(
                "SELECT s FROM Submission s WHERE s.assignment_id =: assignmentid", Submission.class)
                .setParameter("assignmentid", assignmentId);
        List<Submission> submission = theQuery.getResultList();
        return submission;
    }

}
