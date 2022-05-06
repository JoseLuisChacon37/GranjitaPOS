/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. guardaDEta
 */
package Frames;
import clasesAuxiliares.Fecha;
import BDqueries.*;
import clasesAuxiliares.*;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JScrollPane;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.swing.*;

public class OrdenCajero extends javax.swing.JFrame {
   
    String cajero;
    String cveCaj;
    String ticket;
    String cantidad="";
    Double total= 0.00;
    Double descuento = 0.00;
    Double Subtotal= 0.00;   
    private CardLayout cardlayout;
    JScrollPane scrollpane;
    MyModelo modelo;
    ListSelectionModel listSelectionModel;
    JButton[] opciones = new JButton[200];
    Producto[] platillo = new Producto[1000];
    Comanda comandaMesa;
    private LinkedList listaPlatos = new LinkedList();
    Integer[] lineaSelected = new Integer[100];
    Integer csecuencia=1;
    int filaSeleccionada;  
    Double  precios =0.00;
    String[] precioMod = new String[200];
    Boolean cash = false;

    public OrdenCajero( String nombre, String cveCajero) {
        initComponents();
        cveCaj=cveCajero;
        cajero=nombre;
        btnImprime.setVisible(false);
        lblCajero.setText(cajero);
        txtefvo.setEditable(false);
        txttarj.setEditable(false);
        txtresta.setEditable(false);
        cardlayout = new CardLayout();
        pnlPrincipal.setLayout(cardlayout);
        pnlPrincipal.add("menu",pnlCarta);
        pnlPrincipal.add("opciones",pnlOpciones);
        comDescuento.setEnabled(false);
        comDescuento.removeAllItems();
        comDescuento.addItem("0");
        comDescuento.addItem("5");
        comDescuento.addItem("10");
        comDescuento.addItem("40");
        comDescuento.addItem("50");
        comDescuento.addItem("70");  
        comDescuento.addItem("100");     
        comDescuento.setSelectedItem("0");
        Fecha fecha = new Fecha();
        String fechaHoy= fecha.getFecha3();
        ticket = fecha.fechaNoFormato()+  "-" + fecha.getHoraNoFormato();    
        txtTicket.setText(ticket);
        
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB(); 
        // para mostrar el numero de clientes y el total
        String sSQL ="SELECT count(numTicket), sum(total) from comanda where fecha ='" + fechaHoy+"'";
        try{
          Statement st = link.createStatement();
          ResultSet rs = st.executeQuery(sSQL);
                            
          while(rs.next()){              // sacar los datos de cada renglon de la BD
                numeroVentas.setText( rs.getString("count(numTicket)"));
                ventaDia.setText(rs.getString("sum(total)"));
          }
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        } 
        
       CargarTablaTicket();
       cardlayout.show(pnlPrincipal,"menu");
        
    }

