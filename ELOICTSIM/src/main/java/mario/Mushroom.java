package mario;

import logica.Coordinaten;
import logica.Meetkunde;

public class Mushroom extends Coordinaten {
    private int startx = 0;
    private int starty = 0;
    private boolean collideLeft = false;
    private boolean collideUp = false;
    private boolean goingLeft = false;
    private boolean goingUp = true;
    private boolean collideDown = false;
    private boolean collideRight = false;
    private boolean gameover = false;
    private boolean dead = false;
    private boolean repaint = false;
    private Thread thread;

    public Mushroom(){
        super(0,0,8);
    }

    public Mushroom(int x, int y){
        super(x,y,8);
    }

    public Mushroom(int x, int y, int radius){
        super(x,y,radius);
        this.startx = x;
        this.starty = y;
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
                            setX(getX() - 5);
                            setY(getY() - 5);
                        } else if (goingLeft) {
                            setX(getX() - 5);
                            setY(getY() + 5);
                        } else if (goingUp){
                            setX(getX() + 5);
                            setY(getY() - 5);
                        } else {
                            setX(getX() + 5);
                            setY(getY() + 5);
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
        return Meetkunde.cirkelOverlaptMetCirkel(getX(), getY(), xc, yc, getRadius(), cirkelRadius);
    }

}
