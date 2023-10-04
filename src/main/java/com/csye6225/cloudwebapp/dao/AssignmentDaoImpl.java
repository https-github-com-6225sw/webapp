package com.csye6225.cloudwebapp.dao;

import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssignmentDaoImpl implements AssignmentDao{

    private EntityManager entityManager;

    @Autowired
    public AssignmentDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }
    @Override
    public List<Assignment>  findAll(){
        TypedQuery<Assignment> theQuery = entityManager.createQuery(
                "from Assignment", Assignment.class);
        List<Assignment> Assignment = theQuery.getResultList();
        return theQuery.getResultList();
    }

    @Override
    public Assignment findById(int theId) {
        Assignment theAssignment = entityManager.find(Assignment.class, theId);
        return theAssignment;
    }

    @Override
    public Assignment save(Assignment theAssignment) {
       Assignment dbAssignment = entityManager.merge(theAssignment);
       return dbAssignment;
    }

    @Override
    public void deleteById(int theId) {
        Assignment theAssignment = entityManager.find(Assignment.class, theId);
        entityManager.remove(theAssignment);
    }

}
