public class Result {
    private int[][] result;

    public Result(int[][] result) {
        this.result = result;
    }

    public int[][] getResult() {
        return result;
    }

    public void printResult() {
        for (int[] row : result) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}
