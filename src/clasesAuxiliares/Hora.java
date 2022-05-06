/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesAuxiliares;
import java.util.*;

/**
 *
 * @author Intech-Torreon
 */
public class Hora {
    
    Calendar calendario = new GregorianCalendar();
    public Hora(){
        
    }
    
    public Integer getHr(){
        return calendario.get(Calendar.HOUR_OF_DAY);
    }
    public Integer getMin(){
    return calendario.get(Calendar.MINUTE);
    }
    public Integer getSe(){
       return  calendario.get(Calendar.SECOND);
    }
    
}
