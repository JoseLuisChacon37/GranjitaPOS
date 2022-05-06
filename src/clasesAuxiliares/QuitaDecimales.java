/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesAuxiliares;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
/**
 *
 * @author CASA
 */
public class QuitaDecimales {
    
  String  CadenaModificar;
        String devolver="";

    public QuitaDecimales (String cadena){
       CadenaModificar = cadena;
    }
    
    public String sinDecimales()
    {
       if(CadenaModificar.indexOf(".")>0) 
       {
         Integer indice =  CadenaModificar.indexOf(".");      
          devolver =  CadenaModificar.substring(0,indice);
       }
     return devolver;

    }
    public String dosDecimales( Double CadenaDoble)
    
    { 
     DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
      simbolos.setDecimalSeparator('.');
     DecimalFormat formateador = new DecimalFormat("####0.00",simbolos);
      devolver =(formateador.format (CadenaDoble));
     return devolver;

    }

}
