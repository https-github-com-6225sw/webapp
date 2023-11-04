package com.csye6225.cloudwebapp.healthchecks;

import com.csye6225.cloudwebapp.rest.AssignmentController;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class Databaseheath {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private StatsDClient statsd;

    Logger logger = LoggerFactory.getLogger(Databaseheath.class);

    @GetMapping(value="/healthz")
    public ResponseEntity<String> databaseHealth() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-cache, no-store, must-revalidate");
        responseHeaders.set("Pragma", "no-cache");
        responseHeaders.set("X-Content-Type-Options", "nosniff");
        statsd.incrementCounter("databasehz");
            try {
                Connection conn = dataSource.getConnection();
                logger.info("database connected");
                return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
            } catch (SQLException ex) {
                logger.error("database not connected");
                return new ResponseEntity<>(responseHeaders, HttpStatus.SERVICE_UNAVAILABLE);
            }
    }
}
