/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesAuxiliares;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 *
 * @author Intech-Torreon
 */
public class Fecha {
    Date fecha;
    SimpleDateFormat formato;
    public Fecha() {
    
        fecha = new Date();
           
     }
    public String getFecha(){
               formato = new SimpleDateFormat("dd/MM/YYYY"); 
        return formato.format(fecha);
    }
    public String fechaNoFormato() {
 
        formato = new SimpleDateFormat("YYYYMMdd");
        return formato.format(fecha);
    }
     public String getHora(){
          formato = new SimpleDateFormat("HH:mm:ss");
          return formato.format(fecha);
    }
    public String getHoraNoFormato(){
          formato = new SimpleDateFormat("HHmmss");
          return formato.format(fecha);
    }
       public String getFecha2(){
               formato = new SimpleDateFormat("YYYY/MM/dd"); 
        return formato.format(fecha);
    }
    public String getFecha3(){
               formato = new SimpleDateFormat("YYYY-MM-dd"); 
        return formato.format(fecha);
    }    
}
