package dam.jmdh;

import javax.swing.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        String basedatos = "sakila";
        String host = "localhost";
        String port = "3306";
        //String parAdic = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos; // + parAdic;
        String user = "root";
        String pwd = "pass";

        //Class.forName("com.mysql.jdbc.Driver");    // No necesario desde SE 6.0
        //Class.forName("com.mysql.cj.jdbc.Driver"); // para MySQL 8.0, no necesario
        try (Connection c = DriverManager.getConnection(urlConnection, user, pwd)) {
            // Inyección de código para sacar todos los actores de la tabla
            // PENELOPE' OR '1=1

            String nombreActor = JOptionPane.showInputDialog("Dame el nombre del actor");
            //dameActor(c,nombreActor);
            dameActorOK(c,nombreActor);

            // insertar
            insertaActor(c);

        } catch (SQLException e) {
            System.out.println("SQL mensaje: " + e.getMessage());
            System.out.println("SQL Estado: " + e.getSQLState());
            System.out.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    private static void dameActor(Connection c , String nombreActor) throws SQLException {
        String sql = "SELECT FIRST_NAME, LAST_NAME FROM ACTOR WHERE FIRST_NAME = '" + nombreActor + "'";
        System.out.println(sql);
        ResultSet rs = c.createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
        }
    }

    private static void dameActorOK(Connection c, String nombreActor) throws SQLException {
        // Prepare statement
        String sqlPrep = "SELECT FIRST_NAME, LAST_NAME FROM ACTOR WHERE FIRST_NAME = ?";
        PreparedStatement preparedStatement = c.prepareStatement(sqlPrep);
        preparedStatement.setString(1,nombreActor);
        ResultSet rs =preparedStatement.executeQuery();
        while(rs.next()){
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
        }
    }

    private static void insertaActor (Connection c) throws SQLException {

        String sqlPrep = "INSERT INTO ACTOR VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = c.prepareStatement(sqlPrep);
        preparedStatement.setInt(1,998);
        preparedStatement.setString(2,"PENÉLOPE");
        preparedStatement.setString(3,"CRUZ");
        preparedStatement.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
        int registroIns =preparedStatement.executeUpdate();
        System.out.println("Num registros insertados: " + registroIns);

        // Segimos insertando registros pero lo hará la sent. precompilada anteriormente
        // evitando que el SGBD vuelva a hacerlo

        for (int i= 1000; i<1025; i++){
            preparedStatement.setInt(1,i);
            preparedStatement.setString(2,"NOMBRE_ACTOR_"+i);
            preparedStatement.setString(3,"APELLIDO_ACTOR_"+i);
            preparedStatement.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
            registroIns+= preparedStatement.executeUpdate();
        }
        System.out.println("Num registros insertados: " + registroIns);

        // Inserción de valor null en la columna 4
        preparedStatement.setInt(1,i);
        preparedStatement.setString(2,"NOMBRE_ACTOR_"+i);
        preparedStatement.setString(3,"APELLIDO_ACTOR_"+i);
        preparedStatement.setNull(4,Types.TIMESTAMP);
        registroIns+= preparedStatement.executeUpdate();

    }
}