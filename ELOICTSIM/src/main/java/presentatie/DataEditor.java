package presentatie;

import logica.Beroepsprofiel;
import logica.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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

    private String username = "root";
    private String password = "Azerty123";
    private String dbms = "mysql";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String database = "eloictsim";

    public DataEditor(){

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
                    resetToevoegen();
                }
            }
        });
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        StudentenCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (StudentenCombo.getSelectedItem().equals("null")){
                    NaamBewField.setText("");
                    VoorBewField.setText("");
                    BeroepBewCombo.setSelectedIndex(0);
                    JaarBewField.setText("");
                    InfoArea.setText("");
                }
                String[] info;
                try {
                    info = Database.getAllStudentInfo(connection(), Database.getStudentId(connection(), (String) StudentenCombo.getSelectedItem()));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                NaamBewField.setText(info[0]);
                VoorBewField.setText(info[1]);
                switch (info[2]){
                    case "WEB_MOBILE_DEVELOPER": BeroepBewCombo.setSelectedItem(Beroepsprofiel.WEB_MOBILE_DEVELOPER); break;
                    case "SOFTWARE_AI_DEVELOPER": BeroepBewCombo.setSelectedItem(Beroepsprofiel.SOFTWARE_AI_DEVELOPER); break;
                    case "INTERNET_OF_THINGS_DEVELOPER": BeroepBewCombo.setSelectedItem(Beroepsprofiel.INTERNET_OF_THINGS_DEVELOPER); break;
                    case "NETWORK_SECURITY_ENGINEER": BeroepBewCombo.setSelectedItem(Beroepsprofiel.NETWORK_SECURITY_ENGINEER); break;
                    case "TELECOMMUNICATIONS_ENGINEER": BeroepBewCombo.setSelectedItem(Beroepsprofiel.TELECOMMUNICATIONS_ENGINEER); break;
                }
                JaarBewField.setText(info[3]);
                InfoArea.setText(info[4]);
                RightPanel.repaint();
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
                    resetBewerken();
                }
                RightPanel.repaint();
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
        frame.setSize(1000, 500);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        BeroepToeCombo = new JComboBox<>(Beroepsprofiel.values());
        BeroepBewCombo = new JComboBox<>(Beroepsprofiel.values());
        try {
            StudentenCombo = new JComboBox<>(Database.getStudentenByName(connection()));
            StudentenCombo.addItem("null");
            StudentenCombo.setSelectedItem("null");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        RightPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                String[] studenten;
                try {
                     studenten = Database.getStudentenByName(connection());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                StudentenCombo = new JComboBox<>(studenten);
                StudentenCombo.setSelectedItem("null");
            }
        };
    }
}
