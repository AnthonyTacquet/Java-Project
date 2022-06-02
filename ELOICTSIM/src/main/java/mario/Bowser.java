package mario;

import logica.Coordinaten;
import logica.Meetkunde;

public class Bowser extends Coordinaten {
    private int startx = 0;
    private int starty = 0;
    private int hit = 0;

    private boolean gameover = false;
    private boolean dead = false;
    private boolean repaint = false;
    private boolean shiver = false;

    private Thread thread;

    public Bowser(){
        super(0,0,30);
    }

    public Bowser(int x, int y){
        super(x,y, 32);
        this.startx = x;
        this.starty = y;
    }

    public Bowser(int x, int y, int radius){
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

    public boolean isDead() {
        return dead;
    }

    public boolean isShiver() {
        return shiver;
    }

    public int getHit(){
        return this.hit;
    }

    public void setRepaint(boolean repaint){
        this.repaint = repaint;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setShiver(boolean shiver) {
        this.shiver = shiver;
    }

    public void addHit(){
        this.hit++;
    }

    public void createThread(){
        if (thread != null)
            return;
        thread = new Thread(() -> {
            boolean first = true;

            while (true){
                if (dead || gameover){
                    setX(5000);
                    setY(0);
                } else {
                    if (!first || !gameover) {
                        setX(startx);
                        setY(starty);
                    }
                    first = false;
                }
                while (!gameover && !dead){
                    int delay = (int) (Math.random() * 10000 + 1000);
                    for (int i = 0; i < 11; i++){
                        switch (i){
                            case 0,1,2,3: setY(getY() - 5); break;
                            case 4: setY(getY() - 2); break;
                            case 5: break;
                            case 6: setY(getY() + 2); break;
                            case 7,8,9,10: setY(getY() + 5); break;
                        }
                        if (i == 6)
                            shiver = true;
                        repaint = true;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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
