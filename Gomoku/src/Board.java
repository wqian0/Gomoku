import java.awt.Point;

public class Board {
    private int size;
    private int stoneCount;
    private int winningNum;
    private int[][] grid;

    Board(int size, int winningNum) {
        stoneCount = 0;
        this.size = size;
        this.winningNum = winningNum;
        grid = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getWinningNum() {
        return winningNum;
    }

    public int getCount() {
        return stoneCount;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = -1;
            }
        }
    }

    public Point placeStone(int row, int column, int color, int width) {
        grid[row][column] = color;
        stoneCount++;
        return new Point(width * (row + 1), width * (column + 1));
    }

    public int checkBoardState() {
        int result = -1;
        int first;
        boolean vertWin = true;
        boolean horWin = true;
        boolean LTDiagWin = true;
        boolean RTDiagWin = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                first = grid[i][j];
                if (first != -1) {
                    for (int k = 0; k < winningNum; k++) {
                        if (j + k >= size || grid[i][j + k] != first) {
                            vertWin = false;
                        }
                        if (i + k >= size || grid[i + k][j] != first) {
                            horWin = false;
                        }
                    }
                    for (int k = 0; k < winningNum; k++) {
                        if (i + k >= size || j + k >= size || grid[i + k][j + k] != first) {
                            LTDiagWin = false;
                        }
                        if (i + k >= size || j - k < 0 || grid[i + k][j - k] != first) {
                            RTDiagWin = false;
                        }
                    }
                    if (vertWin || horWin || LTDiagWin || RTDiagWin) {
                        return first;
                    }
                    vertWin = true;
                    horWin = true;
                    LTDiagWin = true;
                    RTDiagWin = true;
                }
            }
        }
        return result;
    }
}
