package com.example.mutanttestapi.controllers;

import com.example.mutanttestapi.services.DNAService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;

@Controller
public class DNAController {
    private final DNAService dnaService;

    public DNAController(DNAService dnaService) {
        this.dnaService = dnaService;

    }

    @PostMapping(path = "/mutant")
    public ResponseEntity isMutant(@RequestBody String requestBody) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(requestBody, JsonObject.class).getAsJsonArray("dna");
        Iterator<JsonElement> iterator = jsonArray.iterator();
        int N = jsonArray.size();
        String[] dna = new String[N];
        int i = 0;
        while (iterator.hasNext()) {
            String sequence = iterator.next().getAsString();
            if (sequence.length() != N)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNA is not a NxN matrix");
            dna[i] = sequence;
            i++;
        }
        boolean result = dnaService.isMutant(dna);
        if (result) return ResponseEntity.status(HttpStatus.OK).body("It's a mutant!");
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("It's not a mutant");

    }


}
