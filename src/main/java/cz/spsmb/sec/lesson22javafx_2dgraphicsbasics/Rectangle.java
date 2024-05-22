package cz.spsmb.sec.lesson22javafx_2dgraphicsbasics;

public class Rectangle  {
    int x;
    int y;
    int height;

    public Rectangle(int x, int y) {
        this.x = x;
        this.y = y;
        this.height = 25;
    }

    public Rectangle(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void incrementX(){
        x++;
    }
    public void decrementX(){
        x--;
    }

    public void incrementY(){
        y++;
    }

    public void decrementY(){
        y--;
    }
}
