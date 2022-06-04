package logica;

import com.mysql.cj.exceptions.StreamingNotifiable;

import java.util.ArrayList;

public class Student extends Eigenschappen{
    private ArrayList<String> keuzevakken = new ArrayList<>();
    private ArrayList<String> verplichtevakken = new ArrayList<>();
    private int inschrijvingsjaar;

    private boolean collision = false;
    private boolean gameover = false;
    private boolean repaint = false;

    public Student(){
        super(0,0,30);
    }

    public Student(int x, int y, int radius, String naam){
        super(x,y,radius,naam);
    }

    public Student(int x, int y, String naam, String beschrijving){
        super(x,y,naam, beschrijving);
    }

    public Student(String naam, String voornaam, String beschrijving, Beroepsprofiel beroepsprofiel, int inschrijvingsjaar){
        super(naam, voornaam, beschrijving, beroepsprofiel);
        this.inschrijvingsjaar = inschrijvingsjaar;
    }

    public Student(int x, int y, String naam, String voornaam, String beschrijving, Beroepsprofiel beroepsprofiel){
        super(x,y, naam, voornaam, beschrijving, beroepsprofiel);
    }

    public Student(Student student){
        super();
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

    public ArrayList<String> getKeuzevakken() {
        return keuzevakken;
    }

    public ArrayList<String> getVerplichtevakken() {
        return verplichtevakken;
    }

    public int getInschrijvingsjaar(){
        return this.inschrijvingsjaar;
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

    public void setKeuzevakken(ArrayList<String> keuzevakken) {
        this.keuzevakken = keuzevakken;
    }

    public void setVerplichtevakken(ArrayList<String> verplichtevakken) {
        this.verplichtevakken = verplichtevakken;
    }

    public void setInschrijvingsjaar(int inschrijvingsjaar){
        this.inschrijvingsjaar = inschrijvingsjaar;
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
        return Meetkunde.cirkelOverlaptMetCirkel(getX(), getY(), xc, yc, getRadius(), cirkelRadius);
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
                        case 0: setX(getX() - distance); break;
                        case 1: setY(getY() - distance); break;
                        case 2: setX(getX() + distance); break;
                        case 3: setY(getY() + distance); break;
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
