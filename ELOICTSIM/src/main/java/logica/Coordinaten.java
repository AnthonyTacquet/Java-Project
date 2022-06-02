package logica;

public class Coordinaten {
    private int x = 0;
    private int y = 0;
    private int b = 0;
    private int h = 0;
    private int radius = 0;

    public Coordinaten(){}

    public Coordinaten(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinaten(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Coordinaten(int x, int y, int b, int h){
        this.x = x;
        this.y = y;
        this.b = b;
        this.h = h;
    }

    public Coordinaten(int x, int y, int b, int h, int radius){
        this.x = x;
        this.y = y;
        this.b = b;
        this.h = h;
        this.radius = radius;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getB(){
        return this.b;
    }

    public int getH(){
        return this.h;
    }

    public int getRadius(){
        return this.radius;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setB(int b){
        this.b = b;
    }

    public void setH(int h){
        this.h = h;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }
}
