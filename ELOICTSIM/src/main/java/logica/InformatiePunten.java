package logica;

public class InformatiePunten {
    private int x;
    private int y;
    private final int RADIUS = 30;
    private String beschrijving;
    private String naam;
    private String lokaalcode;

    public InformatiePunten(){

    }

    public InformatiePunten(int x, int y, String beschrijving, String naam, String lokaalcode){
        this.x = x;
        this.y = y;
        this.beschrijving = beschrijving;
        this.naam = naam;
        this.lokaalcode = lokaalcode;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getRADIUS() {
        return RADIUS;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public String getNaam() {
        return naam;
    }

    public String getLokaalcode() {
        return lokaalcode;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setLokaalcode(String lokaalcode) {
        this.lokaalcode = lokaalcode;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.RADIUS, cirkelRadius);
    }
}
