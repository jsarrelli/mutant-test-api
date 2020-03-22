package com.example.mutanttestapi.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DNAControllerTests {


    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Test
    void isMutantShouldReturnForbiddenForANonMutant() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("gktiop");
        jsonArray.add("giturh");
        jsonArray.add("fjguto");
        jsonArray.add("gotplt");
        jsonArray.add("fjgklt");
        jsonArray.add("glkotp");
        jsonObject.add("dna",jsonArray);
        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/mutant"), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void isMutantShouldReturnOkForAMutant() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("ATGCGA");
        jsonArray.add("CAGTGC");
        jsonArray.add("TTATGT");
        jsonArray.add("AGAAGG");
        jsonArray.add("CCCCTA");
        jsonArray.add("TCACTG");
        jsonObject.add("dna",jsonArray);
        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/mutant"), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void isMutantShouldReturnBadRequestForNonNxNMatrix() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("ATGCGA");
        jsonArray.add("CAGTGC");
        jsonArray.add("TTATGT");
        jsonArray.add("AGAAG");
        jsonArray.add("CCCCTA");
        jsonArray.add("TCACTG");
        jsonObject.add("dna",jsonArray);
        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/mutant"), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
