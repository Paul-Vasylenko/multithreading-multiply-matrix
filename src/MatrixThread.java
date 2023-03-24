public class MatrixThread extends Thread {
    int[][] result;
    int[] row;
    int[] col;
    int rowIndex;
    int colIndex;
    public MatrixThread(int[][] result, int[] row, int[] col, int rowIndex, int colIndex) {
        this.result=result;
        this.row = row;
        this.col = col;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }
    @Override
    public void run() {
        int matrixACols = row.length;
        for (int k = 0; k < matrixACols; k++) {
            result[rowIndex][colIndex] += row[k] * col[k];
        }
    }
}
