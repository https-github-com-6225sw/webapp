package com.csye6225.cloudwebapp.rest;

import com.csye6225.cloudwebapp.VO.AssignmentVO;
import com.csye6225.cloudwebapp.dao.AssignmentDao;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<Object> getAssignment(@PathVariable String assignmentId){
        Assignment theAssignment = assignmentService.findById(assignmentId);
        AssignmentVO assignmentVO = new AssignmentVO();
        BeanUtils.copyProperties(theAssignment, assignmentVO);
        if(theAssignment == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assignmentVO, HttpStatus.OK);
    }

    @PostMapping("/assignments")
    public ResponseEntity<Object> addAssignment(@RequestBody Assignment theAssignment){
//        theAssignment.setId(0);
        theAssignment.setAssignmentCreated(LocalDateTime.now());
        theAssignment.setAssignmentUpdated(LocalDateTime.now());
        if(theAssignment.getId() != null | theAssignment.getDeadline() == null | theAssignment.getName().trim().length() == 0
        | String.valueOf(theAssignment.getPoints()) == null | String.valueOf(theAssignment.getNumOfAttemps()) == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(theAssignment.getPoints() <= 10 & theAssignment.getPoints() >= 1 ){
        Assignment dbAssignment = assignmentService.save(theAssignment);
        AssignmentVO assignmentVO = new AssignmentVO();
        BeanUtils.copyProperties(theAssignment, assignmentVO);
        return new ResponseEntity<>(assignmentVO, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/assignments/{assignmentId}")
    public ResponseEntity<Object> updateAssignment(@RequestBody Assignment theAssignment, @PathVariable String assignmentId){

        Assignment dbAssignment = assignmentService.save(theAssignment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/assignments/{assignmentId}")
    public ResponseEntity<String> partialUpdate(@PathVariable String assignmentId){
        try {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }}
    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable String assignmentId){
        Assignment tempAssignment = assignmentService.findById(assignmentId);
        if(tempAssignment == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        assignmentService.deleteById(assignmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
