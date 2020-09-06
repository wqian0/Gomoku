import java.awt.Point;
import java.util.ArrayList;

public class Engine {
    private Board b;
    private int color;
    private int oppColor;
    private int width;
    private int winningNum;
    private int[][] grid;

    public Engine(Board b, int color, int oppColor, int winningNum, int width) {
        this.b = b;
        this.width = width;
        this.color = color;
        this.oppColor = oppColor;
        this.winningNum = winningNum;
        grid = b.getGrid();
    }

    public Point monteCarloMove() {
        int row = 0;
        int column = 0;
        while (true) {
            row = (int) (Math.random() * (b.getSize()));
            column = (int) (Math.random() * (b.getSize()));
            if (grid[row][column] == -1 && (row >= winningNum && row <= b.getSize() - 
                    winningNum && column >= winningNum
                    && column <= b.getSize() - winningNum)) {
                return b.placeStone(row, column, color, width);
            }
        }
    }

    public double evaluate(int row, int column) {
        if (row < 0 || row >= b.getSize() || column < 0 || column >= b.getSize()) {
            return -10000;
        }
        if (grid[row][column] == -1 && row > 0 && column > 0 && 
                row < b.getSize() - 1 && column < b.getSize() - 1) {
            int count = 0;
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = column - 1; j <= column + 1; j++) {
                    if (grid[i][j] == color && (i != row || column != j)) {
                        count++;
                    }
                }
            }
            return count;
        }
        return -10000;
    }

    public double chainEvaluate(int row, int column, int colorUsed) {
        int count = 0;
        int subCount_L = 0;
        int subCount_R = 0;
        int subCount_U = 0;
        int subCount_D = 0;
        int subCount_NE = 0;
        int subCount_SW = 0;
        int subCount_NW = 0;
        int subCount_SE = 0;
        int r = row;
        int c = column;
        r--;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_L++;
            r--;
        }
        r = row;
        c = column;
        r++;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_R++;
            r++;
        }
        if (subCount_L + subCount_R >= winningNum - 1) {
            return Integer.MAX_VALUE;
        } else if (((row - subCount_L - 1 >= 0 && row + subCount_R + 1 < b.getSize())
                && (subCount_L + subCount_R == winningNum - 2 && 
                grid[row - subCount_L - 1][column] == -1
                        && grid[row + subCount_R + 1][column] == -1))) {
            count += 200;
        } else if (((row - subCount_L - 1 < 0 || grid[row - subCount_L - 1][column] == oppColor)
                && (row + subCount_R + 1 >= b.getSize() || 
                grid[row + subCount_R + 1][column] == oppColor))) {
            subCount_L = 0;
            subCount_R = 0;
        } else {
            count += subCount_L + subCount_R;
        }
        r = row;
        c = column;
        c--;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_D++;
            c--;
        }
        r = row;
        c = column;
        c++;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_U++;
            c++;
        }
        if (subCount_D + subCount_U >= winningNum - 1) {  // 2-way unstoppable threat
            return Integer.MAX_VALUE;
        } else if (((column - subCount_D - 1 >= 0 && column + subCount_U + 1 < b.getSize())
                && (subCount_D + subCount_U == winningNum - 2 && 
                grid[row][column - subCount_D - 1] == -1
                        && grid[row][column + subCount_U + 1] == -1))) {
            count += 200;
        } else if (((column - subCount_D - 1 < 0 || grid[row][column - subCount_D - 1] == oppColor)
                && (column + subCount_U + 1 >= b.getSize() || 
                grid[row][column + subCount_U + 1] == oppColor))) {
            subCount_D = 0;
            subCount_U = 0;
        } else {
            count += subCount_D + subCount_U;
        }

        r = row;
        c = column;
        r--;
        c--;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_NW++;
            c--;
            r--;
        }
        r = row;
        c = column;
        r++;
        c++;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_SE++;
            c++;
            r++;
        }
        if (subCount_NW + subCount_SE >= winningNum - 1) {
            return Integer.MAX_VALUE;
        } else if (((row - subCount_NW - 1 >= 0 && column - subCount_NW - 1 >= 0 
                && row + subCount_SE + 1 < b.getSize()
                && column + subCount_SE + 1 < b.getSize())
                && (subCount_NW + subCount_SE == winningNum - 2
                        && grid[row - subCount_NW - 1][column - subCount_NW - 1] == -1
                        && grid[row + subCount_SE + 1][column + subCount_SE + 1] == -1))) {
            count += 200;
        } else if (((row - subCount_NW - 1 < 0 || column - subCount_NW - 1 < 0
                || grid[row - subCount_NW - 1][column - subCount_NW - 1] == oppColor)
                && (row + subCount_SE + 1 >= b.getSize() || column + subCount_SE + 1 >= b.getSize()
                        || grid[row + subCount_SE + 1][column + subCount_SE + 1] == oppColor))) {
            subCount_NW = 0;
            subCount_SE = 0;
        } else {
            count += subCount_NW + subCount_SE;
        }
        r = row;
        c = column;
        r--;
        c++;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_NE++;
            r--;
            c++;
        }
        r = row;
        c = column;
        r++;
        c--;
        while (r >= 0 && c >= 0 && r < b.getSize() && c < b.getSize() && grid[r][c] == colorUsed) {
            subCount_SW++;
            r++;
            c--;
        }
        if (subCount_NE + subCount_SW >= winningNum - 1) {
            return Integer.MAX_VALUE;
        } else if (((row - subCount_NE - 1 >= 0 && 
                column + subCount_NE + 1 < b.getSize()
                && row + subCount_SW + 1 < b.getSize() && column - subCount_SW - 1 >= 0)
                && (subCount_NE + subCount_SW == winningNum - 2
                        && grid[row - subCount_NE - 1][column + subCount_NE + 1] == -1
                        && grid[row + subCount_SW + 1][column - subCount_SW - 1] == -1))) {
            count += 200;
        } else if (((row - subCount_NE - 1 < 0 || column + subCount_NE + 1 >= b.getSize()
                || grid[row - subCount_NE - 1][column + subCount_NE + 1] == oppColor)
                && (row + subCount_SW + 1 >= b.getSize() || column - subCount_SW - 1 < 0
                        || grid[row + subCount_SW + 1][column - subCount_SW - 1] == oppColor))) {
            subCount_NE = 0;
            subCount_SW = 0;
        } else {
            count += subCount_NE + subCount_SW;
        }
        return count;
    }

    public double[] getBestMove(int[][] input, int color, int oppColor, double aggressiveness) {
        double[] result = new double[3]; // index 0-high score, index 1-row, index 2-col
        double highestScore = Integer.MIN_VALUE;
        double tempScore = Integer.MIN_VALUE;
        double highestTiebreak = Integer.MIN_VALUE;
        ArrayList<double[]> ties = new ArrayList<>();
        ArrayList<double[]> secondTies = new ArrayList<>();
        for (int r = 0; r < b.getSize(); r++) {
            for (int c = 0; c < b.getSize(); c++) {
                if (input[r][c] == -1) {
                    tempScore = chainEvaluate(r, c, color) * aggressiveness
                            + chainEvaluate(r, c, oppColor) * (1 - aggressiveness);
                    if (tempScore > highestScore) {
                        highestScore = tempScore;
                        ties = new ArrayList<>();
                        ties.add(new double[] { tempScore, r, c });
                    } else if (tempScore == highestScore) {
                        ties.add(new double[] { tempScore, r, c });
                    }
                }
            }
        }
        for (double[] array : ties) {
            if (chainEvaluate((int) array[1], (int) array[2], oppColor) > highestTiebreak) {
                highestTiebreak = chainEvaluate((int) array[1], (int) array[2], oppColor);
                secondTies = new ArrayList<>();
                secondTies.add(array);
            } else if (chainEvaluate((int) array[1], (int) array[2], oppColor) == highestTiebreak) {
                secondTies.add(array);
            }
        }
        result = secondTies.get((int) (Math.random() * secondTies.size()));
        result[0] = highestScore;
        return result;
    }

    public double getCumulativeScore(int[][] input, int color, int oppColor, int turnColor, double aggressiveness) {
        if (turnColor == color) {
            return getBestMove(input, color, oppColor, aggressiveness)[0]
                    - getBestMove(input, oppColor, color, aggressiveness)[0] + 1;
        } else {
            return getBestMove(input, color, oppColor, aggressiveness)[0]
                    - getBestMove(input, oppColor, color, aggressiveness)[0] - 1;
        }
    }

    public int[][] getHypotheticalBoard(int[][] input, int row, int column, int color) {
        int[][] result = new int[b.getSize()][b.getSize()];
        result = input.clone();
        result[row][column] = color;
        return result;
    }

    public Point evaluatedMove(double aggressiveness) {
        double[] bestMove = getBestMove(grid, color, oppColor, aggressiveness);

        double[] oppBestMove = getBestMove(grid, oppColor, color, aggressiveness);
        if (oppBestMove[0] > bestMove[0]) {
            return b.placeStone((int) oppBestMove[1], (int) oppBestMove[2], color, width);
        } else {
            return b.placeStone((int) bestMove[1], (int) bestMove[2], color, width);
        }

    }

}
