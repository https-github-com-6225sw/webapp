package com.csye6225.cloudwebapp.rest;

import com.csye6225.cloudwebapp.dao.AssignmentDao;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class AssignmentController {

    private AssignmentService assignmentService;

    @Autowired
    private AssignmentController(AssignmentService theAssignmentService){
        assignmentService = theAssignmentService;
    }

    @GetMapping("/assignments")
    public List<Assignment> findAll(){
        return assignmentService.findAll();
    }

    @GetMapping("/assignments/{assignmentId}")
    public Assignment getAssignment(@PathVariable int assignmentId){
        Assignment theAssignment = assignmentService.findById(assignmentId);

        if(theAssignment == null){
            throw new RuntimeException("assignment Id not found - " + assignmentId);
        }
        return theAssignment;
    }

    @PostMapping("/assignments")
    public Assignment addAssignment(@RequestBody Assignment theAssignment) throws Exception {
        theAssignment.setId(0);
        if(theAssignment.getPoints() <= 10 & theAssignment.getPoints() >= 1){
        Assignment dbAssignment = assignmentService.save(theAssignment);
        return dbAssignment;
        }else {
            throw new ArithmeticException("Invalid Assignment point - " + theAssignment.getPoints());
        }
    }

    @PutMapping("/assignments/{assignmentId}")
    public Assignment updateAssignment(@RequestBody Assignment theAssignment, @PathVariable int assignmentId){
        Assignment dbAssignment = assignmentService.save(theAssignment);
        return dbAssignment;
    }

    @DeleteMapping("/assignments/{assignmentId}")
    public String deleteAssignment(@PathVariable int assignmentId){
        Assignment tempAssignment = assignmentService.findById(assignmentId);
        if(tempAssignment == null){
            throw new RuntimeException("Assignment id not found - " + assignmentId);
        }
        assignmentService.deleteById(assignmentId);
        return "Deleted assignment id - " + assignmentId;
    }

}
