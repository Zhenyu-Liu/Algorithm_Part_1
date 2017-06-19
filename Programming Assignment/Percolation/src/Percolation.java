/*--------------------------------------------------------------------
 * Author Zhenyu Liu
 * Date: 6/16/17
 *
 * Purpose: Learn to use just WeightedQuickUnionUF and monte carlo to
 * solve Percolation problem. As for this Percolation class we construct
 * a n * n grid and implement several essential methods to simulate the
 * situation.
 *
 * Compilation: javac Percolation.java
 * Test via PercolationVisualizer
 * Execution: PercolationVisualizer input20.txt
 * The result will shows on the java application
 --------------------------------------------------------------------*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private WeightedQuickUnionUF mainGrid, grid; // Use two UF object for solve backwash

  private int size; // size of grid

  private boolean[] openStatus; // true if site is open

  private int numberOfOpenSite; // track number of opened site

  /**
   * Constructor: Initial two grid mainGrid will contain both top
   * and bottom visual site, and grid will only have top visual site
   *
   * @param n size of the grid
   */
  public Percolation(int n) {

    if (n <= 0) {
      throw new IllegalArgumentException("size of grid need greater than 0");
    }

    mainGrid = new WeightedQuickUnionUF(n * n + 2);
    grid = new WeightedQuickUnionUF(n * n + 1);
    size = n;
    numberOfOpenSite = 0;
    openStatus = new boolean[n * n + 1];
  }

  /**
   * Convert 2D matrix into 1D number
   *
   * @param row: interval [1, size]
   * @param col interval [1, size]
   * @return the index that in UF object and openSite array
   */
  private int xyToID(int row, int col) {

    return (row - 1) * size + col;
  }

  /**
   * Check if input row and col number is in the size of grid
   *
   * @param row row number
   * @param col col number
   * @return true if row and col number is within the size of grid (size * size)
   */
  private boolean isVaild(int row, int col) {

    if (row <= 0 || row > size) {
      return false;
    }
    if (col <= 0 || col > size) {
      return false;
    }

    int idx = xyToID(row, col);    // Index in UF object
    return idx >= 1 && idx <= size * size;
  }

  /**
   * Union all adjacent open site for current open site
   *
   * @param idx index of current site in UF object
   */
  private void merge(int idx) {

    // Adjacent site index
    int top = idx - size;
    int bottom = idx + size;
    int left = idx - 1;
    int right = idx + 1;

    // union adjacent site if there is a open
    if (top > 0 && openStatus[top]) {
      mainGrid.union(idx, top);
      grid.union(idx, top);
    }
    if (bottom <= size * size && openStatus[bottom]) {
      mainGrid.union(idx, bottom);
      grid.union(idx, bottom);
    }
    if ((idx - 1) % size != 0 && openStatus[left]) {
      mainGrid.union(idx, left);
      grid.union(idx, left);
    }
    if (idx % size != 0 && openStatus[right]) {
      mainGrid.union(idx, right);
      grid.union(idx, right);
    }

  }

  /**
   * Open current site and union this site with all adjacent site.
   * handle special case for top and bottom row
   * For the grid UF object, does not have bottom visual site
   *
   * @param row row number
   * @param col col number
   */
  public void open(int row, int col) {

    int idx = xyToID(row, col);

    if (!isVaild(row, col)) {
      throw new IndexOutOfBoundsException("index out of bound");
    }

    if (!openStatus[idx]) {
      openStatus[idx] = true;
      numberOfOpenSite++;
      merge(idx);

      if (row == 1) {
        mainGrid.union(idx, 0);
        grid.union(idx, 0);
      }

      if (row == size) {
        mainGrid.union(idx, size * size + 1);
      }
    }
  }

  /**
   * Check current site is open
   *
   * @param row row number
   * @param col col number
   * @return true if site is open
   */
  public boolean isOpen(int row, int col) {

    if (!isVaild(row, col)) {
      throw new IndexOutOfBoundsException("index out of bound");
    }

    return openStatus[xyToID(row, col)];
  }

    /*----------------------------------------------------------------------------------------
     * A full site is an open site that can be connected to an open site in the top row via a
     * chain of neighboring (left, right, up, down) open sites. If there is a full site in
     * the bottom row, then we say that the system percolates.
     ---------------------------------------------------------------------------------------*/

  /**
   * Check if current site is connected with the top visual site
   *
   * @param row row number
   * @param col col number
   * @return true if current site has connection with top visual site
   */
  public boolean isFull(int row, int col) {

    if (!isVaild(row, col)) {
      throw new IndexOutOfBoundsException("index out of bound");
    }

    int idx = xyToID(row, col);

        /* Key to avoid backwash, check both mainGrid (contain both top&bottom visual site)
         * and grid (only contain the top) if they both have connection to the top, then it
         * is full. Because in the grid, last row site does not have connection with bottom
         * visual site
         */
    return mainGrid.connected(0, idx) && grid.connected(0, idx);
  }

  /**
   * Keep track the number of opened sites in the mainGrid
   *
   * @return number of opened sites
   */
  public int numberOfOpenSites() {
    return numberOfOpenSite;
  }

  /**
   * Check the if the grid is percolate
   *
   * @return true if the grid is percolate
   */
  public boolean percolates() {
    return mainGrid.connected(0, size * size + 1);
  }

}
