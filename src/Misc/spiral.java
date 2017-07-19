package Misc;

/**
 * Created by navot.dako on 7/4/2017.
 */
public class spiral {

    private static int rightBound;
    private static int bottomBound;
    private static int upBound;
    private static int leftBound;
    private static int[][] matrix;

    static int colIndex = 0;
    static int rowIndex = 0;
    static int count = 0;

    public static void main(String[] args) {
        matrix = createMatrix(2, 2, 3);
        System.out.println();
        spiral(matrix);
        System.out.println("\nDone");
    }

    private static void spiral(int[][] matrix) {
        int columns = matrix[0].length;
        int rows = matrix.length;
        rightBound = columns;
        bottomBound = rows;
        upBound = 0;
        leftBound = 0;
        while (count < (columns * rows)) {
            goAround();
        }
    }

    private static void goAround() {

        for (colIndex = leftBound; colIndex < rightBound; colIndex++) {
            System.out.print(matrix[rowIndex][colIndex] + ",");
            count++;
        }
        upBound++;
        colIndex--;
        for (rowIndex = upBound; rowIndex < bottomBound; rowIndex++) {
            System.out.print(matrix[rowIndex][colIndex] + ",");
            count++;
        }
        rightBound--;
        rowIndex--;
        for (colIndex = rightBound - 1; colIndex >= leftBound; colIndex--) {
            System.out.print(matrix[rowIndex][colIndex] + ",");
            count++;
        }
        bottomBound--;
        colIndex++;
        for (rowIndex = bottomBound - 1; rowIndex >= upBound; rowIndex--) {
            System.out.print(matrix[rowIndex][colIndex] + ",");
            count++;
        }
        leftBound++;
        rowIndex++;

    }

    private static int[][] createMatrix(int r, int c, int num) {
        int[][] matrix = new int[c][r];
        int temp = num;
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                matrix[i][j] = temp;
                temp += num;
            }
        }
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        return matrix;
    }
}
