
import java.awt.Point;
import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConnectFive extends Application {
    Board b;
    Engine E;
    Engine E_2;
    final int width = 28;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Replace with path to Go Stone Image
        File black = new File("C:\\Users\\Billy\\Documents\\Black Go Stone.png");
        File white = new File("C:\\Users\\Billy\\Documents\\White Go Stone.png");
        Image blackStone = new Image(black.toURI().toString());
        Image whiteStone = new Image(white.toURI().toString());
        b = new Board(19, 5);
        E = new Engine(b, 1, 0, b.getWinningNum(), width);
        E_2 = new Engine(b, 0, 1, b.getWinningNum(), width);
        b.clearBoard();
        primaryStage.setTitle("Board");
        Group root = new Group();
        Canvas canvas = new Canvas(width * (b.getSize() + 2), width * (b.getSize() + 2));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (b.checkBoardState() == -1) {

                    double x = t.getSceneX();
                    double y = t.getSceneY();
                    Point P = new Point();
                    Point P_2 = new Point();
                    P.setLocation(x, y);
                    if (P.x >= width / 2 && P.y >= width / 2 && P.x <= width * (b.getSize() + .49)
                            && P.y <= width * (b.getSize() + .49)) {
                        P = getClosestLocation(P);
                        if (getCurrentColor(P) == -1) {
                            placeOnBoard(P, 0);
                            gc.drawImage(whiteStone, P.getX() - (double) width / 4, P.getY() - (double) width / 4,
                                    (double) width / 2, (double) width / 2);
                            if (b.checkBoardState() == -1) {
                                P_2 = E.evaluatedMove(1);
                                gc.drawImage(blackStone, P_2.getX() - (double) width / 4,
                                        P_2.getY() - (double) width / 4, (double) width / 2, (double) width / 2);
                            }
                        } else {
                            System.out.println("Trying to click on a " + getCurrentColor(P));
                        }
                    }
                } else {
                    System.out.println("Color " + b.checkBoardState() + " wins!");
                    primaryStage.setTitle("Color " + b.checkBoardState() + " wins!");
                }
            }
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Point getClosestLocation(Point input) {
        Point result = new Point();
        double resultX = 0;
        double resultY = 0;
        double x = input.getX();
        double y = input.getY();
        if (x / width - (int) (x / width) < 0.5)
            resultX = width * (int) (x / width);
        else
            resultX = width * ((int) (x / width) + 1);
        if (y / width - (int) (y / width) < 0.5)
            resultY = width * (int) (y / width);
        else
            resultY = width * ((int) (y / width) + 1);
        result.setLocation(resultX, resultY);
        return result;
    }

    private void placeOnBoard(Point input, int color) {
        int row = (int) (Math.round(input.getX() / width - 1));
        int column = (int) (Math.round(input.getY() / width - 1));
        b.placeStone(row, column, color, width);
    }

    private int getCurrentColor(Point input) {
        int row = (int) (Math.round(input.getX() / width - 1));
        int col = (int) (Math.round(input.getY() / width - 1));
        return b.getGrid()[row][col];
    }

    private int getNextColor(Point input) {
        int row = (int) (Math.round(input.getX() / width - 1));
        int col = (int) (Math.round(input.getY() / width - 1));
        int current = b.getGrid()[row][col];
        if (current == -1) {
            return 0;
        } else if (current == 0) {
            return 1;
        }
        return -1;
    }

    private void drawGrid(GraphicsContext gc) {

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for (int i = width; i <= width * b.getSize(); i += width) {
            gc.strokeLine(i, width, i, width * b.getSize());
            gc.strokeLine(width, i, width * b.getSize(), i);
        }
    }

    private void drawShapes(GraphicsContext gc) {
        drawGrid(gc);
    }

}
