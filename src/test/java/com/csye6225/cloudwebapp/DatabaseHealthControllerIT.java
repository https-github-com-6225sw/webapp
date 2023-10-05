package com.csye6225.cloudwebapp;

import com.csye6225.cloudwebapp.CloudwebappApplication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CloudwebappApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabaseHealthControllerIT {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testDatabaseHealthCheck() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/healthz"),
                HttpMethod.GET, entity, String.class);

        //String expected = "{\"id\":\"Course1\",\"name\":\"Spring\",\"description\":\"10 Steps\"}";

        HttpStatusCode result = response.getStatusCode();
        //JSONAssert.assertEquals(expected, response.getBody(), false);
        //assertEquals(result.value(), 200);
        assertThat(result.value()).isEqualTo(200);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
