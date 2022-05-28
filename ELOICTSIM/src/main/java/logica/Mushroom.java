package logica;

public class Mushroom {
    int x;
    int y;
    int startx = 0;
    int starty = 0;
    int radius = 8;
    boolean collideLeft = false;
    boolean collideUp = false;
    boolean goingLeft = false;
    boolean goingUp = true;
    boolean collideDown = false;
    boolean collideRight = false;
    boolean gameover = false;
    boolean dead = false;
    boolean repaint = false;
    Thread thread;

    public Mushroom(){
        this.x = 0;
        this.y = 0;
    }

    public Mushroom(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Mushroom(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.startx = x;
        this.starty = y;
        this.radius = radius;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getRadius(){
        return this.radius;
    }

    public boolean isGameover() {
        return gameover;
    }

    public boolean isDead(){
        return dead;
    }

    public boolean isRepaint(){
        return this.repaint;
    }

    public boolean isCollideLeft() {
        return collideLeft;
    }

    public boolean isCollideRight() {
        return collideRight;
    }

    public boolean isCollideUp() {
        return collideUp;
    }

    public boolean isCollideDown() {
        return collideDown;
    }

    public boolean isGoingLeft() {
        return goingLeft;
    }

    public boolean isGoingUp() {
        return goingUp;
    }


    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public void setRepaint(boolean repaint){
        this.repaint = repaint;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public void setDead(boolean dead){
        this.dead = dead;
    }

    public void setCollideLeft(boolean collideLeft) {
        this.collideLeft = collideLeft;
    }

    public void setCollideRight(boolean collideRight) {
        this.collideRight = collideRight;
    }

    public void setCollideUp(boolean collideUp) {
        this.collideUp = collideUp;
    }

    public void setCollideDown(boolean collideDown) {
        this.collideDown = collideDown;
    }

    public void setGoingLeft(boolean goingLeft) {
        this.goingLeft = goingLeft;
    }

    public void setGoingUp(boolean goingUp) {
        this.goingUp = goingUp;
    }

    public void createThread(){
        if (thread != null)
            return;
        thread = new Thread((() -> {
            boolean first = true;
            while (true){
                if (dead){
                    setX(5000);
                    setY(0);
                } else {
                    if (!first) {
                        setX(startx);
                        setY(starty);
                    }
                    first = false;
                }
                while (!gameover && !dead){
                    boolean bounce = true;

                    while(bounce && !gameover && !dead){
                        if (collideLeft)
                            setGoingLeft(false);
                        else if (collideRight)
                            setGoingLeft(true);

                        if (collideUp)
                            setGoingUp(false);
                        else if (collideDown)
                            setGoingUp(true);

                        if (goingLeft && goingUp){
                            x -= 5;
                            y -= 5;
                        } else if (goingLeft) {
                            x -= 5;
                            y += 5;
                        } else if (goingUp){
                            x += 5;
                            y -= 5;
                        } else {
                            x += 5;
                            y += 5;
                        }

                        repaint = true;
                        if (!gameover && !dead){
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            bounce = false;
                        }
                    }
                }
            }
        }));
        thread.start();
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }

}
