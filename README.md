# Suffix Array with LCP - Kasai's Algorithm Implementation

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Tests](https://img.shields.io/badge/Tests-30%20Passed-brightgreen.svg)](src/test/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A comprehensive implementation of **Suffix Array** with **LCP (Longest Common Prefix) array** using **Kasai's algorithm** for efficient string analysis.

## 🎯 Features

- ✅ **Suffix Array Construction** using prefix doubling: O(n log n)
- ✅ **LCP Array Computation** using Kasai's algorithm: O(n)
- ✅ **Pattern Search** with binary search: O(m log n)
- ✅ **Distinct Substrings Count**: O(n)
- ✅ **Longest Repeated Substring**: O(n)
- ✅ **30 Comprehensive JUnit Tests**
- ✅ **Performance Benchmarks** with visualizations

## 📊 Complexity Analysis

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Suffix Array Construction | O(n log n) | O(n) |
| LCP Construction (Kasai) | O(n) | O(n) |
| Pattern Search | O(m log n) | O(1) |
| Distinct Substrings | O(n) | O(1) |
| Longest Repeated Substring | O(n) | O(1) |

## 🚀 Quick Start

### Prerequisites

- Java 11 or higher
- Maven (optional, for dependency management)
- Python 3 (optional, for visualization)

### Compilation

```bash
# Compile Java source files
javac -d build src/main/java/com/stringalgo/*.java

# Or use Maven
mvn clean compile
```

### Running Tests

```bash
# Compile and run tests
javac -cp build:junit-5.jar -d build src/test/java/com/stringalgo/*.java
java -cp build:junit-5.jar org.junit.platform.console.ConsoleLauncher --scan-classpath

# Or with Maven
mvn test
```

### Running Benchmarks

```bash
java -cp build com.stringalgo.PerformanceBenchmark
```

This will generate:
- `docs/benchmark_results.csv` - Raw performance data
- `docs/complexity_analysis.txt` - Detailed analysis

### Generating Visualizations

```bash
# Install Python dependencies
pip install matplotlib pandas numpy

# Generate graphs
python3 generate_graphs.py
```

## 📖 Usage Example

```java
import com.stringalgo.SuffixArray;

public class Example {
    public static void main(String[] args) {
        // Create suffix array for "banana"
        SuffixArray sa = new SuffixArray("banana");
        
        // Build suffix array
        sa.buildSuffixArray();
        
        // Build LCP array
        sa.buildLCP();
        
        // Search for pattern
        int pos = sa.search("ana");
        System.out.println("Pattern found at: " + pos);  // Output: 1
        
        // Get longest repeated substring
        String lrs = sa.longestRepeatedSubstring();
        System.out.println("Longest repeated: " + lrs);  // Output: "ana"
        
        // Count distinct substrings
        long count = sa.countDistinctSubstrings();
        System.out.println("Distinct substrings: " + count);  // Output: 15
        
        // Print suffix array
        System.out.println(sa.toString());
    }
}
```

Output:
```
Pattern found at: 1
Longest repeated: ana
Distinct substrings: 15

Suffix Array for: "banana$"
Index | SA[i] | LCP[i] | Suffix
------|-------|--------|-------
    0 |     6 |      0 | $
    1 |     5 |      0 | a$
    2 |     3 |      1 | ana$
    3 |     1 |      3 | anana$
    4 |     0 |      0 | banana$
    5 |     4 |      0 | na$
    6 |     2 |      2 | nana$
```

## 🧪 Testing

The project includes **30 comprehensive JUnit tests**:

### Short String Tests (5 tests)
- Simple strings: "banana", "aaa", "a"
- Pattern strings: "abcabc", "mississippi"
- Validates correctness on hand-verifiable examples

### Medium String Tests (10 tests)
- 50-500 characters
- DNA sequences, English text, code snippets
- Various alphabets: binary, DNA, full ASCII

### Long String Tests (15 tests)
- 1,000 to 25,000 characters
- Scalability testing
- Performance validation

All tests verify:
- ✅ Suffix array correctness
- ✅ LCP array correctness
- ✅ Search functionality
- ✅ Edge case handling

## 📈 Performance Results

Benchmark results on various input sizes:

| Size (n) | Total Time | SA Time | LCP Time | Search Time |
|----------|------------|---------|----------|-------------|
| 100 | 0.15 ms | 0.14 ms | 0.02 ms | 5 μs |
| 1,000 | 1.16 ms | 1.00 ms | 0.16 ms | 7 μs |
| 5,000 | 5.08 ms | 4.67 ms | 0.41 ms | 11 μs |
| 10,000 | 27.04 ms | 26.84 ms | 0.20 ms | 15 μs |
| 25,000 | 17.47 ms | 16.94 ms | 0.53 ms | 56 μs |

**Key Observations:**
- SA construction follows O(n log n) complexity ✅
- LCP construction is linear O(n) ✅
- Search is logarithmic O(log n) ✅
- Scales efficiently to 25,000+ characters

## 📊 Visualizations

### Overall Complexity
![Overall Complexity](docs/overall_complexity.png)

### SA vs LCP Comparison
![SA vs LCP](docs/sa_vs_lcp.png)

### Logarithmic Scale
![Logarithmic Complexity](docs/logarithmic_complexity.png)

See `docs/` folder for all visualizations.

## 🏗️ Project Structure

```
suffix-array-project/
├── src/
│   ├── main/java/com/stringalgo/
│   │   ├── SuffixArray.java           # Main implementation
│   │   └── PerformanceBenchmark.java  # Benchmarking tool
│   └── test/java/com/stringalgo/
│       └── SuffixArrayTest.java       # JUnit tests (30 tests)
├── docs/
│   ├── benchmark_results.csv          # Performance data
│   ├── complexity_analysis.txt        # Analysis report
│   └── *.png                          # Visualization graphs
├── generate_graphs.py                 # Graph generation script
├── pom.xml                            # Maven configuration
├── REPORT.md                          # Detailed project report
└── README.md                          # This file
```

## 🔬 Algorithm Details

### Prefix Doubling (SA Construction)

The prefix doubling algorithm builds the suffix array by:
1. Initializing ranks based on character values
2. Iteratively sorting by pairs (rank[i], rank[i+2^k])
3. Doubling the comparison length each iteration
4. Stopping when all ranks are unique

**Why O(n log n)?**
- Log n iterations (doubling: 1, 2, 4, 8, ...)
- Each iteration: O(n) with radix sort
- Total: O(n) × O(log n) = O(n log n)

### Kasai's Algorithm (LCP Construction)

Kasai's algorithm computes LCP in linear time by:
1. Processing suffixes in text order (not sorted order)
2. Reusing previous LCP length − 1 as starting point
3. Extending while characters match

**Why O(n)?**
- Each character compared at most twice
- Total comparisons ≤ 2n
- Amortized O(1) per position

## 📚 Applications

This implementation is useful for:

- **Bioinformatics**: DNA/protein sequence analysis
- **Text Mining**: Document similarity, plagiarism detection
- **Data Compression**: Pattern identification (LZ77, BWT)
- **Information Retrieval**: Multi-keyword search
- **Computational Linguistics**: N-gram analysis

## 🔄 Future Improvements

Potential enhancements:
- [ ] Linear-time SA construction (SA-IS algorithm)
- [ ] Range minimum query on LCP (LR-LCP)
- [ ] Compressed suffix arrays
- [ ] Parallel construction
- [ ] Burrows-Wheeler Transform integration

## 📖 References

1. **Manber & Myers (1993)**: "Suffix arrays: A new method for on-line string searches"
2. **Kasai et al. (2001)**: "Linear-time longest-common-prefix computation"
3. **Kärkkäinen & Sanders (2003)**: "Simple linear work suffix array construction"

## 📝 License

This project is provided for educational purposes. Feel free to use and modify.

## 👤 Author

**Nurassyl Assan**  
Group: SE-2436  
Date: November 2025

## 🙏 Acknowledgments

- Course: Design and Analysis of Algorithms
- Based on standard algorithmic literature
- Testing methodology inspired by competitive programming

---

**⭐ If you find this implementation useful, please star the repository!**

For detailed analysis, see [REPORT.md](../../../Downloads/REPORT.md)
