package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.requests.DNATestRequest;
import com.example.mutanttestapi.models.DnaType;
import com.example.mutanttestapi.services.DNAService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DnaControllerTest {


    private DNAService dnaService = mock(DNAService.class);
    private DNAController dnaController = spy(new DNAController(dnaService));

    @Test
    void isMutantShouldThrowExceptionForANonNxNMatrix() {
        String[] dna = {"ATCGGT", "TCGAGT", "TAG", "TGACTC", "TCGGCA", "THCAGC"};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        assertThrows(ResponseStatusException.class, () -> {
            dnaController.isMutant(requestBody);
        });

    }

    @Test
    void isMutantShouldThrowExceptionForANonValidBase() {
        String[] dna = {"ATCGGT", "PPPPPP", "SSSSSS", "TGACTC", "TCGGCA", "THCAGC"};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        assertThrows(ResponseStatusException.class, () -> {
            dnaController.isMutant(requestBody);
        });
    }

    @Test
    void isMutantShouldReturnOkForAMutantAndSaveDNA() {
        String[] dna = {};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        when(dnaService.isMutant(any(String[].class))).thenReturn(true);
        ResponseEntity result = dnaController.isMutant(requestBody);
        verify(dnaService, Mockito.times(1)).insertNewDna(any(), any());
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    void isMutantShouldReturnForbiddenForANonMutantAndSaveDNA() {
        String[] dna = {};
        DNATestRequest requestBody = new DNATestRequest();
        requestBody.setDna(dna);
        when(dnaService.isMutant(any(String[].class))).thenReturn(false);
        ResponseEntity result = dnaController.isMutant(requestBody);
        verify(dnaService, Mockito.times(1)).insertNewDna(any(), any());
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }


    @Test
    void calculateStatsShouldReturnMutantCountHumanCountAndRatio() {
        Gson gson = new Gson();
        when(dnaService.countByType(DnaType.MUTANT)).thenReturn((long) 40);
        when(dnaService.countByType(DnaType.HUMAN)).thenReturn((long) 100);
        ResponseEntity result = dnaController.calculateStats();
        JsonObject body = gson.fromJson((String) result.getBody(), JsonObject.class);
        assert body != null;
        assertEquals("40", body.get("count_mutant_dna").getAsString());
        assertEquals("100", body.get("count_human_dna").getAsString());
        assertEquals("0.4", body.get("ratio").getAsString());
    }

}
