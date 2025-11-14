package com.stringalgo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.io.*;

/**
 * Comprehensive test suite for Suffix Array implementation with LCP.
 * Tests include:
 * - Short strings (5 tests)
 * - Medium-length strings (10 tests)
 * - Long strings (15 tests)
 * - Performance benchmarks
 */
public class SuffixArrayTest {
    
    private List<TestResult> performanceResults;
    
    @BeforeEach
    public void setUp() {
        performanceResults = new ArrayList<>();
    }
    
    // ==================== SHORT STRING TESTS (5 tests) ====================
    
    @Test
    @DisplayName("Short Test 1: Simple string 'banana'")
    public void testShort1_Banana() {
        String text = "banana";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        int[] expectedSA = {6, 5, 3, 1, 0, 4, 2}; // banana$
        int[] expectedLCP = {0, 0, 1, 3, 0, 0, 2};
        
        assertArrayEquals(expectedSA, sa.getSuffixArray(), "SA mismatch for 'banana'");
        assertArrayEquals(expectedLCP, sa.getLCP(), "LCP mismatch for 'banana'");
        
        recordPerformance("banana", 7, endTime - startTime);
        System.out.println("Short Test 1 (banana): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Short Test 2: String with repeated characters 'aaa'")
    public void testShort2_RepeatedChars() {
        String text = "aaa";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        // aaa$ -> suffixes: $, a$, aa$, aaa$
        assertEquals(4, sa.getLength());
        assertTrue(sa.getSuffixArray()[0] == 3); // $ comes first
        
        // Check LCP correctness
        int[] lcp = sa.getLCP();
        assertTrue(lcp[1] == 0); // $ vs a$
        assertTrue(lcp[2] >= 1); // a$ vs aa$ (at least 1 common)
        assertTrue(lcp[3] >= 2); // aa$ vs aaa$ (at least 2 common)
        
        recordPerformance("aaa", 4, endTime - startTime);
        System.out.println("Short Test 2 (aaa): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Short Test 3: Single character 'a'")
    public void testShort3_SingleChar() {
        String text = "a";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals(2, sa.getLength()); // a$
        assertEquals(0, sa.getLCP()[1]); // No common prefix between $ and a$
        
        recordPerformance("a", 2, endTime - startTime);
        System.out.println("Short Test 3 (a): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Short Test 4: String 'abcabc'")
    public void testShort4_Pattern() {
        String text = "abcabc";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        // Check longest repeated substring
        String lrs = sa.longestRepeatedSubstring();
        assertEquals("abc", lrs);
        
        // Check distinct substrings count
        long distinct = sa.countDistinctSubstrings();
        assertTrue(distinct > 0 && distinct <= 28); // 7*8/2 = 28 total possible
        
        recordPerformance("abcabc", 7, endTime - startTime);
        System.out.println("Short Test 4 (abcabc): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Short Test 5: String 'mississippi'")
    public void testShort5_Mississippi() {
        String text = "mississippi";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals(12, sa.getLength());
        
        // Test search functionality
        int pos = sa.search("issi");
        assertTrue(pos >= 0 && pos < text.length());
        assertEquals("issi", text.substring(pos, pos + 4));
        
        recordPerformance("mississippi", 12, endTime - startTime);
        System.out.println("Short Test 5 (mississippi): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    // ==================== MEDIUM STRING TESTS (10 tests) ====================
    
    @Test
    @DisplayName("Medium Test 1: DNA sequence (50 chars)")
    public void testMedium1_DNA() {
        String text = "ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTAC";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals(51, sa.getLength());
        assertTrue(sa.search("ACGT") >= 0);
        assertTrue(sa.longestRepeatedSubstring().length() >= 4);
        
        recordPerformance("DNA-50", 51, endTime - startTime);
        System.out.println("Medium Test 1 (DNA-50): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 2: English sentence (100 chars)")
    public void testMedium2_Sentence() {
        String text = "The quick brown fox jumps over the lazy dog. " +
                      "The quick brown fox jumps over the lazy dog again.";
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertTrue(sa.search("quick") >= 0);
        assertTrue(sa.search("brown") >= 0);
        assertTrue(sa.search("xyz") < 0); // Not found
        
        String lrs = sa.longestRepeatedSubstring();
        assertTrue(lrs.contains("quick") || lrs.contains("brown") || lrs.contains("fox"));
        
        recordPerformance("Sentence-100", text.length() + 1, endTime - startTime);
        System.out.println("Medium Test 2 (Sentence-100): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 3: Repeated pattern (150 chars)")
    public void testMedium3_RepeatedPattern() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            sb.append("hello");
        }
        String text = sb.toString();
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals("hello", sa.longestRepeatedSubstring());
        assertTrue(sa.search("hello") >= 0);
        
        recordPerformance("Repeated-150", text.length() + 1, endTime - startTime);
        System.out.println("Medium Test 3 (Repeated-150): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 4: Random alphanumeric (200 chars)")
    public void testMedium4_Random200() {
        String text = generateRandomString(200, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals(201, sa.getLength());
        assertNotNull(sa.getSuffixArray());
        assertNotNull(sa.getLCP());
        
        // Verify SA is a permutation
        boolean[] seen = new boolean[201];
        for (int idx : sa.getSuffixArray()) {
            assertFalse(seen[idx], "Duplicate index in SA");
            seen[idx] = true;
        }
        
        recordPerformance("Random-200", 201, endTime - startTime);
        System.out.println("Medium Test 4 (Random-200): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 5: Fibonacci string (233 chars)")
    public void testMedium5_Fibonacci() {
        String text = generateFibonacciString(13); // F(13) = 233
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertTrue(text.length() >= 200);
        long distinct = sa.countDistinctSubstrings();
        assertTrue(distinct > 0);
        
        recordPerformance("Fibonacci-233", text.length() + 1, endTime - startTime);
        System.out.println("Medium Test 5 (Fibonacci-233): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 6: Alternating pattern (300 chars)")
    public void testMedium6_Alternating() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 150; i++) {
            sb.append(i % 2 == 0 ? 'A' : 'B');
        }
        String text = sb.toString();
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        String lrs = sa.longestRepeatedSubstring();
        assertTrue(lrs.length() >= 2); // At least "AB" or "BA"
        
        recordPerformance("Alternating-300", text.length() + 1, endTime - startTime);
        System.out.println("Medium Test 6 (Alternating-300): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 7: Palindrome (250 chars)")
    public void testMedium7_Palindrome() {
        String half = generateRandomString(125, "ABCD");
        String text = half + new StringBuilder(half).reverse().toString();
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals(251, sa.getLength());
        String lrs = sa.longestRepeatedSubstring();
        assertTrue(lrs.length() > 0);
        
        recordPerformance("Palindrome-250", 251, endTime - startTime);
        System.out.println("Medium Test 7 (Palindrome-250): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 8: Binary string (400 chars)")
    public void testMedium8_Binary() {
        String text = generateRandomString(400, "01");
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertTrue(sa.search("0") >= 0);
        assertTrue(sa.search("1") >= 0);
        
        recordPerformance("Binary-400", 401, endTime - startTime);
        System.out.println("Medium Test 8 (Binary-400): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 9: Code snippet (350 chars)")
    public void testMedium9_Code() {
        String text = "public class Example { " +
                      "public static void main(String[] args) { " +
                      "System.out.println(\"Hello World\"); " +
                      "for (int i = 0; i < 10; i++) { " +
                      "System.out.println(i); " +
                      "} } " +
                      "private int calculate(int x, int y) { " +
                      "return x + y; } }";
        text = text.repeat(2); // ~350 chars
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertTrue(sa.search("public") >= 0);
        assertTrue(sa.search("System") >= 0);
        
        recordPerformance("Code-" + text.length(), text.length() + 1, endTime - startTime);
        System.out.println("Medium Test 9 (Code-" + text.length() + "): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    @Test
    @DisplayName("Medium Test 10: Mixed case (500 chars)")
    public void testMedium10_MixedCase() {
        String text = generateRandomString(500, 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        sa.buildLCP();
        long endTime = System.nanoTime();
        
        assertEquals(501, sa.getLength());
        long distinct = sa.countDistinctSubstrings();
        assertTrue(distinct > 0);
        
        recordPerformance("MixedCase-500", 501, endTime - startTime);
        System.out.println("Medium Test 10 (MixedCase-500): PASSED - " + 
                          (endTime - startTime) / 1000 + " μs");
    }
    
    // ==================== LONG STRING TESTS (15 tests) ====================
    
    @Test
    @DisplayName("Long Test 1: 1000 characters")
    public void testLong1_1K() {
        String text = generateRandomString(1000, "ABCDEFGHIJ");
        testLongString("Long-1K", text);
    }
    
    @Test
    @DisplayName("Long Test 2: 2000 characters")
    public void testLong2_2K() {
        String text = generateRandomString(2000, "ABCDE");
        testLongString("Long-2K", text);
    }
    
    @Test
    @DisplayName("Long Test 3: 3000 characters")
    public void testLong3_3K() {
        String text = generateRandomString(3000, "ABC");
        testLongString("Long-3K", text);
    }
    
    @Test
    @DisplayName("Long Test 4: 4000 characters - DNA")
    public void testLong4_4K() {
        String text = generateRandomString(4000, "ACGT");
        testLongString("Long-4K-DNA", text);
    }
    
    @Test
    @DisplayName("Long Test 5: 5000 characters - Binary")
    public void testLong5_5K() {
        String text = generateRandomString(5000, "01");
        testLongString("Long-5K-Binary", text);
    }
    
    @Test
    @DisplayName("Long Test 6: 6000 characters - Alphabet")
    public void testLong6_6K() {
        String text = generateRandomString(6000, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        testLongString("Long-6K-Alphabet", text);
    }
    
    @Test
    @DisplayName("Long Test 7: 7000 characters - Repeated")
    public void testLong7_7K() {
        StringBuilder sb = new StringBuilder();
        String pattern = "ABCDEFGHIJ";
        for (int i = 0; i < 700; i++) {
            sb.append(pattern);
        }
        testLongString("Long-7K-Repeated", sb.toString());
    }
    
    @Test
    @DisplayName("Long Test 8: 8000 characters")
    public void testLong8_8K() {
        String text = generateRandomString(8000, "ABCDEFGHIJKLMNOP");
        testLongString("Long-8K", text);
    }
    
    @Test
    @DisplayName("Long Test 9: 9000 characters")
    public void testLong9_9K() {
        String text = generateRandomString(9000, "ABCDEF");
        testLongString("Long-9K", text);
    }
    
    @Test
    @DisplayName("Long Test 10: 10000 characters")
    public void testLong10_10K() {
        String text = generateRandomString(10000, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        testLongString("Long-10K", text);
    }
    
    @Test
    @DisplayName("Long Test 11: 12000 characters")
    public void testLong11_12K() {
        String text = generateRandomString(12000, "ABCDE");
        testLongString("Long-12K", text);
    }
    
    @Test
    @DisplayName("Long Test 12: 15000 characters")
    public void testLong12_15K() {
        String text = generateRandomString(15000, "ABCDEFGHIJ");
        testLongString("Long-15K", text);
    }
    
    @Test
    @DisplayName("Long Test 13: 18000 characters")
    public void testLong13_18K() {
        String text = generateRandomString(18000, "ABC");
        testLongString("Long-18K", text);
    }
    
    @Test
    @DisplayName("Long Test 14: 20000 characters")
    public void testLong14_20K() {
        String text = generateRandomString(20000, "ABCDEFGH");
        testLongString("Long-20K", text);
    }
    
    @Test
    @DisplayName("Long Test 15: 25000 characters")
    public void testLong15_25K() {
        String text = generateRandomString(25000, "ABCDEFGHIJKLMNOP");
        testLongString("Long-25K", text);
    }
    
    // ==================== HELPER METHODS ====================
    
    private void testLongString(String name, String text) {
        SuffixArray sa = new SuffixArray(text);
        
        long startTime = System.nanoTime();
        sa.buildSuffixArray();
        long saTime = System.nanoTime();
        sa.buildLCP();
        long lcpTime = System.nanoTime();
        
        long totalTime = lcpTime - startTime;
        long saOnlyTime = saTime - startTime;
        long lcpOnlyTime = lcpTime - saTime;
        
        // Verify correctness
        assertNotNull(sa.getSuffixArray());
        assertNotNull(sa.getLCP());
        assertEquals(text.length() + 1, sa.getLength());
        
        // Test search with binary search
        if (text.length() >= 3) {
            String pattern = text.substring(0, Math.min(3, text.length()));
            long searchStart = System.nanoTime();
            int pos = sa.search(pattern);
            long searchTime = System.nanoTime() - searchStart;
            assertTrue(pos >= 0);
            
            recordPerformance(name, text.length() + 1, totalTime, 
                            saOnlyTime, lcpOnlyTime, searchTime);
        } else {
            recordPerformance(name, text.length() + 1, totalTime, 
                            saOnlyTime, lcpOnlyTime, 0);
        }
        
        System.out.printf("%s: PASSED - Total: %d μs (SA: %d μs, LCP: %d μs)%n", 
                         name, totalTime / 1000, saOnlyTime / 1000, lcpOnlyTime / 1000);
    }
    
    private void recordPerformance(String name, int length, long totalTime) {
        recordPerformance(name, length, totalTime, 0, 0, 0);
    }
    
    private void recordPerformance(String name, int length, long totalTime,
                                   long saTime, long lcpTime, long searchTime) {
        performanceResults.add(new TestResult(name, length, totalTime, 
                                             saTime, lcpTime, searchTime));
    }
    
    private String generateRandomString(int length, String alphabet) {
        Random rand = new Random(42); // Fixed seed for reproducibility
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
    
    private String generateFibonacciString(int n) {
        if (n == 0) return "A";
        if (n == 1) return "B";
        
        String prev2 = "A";
        String prev1 = "B";
        
        for (int i = 2; i <= n; i++) {
            String current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    @org.junit.jupiter.api.AfterAll
    public static void generateReport() {
        System.out.println("\n========== TEST EXECUTION COMPLETED ==========");
    }
    
    // Inner class for test results
    static class TestResult {
        String name;
        int length;
        long totalTime;
        long saTime;
        long lcpTime;
        long searchTime;
        
        TestResult(String name, int length, long totalTime, 
                  long saTime, long lcpTime, long searchTime) {
            this.name = name;
            this.length = length;
            this.totalTime = totalTime;
            this.saTime = saTime;
            this.lcpTime = lcpTime;
            this.searchTime = searchTime;
        }
    }
}
