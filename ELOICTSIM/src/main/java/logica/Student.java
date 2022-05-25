package logica;

import java.util.ArrayList;

public class Student {
    private int x;
    private int y;
    private int radius;
    String naam = "";
    String voornaam = "";
    String beschrijving = "";
    String beroepsprofiel = "";
    ArrayList<String> keuzevakken = new ArrayList<>();
    ArrayList<String> verplichtevakken = new ArrayList<>();


    public Student(){
        this.x = 0;
        this.y = 0;
        this.radius = 30;
        this.naam = "";
        this.beschrijving = "";
    }

    public Student(int x, int y, int radius, String naam){
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.beschrijving = "";
    }

    public Student(int x, int y, String naam, String beschrijving){
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.radius = 30;
        this.beschrijving = beschrijving;
    }

    public Student(int x, int y, String naam, String voornaam, String beschrijving, String beroepsprofiel){
        this.naam = naam;
        this.x = x;
        this.y = y;
        this.radius = 30;
        this.voornaam = voornaam;
        this.beschrijving = beschrijving;
        this.beroepsprofiel = beroepsprofiel;
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

    public String getBeschrijving(){
        return this.beschrijving;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getBeroepsprofiel() {
        return beroepsprofiel;
    }

    public ArrayList<String> getKeuzevakken() {
        return keuzevakken;
    }

    public ArrayList<String> getVerplichtevakken() {
        return verplichtevakken;
    }

    public String getKeuzevakkenString() {
        int count = 0;
        StringBuilder string = new StringBuilder(Hulp.removeDuplicates(getKeuzevakken()).toString());
        for (int i = 0; i < string.length(); i++){
            if (string.charAt(i) == ','){
                count++;
            }
            if (count == 5){
                count = 0;
                string.replace(i, i+1, "<br/>");
            }
        }
        return string.toString();
    }

    public String getVerplichtevakkenString() {
        int count = 0;
        StringBuilder string = new StringBuilder(Hulp.removeDuplicates(getVerplichtevakken()).toString());
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

    public void setBeschrijving(String beschrijving){
        this.beschrijving = beschrijving;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public void setBeroepsprofiel(String beroepsprofiel) {
        this.beroepsprofiel = beroepsprofiel;
    }

    public void setKeuzevakken(ArrayList<String> keuzevakken) {
        this.keuzevakken = keuzevakken;
    }

    public void setVerplichtevakken(ArrayList<String> verplichtevakken) {
        this.verplichtevakken = verplichtevakken;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }


}
