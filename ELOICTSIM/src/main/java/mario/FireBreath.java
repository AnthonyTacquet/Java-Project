package mario;

import logica.Coordinaten;
import logica.Meetkunde;

public class FireBreath extends Coordinaten {
    private Thread thread;

    boolean gameover = false;
    boolean dead = false;
    boolean repaint = false;
    boolean collision = false;

    public FireBreath(){
        super(0,0,8);
    }

    public FireBreath(int x, int y){
        super(x,y,8);
    }

    public FireBreath(int x, int y, int radius, boolean bool){
        super(x,y,radius);
        if (bool)
            createThread();
    }

    public boolean isGameover() {
        return gameover;
    }

    public boolean isRepaint(){
        return this.repaint;
    }

    public boolean isCollision(){
        return this.collision;
    }

    public boolean isDead() {
        return dead;
    }

    public Thread getThread(){
        return thread;
    }

    public void setRepaint(boolean repaint){
        this.repaint = repaint;
    }

    public void setCollision(boolean collision){
        this.collision = collision;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void createThread(){
        thread = new Thread(() -> {
            while (!gameover && !dead && !collision){
                setX(getX() - 5);
                if (!collision) {
                    if (!gameover && !dead){
                        repaint = true;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else {
                    dead = true;
                }
            }
        });
        thread.start();
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(getX(), getY(), xc, yc, getRadius(), cirkelRadius);
    }
}
