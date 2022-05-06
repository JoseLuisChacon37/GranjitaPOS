package BDqueries;
import java.sql.*;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Intech-Torreon
 */
public class dbConnect {
    public String db= "tapatia";
    public String url = "jdbc:mysql://localhost/"+db;
    public String user = "root";
    public String pass = "karlita37";
    
    public dbConnect(){
        
    }
    public Connection conectarDB2() {
        Connection link = null;
        try {
            //cargar driver mysql
            Class.forName("org.gjt.mm.mysql.Driver");
            link = DriverManager.getConnection(this.url, this.user, this.pass);
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            
        }
            return link;
    }

    
}
