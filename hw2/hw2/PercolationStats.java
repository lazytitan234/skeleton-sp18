package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private int size; //size refers to N
    private int numExp; //value of T
    private PercolationFactory creator;
    private double[] fractions;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        size = N;
        numExp = T;
        creator = pf;
        fractions = new double[numExp];
        for (int i = 0; i < numExp; i++) {
            Percolation exp = creator.make(size);
            while (!exp.percolates()) {
                int siteNum = StdRandom.uniform(size * size);
                Converter result = intToXY(siteNum);
                exp.open(result.row, result.col);
            }
            fractions[i] = (double) exp.numberOfOpenSites() / (size * size);
        }
    }

    public double mean() {
        return StdStats.mean(fractions);
    }

    public double stddev() {
        return StdStats.stddev(fractions);
    }

    public double confidenceLow() {
        double mean = mean();
        double sd = stddev();
        double denom = Math.pow(numExp, 0.5);
        return mean - (1.96 * sd / denom);
    }

    public double confidenceHigh() {
        double mean = mean();
        double sd = stddev();
        double denom = Math.pow(numExp, 0.5);
        return mean + (1.96 * sd / denom);
    }

    private class Converter {
        int row;
        int col;

        private Converter(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private Converter intToXY(int integer) {
        int row = integer / size;
        int col = integer % size;
        return new Converter(row, col);
    }
}
