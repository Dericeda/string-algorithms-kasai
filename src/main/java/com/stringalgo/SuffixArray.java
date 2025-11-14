package com.stringalgo;

import java.util.Arrays;

/**
 * Suffix Array implementation with LCP (Longest Common Prefix) array
 * using Kasai's algorithm for efficient construction.
 * 
 * Time Complexity:
 * - SA construction: O(n log n) using prefix doubling
 * - LCP construction: O(n) using Kasai's algorithm
 * 
 * Space Complexity: O(n)
 */
public class SuffixArray {
    
    private String text;
    private int n;
    private int[] suffixArray;
    private int[] lcp;
    private int[] rank;
    
    /**
     * Constructs a Suffix Array for the given text.
     * Automatically appends a sentinel character '$' if not present.
     * 
     * @param text the input string
     */
    public SuffixArray(String text) {
        // Add sentinel if not present
        if (!text.endsWith("$")) {
            this.text = text + "$";
        } else {
            this.text = text;
        }
        this.n = this.text.length();
        this.suffixArray = new int[n];
        this.rank = new int[n];
    }
    
    /**
     * Builds the suffix array using prefix doubling algorithm.
     * 
     * Algorithm steps:
     * 1. Initialize ranks based on character values
     * 2. Iteratively sort suffixes by doubling prefix length
     * 3. Use pair of ranks (rank[i], rank[i+2^k]) for sorting
     * 4. Continue until all ranks are unique
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public void buildSuffixArray() {
        // Step 1: Initialize suffix array with indices
        for (int i = 0; i < n; i++) {
            suffixArray[i] = i;
        }
        
        // Step 2: Initialize ranks based on character values
        for (int i = 0; i < n; i++) {
            rank[i] = text.charAt(i);
        }
        
        // Temporary array for next rank values
        int[] tempRank = new int[n];
        
        // Step 3: Prefix doubling - sort by 2^k length prefixes
        for (int k = 1; k < n; k *= 2) {
            // Sort suffixes by pair (rank[i], rank[i+k])
            final int gap = k;
            Integer[] indices = new Integer[n];
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }
            
            // Comparator: compare by (rank[i], rank[i+k])
            Arrays.sort(indices, (a, b) -> {
                if (rank[a] != rank[b]) {
                    return rank[a] - rank[b];
                }
                int rankA = (a + gap < n) ? rank[a + gap] : -1;
                int rankB = (b + gap < n) ? rank[b + gap] : -1;
                return rankA - rankB;
            });
            
            // Update suffix array
            for (int i = 0; i < n; i++) {
                suffixArray[i] = indices[i];
            }
            
            // Step 4: Assign new ranks
            tempRank[suffixArray[0]] = 0;
            for (int i = 1; i < n; i++) {
                int prev = suffixArray[i - 1];
                int curr = suffixArray[i];
                
                // Check if current suffix is same as previous
                boolean same = (rank[prev] == rank[curr]);
                if (same && curr + gap < n && prev + gap < n) {
                    same = (rank[prev + gap] == rank[curr + gap]);
                } else if (same) {
                    same = ((curr + gap >= n) == (prev + gap >= n));
                }
                
                tempRank[curr] = same ? tempRank[prev] : tempRank[prev] + 1;
            }
            
            // Copy new ranks
            System.arraycopy(tempRank, 0, rank, 0, n);
            
            // If all ranks are unique, we're done
            if (tempRank[suffixArray[n - 1]] == n - 1) {
                break;
            }
        }
    }
    
    /**
     * Builds the LCP array using Kasai's algorithm.
     * 
     * Key insight: If suffix at position i has LCP of length k with its
     * predecessor in SA, then suffix at i+1 has LCP of at least k-1
     * with its predecessor.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public void buildLCP() {
        lcp = new int[n];
        
        // Build inverse suffix array (rank array)
        int[] invSA = new int[n];
        for (int i = 0; i < n; i++) {
            invSA[suffixArray[i]] = i;
        }
        
        int k = 0; // Length of current LCP
        
        // Process suffixes in text order
        for (int i = 0; i < n; i++) {
            // Skip the first suffix in sorted order (no predecessor)
            if (invSA[i] == 0) {
                k = 0;
                continue;
            }
            
            // Get the previous suffix in sorted order
            int j = suffixArray[invSA[i] - 1];
            
            // Extend LCP while characters match
            while (i + k < n && j + k < n && 
                   text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }
            
            lcp[invSA[i]] = k;
            
            // Decrease k for next iteration (key optimization)
            if (k > 0) {
                k--;
            }
        }
    }
    
    /**
     * Searches for a pattern in the text using binary search on suffix array.
     * 
     * Time Complexity: O(m log n) where m = pattern length
     * 
     * @param pattern the pattern to search
     * @return the starting index of first occurrence, or -1 if not found
     */
    public int search(String pattern) {
        int left = 0, right = n - 1;
        int m = pattern.length();
        
        // Binary search for leftmost occurrence
        while (left < right) {
            int mid = (left + right) / 2;
            String suffix = text.substring(suffixArray[mid], 
                                          Math.min(suffixArray[mid] + m, n));
            
            if (suffix.compareTo(pattern) < 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        // Check if pattern exists at found position
        if (left < n) {
            int start = suffixArray[left];
            if (start + m <= n) {
                String suffix = text.substring(start, start + m);
                if (suffix.equals(pattern)) {
                    return start;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Counts the number of distinct substrings in the text.
     * 
     * Formula: Total substrings - Duplicate substrings
     *        = n(n+1)/2 - Σ(lcp[i])
     * 
     * Time Complexity: O(n)
     * 
     * @return number of distinct substrings
     */
    public long countDistinctSubstrings() {
        if (lcp == null) {
            buildLCP();
        }
        
        long totalSubstrings = (long) n * (n + 1) / 2;
        long duplicates = 0;
        
        for (int i = 0; i < n; i++) {
            duplicates += lcp[i];
        }
        
        return totalSubstrings - duplicates;
    }
    
    /**
     * Finds the longest repeated substring in the text.
     * 
     * Time Complexity: O(n)
     * 
     * @return the longest repeated substring
     */
    public String longestRepeatedSubstring() {
        if (lcp == null) {
            buildLCP();
        }
        
        int maxLen = 0;
        int maxIndex = 0;
        
        for (int i = 0; i < n; i++) {
            if (lcp[i] > maxLen) {
                maxLen = lcp[i];
                maxIndex = i;
            }
        }
        
        if (maxLen == 0) {
            return "";
        }
        
        return text.substring(suffixArray[maxIndex], 
                             suffixArray[maxIndex] + maxLen);
    }
    
    // Getters
    public int[] getSuffixArray() {
        return suffixArray;
    }
    
    public int[] getLCP() {
        return lcp;
    }
    
    public String getText() {
        return text;
    }
    
    public int getLength() {
        return n;
    }
    
    /**
     * Returns a string representation of the suffix array with suffixes.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Suffix Array for: \"").append(text).append("\"\n");
        sb.append("Index | SA[i] | LCP[i] | Suffix\n");
        sb.append("------|-------|--------|-------\n");
        
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%5d | %5d | %6d | %s\n", 
                i, suffixArray[i], 
                (lcp != null ? lcp[i] : -1),
                text.substring(suffixArray[i])));
        }
        
        return sb.toString();
    }
}
