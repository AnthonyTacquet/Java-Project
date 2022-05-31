package logica;

import java.util.ArrayList;

public class Docent {
    private int x;
    private int y;
    private int radius;
    private String naam = "";
    private String voornaam = "";
    private String beschrijving = "";
    private ArrayList<String> vakken = new ArrayList<>();

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

    public Docent(int x, int y, String naam, String voornaam, String beschrijving){
        this.naam = naam;
        this.voornaam = voornaam;
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

    public String getVoornaam() {
        return voornaam;
    }
    public String getBeschrijving() {
        return beschrijving;
    }
    public ArrayList<String> getVakken() {
        return this.vakken;
    }

    public String getVakkenString() {
        int count = 0;
        StringBuilder string = new StringBuilder(Hulp.removeDuplicates(getVakken()).toString());
        for (int i = 0; i < string.length(); i++){
            if (string.charAt(i) == ','){
                count++;
            }
            if (count == 4){
                count = 0;
                string.replace(i, i+1, "<br/>");
            }
        }
        return string.toString();
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

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public void setVakken(ArrayList<String> vakken) {
        this.vakken = vakken;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }
}
