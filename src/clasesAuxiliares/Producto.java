/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesAuxiliares;

/**
 *
 * @author Intech-Torreon
 */
   public class Producto {
        
   public       String numeroPlato;
   public       String descripcionPlato;
  public        String precioPlato;
     public    String categoriaPlato;
    public     String stock;
    public     String peso;
        
       public  Producto (String np, String dp, String pp, String cp, String stockable, String pesable){
            numeroPlato=np;
            descripcionPlato=dp;
            precioPlato=pp;
            categoriaPlato = cp;
            stock=stockable;
            peso = pesable;
         
        }
    
  }
