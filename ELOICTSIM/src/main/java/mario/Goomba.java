package mario;

import logica.Coordinaten;
import logica.Meetkunde;

public class Goomba extends Coordinaten {
    private int startx = 0;
    private int starty = 0;

    private boolean gameover = false;
    private boolean dead = false;
    private boolean repaint = false;
    private boolean collision = false;

    Thread thread;

    public Goomba(){
        super(0,0,8);
    }

    public Goomba(int x, int y){
        super(x,y,8);
        this.startx = x;
        this.starty = y;
    }

    public Goomba(int x, int y, int radius){
        super(x,y,radius);
        this.startx = x;
        this.starty = y;
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
        if (thread != null)
            return;
        thread = new Thread(() -> {
            boolean first = true;
            boolean left = true;

            while (true){
                if (dead || gameover){
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

                    if (left){
                        setX(getX() - 5);
                    } else {
                        setX(getX() + 5);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
                        collision = false;
                        left = !left;
                    }
                }


            }
        });
        thread.start();
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(getX(), getY(), xc, yc, getRadius(), cirkelRadius);
    }

}
