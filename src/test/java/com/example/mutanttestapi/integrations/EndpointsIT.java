package com.example.mutanttestapi.integrations;

import com.example.mutanttestapi.ApplicationProperties;
import com.example.mutanttestapi.controllers.requests.DNATestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EndpointsIT {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = ApplicationProperties.INSTANCE.getProperty("base_url");

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
        return baseUrl.concat(uri);
    }
}
