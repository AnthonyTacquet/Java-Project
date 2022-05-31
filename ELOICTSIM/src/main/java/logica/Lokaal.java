package logica;

public class Lokaal {
    private int x;
    private int y;
    private int b;
    private int h;
    private String naam;
    private String code;

    public Lokaal(){
        this.x = 0;
        this.y = 0;
        this.b = 0;
        this.h = 0;
        this.naam = "";
        this.code = "";
    }

    public Lokaal(int x, int y, int b, int h, String naam){
        this.x = x;
        this.y = y;
        this.b = b;
        this.h = h;
        this.naam = naam;
        this.code = "";
    }

    public Lokaal(String naam, String code, int x, int y, int b, int h ){
        this.x = x;
        this.y = y;
        this.b = b;
        this.h = h;
        this.naam = naam;
        this.code = code;
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

    public String getNaam(){
        return this.naam;
    }

    public String getCode(){
        return this.code;
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

    public void setNaam(String naam){
        this.naam = naam;
    }

    public void setCode(String code){
        this.code = code;
    }
}
