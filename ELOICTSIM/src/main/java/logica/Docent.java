package logica;

import java.util.ArrayList;

public class Docent extends Eigenschappen{
    private ArrayList<String> vakken = new ArrayList<>();

    public Docent(){
        super(0,0,30);
    }

    public Docent(int x, int y, int radius, String naam){
        super(x,y,radius,naam);
    }

    public Docent(int x, int y, String naam, String beschrijving){
        super(x,y, 30,naam,beschrijving);
    }

    public Docent(int x, int y, String naam, String voornaam, String beschrijving){
        super(x, y, 30, naam, voornaam, beschrijving);
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
    public void setVakken(ArrayList<String> vakken) {
        this.vakken = vakken;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(getX(), getY(), xc, yc, getRadius(), cirkelRadius);
    }
}
