package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.controllers.requests.DNATestRequest;
import com.example.mutanttestapi.services.DNAService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class DNAController {
    private final DNAService dnaService;

    public DNAController(DNAService dnaService) {
        this.dnaService = dnaService;

    }

    @PostMapping(path = "/mutant")
    public ResponseEntity isMutant(@RequestBody DNATestRequest requestBody) {
        String[] dna = requestBody.getDna();
        int N = dna.length;
        for (String sequence : dna) {
            if (sequence.length() != N)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNA is not a NxN matrix");
        }
        boolean result = dnaService.isMutant(dna);
        if (result) return ResponseEntity.status(HttpStatus.OK).body("It's a mutant!");
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("It's not a mutant");

    }


}
