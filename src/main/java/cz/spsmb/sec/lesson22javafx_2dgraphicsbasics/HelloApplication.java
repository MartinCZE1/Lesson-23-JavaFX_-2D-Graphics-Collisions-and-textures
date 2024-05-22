package cz.spsmb.sec.lesson22javafx_2dgraphicsbasics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    public static final int SCREEN_WIDTH = 1900;
    public static final int SCREEN_HEIGHT = 920;
    public boolean stop = false;
    public int speed = 10;

    Player player;
    List<Enemy> enemies;


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

        player = new Player(random.nextInt(SCREEN_WIDTH), random.nextInt(SCREEN_HEIGHT));
        enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Enemy enemy = new Enemy(random.nextInt(SCREEN_WIDTH), random.nextInt(SCREEN_HEIGHT));
            enemies.add(enemy);
        }

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
        Image currentPlayerTexture = player.getImages()[0];

        switch (direction){
            case up:
                stop = player.getY() < 0;
                if (!stop) player.decrementY();
                break;
            case down:
                stop = player.getY() > SCREEN_HEIGHT - player.height;
                if (!stop) player.incrementY();
                break;
            case left:
                stop = player.getX() < 0;
                if (!stop) player.decrementX();
                currentPlayerTexture = player.getImages()[0];
                break;
            case right:
                stop = player.getX() > SCREEN_HEIGHT - player.height;
                if (!stop) player.incrementX();
                currentPlayerTexture = player.getImages()[1];
                break;
        }

        detectCollisions(player, enemies);
        graphicsContext.setFill(Color.GREENYELLOW);
        graphicsContext.drawImage(currentPlayerTexture, player.x, player.y, player.height, player.height);

        for (Enemy enemy: enemies) {
            graphicsContext.drawImage(enemy.getImage(), enemy.x, enemy.y, enemy.height, enemy.height);
        }
    }

    private void detectCollisions(Player player, List<Enemy> enemies) {
        for (Enemy enemy: enemies) {
            if (player.isInCollision(enemy)){
                enemy.setX(random.nextInt(SCREEN_WIDTH));
                enemy.setY(random.nextInt(SCREEN_HEIGHT));
                enemy.loadTextures();
            }
        }
    }

    private void clearScreen() {

        graphicsContext.clearRect(0,0, SCREEN_WIDTH, SCREEN_WIDTH);
    }

    public static void main(String[] args) {
        launch();
    }
}