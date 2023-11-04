package com.csye6225.cloudwebapp.rest;

import com.csye6225.cloudwebapp.VO.AssignmentVO;
import com.csye6225.cloudwebapp.dao.AssignmentDao;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.service.AssignmentService;
import com.timgroup.statsd.StatsDClient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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
    @Autowired
    private StatsDClient statsd;
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @Autowired
    private AssignmentController(AssignmentService theAssignmentService){
        assignmentService = theAssignmentService;
    }

    Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @GetMapping("/assignments")
    public List<Assignment> findAll(){
        statsd.incrementCounter("getallassignments");
        logger.info("get all assignment");
        return assignmentService.findAll();
    }

    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<Object> getAssignment(@PathVariable String assignmentId){
        statsd.incrementCounter("getassignment");
        logger.info("get assignment by id");
        Assignment theAssignment = assignmentService.findById(assignmentId);
        if(theAssignment == null){
            logger.error("assignment id not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AssignmentVO assignmentVO = new AssignmentVO();
        BeanUtils.copyProperties(theAssignment, assignmentVO);
        return new ResponseEntity<>(assignmentVO, HttpStatus.OK);
    }

    @PostMapping("/assignments")
    public ResponseEntity<Object> addAssignment(@RequestBody Assignment theAssignment, Authentication authentication){
        statsd.incrementCounter("createassignment");
        theAssignment.setAssignmentCreated(LocalDateTime.now());
        theAssignment.setAssignmentUpdated(LocalDateTime.now());
        if(theAssignment.getId() != null | theAssignment.getDeadline() == null | theAssignment.getName().isEmpty()|theAssignment.getName().length() == 0
        | String.valueOf(theAssignment.getPoints()) == null | String.valueOf(theAssignment.getNumOfAttemps()) == null |
                theAssignment.getUser() != null){
            logger.error("input Assignment can not be validated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(theAssignment.getPoints() <= 10 & theAssignment.getPoints() >= 1 ){
            String username = authentication.getName();
            theAssignment.setUser(username);
            Assignment dbAssignment = assignmentService.save(theAssignment);
            AssignmentVO assignmentVO = new AssignmentVO();
            BeanUtils.copyProperties(theAssignment, assignmentVO);
            logger.info("assignment " + dbAssignment.getId() + " " + "created");
            return new ResponseEntity<>(assignmentVO, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/assignments/{assignmentId}")
    public ResponseEntity<Object> updateAssignment(@RequestBody Assignment theAssignment, @PathVariable String assignmentId, Authentication authentication){
        statsd.incrementCounter("updateassignment");
        if(theAssignment.getId() != null | theAssignment.getDeadline() == null | theAssignment.getName() == null
                | String.valueOf(theAssignment.getPoints()) == null | String.valueOf(theAssignment.getNumOfAttemps()) == null |
                theAssignment.getUser() != null | theAssignment.getPoints() > 10 |theAssignment.getPoints() < 0){
            logger.error("input Assignment can not be validated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(!havePermission(authentication.getName(), assignmentId)){
            logger.error("the user does not have permission to update this assignment");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Assignment preAssignment = assignmentService.findById(assignmentId);
        if(preAssignment == null){
            logger.error("cannot update because the assignment does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        theAssignment.setId(preAssignment.getId());
        preAssignment.setPoints(theAssignment.getPoints());
        preAssignment.setName(theAssignment.getName());
        preAssignment.setNumOfAttemps(theAssignment.getNumOfAttemps());

        preAssignment.setDeadline(theAssignment.getDeadline());
        preAssignment.setAssignmentUpdated(LocalDateTime.now());
        assignmentService.save(preAssignment);
        logger.info("assignment " + assignmentId + " " + "updated");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/assignments/{assignmentId}")
    public ResponseEntity<String> partialUpdate(@PathVariable String assignmentId){
        try {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }catch (BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }}
    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable String assignmentId, Authentication authentication){
        statsd.incrementCounter("deleteassignment");
        Assignment tempAssignment = assignmentService.findById(assignmentId);
        if(tempAssignment == null){
            logger.error("cannot delete because the assignment does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!havePermission(authentication.getName(), assignmentId)){
            logger.error("the user does not have permission to update this assignment");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        assignmentService.deleteById(assignmentId);
        logger.info("assignment " + assignmentId + " " + "delete");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    public boolean havePermission(String username, String assignID){
        //find creatername by assignID
        Assignment assignment = assignmentService.findById(assignID);
        if(username.equals(assignment.getUser())){
            return true;
        }
        return false;
    }

}