    //cargat e; detalle del ticlet y ponerlo en l,a tabla
    public void CargarTablaTicket(){
        String[] registro = new String[5];
        String sSQL = "";

        modelo = new MyModelo();     // creasr modelo badado en efaulttablemodel
        modelo.addColumn("Cant.");    //a regar columnas
        modelo.addColumn("Descripcion");
        modelo.addColumn("Precio");
        modelo.addColumn("Peso(gr)");
        modelo.addColumn("Importe");
        Fecha fecha = new Fecha();      
     
        String cenvia="N";
        String cfecha = fecha.getFecha2();
        String ctotal="0.00";
        String cdesc="0.00";
        String ccash="0.00";
        String ccard="0.00";
       
        String cpesa;
        String cstock;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();       
        tblTicket.setModel(modelo);
        TableColumn columna = tblTicket.getColumn("Cant.");  // obtener propiedas de clumna
        columna.setPreferredWidth(5);
        columna = tblTicket.getColumn("Descripcion");
        columna.setPreferredWidth(40);
        columna = tblTicket.getColumn("Precio");
        columna.setPreferredWidth(12);   
        columna = tblTicket.getColumn("Peso(gr)");
        columna.setPreferredWidth(12);  
        columna = tblTicket.getColumn("Importe");
        columna.setPreferredWidth(12);   

        listSelectionModel = tblTicket.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
	tblTicket.setSelectionModel(listSelectionModel);
        
        // crear SQL query

        sSQL ="SELECT comanda.numTicket,  comanda.numMesero, comanda.fecha"
                 + ",detallecomanda.cantidad"
                 + ",comanda.total, comanda.descuento, comanda.cash, comanda.card"
                 + ",detallecomanda.numPlato, detallecomanda.descPlato, detallecomanda.precio, "
                 + "detallecomanda.enviado,detallecomanda.secuencia, "
                + "detallecomanda.kilos,detallecomanda.dinero,"
                + "detallecomanda.stockable, detallecomanda.pesable, detallecomanda.categoria FROM comanda INNER JOIN detallecomanda ON comanda.numticket=detallecomanda.numTicket"
                 + " where comanda.numTicket= '" + ticket + "' ORDER BY detallecomanda.numTicket,detallecomanda.secuencia";

        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
                            
            while(rs.next()){              // sacar los datos de cada renglon de la BD

              //  cticket=rs.getString("comanda.numTicket");
                cfecha=rs.getString("comanda.fecha");

                registro[0]=rs.getString("detallecomanda.cantidad");
                String numprod=rs.getString("detallecomanda.numPlato");    
                registro[1]=rs.getString("detallecomanda.descPlato");     
                registro[2]=rs.getString("detallecomanda.precio");   
                cenvia=rs.getString("detallecomanda.enviado");
                csecuencia=Integer.parseInt(rs.getString("detallecomanda.secuencia"));
                registro[3]=rs.getString("detalelcomanda.kilos");
                registro[4]=rs.getString("detallecomanda.dinero");
                cpesa=rs.getString("detallecomanda.pesable");
                cstock=rs.getString("detallecomanda.stockable");
                String catee=rs.getString("detallecomanda.categoria");
                modelo.addRow(registro);  // agrega renglon a la tabla
                 
                 if(Integer.parseInt(registro[0])>0){
                     Subtotal =   Subtotal + (Integer.parseInt(registro[0])*Float.parseFloat(registro[2]));
                 }
                  // crea un obj detalle comanda para agregarlo ala lista encaqdenada
                DetalleComanda art = new DetalleComanda(ticket, registro[0], numprod, registro[1], registro[2],cenvia,csecuencia,Integer.parseInt(registro[3]),Float.parseFloat(registro[4]),cpesa,cstock,catee);
                listaPlatos.add(art);               

            }
            QuitaDecimales cadena = new QuitaDecimales(Subtotal.toString());
            String resultado= cadena.dosDecimales(Subtotal);
            lblSubtotal.setText("Subtotal: $ " + resultado ); 
            double descu = Double.parseDouble(cdesc);
            resultado= cadena.dosDecimales(descu);

            lblDesc.setText("Descuento : $ "+resultado);

            total = Subtotal - Double.parseDouble(cdesc);
            resultado= cadena.dosDecimales(total);

            lblTotal.setText("Total: $ " + resultado);  
            tblTicket.setModel(modelo);

            comDescuento.setEnabled(true);
           int y = modelo.getRowCount();
           tblTicket.setDefaultRenderer(Object.class, new FormatoTabla());
           if (y==0){                    // si la tabla esta vacia se deshabilitan los botnoes
               btnGuardaOrden.setEnabled(false);
               btnBorrarTicket.setEnabled(false);
                     btnBorraLineas.setEnabled(false);
               btnPagarComanda1.setEnabled(false);
               btnImprime.setEnabled(false);
               lblArticulos.setText("0 Articulos !");
           }
      
           // txtDescripcion.setEditable(false);
            comandaMesa = new Comanda(ticket,"0", cveCaj,cfecha,Float.parseFloat(ctotal), Subtotal, cdesc, ccash, ccard); // se crea la comanda
            // no importa si ya existe en la base de datos o no
          // comandaMesa.enviaComandaBD();
           
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }            
       //   Comanda comandaMesa = new Comanda(ticket,mesa, mesero, fecha.fechaNoFormato() , "121014", "121613");
        // cargar datos de detalle de comanda a la tabla            
    }

  public class Comanda {
    
    String cTicket, cMesa, cCajero, cFecha, cHoraInicial, cHoraFinal, cdesc;
    Double cTotal, cSub,cefvo,ctarj;  
    
    public Comanda (String ticket, String mesa,  String mesero, String fecha, double tot, double subtot,String desc, String efvo, String tarj)           
      {
          cTicket = ticket;
          cMesa = mesa;
          cCajero = mesero;
          cFecha = fecha;
          cTotal = tot;
          cefvo = Double.parseDouble(efvo);
          cdesc=desc;
          ctarj=Double.parseDouble(tarj);
          cSub = subtot;
      }

      //agrega la orden a la bd basado en los datos de la comanda
      public void agregaOrden(){
        Conexion mysql1 = new Conexion();
        Connection link1 = mysql1.conectarDB(); 
        String sSQL="";
        sSQL ="SELECT numTicket  FROM orden  where numTicket= '" + cTicket + "' ";
         try{
            Statement st = link1.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            boolean x = rs.next();
            if (x==false){
     
                sSQL = "INSERT INTO orden (numTicket,numCajero, nomCajero, fecha, pagada, total) "
               + "VALUES (?,?,?,?,?,?)";
               try{
                  PreparedStatement pst1 = link1.prepareStatement(sSQL);
                  pst1.setString(1, cTicket);       
                  pst1.setString(2, cCajero);
                  pst1.setString(3, cajero);
                  pst1.setString(4, cFecha); 
                  pst1.setString(5, "NO");   
                   pst1.setString(6, cTotal.toString());                 
            
                  int p = pst1.executeUpdate();
                  if(p>0){     // se inserto bien en la bd ahora actalizara la mesa
                   //actualziaOrden(); 
                  } else 
                   {JOptionPane.showMessageDialog(null,"Error al crear la Orden, avise al Administrador"); }
                 }catch (SQLException ex) {
                   JOptionPane.showMessageDialog(null,ex);
                   JOptionPane.showMessageDialog(null,"NO se pudo crar la orden Error de BD,avise al administrador");
                 }  
                
            }
          }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"Fallo la busqueda de la Orden, avise al administrador");
          }     
      }
                      
   
      public void enviaComandaBD(){
      
        String sSQL="";
        Integer mov=0;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();  
        sSQL ="SELECT ultimoTicket  FROM ticket";
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while(rs.next()){  // obtener invneario
              mov= Integer.parseInt(rs.getString("ultimoTicket"));
            }
            mov++;
       
        }catch(SQLException ex){
          JOptionPane.showMessageDialog(null,"No se pudo accesar tabla ticket");

        }
        //buscar si la comdan ya esta en la bd sino  hay qyue agregarla
         sSQL ="SELECT numTicket  FROM comanda  where numTicket= '" + cTicket + "' ";
         try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            boolean x = rs.next();
            if (x==false){  // la comanda no existe hay que crearla
                lblSubtotal.setText("Subtotales: $ " + Subtotal );    
                lblDesc.setText("Descuento : $ "+cdesc);
                total = Subtotal - Float.parseFloat(cdesc);
                lblTotal.setText("Totales: $ " + total);     
                sSQL = "INSERT INTO comanda (Movimiento,numTicket, numMesero, fecha, numeroProductos, total, descuento,cash,card) "
               + "VALUES (?,?,?,?,?,?,?,?,?)";
               try{
                  PreparedStatement pst1 = link.prepareStatement(sSQL);
                  pst1.setString(1, mov.toString());
                  pst1.setString(2, cTicket);       
                  pst1.setString(3, cCajero);
                  pst1.setString(4, cFecha); 
                  pst1.setString(5, "1");   
                  pst1.setString(6, cTotal.toString());     
                  pst1.setString(7,cdesc);  
                  pst1.setString(8,cefvo.toString());  
                  pst1.setString(9,ctarj.toString()); 
                  int p = pst1.executeUpdate();
                  if(p>0){     // se inserto bien la comanda ahora actualizar el ultiko ticket
                    //mov++;   // incrementa el numero de ticket
                    sSQL="UPDATE ticket set ultimoTicket=?";
                    try {
                     PreparedStatement pst2 = link.prepareStatement(sSQL);
                     pst2.setString(1,mov.toString());
       
                     int n1 = pst1.executeUpdate();
                     if(n1<0)
                        JOptionPane.showMessageDialog(null,"Error no se actualizo el ultimo ticket");
                    }         
                    catch (SQLException ex) {
                       JOptionPane.showMessageDialog(null,ex); 
                      JOptionPane.showMessageDialog(null,"Ërror al acutlizar tabla ticket") ;
                    }                   
                  }else {
                      JOptionPane.showMessageDialog(null,"Error al enviar Venta a BD, avise al Administrador");
                    }
                 }catch (SQLException ex) {
                   JOptionPane.showMessageDialog(null,ex);
                   JOptionPane.showMessageDialog(null ,"NO sepudo crear Venta en BD,avise al administrador");
                 }  
            }      // del if
            
          }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"Fallo la busqueda del ticket y comanda, avise al administrador");
          }   
                   
      }
  }  
    
  public class DetalleComanda {
        String dticket;
        String dcantidad;
        String dnumplato;
        String ddescplato;
        String dprecio;
        String denviado;
        Integer dsecuencia;
        Integer dkilos;
        Float ddinero;
        String dstock;
        String dpeso;
        String dcatego;
        
        DetalleComanda( String ti, String ca, String nu, String de, String pr, String en, Integer se, Integer kilos, Float dinero, String stock, String peso, String catego){
            dticket=ti;
            dcantidad=ca;
            dnumplato=nu;
            ddescplato=de;
            dprecio=pr;
            denviado=en;
            dsecuencia=se;
            dkilos=kilos;
            ddinero=dinero;
            dstock=stock;
            dpeso = peso;
            dcatego = catego;
        }
        
   public void guardaDetalle(){
       String sSQL = "";
     Conexion mysql = new Conexion();
     Connection link = mysql.conectarDB();  
     Double importe;
     // validar que no se meta doble detalle
     sSQL = "SELECT * from detallecomanda where numTicket='"+dticket +"' and numPlato='" + dnumplato +"'and secuencia ='"+dsecuencia+"'";
     try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
     boolean existe = rs.next();
     if(!existe){
     sSQL = "INSERT INTO detallecomanda(numTicket,cantidad, numPlato, descPlato,precio, enviado, secuencia,kilos,dinero,stockable,pesable,categoria) "
               + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
     try{
        PreparedStatement pst = link.prepareStatement(sSQL);
        pst.setString(1, dticket);       
        pst.setString(2, dcantidad);
        pst.setString(3, dnumplato);
        pst.setString(4, ddescplato); 
        pst.setString(5, dprecio);   
         pst.setString(6, denviado); 
         pst.setString(7, dsecuencia.toString());
        pst.setString(8, dkilos.toString() ); 
        pst.setString(9, ddinero.toString());
        pst.setString(10, dstock);
        pst.setString(11, dpeso);
        pst.setString(12, dcatego);
       Double inventa = 0.00;
       String pesa="NO";
       int n = pst.executeUpdate();
       if(n>0){
          // rebajarlo del inventario
         sSQL = "SELECT inventario,peso from plato where numPlato="+dnumplato;
         try{
             st = link.createStatement();
            rs = st.executeQuery(sSQL);
             while(rs.next()){  // obtener invneario
             inventa= Double.parseDouble(rs.getString("inventario"));
             pesa = rs.getString("peso");
             }
             if(pesa.equals("SI")) {
               inventa= inventa - (dkilos*Integer.parseInt(dcantidad));
             } else inventa=inventa - Integer.parseInt(dcantidad);
         // svtulziar inventario en plato    
        sSQL = "UPDATE plato" +
                   " SET inventario = ? " +
                   "WHERE numPlato = " + dnumplato;     
          try {
       PreparedStatement pst1 = link.prepareStatement(sSQL);
            pst1.setString(1,inventa.toString());
       
       int n1 = pst1.executeUpdate();
       if(n1<0)
           JOptionPane.showMessageDialog(null,"Error no se pudo actualziar el Inventario");
          // Checar si es combo para actualizar todos los art del combo
          if(dcatego.equals("COMBOS")) 
          {  aplicaCombo(dnumplato,dcantidad); } 
  
       }         
       catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con elinventario") ;
       } 
        
        
         }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se encontro el articulo para actualizar Inventario");
          } 
        
           
       }
     }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se pudo guardar el registro de comanda en la BD");
     } 
     }                            
          }
      catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex +"Ërror no se pudo accesar el archivo detalle comanda") ;
         
     }
     }
   
   public void calculaTotales() 
   {
       DetalleComanda art;
       comandaMesa.cSub= 0.00;
       comandaMesa.cTotal = 0.00;
       Subtotal= 0.00;
              lblArticulos.setText("Numero de Articulos: "+modelo.getRowCount());

     //  comandaMesa.
       for (int i=0; i <modelo.getRowCount(); i++)
        {
            art = (DetalleComanda) listaPlatos.get(i);
                String cant =  (String)modelo.getValueAt(i,0);
                String importe = (String) modelo.getValueAt(i,4);              
                
                if(Integer.parseInt(cant)>0)
                 Subtotal =   Subtotal + Float.parseFloat(importe);         
        }
                  comandaMesa.cSub = Subtotal;
                 comandaMesa.cTotal = comandaMesa.cTotal + Subtotal;
                QuitaDecimales cadena = new QuitaDecimales(comandaMesa.cSub.toString());
                String resultado= cadena.dosDecimales(comandaMesa.cSub);
                 lblSubtotal.setText("Subtotal: $ " + resultado );    
                resultado= cadena.dosDecimales(comandaMesa.cTotal);

                 lblTotal.setText("Total: $ " + resultado);  
              if(comandaMesa.cTotal>0){
                 btnCasa.setEnabled(false);
                  btnSalir.setEnabled(false);
                  btnPagarComanda1.setEnabled(true);
                  btnImprime.setEnabled(true);
             } else {
                                  btnCasa.setEnabled(true);
                  btnSalir.setEnabled(true); 
                  btnPagarComanda1.setEnabled(false);
                  btnImprime.setEnabled(false);
              }
   }
  }
  
