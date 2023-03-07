public class MatrixThread extends Thread {
    int[][] result;
    int[][] matrix1;
    int[][] matrix2;
    int row;

    public MatrixThread(int[][] result, int[][] m1, int[][] m2, int row) {
        this.result=result;
        matrix1=m1;
        matrix2=m2;
        this.row = row;
    }

    @Override
    public void run() {
        System.out.println("Thread started for row " + row + " and number of thread is " + currentThread().getName());
        int cols1 = matrix1[0].length;
        int cols2 = matrix2[0].length;

        for (int j = 0; j < cols2; j++) {
            for (int k = 0; k < cols1; k++) {
                result[row][j] += matrix1[row][k] * matrix2[k][j];
            }
        }


    }
}
