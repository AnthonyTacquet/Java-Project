package logica;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {

    public static void addStudent(Connection connection, String naam, String voornaam, Integer x, Integer y, Beroepsprofiel beroepsprofiel, int inschrijvingsjaar, String infopunt) throws SQLException {
        if (naam == null || naam.equals(""))
            throw new IllegalArgumentException("Naam kan niet leeg zijn");
        if (voornaam == null || voornaam.equals(""))
            throw new IllegalArgumentException("Voornaam kan niet leeg zijn");
        if (Integer.toString(inschrijvingsjaar).length() != 4 || inschrijvingsjaar > LocalDate.now().getYear())
            throw new IllegalArgumentException("Inschrijvingsjaar is incorrect");
        if (beroepsprofiel == Beroepsprofiel.NULL)
            throw new IllegalArgumentException("Student moet beroepsprofiel hebben");
        if (x == null)
            x = 0;
        if (y == null)
            y = 0;

        Integer idPersoon = null;
        Integer idBeroep = null;

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Get ID personen table
            PreparedStatement stmt2 = connection.prepareStatement("select (min(id) + 1) as minID from personen table1 where not exists (select id from personen table2 where table1.id + 1 = table2.id);");
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()){
                idPersoon = (Integer) rs2.getInt("minID");
            }
            System.out.println(idPersoon);

            if (idPersoon == null)
                throw new Exception("Student toevoegen is mislukt!");

            // Insert in table
            PreparedStatement stmt1 = connection.prepareStatement("insert into personen (id, familienaam, voornaam) values (?, ?, ?);");
            stmt1.setInt(1, idPersoon);
            stmt1.setString(2, naam);
            stmt1.setString(3, voornaam);
            stmt1.executeUpdate();

            // Start new transaction
            connection.commit();

            // Get ID Beroepsprofiel table
            PreparedStatement stmt3 = connection.prepareStatement("select id from beroepsprofielen where naam = ?;");
            stmt3.setString(1, beroepsprofiel.toString().toUpperCase());
            ResultSet rs3 = stmt3.executeQuery();
            if (rs3.next()){
                idBeroep = (Integer) rs3.getInt("id");
            }
            System.out.println(idBeroep);

            // Insert in studenten table
            PreparedStatement stmt4 = connection.prepareStatement("insert into studenten (id, beroepsprofiel_id, inschrijvingsjaar) values (?, ?, ?);");
            stmt4.setInt(1, idPersoon);
            stmt4.setInt(2, idBeroep);
            stmt4.setInt(3, inschrijvingsjaar);
            stmt4.executeUpdate();

            // Insert in Informatiepunten table
            if (infopunt.equals("")){
                PreparedStatement stmt5 = connection.prepareStatement("insert into informatiepunten (x, y, persoon_id, beschrijving) values (?, ?, ?, null);");
                stmt5.setInt(1, x);
                stmt5.setInt(2, y);
                stmt5.setInt(3, idPersoon);
                stmt5.executeUpdate();

            }
            else{
                PreparedStatement stmt5 = connection.prepareStatement("insert into informatiepunten (x, y, persoon_id, beschrijving) values (?, ?, ?, ?);");
                stmt5.setInt(1, x);
                stmt5.setInt(2, y);
                stmt5.setInt(3, idPersoon);
                stmt5.setString(4, infopunt);
                stmt5.executeUpdate();
            }

            // Commit changes
            connection.commit();

        } catch (Exception e){
            connection.rollback();
            throw new SQLException("Runtime error: " + e.getMessage());
        } finally {
            connection.close();
        }



    }

    public static void updateStudent(Connection connection, int id, String naam, String voornaam, Integer x, Integer y, Beroepsprofiel beroepsprofiel, Integer inschrijvingsjaar, String infopunt) throws Exception {
        try {
            //Start transaction
            connection.setAutoCommit(false);

            // Update Personen
            if (naam != null){
                PreparedStatement stmt = connection.prepareStatement("update personen set familienaam = ? where id = ?;");
                stmt.setString(1, naam);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            if (voornaam != null){
                PreparedStatement stmt = connection.prepareStatement("update personen set voornaam = ? where id = ?;");
                stmt.setString(1, voornaam);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // Update Studenten
            if (beroepsprofiel != Beroepsprofiel.NULL){
                PreparedStatement stmt = connection.prepareStatement("update studenten set studenten.beroepsprofiel_id = (select beroepsprofielen.id from beroepsprofielen where beroepsprofielen.naam = ?) where studenten.id = ?;");
                stmt.setString(1, beroepsprofiel.toString().toUpperCase());
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            if (inschrijvingsjaar != null){
                PreparedStatement stmt = connection.prepareStatement("update studenten set studenten.inschrijvingsjaar = ? where id = ?;");
                stmt.setInt(1, inschrijvingsjaar);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // Update informatiepunten
            if (infopunt != null){
                PreparedStatement stmt = connection.prepareStatement("update informatiepunten set informatiepunten.beschrijving = ? where informatiepunten.persoon_id = ?;");
                stmt.setString(1, infopunt);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // Update informatiepunten
            if (x != null){
                PreparedStatement stmt = connection.prepareStatement("update informatiepunten set informatiepunten.x = ? where informatiepunten.persoon_id = ?;");
                stmt.setInt(1, x);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            if (y != null){
                PreparedStatement stmt = connection.prepareStatement("update informatiepunten set informatiepunten.y = ? where informatiepunten.persoon_id = ?;");
                stmt.setInt(1, y);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // Commit changes
            connection.commit();

        } catch (Exception exception){
            connection.rollback();
        } finally {
            connection.close();
        }
    }



    public static void removeStudent(Connection connection, int id) throws SQLException {

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Delete student
            PreparedStatement stmt1 = connection.prepareStatement("delete from studenten where id = ?;");
            stmt1.setInt(1, id);
            stmt1.executeUpdate();

            // Delete informatiepunt
            PreparedStatement stmt2 = connection.prepareStatement("delete from informatiepunten where persoon_id = ?;");
            stmt2.setInt(1, id);
            stmt2.executeUpdate();

            // Delete persoon
            PreparedStatement stmt3 = connection.prepareStatement("delete from personen where id = ?;");
            stmt3.setInt(1, id);
            stmt3.executeUpdate();

            // Commit changes
            connection.commit();
        } catch (Exception exception){
            connection.rollback();
            System.out.println(exception);
        } finally {
            connection.close();
        }
    }

    public static String[] getStudentenByName(Connection connection) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement("select * from personen inner join studenten on personen.id = studenten.id;");
        ResultSet resultSet = stmt.executeQuery();
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
        PreparedStatement stmt = connection.prepareStatement("select personen.id from personen inner join studenten on personen.id = studenten.id where voornaam = ? and familienaam = ?;");
        stmt.setString(1, naam[0]);
        stmt.setString(2, naam[1]);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()){
            id = resultSet.getInt("id");
        }

        connection.close();
        return id;
    }

    public static Student getStudentInfo(Connection connection, Integer id) throws SQLException {
        Student student = new Student();
        PreparedStatement stmt = connection.prepareStatement("select * from personen inner join studenten on personen.id = studenten.id inner join beroepsprofielen on studenten.beroepsprofiel_id = beroepsprofielen.id left join informatiepunten on informatiepunten.persoon_id = personen.id where personen.id = ?;");
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            student = new Student(resultSet.getInt("x"), resultSet.getInt("y") , resultSet.getString("familienaam"), resultSet.getString("voornaam"), resultSet.getString("beschrijving"), Beroepsprofiel.valueOf(resultSet.getString("naam")), Integer.parseInt(resultSet.getString("inschrijvingsjaar")));
        }

        connection.close();
        return student;
    }

    public static ArrayList<ArrayList<String>> getAllStudentInfo(Connection connection) throws SQLException {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement("select * from personen inner join studenten on personen.id = studenten.id inner join beroepsprofielen on studenten.beroepsprofiel_id = beroepsprofielen.id left join informatiepunten on informatiepunten.persoon_id = personen.id;");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            ArrayList<String> stringlist = new ArrayList<>();
            stringlist.add(resultSet.getString("personen.id"));
            stringlist.add(resultSet.getString("voornaam"));
            stringlist.add(resultSet.getString("familienaam"));
            stringlist.add(resultSet.getString("inschrijvingsjaar"));
            stringlist.add(resultSet.getString("x"));
            stringlist.add(resultSet.getString("y"));
            stringlist.add(resultSet.getString("naam"));
            stringlist.add(resultSet.getString("beschrijving"));
            list.add(stringlist);
        }

        connection.close();
        return list;
    }
}
