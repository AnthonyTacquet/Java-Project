package logica;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {

    public static void addStudent(Connection connection, String naam, String voornaam, Beroepsprofiel beroepsprofiel, int inschrijvingsjaar, String infopunt) throws Exception {
        if (naam == null || naam.equals(""))
            throw new IllegalArgumentException("Naam kan niet leeg zijn");
        if (voornaam == null || voornaam.equals(""))
            throw new IllegalArgumentException("Voornaam kan niet leeg zijn");
        if (Integer.toString(inschrijvingsjaar).length() != 4 || inschrijvingsjaar > LocalDate.now().getYear())
            throw new IllegalArgumentException("Inschrijvingsjaar is incorrect");
        if (beroepsprofiel == Beroepsprofiel.NULL)
            throw new IllegalArgumentException("Student moet beroepsprofiel hebben");

        Integer idPersoon = null;
        Integer idBeroep = null;
        String insertInfopunt = "";

        // Insert in table
        String insertPersonen = "insert into personen (familienaam, voornaam) values (\"" + naam + "\", \"" + voornaam + "\");";
        Statement stmt1 = connection.createStatement();
        stmt1.executeUpdate(insertPersonen);

        // Get ID personen table
        String selectId = "select id from personen where familienaam = \"" + naam + "\";";
        Statement stmt2 = connection.createStatement();
        ResultSet rs2 = stmt2.executeQuery(selectId);
        if (rs2.next()){
            idPersoon = (Integer) rs2.getInt("id");
        }

        if (idPersoon == null)
            throw new Exception("Student toevoegen is mislukt!");

        // Get ID Beroepsprofiel table
        String selectBeroepId = "select id from beroepsprofielen where naam = \"" + beroepsprofiel.toString().toUpperCase() + "\";";
        Statement stmt3 = connection.createStatement();
        ResultSet rs3 = stmt3.executeQuery(selectBeroepId);
        if (rs3.next()){
            idBeroep = (Integer) rs3.getInt("id");
        }

        // Insert in studenten table
        String insertStudenten = "insert into studenten (id, beroepsprofiel_id, inschrijvingsjaar) values (" + idPersoon + ", " + idBeroep + ", " + inschrijvingsjaar + ");";
        Statement stmt4 = connection.createStatement();
        stmt4.executeUpdate(insertStudenten);

        // Insert in Informatiepunten table
        if (infopunt.equals(""))
            insertInfopunt = "insert into informatiepunten (persoon_id, beschrijving) values (" + idPersoon + ", null);";
        else
            insertInfopunt = "insert into informatiepunten (x, y, persoon_id, beschrijving) values (0, 0, " + idPersoon + ", \"" + infopunt + "\");";
        Statement stmt5 = connection.createStatement();
        stmt5.executeUpdate(insertInfopunt);

        connection.close();
    }

    public static void removeStudent(Connection connection, int id) throws SQLException {

        // Delete student
        String deleteStudent = "delete from studenten where id = " + id + ";";
        Statement stmt1 = connection.createStatement();
        stmt1.executeUpdate(deleteStudent);

        // Delete informatiepunt
        String deleteInformatiepunt = "delete from informatiepunten where persoon_id = " + id + ";";
        Statement stmt2 = connection.createStatement();
        stmt2.executeUpdate(deleteInformatiepunt);

        // Delete persoon
        String deletePersonen = "delete from personen where id = " + id + ";";
        Statement stmt3 = connection.createStatement();
        stmt3.executeUpdate(deletePersonen);

        connection.close();
    }

    public static String[] getStudentenByName(Connection connection) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        String deletePersonen = "select * from personen inner join studenten on personen.id = studenten.id;";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(deletePersonen);
        while (resultSet.next()){
            String naam = resultSet.getString("voornaam");
            naam += " - ";
            naam += resultSet.getString("familienaam");
            list.add(naam);
        }

        connection.close();
        return list.toArray(new String[0]);
    }

    public static Integer getStudentId(Connection connection, String naamEnVoornaam) throws SQLException {
        Integer id = null;
        String[] naam = naamEnVoornaam.split(" - ");
        String getId = "select personen.id from personen inner join studenten on personen.id = studenten.id where voornaam = \"" + naam[0] + "\" and familienaam = \"" + naam[1] + "\";";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(getId);
        if (resultSet.next()){
            id = resultSet.getInt("id");
        }

        connection.close();
        return id;
    }

    public static String[] getAllStudentInfo(Connection connection, Integer id) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        String getPersoon = "select * from personen inner join studenten on personen.id = studenten.id inner join beroepsprofielen on studenten.beroepsprofiel_id = beroepsprofielen.id inner join informatiepunten on informatiepunten.persoon_id = personen.id where personen.id = " + id + ";";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(getPersoon);
        while (resultSet.next()){
            list.add(resultSet.getString("familienaam"));
            list.add(resultSet.getString("voornaam"));
            list.add(resultSet.getString("naam"));
            list.add(resultSet.getString("inschrijvingsjaar"));
            list.add(resultSet.getString("beschrijving"));
        }

        connection.close();
        return list.toArray(new String[0]);
    }
}
