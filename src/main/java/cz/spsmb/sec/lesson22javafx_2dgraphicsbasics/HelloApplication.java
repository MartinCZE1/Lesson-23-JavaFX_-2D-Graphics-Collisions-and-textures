package cz.spsmb.sec.lesson22javafx_2dgraphicsbasics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public boolean stop = false;
    public int speed = 10;

    Rectangle rectangle;


    Random random = new Random();
    GraphicsContext graphicsContext;
    Direction direction = Direction.down;

    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode key = event.getCode();
            stop = false;
            switch (key) {
                case W:
                    System.out.println("W");
                    direction = Direction.up;
                    break;
                case A:
                    System.out.println("A");
                    direction = Direction.left;
                    break;
                case S:
                    System.out.println("S");
                    direction = Direction.down;
                    break;
                case D:
                    System.out.println("D");
                    direction = Direction.right;
                    break;
                case SHIFT:
                    speed = 10000;
                    break;
            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()){
                case SHIFT -> {
                    speed = 10;
                }
            }
        });

        stage.setTitle("FX Demo");
        stage.setScene(scene);
        stage.show();

        rectangle = new Rectangle(random.nextInt(SCREEN_WIDTH), random.nextInt(SCREEN_HEIGHT));

        AnimationTimer animationTimer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (now - lastTick > 1000000000l/speed) {
                    lastTick = now;
                    tick();
                }
            }
        };
        animationTimer.start();
    }

    private void tick() {
        if (stop){
            return;
        }

        clearScreen();
        // Každé překreslení obrazovky
        switch (direction){
            case up:
                stop = rectangle.getY() < 0;
                if (!stop) rectangle.decrementY();
                break;
            case down:
                stop = rectangle.getY() > SCREEN_HEIGHT - rectangle.height;
                if (!stop) rectangle.incrementY();
                break;
            case left:
                stop = rectangle.getX() < 0;
                if (!stop) rectangle.decrementX();
                break;
            case right:
                stop = rectangle.getX() > SCREEN_HEIGHT - rectangle.height;
                if (!stop) rectangle.incrementX();
                break;
        }
        graphicsContext.setFill(Color.GREENYELLOW);
        graphicsContext.fillRect(rectangle.x, rectangle.y, rectangle.height, rectangle.height);


    }

    private void clearScreen() {

        graphicsContext.clearRect(0,0, SCREEN_WIDTH, SCREEN_WIDTH);
    }

    public static void main(String[] args) {
        launch();
    }
}