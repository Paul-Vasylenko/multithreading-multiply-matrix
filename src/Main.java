import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        int[][] transposeMatrix2 = transpose(matrix2);
        List<MatrixThread> threads = new ArrayList<>();
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                MatrixThread thread = new MatrixThread(result, matrix1[i], transposeMatrix2[j], i, j);
                thread.start();
                threads.add(thread);
            }
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
            MatrixFoxThread thread = new MatrixFoxThread(cBlocks, blocks1, blocks2, row, blockSize);

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

    private static int[][] transpose(int[][] matrix){
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[cols][rows];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }
    public static void convertTo2DArray(int[][][][] arr, int[][] result) {
        int subMatrixSize = arr[0][0].length;
        int numSubMatrices = arr.length;
        int numRows = numSubMatrices * subMatrixSize;
        for (int i = 0; i < numSubMatrices; i++) {
            for (int j = 0; j < numSubMatrices; j++) {
                int[][] subMatrix = arr[i][j];
                int subMatrixStartRow = i * subMatrixSize;
                int subMatrixStartCol = j * subMatrixSize;
                for (int k = 0; k < subMatrixSize; k++) {
                    for (int l = 0; l < subMatrixSize; l++) {
                        result[subMatrixStartRow + k][subMatrixStartCol + l] = subMatrix[k][l];
                    }
                }
            }
        }
    }
    public static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(5) + 1;
            }
        }
        return matrix;
    }
    public static void main(String[] args) {
        int SIZE = 4;
        int[][] matrix1 = generateMatrix(SIZE,SIZE);
        int[][] matrix2 = generateMatrix(SIZE,SIZE);
        try {
//            long startSimple = System.currentTimeMillis();
            Result res = multiplyMatrix(matrix1, matrix2);
            res.printResult();
//            long endSimple = System.currentTimeMillis();

//            long startFox = System.currentTimeMillis();
//            Result res = multiplyMatrixFox(matrix1, matrix2, 2);
//            res.printResult();
//            long endFox = System.currentTimeMillis();

//            long elapsedSimple = endSimple - startSimple;
//            long elapsedFox = endFox - startFox;

//            System.out.println("Simple algorithm took " + elapsedSimple + " ms");
//            System.out.println("Fox algorithm took " + elapsedFox + " ms");
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}