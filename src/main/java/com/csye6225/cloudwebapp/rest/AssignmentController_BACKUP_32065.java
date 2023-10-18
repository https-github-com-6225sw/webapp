package com.csye6225.cloudwebapp.rest;

import com.csye6225.cloudwebapp.dao.AssignmentDao;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
=======
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.WebDataBinder;
>>>>>>> ed95c54f62b6122287bb8ea92f8af3ec154e77d8
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class AssignmentController {

    private AssignmentService assignmentService;
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
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
    public Assignment addAssignment(@Valid @RequestBody Assignment theAssignment) throws Exception {
//        theAssignment.setId(0);
        if(theAssignment.getPoints() <= 10 & theAssignment.getPoints() >= 1){
        Assignment dbAssignment = assignmentService.save(theAssignment);
        return dbAssignment;
        }else {
            throw new ArithmeticException("Invalid Assignment point - " + theAssignment.getPoints());
        }
    }

    @PutMapping("/assignments/{assignmentId}")
    public Assignment updateAssignment(@Valid @RequestBody Assignment theAssignment, @PathVariable int assignmentId){
        Assignment dbAssignment = assignmentService.save(theAssignment);
        return dbAssignment;
    }

    @PatchMapping("/assignments/{assignmentId}")
    public ResponseEntity<String> partialUpdate(@PathVariable int assignmentId){
        try {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }}
    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable int assignmentId){
        Assignment tempAssignment = assignmentService.findById(assignmentId);
        if(tempAssignment == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        assignmentService.deleteById(assignmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
<<<<<<< HEAD
=======

>>>>>>> ed95c54f62b6122287bb8ea92f8af3ec154e77d8
    }

}
