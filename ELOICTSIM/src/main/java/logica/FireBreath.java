package logica;

public class FireBreath {
    int x;
    int y;
    int startx = 0;
    int starty = 0;
    int radius = 8;
    Thread thread;

    boolean gameover = false;
    boolean dead = false;
    boolean repaint = false;
    boolean collision = false;

    public FireBreath(){
        this.x = 0;
        this.y = 0;
    }

    public FireBreath(int x, int y){
        this.x = x;
        this.y = y;
        this.startx = x;
        this.starty = y;
    }

    public FireBreath(int x, int y, int radius, boolean bool){
        this.x = x;
        this.y = y;
        this.startx = x;
        this.starty = y;
        this.radius = radius;
        if (bool)
            createThread();
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
            while (!gameover && !dead){
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
                    collision = false;
                    dead = true;
                }
            }
        });
        thread.start();
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }
}
