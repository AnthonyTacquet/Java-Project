package logica;

import java.util.ArrayList;

public class Student {
    private int x;
    private int y;
    private int radius;
    private String naam = "";
    private String voornaam = "";
    private String beschrijving = "";
    private String beroepsprofiel = "";
    private ArrayList<String> keuzevakken = new ArrayList<>();
    private ArrayList<String> verplichtevakken = new ArrayList<>();

    private boolean collision = false;
    private boolean gameover = false;
    private boolean repaint = false;

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

    public Student(Student student){
        this.naam = student.getNaam();
        this.x = student.getX();
        this.y = student.getY();
        this.radius = student.getRadius();
        this.voornaam = student.getVoornaam();
        this.beschrijving = student.getBeschrijving();
        this.beroepsprofiel = student.getBeroepsprofiel();
        this.keuzevakken = student.getKeuzevakken();
        this.verplichtevakken = student.getVerplichtevakken();
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

    public boolean isRepaint() {
        return repaint;
    }

    public boolean isCollision() {
        return collision;
    }

    public boolean isGameover() {
        return gameover;
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

    public void setRepaint(boolean repaint) {
        this.repaint = repaint;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public boolean intersect(int xc, int yc, int cirkelRadius){
        return Meetkunde.cirkelOverlaptMetCirkel(this.x, this.y, xc, yc, this.radius, cirkelRadius);
    }
    public void createThread(){
        Thread thread = new Thread(() -> {
            boolean skip = false;
            int direction = 0;
            while (true){
                if (!skip)
                   direction = (int) (Math.random() * 4);
                //System.out.println(direction);
                for (int j = 0; j < (int) (Math.random() * 25 + 5); j++) {
                    int distance = 5;
                    if (skip)
                        distance = 10;
                    switch (direction){
                        case 0: x -= distance; break;
                        case 1: y -= distance; break;
                        case 2: x += distance; break;
                        case 3: y += distance; break;
                    }
                    skip = false;

                    if (!collision) {
                        if (!gameover) {
                            repaint = true;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        skip = true;
                        collision = false;
                        j = 30;
                        switch (direction){
                            case 0: direction = 2; break;
                            case 1: direction = 3; break;
                            case 2: direction = 0; break;
                            case 3: direction = 1; break;
                        }
                    }
                }
                if (!gameover) {
                    try {
                        Thread.sleep((int) (Math.random() * 1000 + 500));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }


}
