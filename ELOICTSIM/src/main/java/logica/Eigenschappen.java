package logica;

public class Eigenschappen extends Coordinaten{
    private String naam = "";
    private String voornaam = "";
    private String beschrijving = "";
    private String beroepsprofiel = "";

    public Eigenschappen(){}

    public Eigenschappen(String naam){
        this.naam = naam;
    }

    public Eigenschappen(int x, int y, int radius, String naam){
        super(x,y,radius);
        this.naam = naam;
    }

    public Eigenschappen(String naam, String beschrijving){
        this.naam = naam;
        this.beschrijving = beschrijving;
    }

    public Eigenschappen(int x, int y, String naam, String beschrijving){
        super(x,y);
        this.naam = naam;
        this.beschrijving = beschrijving;
    }

    public Eigenschappen(int x, int y, int radius, String naam, String beschrijving){
        super(x,y, radius);
        this.naam = naam;
        this.beschrijving = beschrijving;
    }

    public Eigenschappen(String naam, String voornaam, String beschrijving){
        this.naam = naam;
        this.voornaam = voornaam;
        this.beschrijving = beschrijving;
    }

    public Eigenschappen(int x, int y, String naam, String voornaam, String beschrijving){
        super(x,y);
        this.naam = naam;
        this.voornaam = voornaam;
        this.beschrijving = beschrijving;
    }

    public Eigenschappen(String naam, String voornaam, String beschrijving, String beroepsprofiel){
        this.naam = naam;
        this.voornaam = voornaam;
        this.beschrijving = beschrijving;
        this.beroepsprofiel = beroepsprofiel;
    }

    public Eigenschappen(int x, int y, String naam, String voornaam, String beschrijving, String beroepsprofiel){
        super(x,y);
        this.naam = naam;
        this.voornaam = voornaam;
        this.beschrijving = beschrijving;
        this.beroepsprofiel = beroepsprofiel;
    }

    public Eigenschappen(int x, int y, int radius, String naam, String voornaam, String beschrijving){
        super(x,y, radius);
        this.naam = naam;
        this.voornaam = voornaam;
        this.beschrijving = beschrijving;
    }

    public Eigenschappen(int x, int y){
        super(x,y);
    }

    public Eigenschappen(int x, int y, int radius){
        super(x,y,radius);
    }

    public Eigenschappen(int x, int y, int b, int h){
        super(x,y,b,h);
    }

    public Eigenschappen(int x, int y, int b, int h, int radius){
        super(x,y,b,h,radius);
    }

    public String getNaam(){
        return this.naam;
    }

    public String getVoornaam(){
        return this.voornaam;
    }

    public String getBeschrijving(){
        return this.beschrijving;
    }

    public String getBeroepsprofiel(){
        return this.beroepsprofiel;
    }

    public void setNaam(String naam){
        this.naam = naam;
    }

    public void setVoornaam(String voornaam){
        this.voornaam = voornaam;
    }

    public void setBeschrijving(String beschrijving){
        this.beschrijving = beschrijving;
    }

    public void setBeroepsprofiel(String beroepsprofiel){
        this.beroepsprofiel = beroepsprofiel;
    }
}
