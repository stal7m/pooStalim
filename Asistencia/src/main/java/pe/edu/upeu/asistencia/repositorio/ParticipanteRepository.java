package pe.edu.upeu.asistencia.repositorio;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import pe.edu.upeu.asistencia.conexion.ConDB;
import pe.edu.upeu.asistencia.enuns.CARRERA;
import pe.edu.upeu.asistencia.enuns.TIPO_PARTICIPANTE;
import pe.edu.upeu.asistencia.modelo.Participante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ParticipanteRepository {
 protected List<Participante> participantes =null;

 Connection con= ConDB.getConexion();
 PreparedStatement pst=null;
 ResultSet rs=null;

 public void save(Participante p){
  String sql="INSERT INTO participante\n" +
          "(dni, nombre, apellidos, carrera, tipo_participante, estado)\n" +
          "VALUES(?, ?, ?, ?, ?, 1);";
  try {
   pst= con.prepareStatement(sql);
   pst.setString(1,p.getDni().getValue());
   pst.setString(2,p.getNombre().getValue());
   pst.setString(3,p.getApellidos().getValue());
   pst.setString(4, p.getCarrera().name());
   pst.setString(5,p.getTipoParticipante().name());
   pst.executeUpdate();
  } catch (SQLException e) {
   throw new RuntimeException(e);
  }
 }

 public List<Participante> findAll(){
  participantes =new ArrayList<>();
  try {
   pst=con.prepareStatement("SELECT * FROM participante");
   rs=pst.executeQuery();
   while(rs.next()){
    Participante p=new Participante();
    p.setDni(new SimpleStringProperty(rs.getString("dni")));
    p.setNombre(new SimpleStringProperty(rs.getString("nombre")));
    p.setApellidos(new SimpleStringProperty(rs.getString("apellidos")));
    p.setCarrera(CARRERA.Sistemas.valueOf(rs.getString("carrera")));
    p.setTipoParticipante(TIPO_PARTICIPANTE.valueOf(rs.getString("tipo_participante")));
    p.setEstado(new SimpleBooleanProperty(rs.getBoolean("estado")));
    participantes.add(p);
   }
  }catch(SQLException e){
   System.out.println(e.getMessage());
  }
  return participantes;
 }

 public Participante update(Participante p){
  String sql=" UPDATE participante \n" +
          "SET nombre=?, apellidos=?, carrera=?, tipo_participante=?, estado=? \n" +
          "WHERE dni=? ";
  int i=0;
  try {
   pst=con.prepareStatement(sql);
   pst.setString(++i, p.getNombre().getValue());
   pst.setString(++i, p.getApellidos().getValue());
   pst.setString(++i, p.getCarrera().name());
   pst.setString(++i, p.getTipoParticipante().name());
   pst.setBoolean(++i, p.getEstado().getValue());
   pst.setString(++i, p.getDni().getValue());
   pst.executeUpdate();
  } catch (SQLException e) {
   throw new RuntimeException(e);
  }
  return p;
 }

 public void delete(String dni){
  String sql=" DELETE FROM participante WHERE dni=? ";
  int i=0;
  try {
   pst=con.prepareStatement(sql);
   pst.setString(++i, dni);
   pst.executeUpdate();
  } catch (SQLException e) {
   throw new RuntimeException(e);
  }
 }




}