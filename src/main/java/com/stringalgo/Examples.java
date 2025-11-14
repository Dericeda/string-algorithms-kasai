package com.stringalgo;

/**
 * Examples demonstrating various uses of Suffix Array with LCP.
 * Run this class to see practical applications.
 */
public class Examples {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("SUFFIX ARRAY WITH LCP - USAGE EXAMPLES");
        System.out.println("=".repeat(80));
        System.out.println();
        
        example1_BasicUsage();
        example2_PatternSearch();
        example3_DistinctSubstrings();
        example4_LongestRepeatedSubstring();
        example5_DNAAnalysis();
        example6_TextAnalysis();
    }
    
    /**
     * Example 1: Basic suffix array construction and display
     */
    public static void example1_BasicUsage() {
        System.out.println("EXAMPLE 1: Basic Suffix Array Construction");
        System.out.println("-".repeat(80));
        
        String text = "banana";
        System.out.println("Input text: \"" + text + "\"");
        System.out.println();
        
        SuffixArray sa = new SuffixArray(text);
        sa.buildSuffixArray();
        sa.buildLCP();
        
        System.out.println(sa.toString());
        System.out.println();
    }
    
    /**
     * Example 2: Searching for patterns
     */
    public static void example2_PatternSearch() {
        System.out.println("EXAMPLE 2: Pattern Search");
        System.out.println("-".repeat(80));
        
        String text = "The quick brown fox jumps over the lazy dog";
        System.out.println("Text: \"" + text + "\"");
        System.out.println();
        
        SuffixArray sa = new SuffixArray(text);
        sa.buildSuffixArray();
        
        String[] patterns = {"quick", "fox", "lazy", "cat", "the"};
        
        for (String pattern : patterns) {
            int pos = sa.search(pattern);
            if (pos >= 0) {
                System.out.printf("✓ Pattern \"%s\" found at position %d%n", pattern, pos);
            } else {
                System.out.printf("✗ Pattern \"%s\" NOT found%n", pattern);
            }
        }
        System.out.println();
    }
    
    /**
     * Example 3: Counting distinct substrings
     */
    public static void example3_DistinctSubstrings() {
        System.out.println("EXAMPLE 3: Distinct Substrings Count");
        System.out.println("-".repeat(80));
        
        String[] texts = {"aaa", "abc", "abcabc", "mississippi"};
        
        for (String text : texts) {
            SuffixArray sa = new SuffixArray(text);
            sa.buildSuffixArray();
            sa.buildLCP();
            
            long distinct = sa.countDistinctSubstrings();
            long total = (long) text.length() * (text.length() + 1) / 2;
            
            System.out.printf("Text: %-15s | Length: %2d | Distinct: %3d | Total: %3d | Ratio: %.2f%%%n",
                "\"" + text + "\"", text.length(), distinct, total, 
                (distinct * 100.0 / total));
        }
        System.out.println();
    }
    
    /**
     * Example 4: Finding longest repeated substring
     */
    public static void example4_LongestRepeatedSubstring() {
        System.out.println("EXAMPLE 4: Longest Repeated Substring");
        System.out.println("-".repeat(80));
        
        String[] texts = {
            "banana",
            "mississippi",
            "abcabcabc",
            "The theory of everything is the quest for everything"
        };
        
        for (String text : texts) {
            SuffixArray sa = new SuffixArray(text);
            sa.buildSuffixArray();
            sa.buildLCP();
            
            String lrs = sa.longestRepeatedSubstring();
            
            System.out.printf("Text: %-55s%n", "\"" + text + "\"");
            System.out.printf("  → Longest repeated: \"%s\" (length: %d)%n%n", 
                lrs, lrs.length());
        }
        System.out.println();
    }
    
    /**
     * Example 5: DNA sequence analysis
     */
    public static void example5_DNAAnalysis() {
        System.out.println("EXAMPLE 5: DNA Sequence Analysis");
        System.out.println("-".repeat(80));
        
        String dna = "ACGTACGTACGTACGT";
        System.out.println("DNA Sequence: " + dna);
        System.out.println();
        
        SuffixArray sa = new SuffixArray(dna);
        sa.buildSuffixArray();
        sa.buildLCP();
        
        // Find repeated motifs
        String lrs = sa.longestRepeatedSubstring();
        System.out.println("Longest repeated motif: " + lrs);
        
        // Count distinct k-mers (k=4)
        long distinct = sa.countDistinctSubstrings();
        System.out.println("Total distinct substrings: " + distinct);
        
        // Search for specific patterns
        String[] motifs = {"ACGT", "CGTA", "AAAA", "TTTT"};
        System.out.println("\nMotif search:");
        for (String motif : motifs) {
            int pos = sa.search(motif);
            System.out.printf("  %s: %s%n", motif, pos >= 0 ? "Found at " + pos : "Not found");
        }
        System.out.println();
    }
    
    /**
     * Example 6: Text analysis and compression potential
     */
    public static void example6_TextAnalysis() {
        System.out.println("EXAMPLE 6: Text Analysis & Compression Potential");
        System.out.println("-".repeat(80));
        
        String text = "To be or not to be, that is the question";
        System.out.println("Text: \"" + text + "\"");
        System.out.println("Length: " + text.length() + " characters");
        System.out.println();
        
        SuffixArray sa = new SuffixArray(text);
        sa.buildSuffixArray();
        sa.buildLCP();
        
        // Statistics
        long totalSubstrings = (long) text.length() * (text.length() + 1) / 2;
        long distinctSubstrings = sa.countDistinctSubstrings();
        long repeatedSubstrings = totalSubstrings - distinctSubstrings;
        String lrs = sa.longestRepeatedSubstring();
        
        System.out.println("Analysis:");
        System.out.println("  Total substrings: " + totalSubstrings);
        System.out.println("  Distinct substrings: " + distinctSubstrings);
        System.out.println("  Repeated substrings: " + repeatedSubstrings);
        System.out.printf("  Repetition ratio: %.2f%%%n", (repeatedSubstrings * 100.0 / totalSubstrings));
        System.out.println("  Longest repeated: \"" + lrs + "\" (length: " + lrs.length() + ")");
        System.out.println();
        
        // LCP analysis
        int[] lcp = sa.getLCP();
        int maxLCP = 0;
        double avgLCP = 0;
        for (int val : lcp) {
            maxLCP = Math.max(maxLCP, val);
            avgLCP += val;
        }
        avgLCP /= lcp.length;
        
        System.out.println("LCP Statistics:");
        System.out.printf("  Max LCP: %d%n", maxLCP);
        System.out.printf("  Average LCP: %.2f%n", avgLCP);
        System.out.printf("  Compression potential: %s%n", 
            maxLCP > 5 ? "High (many long repeats)" : "Low (few repeats)");
        System.out.println();
    }
}
