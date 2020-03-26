package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.exceptions.BadDnaBaseException;
import com.example.mutanttestapi.exceptions.DnaNotAMatrixException;
import com.example.mutanttestapi.models.DnaType;
import com.example.mutanttestapi.requests.DNATestRequest;
import com.example.mutanttestapi.services.DNAService;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Tag(name = "Mutant Test", description = "The mutant testing API")
@RestController
public class DNAController {
    private final DNAService dnaService;
    private Logger logger = LoggerFactory.getLogger(DNAController.class);

    public DNAController(DNAService dnaService) {
        this.dnaService = dnaService;

    }

    @Operation(summary = "Mutant Test", description = "Retrieves whether some DNA is mutant or not")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {@ExampleObject("It's a mutant")}, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", content = @Content(examples = {@ExampleObject("It's not a mutant")}, schema = @Schema(implementation = String.class))),
    })
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

    @Operation(summary = "Stats", description = "Retrieves the ratio between humans and mutants ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "According information")})
    @GetMapping(path = "/stats")
    public ResponseEntity calculateStats() {
        logger.info("Stats calculation request received.");

        BigDecimal numberOfMutants = new BigDecimal(dnaService.countByType(DnaType.MUTANT));
        BigDecimal numberOfHumans = new BigDecimal(dnaService.countByType(DnaType.HUMAN));
        BigDecimal ratio;
        try {
            ratio = numberOfMutants.divide(numberOfHumans, 1, RoundingMode.CEILING);
        } catch (Exception e) {
            // this is only for the case when we have an empty database or no humans, division by 0 throws exception
            ratio = new BigDecimal(-1);
        }

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
