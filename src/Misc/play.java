package Misc;

/**
 * Created by navot.dako on 7/4/2017.
 */
public class play {

    private static int rightBound;
    private static int bottomBound;
    private static int upBound;
    private static int leftBound;
    private static int[][] matrix;

    static int i = 0;
    static int j = 0;
    static int count = 0;

    public static void main(String[] args) {
        matrix = createMatrix(2, 4, 3);
        System.out.println();
        spiral(matrix);
        System.out.println();
        System.out.println("Done");
    }

    private static void spiral(int[][] matrix) {
        int columns = matrix[0].length;
        int rows = matrix.length;
        rightBound = columns;
        bottomBound = rows;
        upBound = 0;
        leftBound = 0;
        while (count < (columns * rows)) {
            go(1);
            go(2);
            go(3);
            go(4);
        }
    }

    private static void go(int state) {
        switch (state) {
            case 1: {
                for (i = leftBound; i < rightBound; i++) {
                    System.out.print(matrix[j][i] + ",");
                    count++;
                }
                upBound++;
                i--;
                break;
            }
            case 2: {
                for (j = upBound; j < bottomBound; j++) {
                    System.out.print(matrix[j][i] + ",");
                    count++;
                }
                rightBound--;
                j--;
                break;
            }
            case 3: {
                for (i = rightBound - 1; i >= leftBound; i--) {
                    System.out.print(matrix[j][i] + ",");
                    count++;
                }
                bottomBound--;
                i++;
                break;
            }
            case 4: {
                for (j = bottomBound - 1; j >= upBound; j--) {
                    System.out.print(matrix[j][i] + ",");
                    count++;
                }
                leftBound++;
                j++;
                break;
            }
        }
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
