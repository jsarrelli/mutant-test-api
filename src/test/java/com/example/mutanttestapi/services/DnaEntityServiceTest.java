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
        String[] dna = {"aabbee", "cccfdd", "dsfdfs", "laaaal", "peprit", "ytsrht"};
        int numberOfSequences = dnaService.checkHorizontal(dna);
        assertEquals(1, numberOfSequences);
    }

    @Test
    void checkHorizontalShouldBeTwo() {
        String[] dna = {"aaeeee", "cccfdd", "dsfdfs", "laaaal", "peprit", "ytsrht"};
        int numberOfSequences = dnaService.checkHorizontal(dna);
        assertEquals(2, numberOfSequences);
    }

    @Test
    void checkVerticalShouldBeOne() {
        String[] dna = {"aslgfk", "cccgdd", "dsfgfs", "dlogkj", "pepgit", "ytsght"};
        int numberOfSequences = dnaService.checkVertical(dna);
        assertEquals(1, numberOfSequences);
    }

    @Test
    void checkVerticalShouldBeTwo() {
        String[] dna = {"aslgfk", "cccgdk", "dsfgfk", "dlogkk", "pepgik", "ytsghk"};
        int numberOfSequences = dnaService.checkVertical(dna);
        assertEquals(2, numberOfSequences);
    }

    @Test
    void checkDiagonalShouldBeOne() {
        String[] dna = {"ABBCKH", "AABCAH", "SAASSF", "AFSAFS", "AFSAFS", "AFSAFS"};
        int numberOfSequences = dnaService.checkDiagonal(dna);
        assertEquals(1, numberOfSequences);
    }

    @Test
    void checkDiagonalShouldBeTwo() {
        String[] dna = {"FLFKRN", "ORIUTJ", "SBMBLK", "DSBSDE", "CBSBWE", "DSFSBS"};
        int numberOfSequences = dnaService.checkDiagonal(dna);
        assertEquals(2, numberOfSequences);
    }

    @Test
    void checkDiagonalShouldBeThree() {
        String[] dna = {"LKIOPG", "KGKGUI", "LJGKKT", "LGKGKT", "GLKTGF", "FKGITG"};
        int numberOfSequences = dnaService.checkDiagonal(dna);
        assertEquals(3, numberOfSequences);
    }

    @Test
    void checkForMutantDnaShouldBeTrue_1() {
        String[] dna = {"LKGGGG", "KGKGUI", "LJGKKT", "LGKGKT", "GLKTGF", "FKGITG"};
        boolean result = dnaService.isMutant(dna);
        assertTrue(result);
    }

    @Test
    void checkForMutantDnaShouldBeTrue_2() {
        String[] dna = {"GGGGTL", "GLTORP", "TOYJRT", "GPROTT", "GLOYPT", "OYUTIT"};
        boolean result = dnaService.isMutant(dna);
        assertTrue(result);
    }


    @Test
    void checkForMutantDnaShouldBeTrue_3() {
        String[] dna = {"dgdfkt", "gptoyl", "gjllll", "gljpyg", "gotjyo", "goypjm"};
        boolean result = dnaService.isMutant(dna);
        assertTrue(result);
    }

    @Test
    void checkForMutantDnaShouldBeFalseForNoSequence() {
        String[] dna = {"gktiop", "giturh", "fjguto", "gotplt", "fjgklt", "glkotp"};
        boolean result = dnaService.isMutant(dna);
        assertFalse(result);
    }

    @Test
    void checkForMutantDnaShouldBeFalseForOnlyOneSequence() {
        String[] dna = {"kgitop", "gktjyk", "gltopt", "lktito", "gmttfs", "dsuwtg"};
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
