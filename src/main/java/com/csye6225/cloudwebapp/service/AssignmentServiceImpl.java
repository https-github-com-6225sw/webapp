package com.csye6225.cloudwebapp.service;

import com.csye6225.cloudwebapp.dao.AssignmentDao;
import com.csye6225.cloudwebapp.entity.Assignment;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService{

    private AssignmentDao assignmentDao;

    @Autowired
    public AssignmentServiceImpl (AssignmentDao theAssignmentDao){
        assignmentDao = theAssignmentDao;
    }

    @Override
    public List<Assignment> findAll() {

        return assignmentDao.findAll();
    }

    @Override
    public Assignment findById(String theId) {
        return assignmentDao.findById(theId);
    }

    @Transactional
    @Override
    public Assignment save(Assignment theAssignment) {
        return assignmentDao.save(theAssignment);
    }

    @Transactional
    @Override
    public void deleteById(String theId) {
        assignmentDao.deleteById(theId);
    }
}