public void aplicaCombo( String combo,String canti)
   {
       String sSQL = "";
       Double inventa=0.00;
     Conexion mysql = new Conexion();
     Connection link = mysql.conectarDB();   
    sSQL = "SELECT prodcombo.numPlato,prodcombo.cantidad,inventario from prodcombo inner join plato on"
            + " plato.numPlato=prodcombo.numPlato where numCombo="+combo;
    try{
       Statement st3 = link.createStatement();
       ResultSet rs3 = st3.executeQuery(sSQL);
       while(rs3.next()){  // recorrer todos los prod del combo 
         String numplato=rs3.getString("prodcombo.numPlato");
         String cantidad=rs3.getString("cantidad");
         
         //Ahora a rebajar ese produicto del invnetario
        // sSQL = "SELECT inventario from plato where numPlato="+numplato;
        // try{
         //   ResultSet rs1 = st3.executeQuery(sSQL);
           //  while(rs1.next()){  // obtener invneario
             inventa= Double.parseDouble(rs3.getString("inventario"));
          //   }
               inventa= inventa - (Integer.parseInt(canti)*Integer.parseInt(cantidad));
         // svtulziar inventario en plato    
        sSQL = "UPDATE plato" +
                   " SET inventario = ? " +
                   "WHERE numPlato = " + numplato;     
         try {
             PreparedStatement pst1 = link.prepareStatement(sSQL);
              pst1.setString(1,inventa.toString());
       
              int n1 = pst1.executeUpdate();
             if(n1<0)
           JOptionPane.showMessageDialog(null,"Error no se pudo actualziar el Inventario al aplicar COmbo");
  
           }         
             catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con elinventario") ;
           } 
        
       
     /*    }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se encontro el articulo para actualizar Inventario");
          } */
              
       } //end del while
                              
     }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se pudo accesar los productos Del combo ");
          }      
   }   
  
// cuando se borra un producto y es combo devuielve los proictos al invnetario
   
   public void devuelveCombo( String combo,String canti)
   {
       String sSQL = "";
       Double inventa=0.00;
     Conexion mysql = new Conexion();
     Connection link = mysql.conectarDB();   
    sSQL = "SELECT prodcombo.numPlato,prodcombo.cantidad,inventario from prodcombo inner join plato on"
            + " plato.numPlato=prodcombo.numPlato where numCombo="+combo;
    try{
       Statement st3 = link.createStatement();
       ResultSet rs3 = st3.executeQuery(sSQL);
       while(rs3.next()){  // recorrer todos los prod del combo 
         String numplato=rs3.getString("prodcombo.numPlato");
         String cantidad=rs3.getString("cantidad");
         
        
             inventa= Double.parseDouble(rs3.getString("inventario"));
          //   }
               inventa= inventa + (Integer.parseInt(canti)*Integer.parseInt(cantidad));
         // svtulziar inventario en plato    
        sSQL = "UPDATE plato" +
                   " SET inventario = ? " +
                   "WHERE numPlato = " + numplato;     
         try {
             PreparedStatement pst1 = link.prepareStatement(sSQL);
              pst1.setString(1,inventa.toString());
       
              int n1 = pst1.executeUpdate();
             if(n1<0)
           JOptionPane.showMessageDialog(null,"Error no se pudo actualziar el Inventario al borrar COmbo");
  
           }         
             catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con elinventario") ;
           } 
        
       
     /*    }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se encontro el articulo para actualizar Inventario");
          } */
              
       } //end del while
                              
     }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se pudo accesar los productos Del combo ");
          }      
   }   
    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) { 
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
 
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting(); 
         //   output.append("Event for indexes "
                      //    + firstIndex + " - " + lastIndex
                       //   + "; isAdjusting is " + isAdjusting
                       //   + "; selected indexes:");

            if (lsm.isSelectionEmpty()) {
               // output.append(" <none>");
               btnBorraLineas.setEnabled(false);
               // btnModifica.setEnabled(false);
            } else {
                // Find out which indexes are selected.
              if (isAdjusting){
                   cardlayout.show(pnlPrincipal,"menu");
                  btnBorraLineas.setEnabled(true);
             //     btnModifica.setEnabled(true);
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                int y=0;
                for (int i=0 ; i<=99;i++){     // limpiar tdoso los indices de ;as lineas sleccionadas
                     lineaSelected[i]=-1;
                }
                for (int i = minIndex; i <= maxIndex; i++) { // obteber los indices de las lineas seleccionadas
                    if (lsm.isSelectedIndex(i)) {
            //        
                     String cant = (String) modelo.getValueAt(i,0);
                      // si selecciona una modificacion descativa el boton de modificar           
                  //  if(cant.equals("  ")){
                    //   btnModifica.setEnabled(false);
                    //  }
               String plato = (String) modelo.getValueAt(i,1);
                String desc = (String) modelo.getValueAt(i,2);             
              
                      lineaSelected[y]=i;
                      y=y+1;
                     
                    }
                } 
            }
            }
          //  output.append(newline);
           // output.setCaretPosition(output.getDocument().getLength());
        }
    }
  public class FormatoTabla extends DefaultTableCellRenderer
{

