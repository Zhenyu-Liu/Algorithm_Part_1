import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author Zhenyu Liu
 *
 * Date: 6/16/17
 */
public class Percolation {

    private WeightedQuickUnionUF siteMatrix;

    private int size;

    private boolean[] openStatus;

    private int numberOfOpenSite;


    private int xyToID (int row, int col){
        return (row - 1)*size + col;
    }

    private boolean isVaild(int row, int col){

        if (row <= 0 || row > size) return false;
        if (col <= 0 || col > size) return false;

        int idx = xyToID(row, col);
        return idx >= 1 && idx <= size * size;
    }

    // check the open status around this index connect the union find object if found
    private void merge(int idx){

        // Adjacent site index
        int top = idx - size;
        int bottom = idx + size;
        int left = idx - 1;
        int right = idx + 1;

        // union adjacent site if there is a open
        if (top > 0  && openStatus[top]) siteMatrix.union(idx, top);
        if (bottom <= size*size && openStatus[bottom]) siteMatrix.union(idx, bottom);
        if ((idx - 1) % size != 0 && openStatus[left]) siteMatrix.union(idx, left);
        if (idx % size != 0 && openStatus[right]) siteMatrix.union(idx, right);

    }

    public Percolation (int n){

        /*
         * create 1-dimensional union find object [1, n^2],
         * with two visual-site 0 and n^2 + 1
        */
        siteMatrix = new WeightedQuickUnionUF(n * n + 2);
        size = n;
        numberOfOpenSite = 0;
        // Align with the index of UF object [1, n*n]
        openStatus = new boolean[n * n + 1];

        // connect top & bottom visual site with top and bottom row

        // top row
        for (int i = 1; i <= n; i++){
            siteMatrix.union(0, i);
        }

        // bottom row
        for (int i = xyToID(size, 1); i <= n * n; i++){
            siteMatrix.union(n * n + 1, i);
        }

    }

    public void open(int row, int col){

        int idx = xyToID(row, col);

        if (!isVaild(row, col)) throw new IndexOutOfBoundsException("index out of bound");

        if (!openStatus[idx]){
            openStatus[idx] = true;
            numberOfOpenSite++;
            merge(idx);
        }
    }


    public boolean isOpen(int row, int col){

        if (!isVaild(row, col))
            throw new IndexOutOfBoundsException("index out of bound");

        return openStatus[xyToID(row, col)];
    }
    /*
     * A full site is an open site that can be connected to an open site in the top row via a
     * chain of neighboring (left, right, up, down) open sites. If there is a full site in
     * the bottom row, then we say that the system percolates.
     */
    public boolean isFull(int row, int col){

        if (!isVaild(row, col))
            throw new IndexOutOfBoundsException("index out of bound");

        if ((row == 1 || row == size) && !isOpen(row, col)) {
            return false;
        }

        return siteMatrix.connected(0, xyToID(row, col));
    }

    public int numberOfOpenSites(){

        return numberOfOpenSite;
    }

    public boolean percolates(){
        return siteMatrix.connected(0, size * size + 1);
    }

    public static void main(String[] args){

        Percolation A = new Percolation(4);

        A.open(1, 4);
        A.open(2, 4);
        A.open(2, 3);
        A.open(3,3);
        // A.open(4, 3);

        // System.out.println(A.openStatus.length);

        System.out.println(A.numberOfOpenSite);
        // System.out.println(A.siteMatrix.connected(17, 15));
    }

}
