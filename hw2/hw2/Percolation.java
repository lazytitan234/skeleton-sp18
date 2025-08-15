package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size; //value of N
    private int numOpenSites = 0; //number of open sites
    private boolean[][] grid; //used to check for openess
    private WeightedQuickUnionUF percolationSet; //used for percolation
    private WeightedQuickUnionUF fullnessSet; //used for fullness checks

    //percolationSet contains 2 extra sites (virtual top and bottom, at index N and N+1 respectively).
    //all opened top sites are connected to VT. all opened bottom sites are connected to VB.
    //as we open sites we check if its 4 neighbours are open, and if they are, merge their sets
    //at the end of the day check if VT and VB are in the same set
    //fullnessSet contains 1 extra site (virtual top). same logic as percolationSet.

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N must be greater than 0");
        }
        size = N;
        grid = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }
        percolationSet = new WeightedQuickUnionUF(N * N + 2);
        fullnessSet = new WeightedQuickUnionUF(N * N + 1);
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row > size - 1 || col > size - 1) {
            throw new java.lang.IndexOutOfBoundsException("Index must be between 0 and size -1 inclusive");
        }
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            numOpenSites++;
            if (row == 0) {
                percolationSet.union(xyToInt(row, col), size * size);
                fullnessSet.union(xyToInt(row, col), size * size);
                if (isOpen(row + 1, col)) {
                    percolationSet.union(xyToInt(row, col), xyToInt(row + 1, col));
                    fullnessSet.union(xyToInt(row, col), xyToInt(row + 1, col));
                }
            } else if (row == size - 1) {
                percolationSet.union(xyToInt(row, col), size * size + 1);
                if (isOpen(row - 1, col)) {
                    percolationSet.union(xyToInt(row, col), xyToInt(row - 1, col));
                    fullnessSet.union(xyToInt(row, col), xyToInt(row - 1, col));
                }
                if (col != size - 1) {
                    fullnessSet.union(xyToInt(row, col), xyToInt(row, col + 1));
                }
                if (col != 0) {
                    fullnessSet.union(xyToInt(row, col), xyToInt(row, col - 1));
                }
            } else {
                if (isOpen(row - 1, col)) {
                    percolationSet.union(xyToInt(row, col), xyToInt(row - 1, col));
                    fullnessSet.union(xyToInt(row, col), xyToInt(row - 1, col));
                }
                if (col != size - 1 && isOpen(row, col + 1)) {
                    percolationSet.union(xyToInt(row, col), xyToInt(row, col + 1));
                    fullnessSet.union(xyToInt(row, col), xyToInt(row, col + 1));
                }
                if (isOpen(row + 1, col)) {
                    percolationSet.union(xyToInt(row, col), xyToInt(row + 1, col));
                    fullnessSet.union(xyToInt(row, col), xyToInt(row + 1, col));
                }
                if (col != 0 && isOpen(row, col - 1)) {
                    percolationSet.union(xyToInt(row, col), xyToInt(row, col - 1));
                    fullnessSet.union(xyToInt(row, col), xyToInt(row, col - 1));
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row > size - 1 || col > size - 1) {
            throw new java.lang.IndexOutOfBoundsException("Index must be between 0 and size -1 inclusive");
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row > size - 1 || col > size - 1) {
            throw new java.lang.IndexOutOfBoundsException("Index must be between 0 and size -1 inclusive");
        }
        if (fullnessSet.find(xyToInt(row, col)) == fullnessSet.find(size * size)) {
            return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        //takes constant time.
        if (percolationSet.find(size * size) == percolationSet.find(size * size + 1)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Percolation system = new Percolation(10);
        boolean result = system.percolates();
        System.out.println(result);
    }

    private int xyToInt (int row, int col) {
        int value = row * size + col;
        return value;
    }
}
