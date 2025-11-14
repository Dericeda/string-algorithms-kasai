#!/usr/bin/env python3
"""
Generate performance visualization graphs for Suffix Array algorithm.
Reads benchmark data and creates complexity analysis plots.
"""

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import os

# Set style
plt.style.use('seaborn-v0_8-darkgrid')

def load_data(csv_path):
    """Load benchmark data from CSV file."""
    return pd.read_csv(csv_path)

def plot_overall_complexity(df, output_dir):
    """Plot overall time complexity comparison."""
    fig, ax = plt.subplots(figsize=(12, 7))
    
    # Plot actual times
    ax.plot(df['Size'], df['Total(ms)'], 'o-', linewidth=2, markersize=8, 
            label='Actual Total Time', color='#2E86AB')
    ax.plot(df['Size'], df['SA(ms)'], 's-', linewidth=2, markersize=6,
            label='SA Construction', color='#A23B72', alpha=0.7)
    ax.plot(df['Size'], df['LCP(ms)'], '^-', linewidth=2, markersize=6,
            label='LCP Construction', color='#F18F01', alpha=0.7)
    
    # Plot theoretical O(n log n)
    theoretical = (df['n*log(n)'] / df['n*log(n)'].iloc[0]) * df['Total(ms)'].iloc[0]
    ax.plot(df['Size'], theoretical, '--', linewidth=2, 
            label='O(n log n) theoretical', color='#C73E1D', alpha=0.5)
    
    ax.set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
    ax.set_ylabel('Time (milliseconds)', fontsize=12, fontweight='bold')
    ax.set_title('Suffix Array: Overall Time Complexity Analysis', 
                fontsize=14, fontweight='bold')
    ax.legend(fontsize=10, loc='upper left')
    ax.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig(f'{output_dir}/overall_complexity.png', dpi=300, bbox_inches='tight')
    print(f"Saved: {output_dir}/overall_complexity.png")
    plt.close()

def plot_sa_vs_lcp(df, output_dir):
    """Plot SA construction vs LCP construction comparison."""
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(16, 6))
    
    # Left plot: Time comparison
    ax1.plot(df['Size'], df['SA(ms)'], 'o-', linewidth=2.5, markersize=8,
            label='SA Construction O(n log n)', color='#2E86AB')
    ax1.plot(df['Size'], df['LCP(ms)'], 's-', linewidth=2.5, markersize=8,
            label='LCP Construction O(n)', color='#F18F01')
    
    ax1.set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
    ax1.set_ylabel('Time (milliseconds)', fontsize=12, fontweight='bold')
    ax1.set_title('SA vs LCP Construction Time', fontsize=13, fontweight='bold')
    ax1.legend(fontsize=11)
    ax1.grid(True, alpha=0.3)
    
    # Right plot: Ratio
    ratio = df['SA(ms)'] / df['LCP(ms)']
    ax2.plot(df['Size'], ratio, 'o-', linewidth=2.5, markersize=8, color='#A23B72')
    ax2.axhline(y=ratio.mean(), color='#C73E1D', linestyle='--', linewidth=2,
                label=f'Average Ratio: {ratio.mean():.2f}')
    
    ax2.set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
    ax2.set_ylabel('Time Ratio (SA / LCP)', fontsize=12, fontweight='bold')
    ax2.set_title('SA to LCP Time Ratio', fontsize=13, fontweight='bold')
    ax2.legend(fontsize=11)
    ax2.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig(f'{output_dir}/sa_vs_lcp.png', dpi=300, bbox_inches='tight')
    print(f"Saved: {output_dir}/sa_vs_lcp.png")
    plt.close()

def plot_logarithmic_scale(df, output_dir):
    """Plot complexity on logarithmic scale."""
    fig, ax = plt.subplots(figsize=(12, 7))
    
    ax.loglog(df['Size'], df['Total(ms)'], 'o-', linewidth=2.5, markersize=8,
             label='Actual Total Time', color='#2E86AB')
    ax.loglog(df['Size'], df['SA(ms)'], 's-', linewidth=2, markersize=6,
             label='SA Construction', color='#A23B72', alpha=0.7)
    ax.loglog(df['Size'], df['LCP(ms)'], '^-', linewidth=2, markersize=6,
             label='LCP Construction', color='#F18F01', alpha=0.7)
    
    # Reference lines
    x = df['Size'].values
    y_n = x * df['Total(ms)'].iloc[0] / df['Size'].iloc[0]
    y_nlogn = x * np.log2(x) * df['Total(ms)'].iloc[0] / (df['Size'].iloc[0] * np.log2(df['Size'].iloc[0]))
    
    ax.loglog(x, y_n, '--', linewidth=2, label='O(n)', color='green', alpha=0.5)
    ax.loglog(x, y_nlogn, '--', linewidth=2, label='O(n log n)', color='red', alpha=0.5)
    
    ax.set_xlabel('Input Size (n) - Log Scale', fontsize=12, fontweight='bold')
    ax.set_ylabel('Time (ms) - Log Scale', fontsize=12, fontweight='bold')
    ax.set_title('Suffix Array: Logarithmic Complexity Analysis', 
                fontsize=14, fontweight='bold')
    ax.legend(fontsize=10, loc='upper left')
    ax.grid(True, alpha=0.3, which='both')
    
    plt.tight_layout()
    plt.savefig(f'{output_dir}/logarithmic_complexity.png', dpi=300, bbox_inches='tight')
    print(f"Saved: {output_dir}/logarithmic_complexity.png")
    plt.close()

