package presentatie;

import jdk.jfr.DataAmount;
import logica.Beroepsprofiel;
import logica.Database;
import logica.Student;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyledEditorKit;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class DataEditor extends javax.swing.JPanel{
    private JTextField NaamToeField;
    private JTextField VoornaamToeField;
    private JTextField JaarToeField;
    private JTextField SearchField;
    private JComboBox<Beroepsprofiel> BeroepToeCombo;
    private JComboBox<String> StudentenCombo;
    private JTextField NaamBewField;
    private JTextField VoorBewField;
    private JTextField JaarBewField;
    private JTextArea InfoArea;
    private JComboBox<Beroepsprofiel> BeroepBewCombo;
    private JButton refreshButton;
    private JButton addSaveButton;
    private JButton saveChangesButton;
    private JTable StudentTable;
    private JPanel MainPanel;
    private JLabel errorLabel;
    private JTextArea InfoToeArea;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JPanel BottomPanel;
    private JButton deleteButton;

    private DefaultTableModel defaultTable;
    private ArrayList<ArrayList<String>> list;

    private String username = "root";
    private String password = "Azerty123";
    private String dbms = "mysql";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String database = "eloictsim";

    public DataEditor(){
        InfoArea.setLineWrap(true);
        InfoArea.setWrapStyleWord(true);
        InfoToeArea.setLineWrap(true);
        InfoToeArea.setWrapStyleWord(true);
        addSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean exeptions = false;
                try {
                    Database.addStudent(connection(), NaamToeField.getText(), VoornaamToeField.getText(), (Beroepsprofiel) BeroepToeCombo.getSelectedItem(), Integer.parseInt(JaarToeField.getText()), InfoToeArea.getText());
                } catch (Exception exception){
                    exeptions = true;
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText(exception.getMessage());
                }
                if (!exeptions){
                    errorLabel.setForeground(Color.GREEN);
                    errorLabel.setText("Student werd succesvol toegevoegd");
                    StudentenCombo.addItem(VoornaamToeField.getText() + " - " + NaamToeField.getText());
                    resetToevoegen();
                }
            }
        });
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean error = false;
                try {
                    int id = Database.getStudentId(connection(), (String) StudentenCombo.getSelectedItem());
                    Student student = Database.getStudentInfo(connection(), id);
                    if (!student.getNaam().equals(NaamBewField.getText()))
                        Database.updateStudent(connection(), id, NaamBewField.getText(), null, Beroepsprofiel.NULL, null, null);
                    if (!student.getVoornaam().equals(VoorBewField.getText()))
                        Database.updateStudent(connection(), id, null, VoornaamToeField.getText(), Beroepsprofiel.NULL, null, null);
                    if (student.getBeroepsprofiel() != BeroepBewCombo.getSelectedItem())
                        Database.updateStudent(connection(), id, null, null, (Beroepsprofiel) BeroepBewCombo.getSelectedItem(), null, null);
                    if (student.getInschrijvingsjaar() != Integer.parseInt(JaarBewField.getText()))
                        Database.updateStudent(connection(), id, null, null, Beroepsprofiel.NULL, Integer.parseInt(JaarBewField.getText()), null);
                    if (!student.getBeschrijving().equals(InfoArea.getText()))
                        Database.updateStudent(connection(), id, null, null, Beroepsprofiel.NULL, null, InfoArea.getText());


                } catch (SQLException ex) {
                    error = true;
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText(ex.getMessage());
                } catch (Exception ex) {
                    error = true;
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText(ex.getMessage());
                }
                if (!error){
                    errorLabel.setForeground(Color.GREEN);
                    errorLabel.setText("De aanpassingen waren succesvol");
                }

            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //defaultTable.addRow(new Object[]{"ja", "nee"});
                try {
                    ArrayList<Integer> list1 = new ArrayList<>();
                    ArrayList<Integer> list2 = new ArrayList<>();
                    ArrayList<ArrayList<String>> changelist = Database.getAllStudentInfo(connection());
                    // list 1
                    for (int i = 0; i < list.size(); i++){
                        list1.add(Integer.parseInt(list.get(i).get(0)));
                    }
                    // list 2
                    for (int i = 0; i < changelist.size(); i++){
                        list2.add(Integer.parseInt(changelist.get(i).get(0)));
                    }

                    for (int i = 0; i < list2.size(); i++){
                        if (!list1.contains(list2.get(i))){
                            Student student = Database.getStudentInfo(connection(), list2.get(i));
                            defaultTable.addRow(new Object[]{Integer.toString(list2.get(i)), student.getNaam(), student.getVoornaam(), student.getInschrijvingsjaar(), student.getBeroepsprofiel().toString(), student.getBeschrijving()});
                        }
                    }
                    for (int i = 0; i < list1.size(); i++){
                        if (!list2.contains(list1.get(i))){
                            defaultTable.removeRow(i);
                        }
                    }
                    list = changelist;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        StudentenCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(StudentenCombo.getSelectedItem());
                System.out.println(StudentenCombo.getSelectedIndex());
                if (StudentenCombo.getSelectedItem().equals("null")){
                    NaamBewField.setText("");
                    VoorBewField.setText("");
                    BeroepBewCombo.setSelectedIndex(0);
                    JaarBewField.setText("");
                    InfoArea.setText("");
                }
                Student student;
                try {
                    student = Database.getStudentInfo(connection(), Database.getStudentId(connection(), (String) StudentenCombo.getSelectedItem()));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                NaamBewField.setText(student.getNaam());
                VoorBewField.setText(student.getVoornaam());
                BeroepBewCombo.setSelectedItem(student.getBeroepsprofiel());
                JaarBewField.setText(Integer.toString(student.getInschrijvingsjaar()));
                InfoArea.setText(student.getBeschrijving());
            }
        });
        SearchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean exeptions = false;
                int id;
                int answer = JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze student wilt verwijderen?", "Delete student", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION){
                    try {
                        id = Database.getStudentId(connection(), (String) StudentenCombo.getSelectedItem());
                        Database.removeStudent(connection(), id);
                    } catch (Exception exception){
                        exeptions = true;
                        errorLabel.setForeground(Color.RED);
                        errorLabel.setText(exception.getMessage());
                    }
                    if (!exeptions){
                        errorLabel.setForeground(Color.GREEN);
                        errorLabel.setText("Student was succesvol verwijderd");
                        StudentenCombo.removeItem(StudentenCombo.getSelectedItem());
                        resetBewerken();
                    }
                }
            }
        });
    }

    public void resetToevoegen(){
        NaamToeField.setText("");
        VoornaamToeField.setText("");
        BeroepToeCombo.setSelectedIndex(0);
        JaarToeField.setText("");
        InfoToeArea.setText("");
    }

    public void resetBewerken(){
        NaamBewField.setText("");
        VoorBewField.setText("");
        BeroepBewCombo.setSelectedIndex(0);
        JaarBewField.setText("");
        InfoArea.setText("");
    }

    public Connection connection() throws  SQLException{
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.username);
        connectionProps.put("password", this.password);
        conn = DriverManager.getConnection("jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/" + this.database, connectionProps);
        return conn;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataEditor");
        frame.setContentPane(new DataEditor().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
        BeroepToeCombo = new JComboBox<>(Beroepsprofiel.values());
        BeroepBewCombo = new JComboBox<>(Beroepsprofiel.values());
        try {
            list = Database.getAllStudentInfo(connection());
            String[] names = new String[]{"id", "Naam", "Voornaam", "Inschrijvingsjaar", "Beroepsprofiel", "Beschrijving"};
            String[][] strings = new String[list.size()][list.get(0).size()];
            for (int i = 0; i < list.size(); i++){
                for (int j = 0; j < list.get(i).size(); j++){
                    strings[i][j] = list.get(i).get(j);
                }
            }
            defaultTable = new DefaultTableModel(strings, names);
            StudentTable = new JTable(defaultTable);
            StudentTable.setDefaultEditor(Object.class, null);
            StudentTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            // ----------------------------------
            StudentenCombo = new JComboBox<>(Database.getStudentenByName(connection()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        RightPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                RightPanel.setOpaque(false);
            }
        };
        LeftPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                LeftPanel.setOpaque(false);
            }
        };
        BottomPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                BottomPanel.setOpaque(false);
            }
        };
    }
}
