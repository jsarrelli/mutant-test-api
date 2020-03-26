package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.exceptions.BadDnaBaseException;
import com.example.mutanttestapi.exceptions.DnaNotAMatrixException;
import com.example.mutanttestapi.models.DnaType;
import com.example.mutanttestapi.requests.DNATestRequest;
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
        BigDecimal ratio = numberOfMutants.divide(numberOfHumans, 1, RoundingMode.CEILING);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count_mutant_dna", numberOfMutants.toBigInteger());
        jsonObject.addProperty("count_human_dna", numberOfHumans.toBigInteger());
        jsonObject.addProperty("ratio", ratio);
        return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
    }


    private void performDnaValidations(String[] dna) {
        int N = dna.length;
        String acceptanceRegexSequence = "^[TCGA]+$";
        for (String sequence : dna) {
            if (sequence.length() != N) throw new DnaNotAMatrixException();
            if (!sequence.matches(acceptanceRegexSequence)) throw new BadDnaBaseException();
        }
    }
}
