package logica;

public class Vuurbal {
    private int x;
    private int y;
    private int radius = 8;

    private boolean links = false;
    private boolean stop = false;
    private boolean bounce = false;
    private boolean down = true;
    private int height = 0;

    private boolean gameover = false;
    private boolean dead = false;
    private boolean repaint = false;
    private boolean collision = false;

    private Thread thread;

    public Vuurbal(){
        this.x = 0;
        this.y = 0;
    }

    public Vuurbal(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vuurbal(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Vuurbal(int x, int y, int radius, boolean links, boolean createthread){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.links = links;
        if (createthread)
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

    public boolean isBounce() {
        return bounce;
    }

    public boolean isStop() {
        return stop;
    }

    public boolean isLinks() {
        return links;
    }

    public Thread getThread() {
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

    public void setLinks(boolean links) {
        this.links = links;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setBounce(boolean bounce) {
        this.bounce = bounce;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }


    public void createThread(){
        if (thread != null)
            return;
        thread = new Thread(() -> {

            for (int i = 0; i < 10; i++){
                bounce = false;
                while (!gameover && !stop && !bounce) {
                    if (links && down) {
                        x -= 5;
                        y += 5;
                    } else if (links) {
                        if (height == 2){
                            x -= 2;
                            y -= 2;
                        } else{
                            x -= 5;
                            y -= 5;
                        }
                        height++;
                    } else if (down) {
                        x += 5;
                        y += 5;
                    } else {
                        if (height == 2){
                            x += 2;
                            y -= 2;
                        } else{
                            x += 5;
                            y -= 5;
                        }
                        height++;
                    }

                    if (height == 3){
                        down = true;
                        height = 0;
                    }

                    if (!stop && !gameover && !bounce) {

                        repaint = true;
                        try {
                            if (height == 1 || height == 2)
                                Thread.sleep(25);
                            else
                                Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        down = false;
                    }

                }
                if (stop){
                    return;
                }

            }
        });
        thread.start();
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }

    @Override
    public String toString(){
        return "X: " + this.x + ", Y: " + this.y + ", Stop? " + this.stop;
    }

}
