/*----------------------------------------------------------------
 *
 * Author: Zhenyu Liu
 * Written: 6/17/2017
 *
 * Compilation: javac PercolationStats.java
 * Execution:   java PelcolationStats size trials
 *
 * Prints "mean:
 *         stddev:
 *         95% confidence interval"
 *
 ----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private Percolation matrix;
    // keep every independent trails
    private double[] threshold;

    private int numTrials;

    /**
     * Constructor: initial n * n size grid, and iterate number of trials
     * independent percolate test. Then keep all threshold result.
     * @param n: size of the Percolation grid
     * @param trials: number of trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("size of grid and trials must great than 0");

        numTrials = trials;
        threshold = new double[trials];

        // Count number of trials
        for (int i = 0; i < trials; i++) {
            matrix = new Percolation(n);


            while (!matrix.percolates()) {
                // Randomly choose row and col number to open
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                if (matrix.isOpen(row, col)) continue;

                matrix.open(row, col);
            }

            threshold[i] = matrix.numberOfOpenSites() / (double) (n * n);
        }


    }

    /**
     * Calculate mean
     * @return mean amount all threshold result
     */
    public double mean() { return StdStats.mean(threshold); }

    /**
     * Calculate stand division
     * @return stand division amount all threshold result
     */
    public double stddev() { return StdStats.stddev(threshold); }

    /**
     * Calculate low bound of confidence interval
     * @return low bound of confidence interval
     */
    public double confidenceLo() { return mean() - 1.96 * stddev() / Math.sqrt(numTrials); }

    /**
     * Calculate high bound of confidence interval
     * @return high bound of confidence interval
     */
    public double confidenceHi() { return mean() + 1.96 * stddev() / Math.sqrt(numTrials); }


    public static void main(String[] args) {

        int n =  StdIn.readInt();
        int trial = StdIn.readInt();
        PercolationStats stat = new PercolationStats(n, trial);

        StdOut.println("mean \t = " + stat.mean());
        StdOut.println("stddev \t = " + stat.stddev());
        StdOut.printf("95%% confidence interval \t = [%f, %f]",
                stat.confidenceLo(), stat.confidenceHi());
    }
}
