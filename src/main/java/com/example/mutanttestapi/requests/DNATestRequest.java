package com.example.mutanttestapi.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class DNATestRequest {
    @Schema(description = "DNA sequence.",
            example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCTTA\",\"TCACTG\"]", required = true)
    private String[] dna;

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }
}