 public Component getTableCellRendererComponent
 (JTable table, Object value, boolean selected, boolean focused, int row, int column)
 {
         // SI EN CADA FILA DE LA TABLA LA CELDA 5 ES IGUAL A ACTIVO COLOR AZUL
      
    
 super.getTableCellRendererComponent(table, value, selected, focused, row, column);
 return this;
 }
 }  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        pnlArriba = new javax.swing.JPanel();
        btnCasa = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblTicket = new javax.swing.JLabel();
        txtTicket = new javax.swing.JTextField();
        lblhoraIni = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();
        lblCajero = new javax.swing.JLabel();
        btnPagarComanda1 = new javax.swing.JButton();
        numeroVentas = new javax.swing.JLabel();
        ventaDia = new javax.swing.JLabel();
        btnCambiaMenu = new javax.swing.JButton();
        btnTool = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTicket = new javax.swing.JTable();
        lblSubtotal = new javax.swing.JLabel();
        comDescuento = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnImprime = new javax.swing.JButton();
        lblDesc = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblefvo = new javax.swing.JLabel();
        lbltarj = new javax.swing.JLabel();
        lblresta = new javax.swing.JLabel();
        txtefvo = new javax.swing.JTextField();
        txttarj = new javax.swing.JTextField();
        txtresta = new javax.swing.JTextField();
        pnlPrincipal = new javax.swing.JPanel();
        pnlOpciones = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlSelecciona = new javax.swing.JPanel();
        bntcero = new javax.swing.JButton();
        bnt2 = new javax.swing.JButton();
        btnuno = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        lblcantidad = new javax.swing.JLabel();
        pnlCarta = new javax.swing.JPanel();
        btnRefrescos = new javax.swing.JButton();
        btnPolloFajita = new javax.swing.JButton();
        btnSides = new javax.swing.JButton();
        btnCombos = new javax.swing.JButton();
        btnGuardaOrden = new javax.swing.JButton();
        btnBorraLineas = new javax.swing.JButton();
        btnBorrarTicket = new javax.swing.JButton();
        lblArticulos = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnlArriba.setBackground(new java.awt.Color(255, 102, 0));
        pnlArriba.setMaximumSize(new java.awt.Dimension(1200, 1200));

        btnCasa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/home.png"))); // NOI18N
        btnCasa.setToolTipText("Regresar a menu principal de Usuario");
        btnCasa.setMaximumSize(new java.awt.Dimension(197, 169));
        btnCasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCasaActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/puerta2.png"))); // NOI18N
        btnSalir.setToolTipText("Salir del sistema");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        lblTicket.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTicket.setForeground(new java.awt.Color(0, 0, 204));
        lblTicket.setText("Ticket:");

        txtTicket.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lblhoraIni.setText("Aqui va la hora");

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menu1.png"))); // NOI18N
        btnMenu.setToolTipText("Regresar al Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        lblCajero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCajero.setForeground(new java.awt.Color(255, 255, 255));
        lblCajero.setText("Cajero");

        btnPagarComanda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pesos.jpg"))); // NOI18N
        btnPagarComanda1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarComanda1ActionPerformed(evt);
            }
        });

        numeroVentas.setText("aqui el numero de ventas");

        ventaDia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ventaDia.setForeground(new java.awt.Color(255, 255, 255));
        ventaDia.setText("total");

        btnCambiaMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menu3.png"))); // NOI18N
        btnCambiaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiaMenuActionPerformed(evt);
            }
        });

        btnTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/admin2.png"))); // NOI18N
        btnTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToolActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/corte2.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlArribaLayout = new javax.swing.GroupLayout(pnlArriba);
        pnlArriba.setLayout(pnlArribaLayout);
        pnlArribaLayout.setHorizontalGroup(
            pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArribaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCajero)
                    .addGroup(pnlArribaLayout.createSequentialGroup()
                        .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblhoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlArribaLayout.createSequentialGroup()
                                .addComponent(lblTicket)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlArribaLayout.createSequentialGroup()
                                .addComponent(numeroVentas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                                .addComponent(ventaDia, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addComponent(btnPagarComanda1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnCambiaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTool, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCasa, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(805, Short.MAX_VALUE))
        );
        pnlArribaLayout.setVerticalGroup(
            pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArribaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlArribaLayout.createSequentialGroup()
                        .addComponent(lblCajero)
                        .addGap(23, 23, 23)
                        .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTicket)
                            .addComponent(txtTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numeroVentas)
                            .addComponent(ventaDia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblhoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlArribaLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPagarComanda1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnTool, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(btnCambiaMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlArribaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnCasa, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 0, 0), 2, true));

        tblTicket.setBackground(new java.awt.Color(255, 255, 51));
        tblTicket.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 255), 1, true));
        tblTicket.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTicket.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblTicket.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblTicket);
        tblTicket.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        lblSubtotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(0, 0, 204));
        lblSubtotal.setText("Subtotal");

        comDescuento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comDescuento.setForeground(new java.awt.Color(0, 0, 204));
        comDescuento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comDescuento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comDescuentoItemStateChanged(evt);
            }
        });
        comDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comDescuentoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 0));
        jLabel1.setText("% Desc");

        btnImprime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/print2.png"))); // NOI18N
        btnImprime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeActionPerformed(evt);
            }
        });

        lblDesc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDesc.setForeground(new java.awt.Color(0, 153, 0));
        lblDesc.setText("Descuento");

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 102, 0));
        lblTotal.setText("Total");

        lblefvo.setText("Efectivo");

        lbltarj.setText("Tarjeta");

        lblresta.setText("Resta");

        txtefvo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtefvoActionPerformed(evt);
            }
        });

        txttarj.setText(" ");
        txttarj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttarjActionPerformed(evt);
            }
        });

        txtresta.setText("                ");
        txtresta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrestaActionPerformed(evt);
            }
        });

        pnlPrincipal.setLayout(new java.awt.CardLayout());

        pnlOpciones.setBackground(new java.awt.Color(255, 0, 0));
        pnlOpciones.setPreferredSize(new java.awt.Dimension(700, 582));

        pnlSelecciona.setLayout(new java.awt.GridLayout(7, 5));
        jScrollPane2.setViewportView(pnlSelecciona);

        bntcero.setBackground(new java.awt.Color(0, 0, 204));
        bntcero.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bntcero.setForeground(new java.awt.Color(255, 255, 255));
        bntcero.setText("0");
        bntcero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntceroActionPerformed(evt);
            }
        });

        bnt2.setBackground(new java.awt.Color(0, 0, 204));
        bnt2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bnt2.setForeground(new java.awt.Color(255, 255, 255));
        bnt2.setText("2");
        bnt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnt2ActionPerformed(evt);
            }
        });

        btnuno.setBackground(new java.awt.Color(0, 0, 204));
        btnuno.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnuno.setForeground(new java.awt.Color(255, 255, 255));
        btnuno.setText("1");
        btnuno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnunoActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 204));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("3");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 0, 204));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 0, 204));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 0, 204));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 0, 204));
        jButton8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("9");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 0, 204));
        jButton9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("4");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 0, 204));
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("8");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        lblcantidad.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblcantidad.setForeground(new java.awt.Color(255, 255, 255));
        lblcantidad.setText("Cantidad");

        javax.swing.GroupLayout pnlOpcionesLayout = new javax.swing.GroupLayout(pnlOpciones);
        pnlOpciones.setLayout(pnlOpcionesLayout);
        pnlOpcionesLayout.setHorizontalGroup(
            pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOpcionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOpcionesLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bntcero, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bnt2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnuno, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnlOpcionesLayout.setVerticalGroup(
            pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOpcionesLayout.createSequentialGroup()
                .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOpcionesLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bntcero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnuno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bnt2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton9)
                        .addGap(8, 8, 8)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)
                        .addGap(10, 10, 10)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOpcionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlPrincipal.add(pnlOpciones, "card3");

        pnlCarta.setBackground(new java.awt.Color(0, 204, 204));
        pnlCarta.setPreferredSize(new java.awt.Dimension(500, 500));

        btnRefrescos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRefrescos.setForeground(new java.awt.Color(0, 0, 204));
        btnRefrescos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/refresco.jpg"))); // NOI18N
        btnRefrescos.setText("REFRESCOS");
        btnRefrescos.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 0, 0), new java.awt.Color(0, 51, 204)));
        btnRefrescos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefrescos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefrescos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescosActionPerformed(evt);
            }
        });

        btnPolloFajita.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPolloFajita.setForeground(new java.awt.Color(0, 0, 204));
        btnPolloFajita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pollo.png"))); // NOI18N
        btnPolloFajita.setText("POLLO Y FAJITA");
        btnPolloFajita.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 0, 0), new java.awt.Color(0, 51, 204)));
        btnPolloFajita.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPolloFajita.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPolloFajita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPolloFajitaActionPerformed(evt);
            }
        });

        btnSides.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSides.setForeground(new java.awt.Color(0, 0, 204));
        btnSides.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/sides2.jpg"))); // NOI18N
        btnSides.setText("SIDES");
        btnSides.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 0, 0), new java.awt.Color(0, 51, 204)));
        btnSides.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSides.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSides.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSidesActionPerformed(evt);
            }
        });

        btnCombos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCombos.setForeground(new java.awt.Color(0, 0, 204));
        btnCombos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/combos.jpg"))); // NOI18N
        btnCombos.setText("COMBOS");
        btnCombos.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 0, 0), new java.awt.Color(0, 51, 204)));
        btnCombos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCombos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCombos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCombosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCartaLayout = new javax.swing.GroupLayout(pnlCarta);
        pnlCarta.setLayout(pnlCartaLayout);
        pnlCartaLayout.setHorizontalGroup(
            pnlCartaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCartaLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlCartaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPolloFajita, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefrescos, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(137, 137, 137)
                .addGroup(pnlCartaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCombos, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSides, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pnlCartaLayout.setVerticalGroup(
            pnlCartaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCartaLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(pnlCartaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPolloFajita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSides, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(95, 95, 95)
                .addGroup(pnlCartaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCombos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRefrescos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlPrincipal.add(pnlCarta, "card5");

        btnGuardaOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guarda.png"))); // NOI18N
        btnGuardaOrden.setToolTipText("Guardar la orden para pagar despues");
        btnGuardaOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaOrdenActionPerformed(evt);
            }
        });

        btnBorraLineas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borra2.jpg"))); // NOI18N
        btnBorraLineas.setToolTipText("Borrar solo una linea");
        btnBorraLineas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorraLineasActionPerformed(evt);
            }
        });

        btnBorrarTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borra1.jpg"))); // NOI18N
        btnBorrarTicket.setToolTipText("Borrar TODO el ticket");
        btnBorrarTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarTicketActionPerformed(evt);
            }
        });

        lblArticulos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblArticulos.setForeground(new java.awt.Color(0, 0, 153));
        lblArticulos.setText("Numero de Articulos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlArriba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(917, 917, 917)
                                .addComponent(btnImprime, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(lblArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(lblefvo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(comDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbltarj)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(btnBorraLineas, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnBorrarTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtefvo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txttarj, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(txtresta, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(lblresta)
                                        .addGap(29, 29, 29))))
                            .addComponent(btnGuardaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(308, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(329, 329, 329)
                    .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 1697, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(32, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlArriba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(616, 616, 616)
                        .addComponent(btnImprime, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblArticulos)
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSubtotal)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDesc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(comDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblefvo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbltarj)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblresta)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtefvo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttarj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtresta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnGuardaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBorraLineas, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(btnBorrarTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(350, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(123, 123, 123)
                    .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(320, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCasaActionPerformed
         int y = modelo.getRowCount();
         if(y==0){
               String sSQL= "";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();       
        
        // crear SQL praa borrar detallecomanda

        sSQL ="DELETE FROM comanda WHERE numTicket = ? ";

        try{
         PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, ticket);
         pst.executeUpdate(); 
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
         }
        Cajero usuario = new Cajero();
   
        usuario.nombreCajero = cajero;
        usuario.claveCajero=cveCaj;
        usuario.cambiarTitulo();
        usuario.setVisible(true);
        dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCasaActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
       int y = modelo.getRowCount();
         if(y==0){
               String sSQL= "";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();       
        
        // crear SQL praa borrar detallecomanda

        sSQL ="DELETE FROM comanda WHERE numTicket = ? ";

        try{
         PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, ticket);
         pst.executeUpdate(); 
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
         }
        
        Acceso login = new Acceso();
        login.setVisible(true);
        dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed
 
    public void buscaMenu(String catego, Boolean todos){
       if(!todos){
        pnlSelecciona.removeAll();   // remueve todo lo del panel de Seleccio de platillo
       }
        String sSQL= "";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();       
        
        // crear SQL query con todos los prod a buscar por categoria 

        sSQL ="SELECT numPlato, descPlato, precio,stock,peso FROM plato "             
                 + " where categoria = '" + catego +"'";

        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            Integer i=0;                 
            while(rs.next()){              // sacar los datos de cada renglon de la BD
                opciones[i] = new JButton( rs.getString("numPlato") + "  " +rs.getString("descPlato"));
                opciones[i].setFont(new java.awt.Font("Tahoma", 0, 14));
                opciones[i].addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                       PlatilloactionPerformed(evt);
                            }
                           });
                // creamos objeto paltillo y metemos los datos de cada articulo del menu
                Integer y=Integer.parseInt(rs.getString("numPlato"));
                platillo[y] = new Producto(rs.getString("numPlato"),rs.getString("descPlato"),
                        rs.getString("precio"),catego,rs.getString("stock"),rs.getString("peso"));
              
                pnlSelecciona.add(opciones[i]);
            }

        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }  
        cardlayout.show(pnlPrincipal,"opciones"); //muestra panel de opciones de un menu
    }
    //Es el action performed para cuando seleccina un platillo de alguna ocpion dee menu, aplica
    //  a todos los productos
    
    public void PlatilloactionPerformed(ActionEvent evt){
          // procerso para obtener el indice )numerp de plato) del boton presionado 
          String num=evt.getActionCommand();     
          String vector[] = num.split(" ");  // se crea un arreglo con todas las palabras del accion command
         //  String stockable="";
          Integer indice = Integer.parseInt(vector[0]); // en cel 0 queda el numero de plato
          String descplato= platillo[indice].descripcionPlato;          
          Float precio = Float.parseFloat(platillo[indice].precioPlato);
        // JOptionPane.showMessageDialog(null,platillo[indice].peso +" "+platillo[indice].descripcionPlato);
          if(platillo[indice].peso.equals("SI")){
          // llamar a clase que selecciona si el prod se vende por pesos o kilos    
           CantidadDineroKilos selecciona = new CantidadDineroKilos( this,"1",descplato,indice,precio);
           selecciona.setVisible(true);
           } else{    // n se pide pesos ni kilos y se va directo a meterlo a la tabla
              RegresadeCantidadDinero(false,"0",precio.toString(),indice);
          }

    }   
     public void RegresadeCantidadDinero(Boolean band, String kilo, String dinero, Integer indice)  
     {
         String[] registro = new String[5];

          if (cantidad=="") {
              cantidad="1";
          }   
          if(!band){
              Double importe = Double.parseDouble(dinero) * Integer.parseInt(cantidad);

                registro[0]=cantidad;
               String numProd=platillo[indice].numeroPlato;    
                registro[1]=platillo[indice].descripcionPlato;              
                registro[2]=platillo[indice].precioPlato;  
                csecuencia = csecuencia +1;
                String peso = platillo[indice].peso;
                String stock = platillo[indice].stock;
                String catego = platillo[indice].categoriaPlato;
                registro[3] = kilo;
                registro[4] = importe.toString();
               
                modelo.addRow(registro);      // agrega platillo a la tabla del ticket
                // guarda platillo en detallecomanda de la BD
                // ahora lo guardara en la lista encadenada
                DetalleComanda art = new DetalleComanda(ticket, registro[0],numProd, registro[1], registro[2],"0",csecuencia,Integer.parseInt(registro[3]),Float.parseFloat(registro[4]),stock,peso,catego);
                art.guardaDetalle(); // metodo para guardar el detalle en la BD
                listaPlatos.add(art);   // agrega el detalle a la lista encadenada
                art.calculaTotales();
                 
                cantidad="";
                lblcantidad.setText( "Cantidad");
                btnGuardaOrden.setEnabled(true);
                btnBorrarTicket.setEnabled(true);
          }
    }
      
    
    
    public void regresaDeNumeros( String cveSupervisor) {
        Boolean superValido=false;
          if(cveSupervisor!="x"){   // que no haya dado cancelar en la clase de NUmeros
        superValido =validaSupervisor(cveSupervisor);
        if(superValido == false) {
          
            JOptionPane.showMessageDialog(null,"Clave no Valida");
            
        }else {
         for(int i=0; i<lineaSelected.length ;i++){
        // JOptionPane.showMessageDialog(null,"indice liena selectecd  "+i);
         if(lineaSelected[i]>=0){
         String pl= (String) modelo.getValueAt(lineaSelected[i]-i,1);  // sacar el numero de plato de l atabla
         String des= (String) modelo.getValueAt(lineaSelected[i]-i,2); // savar la descripcion del a tabla
        DetalleComanda art;
        art = (DetalleComanda) listaPlatos.get(lineaSelected[i]-i);   
        des=art.ddescplato;
        if(pl.equals("  ")){
           pl="10000";
        }
        if(pl.equals("----")){
           pl="10000";
        }
    
        if(des.equals("==========")){
           pl="9000";
        }
        
         if(des.equals("TO GO - TO GO")){
                pl="9000"; 
         }
        Integer secuencia = art.dsecuencia;    // obtener el indicer de de secuenca del paltillo en ekl ticket
         String sSQL= "";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();       
        
        // crear SQL praa borrar detallecomanda

        sSQL ="DELETE FROM detallecomanda WHERE numTicket = ? and numPlato=? and secuencia = ? and"
                + " descPlato = ?";

        try{
         PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, ticket);
          pst.setString(2, pl);   
                   pst.setString(3, secuencia.toString());
                   pst.setString(4,des);
           
            
         pst.executeUpdate(); 
         
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }   
         listaPlatos.remove(lineaSelected[i]-i);
         modelo.removeRow(lineaSelected[i]-i);
         }
         lineaSelected[i]=-1;
        }
        }
        
            int y = modelo.getRowCount();
        
           if (y==0){
               btnGuardaOrden.setEnabled(false);
               btnBorrarTicket.setEnabled(false);
               btnBorraLineas.setEnabled(false);
           }
          }
    }
    
    public void regresaDeNumeros2( String cveSupervisor) {
        Boolean superValido=false;
     if(cveSupervisor!="x"){   // que no haya dado cancelar en la clase de NUmeros
              System.out.println("Super "+cveSupervisor);
        superValido =validaSupervisor(cveSupervisor);
        if(superValido == false) {
          
            JOptionPane.showMessageDialog(null,"Clave no Valida");
            
        }else {
               
           for (int i=0; i <listaPlatos.size(); i++){
              listaPlatos.remove(i);
          }
                
        
          int y = modelo.getRowCount();
        while(y > 0)
         {
          
           modelo.removeRow(y-1);
           y = modelo.getRowCount();
           
         }
     btnGuardaOrden.setEnabled(false);
     btnBorrarTicket.setEnabled(false);
     btnBorraLineas.setEnabled(false);
          String sSQL= "";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();       
        
        // crear SQL praa borrar detallecomanda

        sSQL ="DELETE FROM detallecomanda WHERE numTicket = ? ";

        try{
                   PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, ticket);
         pst.executeUpdate(); 
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        } 
          }
      }
    }
    
    public boolean validaSupervisor(String supervisor){
        String sSQL=null;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        Boolean encontrado = false;
       sSQL = "SELECT  contrasena from usuario WHERE contrasena = " + supervisor 
              + " and  tipo = 'Super' ";
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                encontrado = true; 
            }
  
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }        
        return encontrado;
    }
    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
         cardlayout.show(pnlPrincipal,"menu");  // muestra panel de menu de opciones
    }//GEN-LAST:event_btnMenuActionPerformed

    private void comDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comDescuentoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_comDescuentoActionPerformed

    private void comDescuentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comDescuentoItemStateChanged
        // TODO add your handling code here:
        if(comDescuento.isEnabled()==true){
                descuento =( Subtotal * Integer.parseInt(comDescuento.getSelectedItem().toString()))/100;
                total = Subtotal - descuento;
        QuitaDecimales cadena =  new QuitaDecimales(descuento.toString());
        String resultado= cadena.dosDecimales(descuento);
        
        lblDesc.setText("Descuento : $ " + resultado);
       resultado= cadena.dosDecimales(total);

        lblTotal.setText("Total : $ " + resultado);
        comandaMesa.cTotal=total;
        comandaMesa.cdesc=descuento.toString();
        }
    }//GEN-LAST:event_comDescuentoItemStateChanged

           
    private void btnImprimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeActionPerformed
        // TODO add your handling code here:
          
         Integer dif = 0;
            
         //Crear archivo con ticket
         ArchivoTicket archivoTicket = new ArchivoTicket();
         archivoTicket.agregaHeader(ticket,"0000",cajero,"Dine in");
         
         // remover platillos del ticket por que se enviaqron a cocina
         // y los agrega al area de texto
         for (int i=0; i <modelo.getRowCount(); i++)
         {
           
            String cant = (String) modelo.getValueAt(i,0);
            String plato = (String) modelo.getValueAt(i,1);
            String descri = (String) modelo.getValueAt(i,2);
            String precio = (String) modelo.getValueAt(i,3);
            if(descri.length() < 15){
                dif = 15 - descri.length();
               for(int y=1; y<=dif;y++){
                descri = descri + " ";
                }
            }
            if(cant.length() < 3){
                dif = 3 - cant.length();
               for(int y=1; y<=dif;y++){
                cant =  " " +cant;
                }
            }    
              if(plato.length() < 3){
                dif = 3 - plato.length();
               for(int y=1; y<=dif;y++){
                plato = " " + plato;
                }
            }    
            
            archivoTicket.agregaLineas(cant,plato,descri,precio);
   
             }
                  archivoTicket.cierraArchivo();
         archivoTicket.imprimir("ticket.txt");   

    }//GEN-LAST:event_btnImprimeActionPerformed

    private void bntceroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntceroActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_bntceroActionPerformed

    private void bnt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnt2ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_bnt2ActionPerformed

    private void btnunoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnunoActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_btnunoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        cantidad += evt.getActionCommand();
        lblcantidad.setText(cantidad);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnPolloFajitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPolloFajitaActionPerformed

        buscaMenu("POLLO",false);  // se llama a la funciona pasandole como parametro
        // el action comanda que aqueivale a la categoria del platillo
        //  Producto[] platillo = new Producto[200];
    }//GEN-LAST:event_btnPolloFajitaActionPerformed

    private void btnSidesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSidesActionPerformed
        buscaMenu("SIDES",false);  // se llama a la funciona pasandole como parametro
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSidesActionPerformed

    private void btnCombosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCombosActionPerformed
        // TODO add your handling code here:
        buscaMenu("COMBOS",false);
    }//GEN-LAST:event_btnCombosActionPerformed

    private void txtrestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrestaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtrestaActionPerformed

    private void txttarjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttarjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttarjActionPerformed

    private void btnRefrescosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescosActionPerformed
        // TODO add your handling code here:
        buscaMenu("REFRESCOS",false);
    }//GEN-LAST:event_btnRefrescosActionPerformed

    private void txtefvoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtefvoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtefvoActionPerformed

    private void btnPagarComanda1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarComanda1ActionPerformed
        // TODO add your handling code here:
        PagarComanda pago = new PagarComanda(this,"1",comandaMesa.cTotal);
          pago.setVisible(true);
    }//GEN-LAST:event_btnPagarComanda1ActionPerformed

    private void btnGuardaOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaOrdenActionPerformed
        // TODO add your handling code here
        Integer dif = 0;

        DetalleComanda art;

        btnGuardaOrden.setEnabled(false);
        comandaMesa.enviaComandaBD();   // salvar los datos de la comadna en la BD
       // comandaMesa.enviaCocina();
        comandaMesa.agregaOrden();
         OrdenCajero orden = new OrdenCajero(cajero, cveCaj);
        orden.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnGuardaOrdenActionPerformed

    private void btnBorraLineasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorraLineasActionPerformed
        // TODO add your handling code here:
        Double inventa=0.00;
        String pesa="";
           if(lineaSelected.length>0) {
                for(int i=0; i<lineaSelected.length ;i++){
                    // JOptionPane.showMessageDialog(null,"indice liena selectecd  "+i);
                    if(lineaSelected[i]>=0){
                        DetalleComanda art;
                        art = (DetalleComanda) listaPlatos.get(lineaSelected[i]-i);
                       String pl= (String) art.dnumplato;  // sacar el numero de plato de l atabla
                        String des= (String) art.ddescplato; // savar la descripcion del a tabla
                        Integer secuencia = art.dsecuencia;    // obtener el indicer de de secuenca del paltillo en ekl ticket
                        String sSQL= "";
                       
                        Conexion mysql = new Conexion();
                        Connection link = mysql.conectarDB();
                        // crear SQL praa borrar detallecomanda
                        sSQL ="DELETE FROM detallecomanda WHERE numTicket = ? and numPlato=? and secuencia = ? and"
                        + " descPlato = ?";
                        try{
                            PreparedStatement pst = link.prepareStatement(sSQL);
                            pst.setString(1, ticket);
                            pst.setString(2, pl);
                            pst.setString(3, secuencia.toString());
                            pst.setString(4,des);
                            pst.executeUpdate();
                        }catch (SQLException ex){
                            JOptionPane.showMessageDialog(null,ex);
                        }
                        listaPlatos.remove(lineaSelected[i]-i);
                        modelo.removeRow(lineaSelected[i]-i);
                        art.calculaTotales();

                        // regresar inventario
                        
                           // rebajarlo del inventario
         sSQL = "SELECT inventario,peso from plato where numPlato="+pl;
         try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
             while(rs.next()){  // obtener invneario
             inventa= Double.parseDouble(rs.getString("inventario"));
             pesa = rs.getString("peso");
             }
             if(pesa.equals("SI")) {
               inventa= inventa + (art.dkilos*Integer.parseInt(art.dcantidad));
             } else inventa=inventa + Integer.parseInt(art.dcantidad);
         // svtulziar inventario en plato    
        sSQL = "UPDATE plato" +
                   " SET inventario = ? " +
                   "WHERE numPlato = " + pl;     
          try {
       PreparedStatement pst1 = link.prepareStatement(sSQL);
            pst1.setString(1,inventa.toString());
       
       int n1 = pst1.executeUpdate();
       if(n1<0)
           JOptionPane.showMessageDialog(null,"Error no se pudo actualziar el Inventario al borrar detalle");
       
          // si es combo devolver al inventario
          if(art.dcatego.equals("COMBOS"))
          { devuelveCombo(pl,art.dcantidad);}
          
          }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con elinventario al borrar detalle") ;
          } 
        
        
         }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se encontro el articulo para actualizar Inventario al borrar detalle");
          } 
        
                        
           }
                    lineaSelected[i]=-1;                                  
                }
            int y = modelo.getRowCount();
            if (y==0){
                btnGuardaOrden.setEnabled(false);
                btnBorrarTicket.setEnabled(false);
                btnBorraLineas.setEnabled(false);
                btnPagarComanda1.setEnabled(false);
            }
        }         
    }//GEN-LAST:event_btnBorraLineasActionPerformed

    private void btnBorrarTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarTicketActionPerformed
        // TODO add your handling code here:
             Conexion mysql = new Conexion();
            Connection link = mysql.conectarDB();
            Integer lista =listaPlatos.size();
         for (int i=0; i <lista; i++){
               // devolver al inventario
           
               DetalleComanda art;
               art = (DetalleComanda) listaPlatos.get(i);
               String pl= (String) art.dnumplato;  // sacar el numero de plato de l atabla
                String sSQL="";  
                sSQL = "SELECT inventario,peso from plato where numPlato="+pl;
         try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            Double inventa=0.00;
            String pesa="NO";
             while(rs.next()){  // obtener invneario
             inventa= Double.parseDouble(rs.getString("inventario"));
             pesa = rs.getString("peso");
             }
             if(pesa.equals("SI")) {
               inventa= inventa + (art.dkilos*Integer.parseInt(art.dcantidad));
             } else inventa=inventa + Integer.parseInt(art.dcantidad);
         // svtulziar inventario en plato    
        sSQL = "UPDATE plato" +
                   " SET inventario = ? " +
                   "WHERE numPlato = " + pl;     
          try {
       PreparedStatement pst1 = link.prepareStatement(sSQL);
            pst1.setString(1,inventa.toString());
       
       int n1 = pst1.executeUpdate();
       if(n1<0)
           JOptionPane.showMessageDialog(null,"Error no se pudo actualziar el Inventario al BORRAR todo el ticket");
       
          // si es combo devolver al inventario
          if(art.dcatego.equals("COMBOS"))
          { devuelveCombo(pl,art.dcantidad);}
          
          }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con elinventario al borrar la comanda") ;
          } 
        
        
         }catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Error no se encontro el articulo para actualizar Inventario al borrar detalle");
          } 
        
               
               // listaPlatos.remove(i);
         }   //del for
            listaPlatos.clear(); 
              
            int y = modelo.getRowCount();
            while(y > 0)
            {
                modelo.removeRow(y-1);
                y = modelo.getRowCount();
            }
            btnGuardaOrden.setEnabled(false);
            btnBorrarTicket.setEnabled(false);
            btnBorraLineas.setEnabled(false);
            btnCasa.setEnabled(true);
            btnSalir.setEnabled(true);
            btnPagarComanda1.setEnabled(false);
            btnImprime.setEnabled(false);
       lblArticulos.setText("Numero de Articulos: "+modelo.getRowCount());

            String sSQL= "";
          //  Conexion mysql = new Conexion();
          //  Connection link = mysql.conectarDB();
            // crear SQL praa borrar detallecomanda
            
           
            sSQL ="DELETE FROM detallecomanda WHERE numTicket = ? and enviado='0'";
            try{
                PreparedStatement pst = link.prepareStatement(sSQL);
                pst.setString(1, ticket);
                pst.executeUpdate();
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(null,ex);
                JOptionPane.showMessageDialog(null,"no se pudo borrar detalle comanda");
            }
           sSQL ="DELETE FROM comanda WHERE numTicket = ? ";
                 try{
                PreparedStatement pst = link.prepareStatement(sSQL);
                pst.setString(1, ticket);
                pst.executeUpdate();
            }catch (SQLException ex){
                JOptionPane.showMessageDialog(null,ex);
                JOptionPane.showMessageDialog(null,"No se pudo borrar comada");
            }
                 
                 
               lblSubtotal.setText("Subtotal: $0.00");
                lblTotal.setText("Total: $0.00");
                lblDesc.setText("Descuento: $0.00");
    }//GEN-LAST:event_btnBorrarTicketActionPerformed

    private void btnCambiaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiaMenuActionPerformed
        // TODO add your handling code here:
                pnlSelecciona.removeAll();   // remueve todo lo del panel de Seleccio de platillo

        buscaMenu("POLLO",true);
        buscaMenu("COMBOS",true);
        buscaMenu("SIDES",true);
        buscaMenu("REFRESCOS",true);
    }//GEN-LAST:event_btnCambiaMenuActionPerformed

    private void btnToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToolActionPerformed
        // TODO add your handling code here:
             ManejaStocks stock = new ManejaStocks();
            stock.setVisible(true);
    }//GEN-LAST:event_btnToolActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
             CorteDia corte = new CorteDia(cajero,cveCaj,false);
        corte.setVisible(true); 
    }//GEN-LAST:event_jButton1ActionPerformed
                                     
