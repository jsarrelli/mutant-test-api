package com.example.mutanttestapi.services;

import com.example.mutanttestapi.models.DnaEntity;
import com.example.mutanttestapi.models.DnaType;
import com.example.mutanttestapi.repositories.DnaRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DnaEntityServiceTest {

    private DnaRepository dnaRepository = mock(DnaRepository.class);
    private DNAService dnaService = new DNAService(dnaRepository);

    @Test
    void checkHorizontalShouldBeOne() {
        String[] dna = {"ATCGGT", "TCGAGT", "TACCCC", "TGACTC", "TCGGCA", "THCAGC"};
        int numberOfSequences = dnaService.checkHorizontal(dna);
        assertEquals(1, numberOfSequences);
    }

    @Test
    void checkHorizontalShouldBeTwo() {
        String[] dna = {"ATCGGT", "TCGAGT", "TACCCC", "TGACTC", "TCGGGG", "THCAGC"};
        int numberOfSequences = dnaService.checkHorizontal(dna);
        assertEquals(2, numberOfSequences);
    }

    @Test
    void checkVerticalShouldBeOne() {
        String[] dna = {"ATCGGT", "TCGAGG", "TAGCCC", "AGACTC", "TCGGCC", "THCAGC"};
        int numberOfSequences = dnaService.checkVertical(dna);
        assertEquals(1, numberOfSequences);
    }

    @Test
    void checkVerticalShouldBeTwo() {
        String[] dna = {"ATCGGT", "TCGAGG", "TAGCCC", "AGGCTC", "TCGGCC", "THCAGC"};
        int numberOfSequences = dnaService.checkVertical(dna);
        assertEquals(2, numberOfSequences);
    }

    @Test
    void checkDiagonalShouldBeOne() {
        String[] dna = {"ATCGGT", "TCCAGT", "TACCCC", "CTCHCC", "THGCAC", "AATHCC"};
        int numberOfSequences = dnaService.checkDiagonal(dna);
        assertEquals(1, numberOfSequences);
    }

    @Test
    void checkDiagonalShouldBeTwo() {
        String[] dna = {"ATCGGT", "TCCAGT", "TGCCCC", "CTCHCC", "TCGGAC", "CATHGC"};
        int numberOfSequences = dnaService.checkDiagonal(dna);
        assertEquals(2, numberOfSequences);
    }

    @Test
    void checkDiagonalShouldBeThree() {
        String[] dna = {"ATCGGT", "TCCAGT", "TGCCTC", "CTCTCC", "TCTGAC", "CATHGC"};
        int numberOfSequences = dnaService.checkDiagonal(dna);
        assertEquals(3, numberOfSequences);
    }

    @Test
    void checkForMutantDnaShouldBeTrue_1() {
        String[] dna = {"ATCGAC", "TCGTAC", "TCTGAC", "ATACGA", "TAACGA", "TAAAAG"};
        boolean result = dnaService.isMutant(dna);
        assertTrue(result);
    }

    @Test
    void checkForMutantDnaShouldBeTrue_2() {
        String[] dna = {"ATCGAC", "ACGTAC", "ACTGAC", "ATACGA", "TAACGG", "TAAGAG"};
        boolean result = dnaService.isMutant(dna);
        assertTrue(result);
    }


    @Test
    void checkForMutantDnaShouldBeTrue_3() {
        String[] dna = {"ATCGAC", "GGGGAC", "ACTGAC", "AAACAC", "TAACGG", "TAAAAG"};
        boolean result = dnaService.isMutant(dna);
        assertTrue(result);
    }

    @Test
    void checkForMutantDnaShouldBeFalseForNoSequence() {
        String[] dna = {"ATCGAC", "GGAGAT", "ACTGAC", "AAACAC", "TATCGG", "TAAAGG"};
        boolean result = dnaService.isMutant(dna);
        assertFalse(result);
    }

    @Test
    void checkForMutantDnaShouldBeFalseForInvalidLetters() {
        String[] dna = {"LLLLLL", "BBBBBB", "KKKKKK", "WWWWWW", "SSSSSS", "FFFFFF"};
        boolean result = dnaService.isMutant(dna);
        assertFalse(result);
    }

    @Test
    void checkForMutantDnaShouldBeFalseForOnlyOneSequence() {
        String[] dna = {"ATCGCC", "GGAGAT", "ACTGAC", "AAACAC", "TATCGG", "AAAAGG"};
        boolean result = dnaService.isMutant(dna);
        assertFalse(result);
    }

    @Test
    void countByTypeShouldCount() {
        when(dnaRepository.countByType(any())).thenReturn((long) 10);
        assertEquals(10, dnaService.countByType(DnaType.HUMAN));
    }

    @Test
    void insertNewDnaShouldSaveDnaEncrypted() {
        String[] dna = {"d", "n", "a"};
        DnaType type = DnaType.HUMAN;
        String hashSequence = DigestUtils.sha256Hex(Arrays.toString(dna));
        ArgumentCaptor<DnaEntity> argumentCaptor = ArgumentCaptor.forClass(DnaEntity.class);
        dnaService.insertNewDna(dna, type);
        verify(dnaRepository, times(1)).save(argumentCaptor.capture());
        DnaEntity passedEntity = argumentCaptor.getValue();
        assertEquals(hashSequence, passedEntity.getId());
        assertEquals(type, passedEntity.getType());
    }


}
