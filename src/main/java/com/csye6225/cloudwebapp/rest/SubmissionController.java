package com.csye6225.cloudwebapp.rest;

//import com.amazonaws.services.sns.model.ListTopicsRequest;
//import com.amazonaws.services.sns.model.ListTopicsResult;
//import com.amazonaws.services.sns.model.PublishRequest;
//import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.csye6225.cloudwebapp.VO.SubmissionVO;
import com.csye6225.cloudwebapp.config.AmazonSnsClient;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.entity.Message;
import com.csye6225.cloudwebapp.entity.Submission;
import com.csye6225.cloudwebapp.service.AssignmentService;
import com.csye6225.cloudwebapp.service.MessaePublisher;
import com.csye6225.cloudwebapp.service.SubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timgroup.statsd.StatsDClient;

import org.aspectj.apache.bcel.classfile.Unknown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.json.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v2")
public class SubmissionController {

    @Value("${sns.topic.arn}")
    private String topicArn;

    @Autowired
    private MessaePublisher messaePublisher;

    @Autowired
    AmazonSnsClient amazonSnsClient;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private StatsDClient statsd;

    @Autowired
    private SubmissionController(SubmissionService theSubmissionService){
        submissionService = theSubmissionService;
    }

    Logger logger = LoggerFactory.getLogger(SubmissionController.class);

    @PostMapping("/assignments/{id}/submission")
    public ResponseEntity<Object> submitAssignment(@PathVariable String id, @RequestBody(required = false) String url, Authentication authentication) throws JsonProcessingException {
        statsd.incrementCounter("submitassignment");

        if(url == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        JSONObject json = new JSONObject(url);
        String theURL = json.getString("submission_url");

        if (theURL.trim().length() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //this assignment must exist
        Assignment preAssignment = assignmentService.findById(id);

        if (LocalDateTime.now().isAfter(preAssignment.getDeadline())){
            logger.error("Cannot submitted ---- " +"assignment " + id + " " + "exceeds due day");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(preAssignment == null){
            logger.error("Cannot submit ---- " + "cannot submit because the assignment does not exist");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

//        if(!havePermission(authentication.getName(), id)){
//            logger.info("Cannot submit ---- the user does not have permission to submit this assignment");
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        //determine if this assignment have been submitted
        List<Submission> preSubmission = submissionService.findByAssignmentId(id);
        //logger.info(preSubmission.toString());

        //if it is the first submission
        if(preSubmission.size() == 0){
            Submission newSubmission = new Submission();
            newSubmission.setAssignment_id(id);
            newSubmission.setSubmission_url(theURL);
            newSubmission.setSubmission_date(LocalDateTime.now());
            newSubmission.setAssignment_updated(LocalDateTime.now());
//            newSubmission.setNum_of_attempts("1");
            preAssignment.setAttemptsUsed("1");
            assignmentService.save(preAssignment);
            submissionService.save(newSubmission);

            SubmissionVO submissionVO = new SubmissionVO();
            BeanUtils.copyProperties(newSubmission, submissionVO);
            logger.info("Assignment submitted ---- " +"assignment " + id + " " + "submitted");

            //publish message to topic
            Message message = new Message(authentication.getName(), id, preAssignment.getName(), newSubmission.getId(), newSubmission.getSubmission_url(), String.valueOf(newSubmission.getSubmission_date()), String.valueOf(newSubmission.getAssignment_updated()),
                    preAssignment.getAttemptsUsed());
            String jsonMessage = new ObjectMapper().writeValueAsString(message);

            publishTopic(jsonMessage, topicArn);

            return new ResponseEntity<>(submissionVO, HttpStatus.CREATED);
        }

        //if the assignment have been submitted
        if(preSubmission.size() != 0){

            //exceeds number of attempts, reject request
            Submission thePreSubmission = preSubmission.get(0);
            if(preAssignment.getAttemptsUsed().equals(preAssignment.getNumOfAttempts())){
                logger.error("Cannot submitted ---- " +"assignment " + id + " " + "exceeds number of attempts");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //exceeds due date, reject request
            else if (LocalDateTime.now().isAfter(preAssignment.getDeadline())){
                logger.error("Cannot submitted ---- " +"assignment " + id + " " + "exceeds due day");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //number of attempts still remains & not pass due date
            else {
                Integer newAttempts = Integer.valueOf(preAssignment.getAttemptsUsed()) + 1;
                Submission reSubmission = new Submission();

//                reSubmission.setId(UUID.randomUUID().toString());
                reSubmission.setSubmission_url(theURL);
                reSubmission.setAssignment_id(thePreSubmission.getAssignment_id());
                reSubmission.setSubmission_date(LocalDateTime.now());
                preAssignment.setAttemptsUsed(String.valueOf(newAttempts));
                reSubmission.setAssignment_updated(LocalDateTime.now());
                assignmentService.save(preAssignment);
                submissionService.save(reSubmission);

                SubmissionVO submissionVO = new SubmissionVO();
                BeanUtils.copyProperties(reSubmission, submissionVO);
                logger.info("Assignment submitted again  ---- " +"assignment " + id + " " + "submitted");

                //publish message to topic
                Message message = new Message(authentication.getName(), id,  preAssignment.getName(),reSubmission.getId(), reSubmission.getSubmission_url(),
                        String.valueOf(reSubmission.getSubmission_date()), String.valueOf(reSubmission.getAssignment_updated()), preAssignment.getAttemptsUsed());
                String jsonMessage = new ObjectMapper().writeValueAsString(message);

                publishTopic(jsonMessage, topicArn);

                return new ResponseEntity<>(submissionVO,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    public boolean havePermission(String username, String assignID){
        //find creatername by assignID
        Assignment assignment = assignmentService.findById(assignID);
        if(username.equals(assignment.getUser())){
            return true;
        }
        return false;
    }

    public void publishTopic(String message, String topicArn) {

        try {
            PublishRequest request = new PublishRequest().withMessage(message).withTopicArn(topicArn);

            PublishResult result = amazonSnsClient.getClient().publish(request);
            logger.info(
                    result.getMessageId()
                            + " Message sent. Status is "
                            + result.getSdkHttpMetadata().getHttpStatusCode());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


}
