package com.csye6225.cloudwebapp.dao;

import com.csye6225.cloudwebapp.entity.Assignment;

import java.util.List;

public interface AssignmentDao {
    List<Assignment> findAll();

    Assignment findById(String theId);

    Assignment save(Assignment theAssignment);

    void deleteById(String theId);
}
