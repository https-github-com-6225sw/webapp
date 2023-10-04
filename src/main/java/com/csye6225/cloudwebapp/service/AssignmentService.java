package com.csye6225.cloudwebapp.service;

import com.csye6225.cloudwebapp.entity.Assignment;

import java.util.List;

public interface AssignmentService {
    List<Assignment> findAll();

    Assignment findById(int theId);

    Assignment save(Assignment theAssignment);

    void deleteById(int theId);
}
