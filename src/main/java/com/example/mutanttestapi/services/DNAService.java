package com.example.mutanttestapi.services;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DNAService {
    private Pattern pattern = Pattern.compile("([a-z])\\1\\1\\1", Pattern.CASE_INSENSITIVE);

    /**
     * For a given dna, returns whether it is mutant or not according to a certain pattern
     *
     * @param dna
     * @return
     */
   public boolean isMutant(String[] dna) {
        int numberOfSequences = 0;
        numberOfSequences += checkHorizontal(dna);
        numberOfSequences += checkVertical(dna);
        numberOfSequences += checkDiagonal(dna);
        return numberOfSequences > 1;
    }

    /**
     * Return number of horizontal mutant sequences found in a particular DNA
     *
     * @param dna dna solicited
     * @return number of sequences found
     */
    int checkHorizontal(String[] dna) {
        int numberOfSequences = 0;
        for (String row : dna) {
            Matcher matcher = pattern.matcher(row);
            if (matcher.find()) numberOfSequences++;
        }
        return numberOfSequences;
    }

    /**
     * Return number of vertical mutant sequences found in a particular DNA
     *
     * @param dna dna solicited
     * @return number of sequences found
     */
    int checkVertical(String[] dna) {
        int numberOfSequences = 0;
        int N = dna.length;
        for (int i = 0; i < N; i++) {
            StringBuilder column = new StringBuilder();
            for (int j = 0; j < N; j++) column.append(dna[j].toCharArray()[i]);
            if (pattern.matcher(column.toString()).find()) numberOfSequences++;
        }
        return numberOfSequences;
    }

    /**
     * Return number of diagonal (in both orientations) mutant sequences found in a particular DNA
     *
     * @param dna dna solicited
     * @return number of sequences found
     */
    int checkDiagonal(String[] dna) {
        int numberOfSequences = 0;
        int N = dna.length;

        //left to right until the middle
        for (int i = 0; i < N; i++) {
            // "/" orientation
            StringBuilder diagonal1 = new StringBuilder();
            for (int j = i, k = 0; j >= 0 && k < N; j--, k++) diagonal1.append(dna[j].toCharArray()[k]);
            if (pattern.matcher(diagonal1.toString()).find()) numberOfSequences++;

            // "\" orientation
            StringBuilder diagonal2 = new StringBuilder();
            for (int j = i, k = N - 1; j >= 0 && k >= 0; j--, k--) diagonal2.append(dna[j].toCharArray()[k]);
            if (pattern.matcher(diagonal2.toString()).find()) numberOfSequences++;
        }

        //right to left until 1-middle to avoid count twice the same sequence
        for (int i = N - 1; i >= 1; i--) {
            // "/" orientation
            StringBuilder diagonal1 = new StringBuilder();
            for (int j = i, k = N - 1; j < N && k >= 0; j++, k--) diagonal1.append(dna[j].toCharArray()[k]);
            if (pattern.matcher(diagonal1.toString()).find()) numberOfSequences++;

            // "\" orientation
            StringBuilder diagonal2 = new StringBuilder();
            //left to right "\" until 1-middle to avoid count twice the same sequence
            for (int j = i, k = 0; j < N && k < N; j++, k++) diagonal2.append(dna[j].toCharArray()[k]);
            if (pattern.matcher(diagonal2.toString()).find()) numberOfSequences++;
        }

        return numberOfSequences;
    }

}
