/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BDqueries;
import java.sql.*;
import javax.swing.*;
import com.mysql.cj.jdbc.*;
      
/**
 *
 * @author Intech-Torreon
 */
public class Conexion {
  
     private static Connection connection=null;
      public Connection conectarDB() {
         try{
             MysqlConnectionPoolDataSource ds=new MysqlConnectionPoolDataSource();
             ds.setServerName("localhost");
             ds.setPort(3306);
             ds.setDatabaseName("granjitapos");
             connection=ds.getConnection("root","rootroot");
          }catch(Exception ex){
              ex.printStackTrace();
              JOptionPane.showMessageDialog(null, ex);
              JOptionPane.showMessageDialog(null,"No se pudo conectar la BD granjitaPOS");
          }
         return connection;
         }
     }
    
