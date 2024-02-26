package com.neolore.card.integration;

import com.neolore.card.data.Book;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookIntegrationTest {
    @Value("${server.port}")
    private int serverPort;
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("ZilchTxn is saved")
    @Order(1)
    void testCreateTxn_WhenValidDetailsProvided_returnsZilchTxn() throws JSONException {
        JSONObject zilchTxnRequestJson = new JSONObject();
        zilchTxnRequestJson.put("note", "tfl");
        zilchTxnRequestJson.put("price", "8.98");
        zilchTxnRequestJson.put("customerId", "14");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("X-API-KEY", "secret123");

        HttpEntity<String> request = new HttpEntity<>(zilchTxnRequestJson.toString(), headers);

        ResponseEntity<Book> createdZilchTxnEntity = testRestTemplate.postForEntity("/api/save",
                request,
                Book.class);
        Book zilchTxnResponse = createdZilchTxnEntity.getBody();
        Assertions.assertEquals(HttpStatus.OK, createdZilchTxnEntity.getStatusCode());
        assertAll(
                () -> assertEquals(zilchTxnRequestJson.get("note"), zilchTxnResponse.getNote()),
                () -> assertEquals(zilchTxnRequestJson.get("price"), zilchTxnResponse.getPrice().toString()),
                () -> assertEquals(zilchTxnRequestJson.get("customerId"), zilchTxnResponse.getCustomerId().toString()),
                () -> assertNotNull(zilchTxnResponse.getId())
        );
    }

    @Test
    @DisplayName("ZilchTxn without API key throws exception")
    @Order(2)
    void testCreateTxn_WhenAPIKeyIsMissing_returns401() throws JSONException {
        JSONObject zilchTxnRequestJson = new JSONObject();
        zilchTxnRequestJson.put("note", "tfl");
        zilchTxnRequestJson.put("price", "8.98");
        zilchTxnRequestJson.put("customerId", "14");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(zilchTxnRequestJson.toString(), headers);

        assertThrows(ResourceAccessException.class, () -> testRestTemplate.postForEntity("/api/save",
                request,
                Book.class));
    }

    @Test
    @DisplayName("ZilchTxn transactions are retrieved")
    @Order(3)
     void testFindAll_returnsAllTxns() throws JSONException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("X-API-KEY", "secret123");
        HttpEntity<String> request = new HttpEntity(headers);

        ResponseEntity<List<Book>> response = testRestTemplate.exchange("/api/list",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Book>>() {
                });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "HTTP Status code should be 200");
        Assertions.assertTrue(response.getBody().size() == 1,
                "There should be exactly 1 user in the list");
    }
}
