package cz.spsmb.sec.lesson22javafx_2dgraphicsbasics;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.List;
import java.util.Random;

public class Enemy extends Rectangle {
    public static final List<String> TEXTURES = List.of("monster1.png", "monster2.png", "monster3.png");

    int score;
    Image image;

    Random random = new Random();

    public Enemy(int x, int y) {
        super(x, y);
        loadTextures();
    }

    public void loadTextures() {
        URL url = getClass().getResource("/images/" + TEXTURES.get(random.nextInt(TEXTURES.size())));
        if (url != null) {
            image = new Image(url.toString());
            setWidth((int) image.getWidth()/2);
            setHeight((int) image.getHeight()/2);
        } else {
            System.out.println("Texture could not be loaded.");
        }
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Image getImage() {
        return image;
    }
}
