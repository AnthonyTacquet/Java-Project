package presentatie;

import logica.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class EloictSimGui extends javax.swing.JPanel{

    //bron: https://huisstijl2.odisee.be/huisstijl/kleurenpalet-en-kleurbalans
    private static final Color SPELER_KLEUR =new Color(200,63,22);//Warmrood
    private static final Color LOKAAL_KLEUR =new Color(31,65,107);//Nachtblauw
    private static final Color DEUR_KLEUR =new Color(211,221,242);//Mistblauw
    private static final Color INVISIBLE =new Color(0,0,0, 1);//TRANSPARENT

    String username = "root";
    String password = "Azerty123";
    private String dbms = "mysql";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String database = "eloictsim";

    private JPanel tekenPanel;
    private JPanel mainPanel;
    private JLabel voornaam;
    private JLabel achternaam;
    private JLabel vakken;
    private JLabel voornaamStudent;
    private JLabel achternaamStudent;
    private JLabel vakkenStudent;
    private JLabel beroepsprofiel;
    private JLabel lokaalNaam;
    private JLabel lokaalCode;
    private JPanel mushroomPanel;
    private JLabel vakkenKeuzeStudent;
    private JLabel beschrijvingStudent;
    private JPanel meldingPanel;

    private Image background, game, bally;
    private Image lokaalKlein, studentKlein, docentKlein;
    private ImageIcon lokaalGroot, studentGroot, docentGroot;

    private Image[] marios = new Image[12];
    private Image[] mariosGroot = new Image[12];

    private Image mushy;
    private Image goomby;

    private Image marioCurrent;

    Thread thread;

    Student mushroom = new Student(600, 280, 8, "mushroom");
    Thread thread2;

    Student goomba = new Student(300, 305, 8, "goomba");
    Thread thread3;

    Thread[] ball = new Thread[10];

    ArrayList<Lokaal> lokalen = new ArrayList<>();
    ArrayList<Deur> deuren = new ArrayList<>();
    ArrayList<Student> studenten = new ArrayList<>();
    ArrayList<Docent> docenten = new ArrayList<>();
    //Student[] studenten = {studentGuillermin, studentLux, studentPoppe, studentThys, studentVerleysen, studentAbdulKhalil};
    //Docent[] docenten = {docentVermeulen, docentVanAssche, docentJacobs, docentSanders, docentKnockaert, docentDemeester};
    final String queryDocenten = "select personen.familienaam, personen.voornaam, vakken.naam from personen inner join docenten on docenten.id = personen.id inner join docenten_has_vakken on docenten_has_vakken.docent_id = docenten.id inner join vakken on vakken.id = docenten_has_vakken.vak_id ";
    final String queryStudenten = "select personen.familienaam, personen.voornaam , beroepsprofielen.naam, vakken.naam as verplicht from personen inner join studenten on studenten.id = personen.id inner join beroepsprofielen on beroepsprofielen.id = beroepsprofiel_id left join keuzevakken on studenten.id = keuzevakken.student_id left join verplichte_vakken on verplichte_vakken.beroepsprofiel_id = beroepsprofielen.id left join vakken on vakken.id = verplichte_vakken.vak_id ";
    private int x = 50;
    private int y = 280;
    private int radius = 16;
    private int centerx = 16;
    private int centery = 16;

    int lastdocent = -100;
    int numberdocent = -10;
    int laststudent = -100;
    int numberstudent = -10;
    int lastlokaal = -100;
    int numberlokaal = -10;
    int mario = 0;
    int collected = 0;
    boolean size = false;
    boolean links = false;
    boolean gameover = false;
    boolean endgame = false;

    Vuurbal[] vuurballen = new Vuurbal[10];
    boolean fire = true;
    boolean dead = false;

    public EloictSimGui() {
        mushroomPanel.setPreferredSize(new Dimension(32, 16));
        tekenPanel.setFocusable(true);
        setPlayground();
        initializeDefaultThreads();
        marioCurrent = marios[0];
        thread.start();
        thread2.start();
        thread3.start();
        try {
            Connection connection = connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tekenPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                docentenPanel();
                studentenPanel();
                lokalenPanel();
                collideGoomba();
                switch (e.getKeyCode()){
                    case 37: verplaatsen(Pijltjes.LINKS); break;
                    case 38: verplaatsen(Pijltjes.BOVEN); break;
                    case 39: verplaatsen(Pijltjes.RECHTS); break;
                    case 40: verplaatsen(Pijltjes.ONDER); break;
                }
            }
        });

        tekenPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                mario(0);
            }
        });
        tekenPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(e.getButton());
                if (e.getButton() == 1)
                    fireball();
            }
        });
    }

    public void verplaatsen(Pijltjes pijltjes){
        int x = this.x;
        int y = this.y;
        //System.out.println("x: " + this.x + ", y: " + this.y);
        if (pijltjes == Pijltjes.LINKS){
            x-=5;
        } else if (pijltjes == Pijltjes.BOVEN){
            y-=5;
        } else if (pijltjes == Pijltjes.RECHTS){
            x+=5;
        } else if (pijltjes == Pijltjes.ONDER){
            y+=5;
        }
        if (!intersection(x, y, this.radius)){
            this.x = x;
            this.y = y;
        }
        if (pijltjes == Pijltjes.LINKS && !links){
            links = true;
        } else if (pijltjes == Pijltjes.RECHTS || pijltjes == Pijltjes.BOVEN || pijltjes == Pijltjes.ONDER){
            links = false;
        }

        mario(this.mario);
        this.mario++;
        if (mario == 12)
            mario = 0;
        tekenPanel.revalidate();
        tekenPanel.repaint();
    }

    public void mario(int picture){
        if (!size)
            marioCurrent = marios[picture];
        else
            marioCurrent = mariosGroot[picture];
        if (links){
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-marioCurrent.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            marioCurrent = op.filter((BufferedImage) marioCurrent, null);
        }
    }

    public boolean intersection(int x, int y, int rad){
        for (int i = 0; i < deuren.size(); i++){
            int radius = rad - rad / 2;
            int x1 = deuren.get(i).getX1();
            int x2 = deuren.get(i).getX2();
            int y2 = deuren.get(i).getY1();
            int y1 = deuren.get(i).getY2();
            if (y1 == y2)
                //if ((x1 < (x - radius) && x2 > (x + radius)) && (y1 - radius <= y && y1 + radius >= y))
                // && y1 - radius < y && y1 + radius > y
                if (x2 + radius <= x && x1 - radius >= x)
                    return false;
            else if (x1 == x2)
                if (y1 + radius <= y && y2 - radius >= y)
                    return false;
            /*else
                return true;
            /*if (Meetkunde.cirkelOverlaptMetLijnstuk(deuren.get(i), x, y, radius))
                return false;*/

        }
        for (int i = 0; i < lokalen.size(); i++){
            if (Meetkunde.cirkelOverlaptMetRechthoek(lokalen.get(i), x, y, radius))
                return true;
        }
        return false;
    }

    public void docentenPanel(){
        boolean found = false;
        for (int i = 0; i < docenten.size(); i++){
            if (docenten.get(i).intersect(this.x, this.y,60)){
                numberdocent = i;
                found = true;
            }
        }
        if (!found){
            numberdocent = -10;
            lastdocent = -100;
        }
        if (numberdocent != -10){
            if (lastdocent == numberdocent)
                return;
            else
                lastdocent = numberdocent;

            this.achternaam.setText(docenten.get(numberdocent).getNaam());
        }
        if (numberdocent == -10){
            voornaam.setText("");
            achternaam.setText("");
            vakken.setText("");
        }
    }

    public void studentenPanel(){
        boolean found = false;
        for (int i = 0; i < studenten.size(); i++){
            if (studenten.get(i).intersect(this.x, this.y, 60)){
                numberstudent = i;
                found = true;
            }
        }
        if (!found){
            numberstudent = -10;
            laststudent = -100;
        }

        if (numberstudent != -10){
            if (laststudent == numberstudent)
                return;
            else
                laststudent = numberstudent;

            this.achternaamStudent.setText(studenten.get(numberstudent).getNaam());
            this.voornaamStudent.setText(studenten.get(numberstudent).getVoornaam());
            this.vakkenStudent.setText("<html>" + studenten.get(numberstudent).getVerplichtevakkenString() + "</html>");
            this.vakkenKeuzeStudent.setText("<html>" + studenten.get(numberstudent).getKeuzevakkenString() + "</html>");
            this.beschrijvingStudent.setText(studenten.get(numberstudent).getBeschrijving());
            this.beroepsprofiel.setText(studenten.get(numberstudent).getBeroepsprofiel());
        }

        if (numberstudent == -10){
            this.achternaamStudent.setText("");
            this.voornaamStudent.setText("");
            this.vakkenStudent.setText("");
            this.vakkenKeuzeStudent.setText("");
            this.beschrijvingStudent.setText("");
            this.beroepsprofiel.setText("");
        }

    }

    public void lokalenPanel(){
        boolean found = false;
        for (int i = 0; i < lokalen.size(); i++){
            if (Meetkunde.studentInLokaal(lokalen.get(i), this.x, this.y, this.radius)){
                numberlokaal = i;
                found = true;
            }
        }
        if (!found){
            numberlokaal = -10;
            lastlokaal = -100;
        }

        if (numberlokaal != -10){
            if (lastlokaal == numberlokaal)
                return;
            else
                lastlokaal = numberlokaal;

            lokaalNaam.setText(lokalen.get(numberlokaal).getNaam());
            lokaalCode.setText(lokalen.get(numberlokaal).getCode());
        }

        if (numberlokaal == -10){
            lokaalNaam.setText("");
            lokaalCode.setText("");
        }
    }

    public void fireball(){
        int locatie = 0;
        fire = true;

        for (int i = 0; i < ball.length; i++){
            if (ball[i] == null){
                locatie = i;
            } else if (!ball[i].isAlive()){
                locatie = i;
            }
        }
        System.out.println(locatie);
        vuurballen[locatie].setY(y);
        if (!links)
            vuurballen[locatie].setX(x + centerx);
        else
            vuurballen[locatie].setX(x - centerx);

        int finalLocatie = locatie;
        ball[locatie] = new Thread(new Runnable() {
            @Override
            public void run() {
                playSound("mario/sound/fireball");
                boolean rechts = !links;
                boolean down = true;
                int height = 0;
                for (int i = 0; i < 3; i++){
                    boolean bounce = true;
                    while (bounce && !gameover) {
                        if (goomba.intersect(vuurballen[finalLocatie].getX(), vuurballen[finalLocatie].getY(), 4)){
                            if (!dead)
                                playSound("mario/sound/kick");
                            dead = true;
                        }
                        int x = vuurballen[finalLocatie].getX();
                        int y = vuurballen[finalLocatie].getY();
                        if (!rechts && down) {
                            x -= 5;
                            y += 5;
                        } else if (!rechts) {
                            if (height == 2){
                                x -= 2;
                                y -= 2;
                            } else{
                                x -= 5;
                                y -= 5;
                            }
                            height++;
                        } else if (down) {
                            x += 5;
                            y += 5;
                        } else {
                            if (height == 2){
                                x += 2;
                                y -= 2;
                            } else{
                                x += 5;
                                y -= 5;
                            }
                            height++;
                        }

                        if (height == 3){
                            down = true;
                            height = 0;
                        }

                        if (intersection(vuurballen[finalLocatie].getX(), vuurballen[finalLocatie].getY(), 4)) {
                            i = 5;
                            tekenPanel.revalidate();
                            tekenPanel.repaint();
                            return;
                        }
                        if (!intersection(x, y, 4)) {
                            bounce = true;
                            vuurballen[finalLocatie].setX(x);
                            vuurballen[finalLocatie].setY(y);

                            if (!gameover) {
                                tekenPanel.revalidate();
                                tekenPanel.repaint();
                                try {
                                    if (height == 1 || height == 2)
                                        Thread.sleep(25);
                                    else
                                        Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        } else {
                            bounce = false;
                            down = !down;
                        }

                    }
                }
                vuurballen[finalLocatie].setX(0);
                vuurballen[finalLocatie].setY(0);
                tekenPanel.revalidate();
                tekenPanel.repaint();
            }
        });
        ball[locatie].start();
    }

    public void collideGoomba(){
        if (goomba.intersect(this.x, this.y, this.radius)) {
            playSound("mario/sound/bump");
            if (size) {
                size = false;
                this.radius = 16;
                this.centerx = 16;
                this.centery = 16;
            } else if (!gameover){
                playSound("mario/sound/dead");
                gameover = true;
                tekenPanel.repaint();
                tekenPanel.revalidate();
            }
        }
    }

    public void collectMushroom(){
        if (mushroom.intersect(this.x, this.y, this.radius)){
            playSound("mario/sound/powerup");
            collected++;
            Lokaal lokaal = lokalen.get((int) (Math.random() * lokalen.size()));
            if (!size){
                size = true;
                this.radius = 32;
                this.centerx = 32;
                this.centery = 32;
                while (intersection(this.x, this.y, radius)){
                    for (int i = 0; i < 32; i ++){
                        if (!intersection(this.x + i, this.y, this.radius)){
                            this.x += i;
                            return;
                        } else if(!intersection(this.x, this.y + i, this.radius)){
                            this.y += i;
                            return;
                        } else if(!intersection(this.x - i, this.y, this.radius)){
                            this.x -= i;
                            return;
                        } else if(!intersection(this.x, this.y - i, this.radius)){
                            this.y -= i;
                            return;
                        }
                    }
                }
            }
            if (collected < 2){
                mushroom.setX((int) (Math.random() * (lokaal.getX() + 20) + (lokaal.getB() - 20)));
                mushroom.setY((int) (Math.random() * (lokaal.getY() + 20) + (lokaal.getH() - 20)));
            } else {
                mushroom.setX(5000);
                mushroom.setY(5000);
                thread2.interrupt();
            }
            if (size){
                marioCurrent = mariosGroot[0];
            }

            mushroomPanel.revalidate();
            mushroomPanel.repaint();
            tekenPanel.revalidate();
            tekenPanel.repaint();
        }
    }

    public Connection connection() throws  SQLException{
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.username);
        connectionProps.put("password", this.password);
        conn = DriverManager.getConnection("jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/" + this.database, connectionProps);
        return conn;
    }


    private void createUIComponents() {
        background = laadAfbeelding("d-gang");
        lokaalKlein = laadAfbeelding("32px/lokaal");
        studentKlein = laadAfbeelding("32px/student");
        docentKlein = laadAfbeelding("32px/docent");
        mushy = laadAfbeelding("mario/new/mushy");
        goomby = laadAfbeelding("mario/new/goomba");

        lokaalGroot = laadIcoon("64px/lokaal");
        studentGroot = laadIcoon("64px/student");
        docentGroot = laadIcoon("64px/docent");
        game = laadAfbeelding("mario/gameover");
        bally = laadAfbeelding("mario/fireball");

        mushroomPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (collected == 1){
                    g2.drawImage(mushy, 0,0, null);
                }
                else if (collected == 2){
                    g2.drawImage(mushy, 0, 0, null);
                    g2.drawImage(mushy, 16, 0, null);
                }
                else if (collected == 3){
                    g2.drawImage(mushy, 0, 0, null);
                    g2.drawImage(mushy, 16, 0, null);
                    g2.drawImage(mushy, 32, 0, null);
                }

            }
        };

        tekenPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (!gameover)
                    g2.drawImage(background, 0, 0, null);

                if (fire)
                    for (int i = 0; i < vuurballen.length; i++){
                        if (vuurballen[i] != null)
                            g2.drawImage(bally, vuurballen[i].getX() - 4, vuurballen[i].getY() - 4, null);
                    }


                for (int i = 0; i < docenten.size(); i++){
                    g2.drawImage(docentKlein, docenten.get(i).getX() - 16, docenten.get(i).getY() - 16, null);
                }

                for (int i = 0; i < studenten.size(); i++){
                    g2.drawImage(studentKlein, studenten.get(i).getX() - 16, studenten.get(i).getY() - 16, null);
                }


                g2.setColor(LOKAAL_KLEUR);

                for (int i = 0; i < lokalen.size(); i++){
                    g2.drawRect(lokalen.get(i).getX(), lokalen.get(i).getY(), lokalen.get(i).getB(), lokalen.get(i).getH());
                }

                g2.setColor(DEUR_KLEUR);

                for (int i = 0; i < deuren.size(); i++){
                    g2.drawLine(deuren.get(i).getX1(), deuren.get(i).getY1(), deuren.get(i).getX2(), deuren.get(i).getY2());
                }

                g2.setColor(SPELER_KLEUR);
                g2.drawImage(marioCurrent, x - centerx, y - centery, null);
                g2.drawImage(mushy, mushroom.getX() - 8, mushroom.getY() - 8,  null);

                if (!dead)
                    g2.drawImage(goomby, goomba.getX() - 8, goomba.getY() - 8,  null);


                //g2.drawRect(x - 16, y - 16, 32, 32);

                if (gameover){
                    g2.drawImage(game, 0, 0, null);
                    meldingPanel.setVisible(false);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!endgame){
                                endgame = true;
                                int option = JOptionPane.showConfirmDialog(null, "Do you want to play again","Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (option == JOptionPane.YES_OPTION){
                                    gameover = false;
                                    endgame = false;
                                    dead = false;
                                    x = 50;
                                    y = 250;
                                    collected = 0;
                                    meldingPanel.setVisible(true);
                                    tekenPanel.repaint();
                                    tekenPanel.revalidate();
                                } else {
                                    System.exit(0);
                                }

                            }

                        }
                    });

                }
            }

            //Het kan nuttig zijn om je tekenwerk op te splitsen en hier methoden toe te voegen om specifieke zaken te tekenen
        };
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            EloictSimGui.class.getResourceAsStream("/" + url + ".wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public void setPlayground(){
        //Set Marios
        for (int i = 0; i < marios.length; i++){
            String bestand = "mario/new/" + (i + 1);
            marios[i] = laadAfbeelding(bestand);
        }
        for (int i = 0; i < mariosGroot.length; i++){
            String bestand = "mario/new/64px/" + (i + 1);
            mariosGroot[i] = laadAfbeelding(bestand);
        }

        for (int i = 0; i < vuurballen.length; i++){
            vuurballen[i] = new Vuurbal();
        }

        //Set Lokalen
        String queryLokalen = "select * from lokalen;";
        try{
            Connection conn = connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryLokalen);
            while (rs.next()) {
                lokalen.add(new Lokaal(rs.getString("naam"), rs.getString("lokaalcode"), rs.getInt("x"), rs.getInt("y"), rs.getInt("breedte"), rs.getInt("lengte")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Set Deuren
        String queryDeuren = "select * from deuren;";
        try{
            Connection conn = connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryDeuren);
            while (rs.next()) {
                //ystem.out.println("x1: " + rs.getInt("y1") + "x2: "+ rs.getInt("y2"));
                //deuren.add(new Deur(rs.getInt("x1"), rs.getInt("x2"), rs.getInt("y1"), rs.getInt("y2")));
                Deur deur = new Deur();
                deur.setX1(rs.getInt("y1"));
                deur.setX2(rs.getInt("x1"));
                deur.setY1(rs.getInt("x2"));
                deur.setY2(rs.getInt("y2"));
                deuren.add(deur);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Set Studenten
        String queryStudent = "select x, y, informatiepunten.beschrijving, personen.familienaam, personen.voornaam , beroepsprofielen.naam from personen inner join studenten on studenten.id = personen.id inner join beroepsprofielen on beroepsprofielen.id = beroepsprofiel_id left join keuzevakken on studenten.id = keuzevakken.student_id left join informatiepunten on personen.id = informatiepunten.persoon_id;";
        try{
            Connection conn = connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryStudent);
            while (rs.next()) {
                Student student = new Student();
                student.setX(rs.getInt("x"));
                student.setY(rs.getInt("y"));
                student.setBeschrijving(rs.getString("beschrijving"));
                student.setNaam(rs.getString("familienaam"));
                student.setVoornaam(rs.getString("voornaam"));
                student.setBeroepsprofiel(rs.getString("naam"));
                studenten.add(student);
            }
            for (int i = 0; i < studenten.size(); i++){
                String naam = studenten.get(i).getNaam();
                String query2 = "select keuze.naam as keuzevak, verplicht.naam as verplichtvak from personen inner join studenten on studenten.id = personen.id inner join beroepsprofielen on beroepsprofielen.id = beroepsprofiel_id left join keuzevakken on studenten.id = keuzevakken.student_id left join verplichte_vakken on verplichte_vakken.beroepsprofiel_id = beroepsprofielen.id left join vakken as keuze on keuze.id = keuzevakken.vak_id left join vakken as verplicht on verplicht.id = verplichte_vakken.vak_id where personen.familienaam = \"" + naam + "\";";
                ResultSet result = stmt.executeQuery(query2);
                ArrayList<String> keuze = new ArrayList<>();
                ArrayList<String> verplicht = new ArrayList<>();
                while (result.next()){
                    keuze.add(result.getString("keuzevak"));
                    verplicht.add(result.getString("verplichtvak"));
                }
                studenten.get(i).setKeuzevakken(keuze);
                studenten.get(i).setVerplichtevakken(verplicht);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        studenten.get(2).setX(this.x + 200);
        studenten.get(2).setY(this.y);


        //Set Docenten
        String queryDocent = "select personen.familienaam, informatiepunten.x, informatiepunten.y, informatiepunten.beschrijving from informatiepunten right join personen on persoon_id = personen.id inner join docenten on personen.id = docenten.id; ";
        try{
            Connection conn = connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryDocent);
            while (rs.next()) {
                docenten.add(new Docent(rs.getInt("x"), rs.getInt("y"), rs.getString("familienaam"), rs.getString("beschrijving")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < docenten.size(); i++){
            if (docenten.get(i).getX() == 0 || docenten.get(i).getY() == 0){
                docenten.get(i).setX((int) (Math.random() * 220 + 920));
                docenten.get(i).setY((int) (Math.random() * 204 + 22));
            }
        }

    }

    public void initializeDefaultThreads(){
        thread = new Thread(new Runnable() {
            public void run() {
                while (1 == 1){
                    while (!gameover) {
                        int direction = (int) (Math.random() * 4);
                        //System.out.println(direction);

                        for (int i = 0; i < (int) (Math.random() * 25 + 5); i++) {
                            studentenPanel();
                            int x = studenten.get(2).getX();
                            int y = studenten.get(2).getY();
                            //System.out.println("x: " + x + ", y: " + y);
                            if (direction == 0 && x > 0) {
                                x -= 5;
                            } else if (direction == 1 && y > 0) {
                                y -= 5;
                            } else if (direction == 2 && x < 1200) {
                                x += 5;
                            } else if (direction == 3 && y < 550) {
                                y += 5;
                            }

                            if (!intersection(x, y, 15)) {
                                studenten.get(2).setX(x);
                                studenten.get(2).setY(y);
                                if (!gameover) {
                                    tekenPanel.revalidate();
                                    tekenPanel.repaint();
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
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
                }
            }
        });

        thread2  = new Thread(new Runnable() {
            public void run() {
                while (1 == 1){
                    while (!gameover){
                        boolean bounce = true;
                        int direction = (int) (Math.random() * 4);
                        //System.out.println(direction);

                        while(bounce && !gameover){
                            collectMushroom();
                            int x = mushroom.getX();
                            int y = mushroom.getY();

                            if (direction == 0 && x > 0){
                                x-=5;
                                y-=5;
                            } else if (direction == 1 && y > 0){
                                x-=5;
                                y+=5;
                            } else if (direction == 2 && x < 1200){
                                x+=5;
                                y+=5;
                            } else if (direction == 3 && y < 550){
                                x+=5;
                                y-=5;
                            }
                            if (!intersection(x, y, mushroom.getRadius())){
                                bounce = true;
                                mushroom.setX(x);
                                mushroom.setY(y);
                                if (!gameover) {
                                    tekenPanel.revalidate();
                                    tekenPanel.repaint();
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            } else {
                                bounce = false;
                            }
                        }
                    }
                }
            }
        });

        thread3 = new Thread(new Runnable() {
            public void run() {
                while (1 == 1){
                    if (dead){
                        goomba.setX(0);
                        goomba.setY(0);
                    } else {
                        goomba.setX(300);
                        goomba.setY(305);
                    }
                    while (!gameover && !dead){
                        boolean bounce = true;
                        int direction = (int) (Math.random() * 2);
                        //System.out.println(direction);

                        while(bounce && !gameover && !dead){
                            collideGoomba();
                            int x = goomba.getX();
                            int y = goomba.getY();

                            if (direction == 0){
                                x+=5;
                            } else if (direction == 1){
                                x-=5;
                            }
                            if (!intersection(x, y, goomba.getRadius())) {
                                bounce = true;
                                goomba.setX(x);
                                if (!gameover && !dead){
                                    tekenPanel.revalidate();
                                    tekenPanel.repaint();
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            } else {
                                bounce = false;
                            }
                        }
                    }

                }
            }
        });
    }

    private static Image laadAfbeelding(String bestand) {
        try {
            URL resource = EloictSimGui.class.getResource("/" + bestand + ".png");
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ImageIcon laadIcoon(String bestand) {
        return new ImageIcon(laadAfbeelding(bestand));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ELO-ICT SIM - anthony tacquet");
        frame.setContentPane(new EloictSimGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.pack();
        frame.setVisible(true);
        if(frame.getTitle().contains("student"))
            JOptionPane.showMessageDialog(
                    frame,
                    "Zorg dat je naam in de titel van je JFrame staat. \n" +
                            "Vervang hiervoor 'naam student' door je eigen naam in de code",
                    "Pas je naam aan",
                    JOptionPane.WARNING_MESSAGE);
    }

}
