package com.example.mutanttestapi.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnaServiceTests {
    private DNAService dnaService = new DNAService();

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


}
