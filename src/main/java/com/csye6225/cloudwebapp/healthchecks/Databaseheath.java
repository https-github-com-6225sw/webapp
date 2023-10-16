package com.csye6225.cloudwebapp.healthchecks;

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

    @GetMapping(value="/healthz")
    public ResponseEntity<String> databaseHealth() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-cache, no-store, must-revalidate");
        responseHeaders.set("Pragma", "no-cache");
        responseHeaders.set("X-Content-Type-Options", "nosniff");
            try {
                Connection conn = dataSource.getConnection();
                return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
            } catch (SQLException ex) {
                return new ResponseEntity<>(responseHeaders, HttpStatus.SERVICE_UNAVAILABLE);
            }
    }
}
