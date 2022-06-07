package presentatie;

import logica.Beroepsprofiel;
import logica.Database;
import logica.Student;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
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
    private JButton searchButton;
    private JTextArea beschrijvingArea;
    private JScrollPane scrollPane;
    private JTextField xBewField;
    private JTextField yBewField;
    private JTextField xToeField;
    private JTextField yToeField;

    private DefaultTableModel defaultTable;
    private ArrayList<ArrayList<String>> list;

    private String username = "root";
    private String password = "Azerty123";
    private String dbms = "mysql";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String database = "eloictsim";

    public DataEditor(){
        initialize();

        addSaveButton.addActionListener(e -> {
            boolean exeptions = false;
            try {
                Database.addStudent(connection(), NaamToeField.getText(), VoornaamToeField.getText(), Integer.parseInt(xToeField.getText()), Integer.parseInt(yToeField.getText()), (Beroepsprofiel) BeroepToeCombo.getSelectedItem(), Integer.parseInt(JaarToeField.getText()), InfoToeArea.getText());
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
        });
        saveChangesButton.addActionListener(e -> {
            if (StudentenCombo.getSelectedItem().equals("<null>"))
                return;
            boolean error = false;
            try {
                int id = Database.getStudentId(connection(), (String) StudentenCombo.getSelectedItem());
                Student student = Database.getStudentInfo(connection(), id);
                if (!student.getNaam().equals(NaamBewField.getText()))
                    Database.updateStudent(connection(), id, NaamBewField.getText(), null, null, null, Beroepsprofiel.NULL, null, null);
                if (!student.getVoornaam().equals(VoorBewField.getText()))
                    Database.updateStudent(connection(), id, null, VoorBewField.getText(), null, null, Beroepsprofiel.NULL, null, null);
                if (student.getBeroepsprofiel() != BeroepBewCombo.getSelectedItem())
                    Database.updateStudent(connection(), id, null, null, null, null, (Beroepsprofiel) BeroepBewCombo.getSelectedItem(), null, null);
                if (student.getInschrijvingsjaar() != Integer.parseInt(JaarBewField.getText()))
                    Database.updateStudent(connection(), id, null, null, null, null, Beroepsprofiel.NULL, Integer.parseInt(JaarBewField.getText()), null);
                if (!student.getBeschrijving().equals(InfoArea.getText()))
                    Database.updateStudent(connection(), id, null, null, null, null, Beroepsprofiel.NULL, null, InfoArea.getText());
                if (student.getX() != Integer.parseInt(xBewField.getText()))
                    Database.updateStudent(connection(), id, null, null, Integer.parseInt(xBewField.getText()), null, Beroepsprofiel.NULL, null, null);
                if (student.getY() != Integer.parseInt(yBewField.getText()))
                    Database.updateStudent(connection(), id, null, null, null, Integer.parseInt(yBewField.getText()), Beroepsprofiel.NULL, null, null);

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

        });
        refreshButton.addActionListener(e -> {
            try {
                defaultTable = createDefaultTableModel();
                StudentTable.setModel(defaultTable);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        StudentenCombo.addActionListener(e -> {
            if (StudentenCombo.getSelectedItem().equals("<null>")){
                resetBewerken();
                return;
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
            xBewField.setText(Integer.toString(student.getX()));
            yBewField.setText(Integer.toString(student.getY()));
        });
        deleteButton.addActionListener(e -> {
            if (StudentenCombo.getSelectedItem().equals("<null>"))
                return;
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
        });
        searchButton.addActionListener(e -> {
            String[] naam = SearchField.getText().split(" - ");
            for (int i = 0; i < StudentenCombo.getItemCount(); i++){
                for (int j = 0; j < naam.length; j++){
                    if (StudentenCombo.getItemAt(i).contains(naam[j])){
                        StudentenCombo.setSelectedIndex(i);
                        SearchField.setText("");
                        return;
                    }
                }
            }
        });
        SearchField.addActionListener(e -> searchButton.doClick());
        StudentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                beschrijvingArea.setText((String) defaultTable.getValueAt(StudentTable.getSelectedRow(), 5));
            }
        });

    }

    public void initialize(){
        InfoArea.setLineWrap(true);
        InfoArea.setWrapStyleWord(true);
        InfoToeArea.setLineWrap(true);
        InfoToeArea.setWrapStyleWord(true);
        beschrijvingArea.setWrapStyleWord(true);
        beschrijvingArea.setLineWrap(true);
        beschrijvingArea.setEditable(false);
        beschrijvingArea.setOpaque(false);
        beschrijvingArea.setForeground(Color.WHITE);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        StudentTable.setOpaque(false);
        StudentTable.setForeground(Color.WHITE);
        ((DefaultTableCellRenderer)StudentTable.getDefaultRenderer(Object.class)).setOpaque(false);
        StudentTable.setGridColor(Color.WHITE);
    }

    public DefaultTableModel createDefaultTableModel() throws SQLException {
        list = Database.getAllStudentInfo(connection());
        String[] names = new String[]{"id", "Naam", "Voornaam", "Inschrijvingsjaar", "x", "y", "Beroepsprofiel", "Beschrijving"};
        String[][] strings = new String[list.size()][list.get(0).size()];
        for (int i = 0; i < list.size(); i++){
            for (int j = 0; j < list.get(i).size(); j++){
                strings[i][j] = list.get(i).get(j);
            }
        }
        return new DefaultTableModel(strings, names);
    }

    public void resetToevoegen(){
        NaamToeField.setText("");
        VoornaamToeField.setText("");
        BeroepToeCombo.setSelectedIndex(0);
        JaarToeField.setText("");
        InfoToeArea.setText("");
        xToeField.setText("");
        yToeField.setText("");
    }

    public void resetBewerken(){
        NaamBewField.setText("");
        VoorBewField.setText("");
        BeroepBewCombo.setSelectedIndex(0);
        JaarBewField.setText("");
        InfoArea.setText("");
        xBewField.setText("");
        yBewField.setText("");
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
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.setSize(800, 650);
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
            defaultTable = createDefaultTableModel();
            StudentTable = new JTable(defaultTable);
            StudentTable.setDefaultEditor(Object.class, null);
            StudentTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            // ----------------------------------
            StudentenCombo = new JComboBox<>();
            StudentenCombo.addItem("<null>");
            String[] namen = Database.getStudentenByName(connection());
            for (int i = 0 ; i < namen.length; i++){
                StudentenCombo.addItem(namen[i]);
            }
            StudentenCombo.setSelectedItem("<null>");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        RightPanel = new JPanel();
        RightPanel.setOpaque(false);

        LeftPanel = new JPanel();
        LeftPanel.setOpaque(false);

        BottomPanel = new JPanel();
        BottomPanel.setOpaque(false);
        BottomPanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.WHITE));

    }
}
