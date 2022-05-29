package logica;

public class Bowser {
    int x;
    int y;
    int startx = 0;
    int starty = 0;
    int radius = 32;
    int hit = 0;

    boolean gameover = false;
    boolean dead = false;
    boolean repaint = false;
    boolean shiver = false;

    Thread thread;

    public Bowser(){
        this.x = 0;
        this.y = 0;
    }

    public Bowser(int x, int y){
        this.x = x;
        this.y = y;
        this.startx = x;
        this.starty = y;
    }

    public Bowser(int x, int y, int radius){
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

    public boolean isDead() {
        return dead;
    }

    public boolean isShiver() {
        return shiver;
    }

    public int getHit(){
        return this.hit;
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
                            case 0,1,2,3: y-=5; break;
                            case 4: y-=2; break;
                            case 5: break;
                            case 6: y+=2; break;
                            case 7,8,9,10: y+=5; break;
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
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }

}
