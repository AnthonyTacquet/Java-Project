package logica;
public class Docent {
    private int x;
    private int y;
    private int radius;
    String naam = "";
    String beschrijving = "";

    public Docent(){
        this.x = 0;
        this.y = 0;
        this.radius = 30;
        this.naam = "";
        this.beschrijving = "";
    }

    public Docent(int x, int y, int radius, String naam){
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.beschrijving = "";
    }

    public Docent(int x, int y, String naam, String beschrijving){
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.beschrijving = beschrijving;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getRadius() {
        return radius;
    }

    public String getNaam(){
        return this.naam;
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

    public void setNaam(String naam){
        this.naam = naam;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }
}
