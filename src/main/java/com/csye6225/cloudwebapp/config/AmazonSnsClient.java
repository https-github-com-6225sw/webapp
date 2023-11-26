package com.csye6225.cloudwebapp.config;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmazonSnsClient {

//    @Value("${aws.accessKey}")
//    private String accessKey;
//    @Value("${aws.secretKey}")
//    private String secretKey;

    private AmazonSNS client;

    @PostConstruct
    private void initializeAmazonSnsClient() {
        this.client =
                AmazonSNSClientBuilder.standard()
//                       .withCredentials(getAwsCredentialProvider())
                        //.withCredentials(new InstanceProfileCredentialsProvider(false))
                        .withRegion(Region.getRegion(Regions.US_EAST_1).getName())
                        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                        .build();
    }

//    private AWSCredentialsProvider getAwsCredentialProvider() {
//        AWSCredentials awsCredentials =
//                new BasicAWSCredentials(accessKey, secretKey);
//        return new AWSStaticCredentialsProvider(awsCredentials);
//    }

    public AmazonSNS getClient() {
        return client;
    }
}

