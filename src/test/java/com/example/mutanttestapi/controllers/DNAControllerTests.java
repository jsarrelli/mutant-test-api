package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.controllers.requests.DNATestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DNAControllerTests {


    @LocalServerPort
    private int port;

    @Value("${base_url}")
    private String base_url;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Test
    void isMutantShouldReturnForbiddenForANonMutant() {
        String[] dna = {"gktiop", "giturh", "fjguto", "gotplt", "fjgklt", "glkotp"};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        HttpEntity<DNATestRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity response = restTemplate.exchange(
                createURLWithPort("/mutant"), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void isMutantShouldReturnOkForAMutant() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        HttpEntity<DNATestRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity response = restTemplate.exchange(
                createURLWithPort("/mutant"), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void isMutantShouldReturnBadRequestForNonNxNMatrix() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAG", "CCCCTA", "TCACTG"};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        HttpEntity<DNATestRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/mutant"), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return base_url + port + uri;
    }

}
