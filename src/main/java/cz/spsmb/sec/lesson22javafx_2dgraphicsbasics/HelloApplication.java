package cz.spsmb.sec.lesson22javafx_2dgraphicsbasics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    double clickedX;
    double clickedY;

    Player player;
    List<Enemy> enemies;
    Image backgroundImage = new Image(String.valueOf(getClass().getResource("/images/bg.jpg")));


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

        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            System.out.println("Clicked on " + mouseEvent.getX() + " " + mouseEvent.getY());
            direction = Direction.autoMovement;
            clickedX = mouseEvent.getX();
            clickedY = mouseEvent.getY();
        });


        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            onKeyPressed(event);
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
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
            //if (enemy.isInCollision(player)) {
            if (enemy.getX() < player.getX() + player.height && enemy.getX() + player.height > player.getX() &&
                    enemy.getY() < player.getY() + player.height && enemy.getY() + player.height > player.getY()) {
                i--;
                continue;
            }
            enemies.add(enemy);
        }

        AnimationTimer animationTimer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (now - lastTick > 1000000000l / speed) {
                    lastTick = now;
                    tick();
                }
            }
        };
        animationTimer.start();
    }

    private void onKeyPressed(KeyEvent event) {
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
    }

    private void tick() {
        if (stop) {
            return;
        }

        clearScreen();
        // Každé překreslení obrazovky
        Image currentPlayerTexture = player.getImages()[0];

        switch (direction) {
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
                stop = player.getX() > SCREEN_WIDTH - player.height;
                if (!stop) player.incrementX();
                currentPlayerTexture = player.getImages()[1];
                break;
            case autoMovement:
                stop = player.getX() > SCREEN_WIDTH - player.height;
                if (!stop) player.incrementX();
                player.moveToPosition(clickedX, clickedY);
        }

        detectCollisions(player, enemies);
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.setFont(new Font(20));
        graphicsContext.fillText(String.valueOf(player.getScore()), player.x + player.width / 2, player.y - 20);
        graphicsContext.drawImage(currentPlayerTexture, player.x, player.y, player.height, player.height);

        for (Enemy enemy : enemies) {
            graphicsContext.setFill(Color.RED);
            graphicsContext.fillText(String.valueOf(enemy.getScore()), enemy.x + enemy.width / 2, enemy.y - 10);
            graphicsContext.drawImage(enemy.getImage(), enemy.x, enemy.y, enemy.height, enemy.height);
        }
    }

    private void detectCollisions(Player player, List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (player.isInCollision(enemy)) {
                if (player.getScore() > enemy.getScore()) {
                    enemy.setX(random.nextInt(SCREEN_WIDTH));
                    enemy.setY(random.nextInt(SCREEN_HEIGHT));
                    player.setScore(player.getScore() + enemy.getScore());
                    enemy.setScore((Math.max(1, random.nextInt(player.getScore()))));
                    enemy.loadTextures();
                } else {
                    stop = true;
                }
            }
        }
    }

    private void clearScreen() {
        graphicsContext.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_WIDTH);
    }

    public static void main(String[] args) {
        launch();
    }
}