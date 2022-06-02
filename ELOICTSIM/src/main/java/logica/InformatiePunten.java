package logica;

public class InformatiePunten extends Eigenschappen {
    private String naam;
    private String lokaalcode;

    public InformatiePunten(){}

    public InformatiePunten(int x, int y, String beschrijving, String naam, String lokaalcode){
        super(x,y,30, naam, beschrijving);
        this.naam = naam;
        this.lokaalcode = lokaalcode;
    }

    public String getNaam() {
        return naam;
    }

    public String getLokaalcode() {
        return lokaalcode;
    }
    public void setNaam(String naam) {
        super.setNaam(naam);
        this.naam = naam;
    }

    public void setLokaalcode(String lokaalcode) {
        this.lokaalcode = lokaalcode;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(getX(), getY(), xc, yc, getRadius(), cirkelRadius);
    }
}
