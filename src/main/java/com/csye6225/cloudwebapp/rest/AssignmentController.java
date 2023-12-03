package com.csye6225.cloudwebapp.rest;

import com.csye6225.cloudwebapp.VO.AssignmentVO;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.service.AssignmentService;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/v3")
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
        logger.info("Get All assignment ----" + "get all assignment");
        return assignmentService.findAll();
    }

    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<Object> getAssignment(@PathVariable String assignmentId, @RequestBody(required = false) String requestBody){
        statsd.incrementCounter("getassignment");
        if (requestBody != null){
            logger.error("Cannot get ---- the get request cannot have a request body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Assignment theAssignment = assignmentService.findById(assignmentId);
        if(theAssignment == null){
            logger.error("Cannot get ---- " +"assignment id not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AssignmentVO assignmentVO = new AssignmentVO();
        BeanUtils.copyProperties(theAssignment, assignmentVO);
        logger.info("Get assignment ---- " + "get assignment by id");
        return new ResponseEntity<>(assignmentVO, HttpStatus.OK);
    }

    @PostMapping("/assignments")
    public ResponseEntity<Object> addAssignment(@RequestBody Assignment theAssignment, Authentication authentication){
        //metrics
        statsd.incrementCounter("createassignment");
        //assignment points and number of attemps cannot be double
        if(this.isInt(theAssignment.getPoints()) == false | this.isInt(theAssignment.getNumOfAttempts()) == false){
            logger.error("Cannot create ---- " + "input point or number of attempts can not be validated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        theAssignment.setAssignmentCreated(LocalDateTime.now());
        theAssignment.setAssignmentUpdated(LocalDateTime.now());
       theAssignment.setAttemptsUsed("0");

        //id must be null, ddl, name, points, numofattempts must be null
        if(theAssignment.getId() != null | theAssignment.getDeadline() == null | theAssignment.getName().isEmpty()|theAssignment.getName().length() == 0
        | theAssignment.getPoints() == null | theAssignment.getNumOfAttempts() == null |
                theAssignment.getUser() != null){
            logger.error("Cannot create ---- " + "input Assignment body can not be validated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //create
        else if(Integer.valueOf(theAssignment.getPoints()) <= 100 & Integer.valueOf(theAssignment.getPoints()) >= 1
        & Integer.valueOf(theAssignment.getPoints()) <= 100 & Integer.valueOf(theAssignment.getPoints()) >=1){
            String username = authentication.getName();
            theAssignment.setUser(username);
            Assignment dbAssignment = assignmentService.save(theAssignment);
            AssignmentVO assignmentVO = new AssignmentVO();
            BeanUtils.copyProperties(theAssignment, assignmentVO);
            logger.info("Assignment created ---- " +"assignment " + dbAssignment.getId() + " " + "created");
            return new ResponseEntity<>(assignmentVO, HttpStatus.CREATED);
        }
        logger.error("Cannot create ---- " +"assignment points must between 1 and 100");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/assignments/{assignmentId}")
    public ResponseEntity<Object> updateAssignment(@RequestBody Assignment theAssignment, @PathVariable String assignmentId, Authentication authentication){
        statsd.incrementCounter("updateassignment");
        if(isInt(theAssignment.getPoints()) == false | isInt(theAssignment.getNumOfAttempts()) == false){
            logger.error("Cannot create ---- " + "input point or number of attempts can not be validated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(theAssignment.getId() != null | theAssignment.getDeadline() == null | theAssignment.getName() == null
                | theAssignment.getPoints() == null | theAssignment.getNumOfAttempts() == null |
                theAssignment.getUser() != null | Integer.valueOf(theAssignment.getPoints()) > 10 |Integer.valueOf(theAssignment.getPoints()) < 0){
            logger.error("Cannot create ---- " + "input Assignment body can not be validated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(!havePermission(authentication.getName(), assignmentId)){
            logger.error("Cannot create ---- " + "the user does not have permission to update this assignment");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Assignment preAssignment = assignmentService.findById(assignmentId);
        if(preAssignment == null){
            logger.error("Cannot create ---- " + "cannot update because the assignment does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        theAssignment.setId(preAssignment.getId());
        preAssignment.setPoints(theAssignment.getPoints());
        preAssignment.setName(theAssignment.getName());
        preAssignment.setNumOfAttempts(theAssignment.getNumOfAttempts());

        preAssignment.setDeadline(theAssignment.getDeadline());
        preAssignment.setAssignmentUpdated(LocalDateTime.now());
        assignmentService.save(preAssignment);
        logger.info("Assignment updated ---- " +"assignment " + assignmentId + " " + "updated");
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
    public ResponseEntity<String> deleteAssignment(@PathVariable String assignmentId, Authentication authentication,
                                                   @RequestBody(required = false) String requestBody){
        statsd.incrementCounter("deleteassignment");
        if (requestBody != null){
            logger.error("Cannot delete ---- the delete request cannot have a request body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Assignment tempAssignment = assignmentService.findById(assignmentId);
        if(tempAssignment == null){
            logger.error("Cannot delete ---- the assignment does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!havePermission(authentication.getName(), assignmentId)){
            logger.error("Cannot delete ---- the user does not have permission to update this assignment");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        assignmentService.deleteById(assignmentId);
        logger.info("Assignment delete ---- "  + "assignment " + assignmentId + " " + "delete");
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

    public boolean isInt(String str){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }




}
