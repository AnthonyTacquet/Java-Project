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
    private JLabel beschrijvingDocenten;

    private Image background;
    private Image lokaalKlein, studentKlein, docentKlein;
    private ImageIcon lokaalGroot, studentGroot, docentGroot;

    private Image[] marios = new Image[12];
    private Image[] mariosGroot = new Image[12];

    private Image mushy, goomby, game, bally, bowsery, breath;

    private Image marioCurrent;

    Thread studentenThread, mushroomThread, goombaThread, bowserThread;

    Thread[] ball = new Thread[10];
    Bowser bowser = new Bowser(800, 310, 60);

    ArrayList<Lokaal> lokalen = new ArrayList<>();
    ArrayList<Deur> deuren = new ArrayList<>();
    ArrayList<Student> studenten = new ArrayList<>();
    ArrayList<Docent> docenten = new ArrayList<>();
    ArrayList<Goomba> goomba = new ArrayList<>();
    ArrayList<Student> studentenFree = new ArrayList<>();
    ArrayList<Mushroom> mushroom = new ArrayList<>();
    ArrayList<FireBreath> fireBreath = new ArrayList<>();
    ArrayList<Vuurbal> vuurballen = new ArrayList<>();
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
    int backgroundx = 0;
    int backgroundy = 0;

    boolean fire = true;

    public EloictSimGui() {
        mushroomPanel.setPreferredSize(new Dimension(32, 16));
        tekenPanel.setFocusable(true);
        setPlayground();
        initializeDefaultThreads();
        checkThread();
        marioCurrent = marios[0];
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
        int radius = rad - rad / 2;
        for (int i = 0; i < deuren.size(); i++){
            int x1 = deuren.get(i).getX1();
            int x2 = deuren.get(i).getX2();
            int y1 = deuren.get(i).getY1();
            int y2 = deuren.get(i).getY2();


            if (Meetkunde.cirkelOverlaptMetLijnstuk(deuren.get(i), x, y, rad)){
                if (y1 == y2)
                    if (x2 + radius <= x && x1 - radius >= x)
                        return false;
                if (x1 == x2)
                    if (y2 + 2 - radius >= y && y1 - 2 + radius <= y)
                        return false;
            }

        }
        for (int i = 0; i < lokalen.size(); i++){
            if (Meetkunde.cirkelOverlaptMetRechthoek(lokalen.get(i), x, y, rad - 2))
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
            this.voornaam.setText(docenten.get(numberdocent).getVoornaam());
            this.beschrijvingDocenten.setText(docenten.get(numberdocent).getBeschrijving());
            this.vakken.setText("<html>" + docenten.get(numberdocent).getVakkenString() + "</html>");

        }
        if (numberdocent == -10){
            voornaam.setText("");
            achternaam.setText("");
            beschrijvingDocenten.setText("");
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
        vuurballen.add(new Vuurbal());
        int locatie = 0;
        while (vuurballen.size() > 10){
             locatie = vuurballen.size() - 1;
        }

        vuurballen.get(locatie).setY(y);
        if (!links)
            vuurballen.get(locatie).setX(x + centerx);
        else
            vuurballen.get(locatie).setX(x - centerx);

        int finalLocatie = locatie;
        ball[locatie] = new Thread(new Runnable() {
            @Override
            public void run() {
                playSound("mario/sound/fireball");
                boolean rechts = !links;
                boolean down = true;
                boolean hit = false;
                int height = 0;
                for (int i = 0; i < 3; i++){
                    boolean bounce = true;
                    while (bounce && !gameover) {
                        for (int j = 0; j < goomba.size(); j++){
                            if (goomba.get(j).intersect(vuurballen.get(finalLocatie).getX(), vuurballen.get(finalLocatie).getY(), 4)){
                                playSound("mario/sound/kick");
                                goomba.get(j).setDead(true);
                                hit = true;
                                j = 5;
                            }
                        }
                        if (bowser.intersect(vuurballen.get(finalLocatie).getX(), vuurballen.get(finalLocatie).getY(), 4)){
                            playSound("mario/sound/kick");
                            bowser.addHit();
                            hit = true;
                        }
                        int x = vuurballen.get(finalLocatie).getX();
                        int y = vuurballen.get(finalLocatie).getY();
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

                        if (intersection(vuurballen.get(finalLocatie).getX(), vuurballen.get(finalLocatie).getY(), 4))
                            hit = true;

                        if (!intersection(x, y, 4) && !hit) {
                            bounce = true;
                            vuurballen.get(finalLocatie).setX(x);
                            vuurballen.get(finalLocatie).setY(y);

                            if (!gameover) {
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
                    if (hit){
                        vuurballen.get(finalLocatie).setX(5000);
                        vuurballen.get(finalLocatie).setY(0);
                        tekenPanel.repaint();
                        return;
                    }

                }
                vuurballen.get(finalLocatie).setX(5000);
                vuurballen.get(finalLocatie).setY(0);
                tekenPanel.repaint();
            }
        });
        ball[locatie].start();
    }

    public boolean collideGoomba(Goomba goomba){
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
            }
            return true;
        }
        return false;
    }

    public void collideBowser(){
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
        }
    }

    public boolean collectMushroom(Mushroom mushroom){
        if (mushroom.intersect(this.x, this.y, this.radius)){
            playSound("mario/sound/powerup");
            collected++;
            if (!size){
                size = true;
                this.radius = 32;
                this.centerx = 32;
                this.centery = 32;
                grow();
            }

            if (size){
                marioCurrent = mariosGroot[0];
            }

            mushroomPanel.repaint();
            tekenPanel.repaint();
            return true;
        }
        return false;
    }

    public void grow(){
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
        bowsery = laadAfbeelding("mario/bowser");
        breath = laadAfbeelding("mario/breath");

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
                    g2.drawImage(background, backgroundx, backgroundy, null);

                if (fire)
                    for (int i = 0; i < vuurballen.size(); i++){
                        if (vuurballen.get(i) != null)
                            g2.drawImage(bally, vuurballen.get(i).getX() - 4, vuurballen.get(i).getY() - 4, null);
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

                if (!bowser.isDead()){
                    g2.drawImage(bowsery, bowser.getX() - bowser.getRadius(), bowser.getY() - bowser.getRadius(), null);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(bowser.getX() - (bowser.getRadius()), bowser.getY() - (bowser.getRadius() - 2), bowser.getRadius(), 5);
                    g2.setColor(Color.ORANGE);
                    g2.fillRect(bowser.getX() - (bowser.getRadius()), bowser.getY() - (bowser.getRadius() - 2),60 - bowser.getHit() * 3, 5);
                }

                if (!gameover){
                    for (int i = 0; i < mushroom.size(); i++){
                        g2.drawImage(mushy, mushroom.get(i).getX() - 8, mushroom.get(i).getY() - 8,  null);
                    }
                    for (int i = 0; i < goomba.size(); i++){
                        g2.drawImage(goomby, goomba.get(i).getX() - 8, goomba.get(i).getY() - 8, null);
                    }
                    for (int i = 0; i < fireBreath.size(); i++){
                        if (fireBreath.get(i) != null && fireBreath.get(i).getThread() != null){
                            g2.drawImage(breath, fireBreath.get(i).getX() - 8, fireBreath.get(i).getY() - 8, null);
                        }
                    }
                }

                if (gameover){
                    g2.drawImage(game, 0, 0, null);
                    mushroom.clear();
                    goomba.clear();
                    vuurballen.clear();
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
                                    x = 50;
                                    y = 250;
                                    collected = 0;
                                    checkThread();
                                    meldingPanel.setVisible(true);
                                    tekenPanel.repaint();
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

    public synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                if (gameover && !url.equals("mario/sound/dead"))
                    return;
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


    public void checkThread(){
        for (int i = 0; i < 2; i++){
            goomba.add(new Goomba((300 + (i * 50)), 305));
            goomba.get(i).createThread();
        }

        goombaThread = new Thread(() -> {
            while (true){
                for (int i = 0; i < goomba.size(); i++){
                    if (collideGoomba(goomba.get(i)))
                        goomba.get(i).setDead(true);
                    if (intersection(goomba.get(i).getX(), goomba.get(i).getY(), goomba.get(i).getRadius())){
                        goomba.get(i).setCollision(true);
                    }
                    goomba.get(i).setGameover(gameover);
                    if (goomba.get(i).isRepaint()){
                        tekenPanel.repaint();
                        goomba.get(i).setRepaint(false);
                    }
                }
            }
        });
        goombaThread.start();

        for (int i = 0; i < 1; i++){
            mushroom.add(new Mushroom((lokalen.get(i + 2).getX() + (lokalen.get(i + 2).getB() / 2)), (lokalen.get(i + 2).getY() + (lokalen.get(i + 2).getH() / 2))));
            mushroom.get(i).createThread();
        }
        mushroomThread  = new Thread(() -> {
            while (true){
                for (int i = 0; i < mushroom.size(); i++){
                    if (collectMushroom(mushroom.get(i))){
                        Lokaal lokaal = lokalen.get((int) (Math.random() * lokalen.size()));

                        if (collected < 2){
                            mushroom.get(i).setX((int) (Math.random() * (lokaal.getX() + 20) + (lokaal.getB() - 20)));
                            mushroom.get(i).setY((int) (Math.random() * (lokaal.getY() + 20) + (lokaal.getH() - 20)));
                        } else {
                            mushroom.get(i).setDead(true);
                        }
                    }
                    mushroom.get(i).setCollideRight(intersection(mushroom.get(i).getX() + 5, mushroom.get(i).getY(), mushroom.get(i).getRadius()));
                    mushroom.get(i).setCollideLeft(intersection(mushroom.get(i).getX() - 5, mushroom.get(i).getY(), mushroom.get(i).getRadius()));
                    mushroom.get(i).setCollideDown(intersection(mushroom.get(i).getX(), mushroom.get(i).getY() + 5, mushroom.get(i).getRadius()));
                    mushroom.get(i).setCollideUp(intersection(mushroom.get(i).getX(), mushroom.get(i).getY() - 5, mushroom.get(i).getRadius()));
                    mushroom.get(i).setGameover(gameover);
                    if (mushroom.get(i).isRepaint()){
                        tekenPanel.repaint();
                        mushroom.get(i).setRepaint(false);
                    }
                }
            }
        });
        mushroomThread.start();

        bowser.createThread();

        bowserThread = new Thread(() -> {
            while (true){
                setFireBreath();
                if (bowser.intersect(this.x, this.y, this.radius))
                    collideBowser();
                if (bowser.isShiver()){
                    shiver();
                    bowser.setShiver(false);
                }
                int hit = bowser.getHit();
                if (hit == 20){
                    if (!bowser.isDead())
                        playSound("mario/sound/bowserfalls");
                    bowser.setDead(true);
                }
                bowser.setGameover(gameover);
                if (bowser.isRepaint()){
                    if ((int) (Math.random() * 10) == 1){
                        fireBreath.add(new FireBreath(bowser.getX() + 15, bowser.getY() - 20, 8, true));
                    }
                    tekenPanel.repaint();
                    bowser.setRepaint(false);
                }
            }

        });
        bowserThread.start();
    }

    public void setFireBreath(){
        Thread fire = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < fireBreath.size(); i++){
                    if (fireBreath.get(i) != null){
                        if (fireBreath.get(i).intersect(x,y,radius)){
                            collideBowser();
                        }
                        fireBreath.get(i).setGameover(gameover);
                        if (intersection(fireBreath.get(i).getX(), fireBreath.get(i).getY(), fireBreath.get(i).getRadius())){
                            fireBreath.get(i).setCollision(true);
                        }
                    }
                }
            }
        });
        fire.start();
    }

    public void shiver(){
        Thread shivering = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 2; i++){
                        backgroundx = 5;
                        tekenPanel.repaint();
                        Thread.sleep(100);
                        backgroundy = 5;
                        tekenPanel.repaint();
                        Thread.sleep(100);
                        backgroundx = -5;
                        tekenPanel.repaint();
                        Thread.sleep(100);
                        backgroundy = -5;
                        tekenPanel.repaint();
                        Thread.sleep(100);
                    }
                    backgroundx = 0;
                    backgroundy = 0;
                }catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        shivering.start();
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
        Connection conn = null;
        //Set Lokalen
        String queryLokalen = "select * from lokalen;";
        try{
            conn = connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryLokalen);
            while (rs.next()) {
                lokalen.add(new Lokaal(rs.getString("naam"), rs.getString("lokaalcode"), rs.getInt("x"), rs.getInt("y"), rs.getInt("breedte"), rs.getInt("lengte")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //Set Deuren
        String queryDeuren = "select * from deuren;";
        try{
            conn = connection();
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
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //Set Studenten
        String queryStudent = "select x, y, informatiepunten.beschrijving, personen.familienaam, personen.voornaam , beroepsprofielen.naam from personen inner join studenten on studenten.id = personen.id inner join beroepsprofielen on beroepsprofielen.id = beroepsprofiel_id left join keuzevakken on studenten.id = keuzevakken.student_id left join informatiepunten on personen.id = informatiepunten.persoon_id;";
        try{
            conn = connection();
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
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        studenten.get(2).setX(this.x + 200);
        studenten.get(2).setY(this.y);


        //Set Docenten
        String queryDocent = "select personen.familienaam, personen.voornaam, x, y, informatiepunten.beschrijving, vakken.naam from personen inner join docenten on personen.id = docenten.id inner join docenten_has_vakken on docenten_has_vakken.docent_id = docenten.id inner join vakken on docenten_has_vakken.vak_id = vakken.id inner join informatiepunten on informatiepunten.persoon_id = personen.id;";
        try{
            conn = connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryDocent);
            while (rs.next()) {
                Docent docent = new Docent();
                docent.setNaam(rs.getString("familienaam"));
                docent.setVoornaam(rs.getString("voornaam"));
                docent.setX(rs.getInt("x"));
                docent.setY(rs.getInt("y"));
                docent.setBeschrijving(rs.getString("beschrijving"));
                docenten.add(docent);
            }
            for (int i = 0; i < docenten.size(); i++){
                String naam = docenten.get(i).getNaam();
                String query2 = "select vakken.naam from personen inner join docenten on docenten.id = personen.id inner join docenten_has_vakken on docenten_has_vakken.docent_id = docenten.id inner join vakken on docenten_has_vakken.vak_id = vakken.id " + "where familienaam = \"" + naam + "\";";
                ResultSet result = stmt.executeQuery(query2);
                ArrayList<String> vakken = new ArrayList<>();
                while (result.next()){
                    vakken.add(result.getString("naam"));
                }
                docenten.get(i).setVakken(vakken);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < docenten.size(); i++){
            if (docenten.get(i).getX() == 0 || docenten.get(i).getY() == 0){
                docenten.get(i).setX((int) (Math.random() * 220 + 920));
                docenten.get(i).setY((int) (Math.random() * 204 + 22));
            }
        }


    }

    public void initializeDefaultThreads(){
        studentenThread = new Thread(new Runnable() {
            public void run() {
                while (true){
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
