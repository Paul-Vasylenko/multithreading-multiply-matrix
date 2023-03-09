public class MatrixFoxThread extends Thread {
    int[][][][] result;
    int[][][][] matrix1;
    int[][][][] matrix2;
    int row;
    public MatrixFoxThread(int[][][][] result, int[][][][] m1, int[][][][] m2, int row) {
        this.result=result;
        matrix1=m1;
        matrix2=m2;
        this.row = row;
    }

    @Override
    public void run() {
        int cols1 = matrix1[0].length;
        int cols2 = matrix2[0].length;

        for(int j = 0; j < cols2; j++) {
            int[][] blockResult = new int[cols2][cols2];
            for (int k = 0; k < cols1; k++) {
                int[][] sub1 = matrix1[row][k];
                int[][] sub2 = matrix2[k][j];

                int[][] subResult = multiplyMatrices(sub1, sub2);
                blockResult = addMatrices(blockResult, subResult);
            }
            result[row][j] = blockResult;
        }
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

    public static int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }

        return result;
    }
}
