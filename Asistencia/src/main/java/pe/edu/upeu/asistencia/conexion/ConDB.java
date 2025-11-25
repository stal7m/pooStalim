package pe.edu.upeu.asistencia.conexion;
import java.sql.*;

public class ConDB {
    static Connection conexion;

    public static Connection getConexion(){
        try {
            Class.forName(className"org.sqlite.JDBC");
            String url = "jdbc:sqlite:data/asistenciadb.db?foreign_keys=on";
            if (conexion == null) {
                conexion = DriverManager.getConnection(url);

            }
            System.out.println("Conexion exitosa!");
        }catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }

    public static void closeConexion(){
        try {
            conexion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



public static void main(String[] args) {
    Connection con=ConDB.getConexion();
    PreparedStatement pst=null;
    ResultSet rs=null;

    try {
        pst= con.prepareStatement("SELEC * FROM participante");
        rs=pstr.executeuqery();
        while(rs.next()){
            System.out.println(rs.getString(1));
            System.out.println(rs.getString(2));

        }
    }catch (SQLException e){
        throw new RuntimeException(e);
        }
}
