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

    public static void main(String[] args) {
        int[][] matrix1 = {
                {-1, 1},
                {-2, 0},
                {2, 3},
                {4, 5},
        };
        int[][] matrix2 = {
                {-4,-3,6,7},
                {8,-1,0,9}
        };
        try {
            Result result = multiplyMatrix(matrix1, matrix2);
            result.printResult();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}