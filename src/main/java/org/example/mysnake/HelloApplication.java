package org.example.mysnake;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private static final int width = 640;
    private static final int height = 640;
    private static final int row = 16;
    private static final int column = 16;
    private static final int cellSize = height / column;
    private GraphicsContext gc;
    private List<Point> body = new ArrayList<>();
    private Point head;
    private Image img;
    private int x;
    private int y;
    private static final int right = 0;
    private static final int left = 1;
    private static final int up = 2;
    private static final int down = 3;
    private int moveFlag;
    Timeline timeline;
    private boolean gameOver = false;
    private int point = 0;

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Canvas c = new Canvas(width, height);
        root.getChildren().add(c);
        stage.setTitle("Snake");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        for (int i = 0; i < 3; i++) {
            body.add((new Point(5, row / 2)));
        }
        head = body.get(0);
        buildTarget();
        gc = c.getGraphicsContext2D();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.RIGHT && moveFlag != left)
                    moveFlag = right;
                else if (code == KeyCode.LEFT && moveFlag != right)
                    moveFlag = left;
                else if (code == KeyCode.UP && moveFlag != down)
                    moveFlag = up;
                else if (code == KeyCode.DOWN && moveFlag != up)
                    moveFlag = down;
            }
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(160), e -> runGame(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void windowBackground(GraphicsContext gc) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.YELLOW);
                } else {
                    gc.setFill(Color.YELLOWGREEN);
                }
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    private void buildTarget() {
        start:
        while (true) {
            x = (int) (Math.random() * row);
            y = (int) (Math.random() * column);

            for (Point snake : body) {
                if (snake.getX() == x && snake.getY() == y) {
                    continue start;
                }
            }
            img = new Image(getClass().getResource("/image/apple.png").toExternalForm());
            break;
        }
    }

    private void drawTarget(GraphicsContext gc) {
        gc.drawImage(img, x * cellSize, y * cellSize, cellSize, cellSize);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(head.getX() * cellSize, head.getY() * cellSize,
                cellSize, cellSize, 35, 35);
        for (int i = 1; i < body.size(); i++) {
            gc.fillRoundRect(body.get(i).getX() * cellSize, body.get(i).getY() * cellSize,
                    cellSize, cellSize, 20, 20);

        }
    }

    private void moveRight() {
        head.x++;
    }

    private void moveLeft() {
        head.x--;
    }

    private void moveUp() {
        head.y--;
    }

    private void moveDown() {
        head.y++;
    }

    private void eatTarget() {
        if (head.getX() == x && head.getY() == y) {
            AudioClip a = new AudioClip(this.getClass().getResource("/1.wav").toString());
            a.play();
            body.add(new Point(-1, -1));
            buildTarget();
            point += 5;
        }
    }

    private void gameOver_method() {
        if (head.x < 0 || head.y < 0 || head.x * cellSize >= width || head.y * cellSize >= height) {
            gameOver = true;
            point = 0;
        }
        for (int i = 1; i < body.size(); i++) {
            if (head.x == body.get(i).getX() && head.y == body.get(i).getY()) {
                gameOver = true;
                point = 0;
                break;
            }
        }
    }

    private void point_metho() {
        gc.setFill(Color.BLUE);
        gc.fillText("Point:" + point, 10, 33);
    }

    private void runGame(GraphicsContext gc) {

        windowBackground(gc);
        drawTarget(gc);
        drawSnake(gc);
        eatTarget();
        point_metho();

        if (gameOver) {
            AudioClip a = new AudioClip(this.getClass().getResource("/2.wav").toString());
            a.play();
            gc.setFont(new Font("Thahoma", 80));
            gc.setFill(Color.RED);
            gc.fillText("Gama Over", width / 4, height / 2);
            timeline.pause();
        }
        for (int i = body.size() - 1; i >= 1; i--) {
            body.get(i).x = body.get(i - 1).x;
            body.get(i).y = body.get(i - 1).y;
        }
        switch (moveFlag) {
            case right:
                moveRight();
                break;
            case left:
                moveLeft();
                break;
            case up:
                moveUp();
                break;
            case down:
                moveDown();
                break;
        }
        gameOver_method();
    }

}
