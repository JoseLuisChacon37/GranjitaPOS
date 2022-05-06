package clasesAuxiliares;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.awt.print.*;

public class TicketCliente {
FileWriter flwriter = null;
File file = null;
BufferedWriter bfwriter =null;
    public  TicketCliente(){
      try {
       file = new File("ticket.txt");
       flwriter = new FileWriter(file);
       //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
       bfwriter = new BufferedWriter(flwriter);
 
       } catch (IOException e) {
			e.printStackTrace();
		}
        
    }
      
   public void agregaHeader(String ticket, String mesa, String mesero, String tipo){
        Fecha fecha = new Fecha();
      try {
          
          bfwriter.newLine();            
          bfwriter.newLine();        
          bfwriter.newLine();
          bfwriter.newLine();
          bfwriter.newLine();
          
          bfwriter.write("-- -- "+tipo+" - "+tipo+" - "+tipo+" -- --");
          bfwriter.newLine();
          bfwriter.write("=======================================")  ;  
          bfwriter.newLine();
	  bfwriter.write("Ticket: " +ticket + "    Mesa : " +mesa);
          bfwriter.newLine();
          bfwriter.write("Mesero: " +mesero + "     " +fecha.getHora());
          bfwriter.newLine();
          bfwriter.write("=======================================")  ;   
            bfwriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}  
      
   }    
   public void agregaLineas( String cant, String plato, String desc, String prec){
             try {
	 bfwriter.write(cant+" #"+plato+"  "+desc + "  "+prec );
   bfwriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}  
       
   }
   public void cierraArchivo(){
        try {
          
          bfwriter.newLine();            
          bfwriter.newLine();        
          bfwriter.newLine();
          bfwriter.newLine();
          bfwriter.newLine();

          bfwriter.write("=======================================")  ;  
   
		} catch (IOException e) {
			e.printStackTrace();
		}  
        if (bfwriter != null) {
	try {//cierra el flujo principal
	 
           bfwriter.close(); 
	} catch (IOException e) {e.printStackTrace();}
	}
    	if (flwriter != null) {
	try {//cierra el flujo principal
	   flwriter.close();
      
	} catch (IOException e) {e.printStackTrace();}
	}
  }
   
  public void imprimir(String rutaDoc)
{
       PrinterJob job = PrinterJob.getPrinterJob();
      // job.printDialog();    pra que seleccione impresora
       String impresora=job.getPrintService().getName();   //  obtiene el nombre de la impresora por default
      // impresora="Canon MG3600 series Printer";   si se le qiere cambiar el nombre
       System.out.println(impresora);
       //ESTE ES TU CÓDIGO
       java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
       java.io.File fichero = new java.io.File(rutaDoc);
       if (desktop.isSupported(Desktop.Action.PRINT)){
            try {
              try{
                 Process pr = Runtime.getRuntime().exec("Rundll32 printui.dll,PrintUIEntry /y /n \""+impresora+"\"");
                  }catch(Exception ex){
                    System.out.println("Ha ocurrido un error al ejecutar el comando. Error: "+ex);
                  }
            desktop.print(fichero);
           } catch (Exception e){
System.out.print("El sistema no permite imprimir usando la clase Desktop");
e.printStackTrace();
}
}else{
System.out.print("El sistema no permite imprimir usando la clase Desktop");
}
} 
}