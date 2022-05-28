package logica;

public class Goomba {
    int x;
    int y;
    int startx = 0;
    int starty = 0;
    int radius = 8;

    boolean gameover = false;
    boolean dead = false;
    boolean repaint = false;
    boolean collision = false;

    Thread thread;

    public Goomba(){
        this.x = 0;
        this.y = 0;
    }

    public Goomba(int x, int y){
        this.x = x;
        this.y = y;
        this.startx = x;
        this.starty = y;
    }

    public Goomba(int x, int y, int radius){
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

    public boolean isRepaint(){
        return this.repaint;
    }

    public boolean isCollision(){
        return this.collision;
    }

    public boolean isDead() {
        return dead;
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
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }

}
