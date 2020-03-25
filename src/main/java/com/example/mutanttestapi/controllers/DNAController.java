package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.controllers.requests.DNATestRequest;
import com.example.mutanttestapi.models.DnaType;
import com.example.mutanttestapi.services.DNAService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Controller
public class DNAController {
    private final DNAService dnaService;
    private Logger logger = LoggerFactory.getLogger(DNAController.class);

    public DNAController(DNAService dnaService) {
        this.dnaService = dnaService;

    }

    @PostMapping(path = "/mutant")
    public ResponseEntity isMutant(@RequestBody DNATestRequest requestBody) {
        String[] dna = requestBody.getDna();
        logger.info("Mutant testing request received: " + Arrays.toString(dna));

        performDnaValidations(dna);
        boolean result = dnaService.isMutant(dna);
        DnaType type = result ? DnaType.MUTANT : DnaType.HUMAN;
        logger.info("Mutant test result:" + result + " for " + Arrays.toString(dna));

        dnaService.insertNewDna(dna, type);

        if (result) return ResponseEntity.status(HttpStatus.OK).body("It's a mutant!");
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("It's not a mutant");

    }

    @GetMapping(path = "/stats")
    public ResponseEntity calculateStats() {
        logger.info("Stats calculation request received.");

        BigDecimal numberOfMutants = new BigDecimal(dnaService.countByType(DnaType.MUTANT));
        BigDecimal numberOfHumans = new BigDecimal(dnaService.countByType(DnaType.HUMAN));
        BigDecimal ratio = numberOfMutants.divide(numberOfHumans, 3, RoundingMode.CEILING);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count_mutant_dna", numberOfMutants.toBigInteger());
        jsonObject.addProperty("count_human_dna", numberOfHumans.toBigInteger());
        jsonObject.addProperty("ratio", ratio);
        return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
    }


    private void performDnaValidations(String[] dna) {
        int N = dna.length;
        for (String sequence : dna) {
            if (sequence.length() != N)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNA is not a NxN matrix");

            if (!sequence.matches("^[TCGA]+$"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sequence should contain only [T,C,A,G] letters");
        }
    }
}