public void regresadePagarComanda(Boolean cancelo, String efectivo, String tarjeta, Double resta){
    if(!cancelo){     // el pago se realizo bien y el cajero no cancelo
       btnMenu.setEnabled(true);  
       txtefvo.setText(efectivo);
       txttarj.setText(tarjeta);
       txtresta.setText(resta.toString());
       Double efvo = Double.parseDouble(txtefvo.getText());  
       Double tarje = Double.parseDouble(txttarj.getText());
       Double rest = Double.parseDouble(txtresta.getText());
     
       if(rest<=0) {   // pagotodo y se le dio cambio               
          Conexion mysql = new Conexion();
          Connection link;        
          link = mysql.conectarDB();  
          String sSQL="UPDATE orden  SET pagada = ? WHERE numTicket = '" + ticket+ "'";
          try {
            PreparedStatement pst = link.prepareStatement(sSQL);
            pst.setString(1, "SI");
            pst.executeUpdate();
           } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la Orden con los datos de Pago") ;
          }  
          
          comandaMesa.cefvo=efvo;
          comandaMesa.ctarj=tarje;
          comandaMesa.enviaComandaBD();
          comandaMesa.agregaOrden(); 
           
          OrdenCajero orden = new OrdenCajero(cajero, cveCaj);
          orden.setVisible(true);
          dispose();
   
    } else {      // no pago completo y sigue en el mismo ticket
        btnSalir.setEnabled(false);
        btnCasa.setEnabled(false);
       }
  }
}    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrdenCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrdenCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrdenCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrdenCajero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrdenCajero( "cajero","2222").setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bnt2;
    private javax.swing.JButton bntcero;
    private javax.swing.JButton btnBorraLineas;
    private javax.swing.JButton btnBorrarTicket;
    private javax.swing.JButton btnCambiaMenu;
    private javax.swing.JButton btnCasa;
    private javax.swing.JButton btnCombos;
    private javax.swing.JButton btnGuardaOrden;
    private javax.swing.JButton btnImprime;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnPagarComanda1;
    private javax.swing.JButton btnPolloFajita;
    private javax.swing.JButton btnRefrescos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSides;
    private javax.swing.JButton btnTool;
    private javax.swing.JButton btnuno;
    private javax.swing.JComboBox<String> comDescuento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblArticulos;
    private javax.swing.JLabel lblCajero;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTicket;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblcantidad;
    private javax.swing.JLabel lblefvo;
    private javax.swing.JLabel lblhoraIni;
    private javax.swing.JLabel lblresta;
    private javax.swing.JLabel lbltarj;
    private javax.swing.JLabel numeroVentas;
    private javax.swing.JPanel pnlArriba;
    private javax.swing.JPanel pnlCarta;
    private javax.swing.JPanel pnlOpciones;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JPanel pnlSelecciona;
    private javax.swing.JTable tblTicket;
    private javax.swing.JTextField txtTicket;
    private javax.swing.JTextField txtefvo;
    private javax.swing.JTextField txtresta;
    private javax.swing.JTextField txttarj;
    private javax.swing.JLabel ventaDia;
    // End of variables declaration//GEN-END:variables
}