def plot_search_performance(df, output_dir):
    """Plot binary search performance."""
    fig, ax = plt.subplots(figsize=(12, 7))
    
    # Convert to microseconds for better readability
    search_us = df['Search(ns)'] / 1000
    
    ax.plot(df['Size'], search_us, 'o-', linewidth=2.5, markersize=8,
           label='Actual Search Time', color='#2E86AB')
    
    # Theoretical O(log n) - scaled
    theoretical_logn = np.log2(df['Size']) * (search_us.iloc[-1] / np.log2(df['Size'].iloc[-1]))
    ax.plot(df['Size'], theoretical_logn, '--', linewidth=2,
           label='O(log n) theoretical', color='#C73E1D', alpha=0.6)
    
    ax.set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
    ax.set_ylabel('Search Time (microseconds)', fontsize=12, fontweight='bold')
    ax.set_title('Binary Search Performance: O(log n) Pattern Matching', 
                fontsize=14, fontweight='bold')
    ax.legend(fontsize=11)
    ax.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig(f'{output_dir}/search_performance.png', dpi=300, bbox_inches='tight')
    print(f"Saved: {output_dir}/search_performance.png")
    plt.close()

def plot_growth_rates(df, output_dir):
    """Plot growth rate analysis."""
    fig, ax = plt.subplots(figsize=(12, 7))
    
    # Calculate growth rates
    size_ratios = df['Size'].iloc[1:].values / df['Size'].iloc[:-1].values
    time_ratios = df['Total(ms)'].iloc[1:].values / df['Total(ms)'].iloc[:-1].values
    
    # Theoretical O(n log n) ratio
    theoretical_ratios = []
    for i in range(1, len(df)):
        n1, n2 = df['Size'].iloc[i-1], df['Size'].iloc[i]
        ratio = (n2 * np.log2(n2)) / (n1 * np.log2(n1))
        theoretical_ratios.append(ratio)
    
    x_pos = df['Size'].iloc[1:].values
    
    ax.plot(x_pos, time_ratios, 'o-', linewidth=2.5, markersize=8,
           label='Actual Growth Rate', color='#2E86AB')
    ax.plot(x_pos, theoretical_ratios, 's--', linewidth=2, markersize=6,
           label='Expected O(n log n) Growth', color='#C73E1D', alpha=0.6)
    ax.axhline(y=1, color='gray', linestyle=':', linewidth=1.5, alpha=0.5)
    
    ax.set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
    ax.set_ylabel('Growth Rate Factor', fontsize=12, fontweight='bold')
    ax.set_title('Empirical vs Theoretical Growth Rate Analysis', 
                fontsize=14, fontweight='bold')
    ax.legend(fontsize=11)
    ax.grid(True, alpha=0.3)
    
    plt.tight_layout()
    plt.savefig(f'{output_dir}/growth_rates.png', dpi=300, bbox_inches='tight')
    print(f"Saved: {output_dir}/growth_rates.png")
    plt.close()

def plot_component_breakdown(df, output_dir):
    """Plot stacked bar chart of time components."""
    fig, ax = plt.subplots(figsize=(14, 7))
    
    # Select subset for clarity
    step = max(1, len(df) // 10)
    df_subset = df.iloc[::step]
    
    x = np.arange(len(df_subset))
    width = 0.6
    
    ax.bar(x, df_subset['SA(ms)'], width, label='SA Construction', 
          color='#2E86AB', alpha=0.8)
    ax.bar(x, df_subset['LCP(ms)'], width, bottom=df_subset['SA(ms)'],
          label='LCP Construction', color='#F18F01', alpha=0.8)
    
    ax.set_xlabel('Input Size (n)', fontsize=12, fontweight='bold')
    ax.set_ylabel('Time (milliseconds)', fontsize=12, fontweight='bold')
    ax.set_title('Time Component Breakdown: SA vs LCP', 
                fontsize=14, fontweight='bold')
    ax.set_xticks(x)
    ax.set_xticklabels(df_subset['Size'], rotation=45)
    ax.legend(fontsize=11)
    ax.grid(True, alpha=0.3, axis='y')
    
    plt.tight_layout()
    plt.savefig(f'{output_dir}/component_breakdown.png', dpi=300, bbox_inches='tight')
    print(f"Saved: {output_dir}/component_breakdown.png")
    plt.close()

def main():
    # Paths
    csv_path = '/home/claude/suffix-array-project/docs/benchmark_results.csv'
    output_dir = '/home/claude/suffix-array-project/docs'
    
    # Check if benchmark data exists
    if not os.path.exists(csv_path):
        print(f"Error: Benchmark data not found at {csv_path}")
        print("Please run PerformanceBenchmark.java first to generate data.")
        return
    
    # Load data
    print("Loading benchmark data...")
    df = load_data(csv_path)
    
    # Generate all plots
    print("\nGenerating plots...")
    plot_overall_complexity(df, output_dir)
    plot_sa_vs_lcp(df, output_dir)
    plot_logarithmic_scale(df, output_dir)
    plot_search_performance(df, output_dir)
    plot_growth_rates(df, output_dir)
    plot_component_breakdown(df, output_dir)
    
    print("\n✅ All graphs generated successfully!")
    print(f"Graphs saved to: {output_dir}/")

if __name__ == '__main__':
    main()
