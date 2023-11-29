package com.csye6225.cloudwebapp.service;

import software.amazon.awssdk.services.sns.SnsClient;

public interface MessaePublisher {
    
    void pubTopic(SnsClient snsClient, String message, String topicArn);
}
