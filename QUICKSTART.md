# Quick Start Guide

## 🚀 Running the Project

### 1. Compile All Files

```bash
javac -d build src/main/java/com/stringalgo/*.java
```

### 2. Run Examples

```bash
java -cp build com.stringalgo.Examples
```

This will demonstrate:
- Basic suffix array construction
- Pattern searching
- Distinct substring counting
- Longest repeated substring finding
- DNA sequence analysis
- Text compression potential analysis

### 3. Run Performance Benchmarks

```bash
java -cp build com.stringalgo.PerformanceBenchmark
```

This will:
- Test input sizes from 10 to 30,000 characters
- Generate `docs/benchmark_results.csv`
- Generate `docs/complexity_analysis.txt`
- Display real-time performance metrics

### 4. Generate Visualization Graphs

```bash
# Install Python dependencies
pip3 install matplotlib pandas numpy --break-system-packages

# Generate graphs
python3 generate_graphs.py
```

This will create 6 graphs in the `docs/` folder:
- `overall_complexity.png` - Overall time complexity
- `sa_vs_lcp.png` - SA vs LCP comparison
- `logarithmic_complexity.png` - Log-scale analysis
- `search_performance.png` - Binary search performance
- `growth_rates.png` - Empirical vs theoretical growth
- `component_breakdown.png` - Time component breakdown

### 5. Run JUnit Tests

#### Option A: Direct Compilation (if JUnit is available)

```bash
# Download JUnit 5 JARs if needed
wget https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.9.2/junit-jupiter-api-5.9.2.jar
wget https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.9.2/junit-jupiter-engine-5.9.2.jar

# Compile tests
javac -cp build:junit-jupiter-api-5.9.2.jar -d build src/test/java/com/stringalgo/*.java

# Run tests
java -cp build:junit-jupiter-api-5.9.2.jar:junit-jupiter-engine-5.9.2.jar \
  org.junit.platform.console.ConsoleLauncher --scan-classpath
```

#### Option B: Using Maven

```bash
mvn test
```

## 📁 Project Structure

```
suffix-array-project/
├── src/
│   ├── main/java/com/stringalgo/
│   │   ├── SuffixArray.java           # Main implementation
│   │   ├── PerformanceBenchmark.java  # Benchmark tool
│   │   └── Examples.java              # Usage examples
│   └── test/java/com/stringalgo/
│       └── SuffixArrayTest.java       # 30 JUnit tests
├── docs/                              # Generated reports & graphs
├── build/                             # Compiled .class files
├── generate_graphs.py                 # Python visualization script
├── REPORT.md                          # Detailed report
└── README.md                          # Documentation
```

## 📊 Expected Output

### Examples Output
```
EXAMPLE 1: Basic Suffix Array Construction
Suffix Array for: "banana$"
Index | SA[i] | LCP[i] | Suffix
------|-------|--------|-------
    0 |     6 |      0 | $
    1 |     5 |      0 | a$
    2 |     3 |      1 | ana$
...
```

### Benchmark Output
```
Testing size: 1000
  Total: 1.162 ms | SA: 1.002 ms | LCP: 0.160 ms | Search: 0.007 ms
Testing size: 5000
  Total: 5.083 ms | SA: 4.671 ms | LCP: 0.412 ms | Search: 0.011 ms
...
```

### Test Output
```
Short Test 1 (banana): PASSED - 15 μs
Short Test 2 (aaa): PASSED - 8 μs
Medium Test 1 (DNA-50): PASSED - 125 μs
Long Test 1 (1K): PASSED - Total: 1162 μs (SA: 1002 μs, LCP: 160 μs)
...
All 30 tests PASSED ✅
```

## 🎯 What Each Component Does

| File | Purpose |
|------|---------|
| `SuffixArray.java` | Core implementation with prefix doubling & Kasai |
| `PerformanceBenchmark.java` | Automated performance testing |
| `SuffixArrayTest.java` | 30 JUnit tests (5 short, 10 medium, 15 long) |
| `Examples.java` | 6 practical usage demonstrations |
| `generate_graphs.py` | Creates 6 visualization graphs |

## ⏱️ Expected Runtime

- **Examples:** < 1 second
- **Benchmarks:** ~10-15 seconds (tests up to 30K chars)
- **Tests:** ~2-5 seconds (30 tests)
- **Graph Generation:** ~2-3 seconds

## 🐛 Troubleshooting

### "javac: command not found"
```bash
# Install Java
sudo apt-get update
sudo apt-get install default-jdk
```

### "No module named matplotlib"
```bash
# Install Python packages
pip3 install matplotlib pandas numpy --break-system-packages
```

### JUnit tests fail to compile
- Either download JUnit JARs manually (see above)
- Or use Maven: `mvn test`

## 📖 For More Information

- **Detailed Report:** See `REPORT.md`
- **Full Documentation:** See `README.md`
- **Algorithm Details:** Check comments in `SuffixArray.java`

---

**Need help?** All code is well-commented and includes complexity analysis in comments.
