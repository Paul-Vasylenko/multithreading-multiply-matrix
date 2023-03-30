public class MatrixFoxThread extends Thread {
    int[][][][] result;
    int[][] matrix1;
    int[][] matrix2;
    int row;
    int col;
    int blockSize;
    public MatrixFoxThread(int[][][][] result, int[][] m1, int[][] m2, int row, int col, int blockSize) {
        this.result=result;
        matrix1=m1;
        matrix2=m2;
        this.row = row;
        this.col = col;
        this.blockSize = blockSize;
    }

    @Override
    public void run() {
        int[][] subResult = multiplyMatrices(matrix1, matrix2);
        result[row][col] = addMatrices(result[row][col], subResult); // [ [0,0] [0,0] ] => [ [0,1] [0,0] ]; => [ [0,0] [0,1] ]
    }

    public int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        int rowsInFirst = firstMatrix.length;
        int columnsInFirst = firstMatrix[0].length; // same as rows in second matrix
        int columnsInSecond = secondMatrix[0].length;
        int[][] result = new int[rowsInFirst][columnsInSecond];

        for (int i = 0; i < rowsInFirst; i++) {
            for (int j = 0; j < columnsInSecond; j++) {
                for (int k = 0; k < columnsInFirst; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return result;
    }
    public int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
        int numBlocks  = matrix1.length; // number of blocks in each row/column
        int[][] result = new int[numBlocks][numBlocks];

        for (int i = 0; i < numBlocks; i++) {
            for (int j = 0; j < numBlocks; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }

        return result;
    }
}
