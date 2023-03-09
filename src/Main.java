import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Result multiplyMatrix(int[][] matrix1, int[][] matrix2) throws InterruptedException {
        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int columns2 = matrix2[0].length;
        if (columns1 != rows2) {
            throw new IllegalArgumentException("The number of columns of the first matrix must match the number of rows of the second matrix.");
        }
        int[][] result = new int[rows1][columns2];
        List<MatrixThread> threads = new ArrayList<>();
        for (int i = 0; i < rows1; i++) {
            final int row = i;
            MatrixThread thread = new MatrixThread(result, matrix1, matrix2, row);
            thread.start();
            threads.add(thread);
        }

        for (MatrixThread thread : threads) {
                thread.join();
        }

        return new Result(result);
    }

    public static Result multiplyMatrixFox(int[][] matrix1, int[][] matrix2, int blockSize) throws InterruptedException {
        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int columns2 = matrix2[0].length;
        if (columns1 != rows2) {
            throw new IllegalArgumentException("The number of columns of the first matrix must match the number of rows of the second matrix");
        }
        if (rows1 % blockSize != 0 || columns1 % blockSize != 0 || rows2 % blockSize != 0 || columns2 % blockSize != 0) {
            throw new IllegalArgumentException("The matrixes must be evenly divisible into blocks of size " + blockSize);
        }
        int numBlocks  = rows1 / blockSize; // number of blocks in each row/column
        int[][] result = new int[rows1][rows1]; // Result matrix C
        for (int i = 0; i < rows1; i++) { // Fill result matrix with 0
            for (int j = 0; j < rows1; j++) {
                result[i][j] = 0;
            }
        }

        int[][][][] blocks1 = splitMatrixIntoBlocks(matrix1, blockSize); // Matrix with blocks A
        int[][][][] blocks2 = splitMatrixIntoBlocks(matrix2, blockSize); // Matrix with blocks B
        int[][][][] cBlocks = splitMatrixIntoBlocks(result, blockSize); // Matrix with blocks C
        List<MatrixFoxThread> threads = new ArrayList<>();

        for (int i = 0; i < numBlocks; i++) {
            final int row = i;
            MatrixFoxThread thread = new MatrixFoxThread(cBlocks, blocks1, blocks2, row);

            threads.add(thread);
            thread.start();
        }

        for (MatrixFoxThread thread : threads) {
            thread.join();
        }

        convertTo2DArray(cBlocks, result);

        return new Result(result);
    }

    public static int[][][][] splitMatrixIntoBlocks(int[][] matrix, int blockSize) {
        int numBlocks = matrix.length / blockSize;
        int[][][][] blocks = new int[numBlocks][numBlocks][blockSize][blockSize];

        for (int i = 0; i < numBlocks; i++) {
            for (int j = 0; j < numBlocks; j++) {
                for (int x = 0; x < blockSize; x++) {
                    for (int y = 0; y < blockSize; y++) {
                        blocks[i][j][x][y] = matrix[i*blockSize+x][j*blockSize+y];
                    }
                }
            }
        }

        return blocks;
    }

    public static void convertTo2DArray(int[][][][] arr, int[][] result) {
        int size = arr.length;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int[][] subMatrix = arr[i][j];
                int subMatrixStartRow = i * size;
                int subMatrixStartCol = j * size;
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < size; l++) {
                        result[subMatrixStartRow + k][subMatrixStartCol + l] = subMatrix[k][l];
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        int[][] matrix1 = {
                {1,1,2,3},
                {1,2,1,1},
                {1,3,1,2},
                {2,1,1,3},
        };
        int[][] matrix2 = {
                {2,3,1,1},
                {1,1,1,1},
                {2,2,1,3},
                {3,1,1,1},
        };
        try {
//            Result result = multiplyMatrix(matrix1, matrix2);
            Result result = multiplyMatrixFox(matrix1, matrix2, 2);
            result.printResult();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}