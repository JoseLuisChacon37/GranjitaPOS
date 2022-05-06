package Frames;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Frames.*;
import BDqueries.*;
import clasesAuxiliares.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
/** system
 *
 * @author Intech-Torreon
 */
public class ManejaOrdenes extends javax.swing.JFrame  implements MouseListener{
MyModelo modelo;
ListSelectionModel listSelectionModel;
String bandera ="";
String pagada = "NO" ;   
          Fecha fecha = new Fecha();
          String nom;
          String cve;
         
    // se va a crear una comanda nueva para esa mesa
         String hoy = fecha.getFecha2();
Boolean cash = false;
        String ctotal="0.00";
        String cdesc="0.00";
        String ccash="0.00";
        String ccard="0.00";
        String ticket;
 Float Subtotal;        
    /**
     * Creates new form RegistroUsuarios
     */
    public ManejaOrdenes(String nomb, String clav) {
        nom=nomb;
        cve=clav;
        initComponents();
        lbluser.setText(nomb);
         setLocationRelativeTo(null);
           String valor = " ";
       String   sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
                + " CONCAT(numTicket,' ',nomCajero,' ',fecha,' ',pagada,' ',total) LIKE '%"+valor+"%'   and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
        
    }
    
    void CargarTablaOrdenes(String valor){
        btnPagar.setEnabled(false);
        String[] encabezados={"#Orden","Fecha","Total","Pago","Cajero"};
        String[] registro = new String[5];
        String sSQL = valor;
        modelo = new MyModelo();
          modelo.addColumn(encabezados[0]);    //a regar columnas
          modelo.addColumn(encabezados[1]);    //a regar columnas
          modelo.addColumn(encabezados[2]);    //a regar columnas
          modelo.addColumn(encabezados[3]);    //a regar columnas        
            modelo.addColumn(encabezados[4]);    //a regar columnas     
        listSelectionModel = tblOrdenes.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ManejaOrdenes.SharedListSelectionHandler());
        tblOrdenes.addMouseListener( this);
	tblOrdenes.setSelectionModel(listSelectionModel);
        tblOrdenes.setDefaultRenderer(Object.class, new ManejaOrdenes.FormatoTabla());
  
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();    
                
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                registro[0] = rs.getString("numTicket");
                registro[1] = rs.getString("fecha");
                registro[2] = rs.getString("total");
                 registro[3] = rs.getString("pagada");     
                     registro[4] = rs.getString("nomCajero"); 
                modelo.addRow(registro);
        
                        
                
            }
            tblOrdenes.setModel(modelo);
  
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
  
      void buscarOrden(String comandaTicket){
          ticket = comandaTicket;
          String[] registro = new String[4];
        String sSQL="";
        String cenvia="";
       
 
        txtNoEditable.setText("");
        txtNoEditable.append("Cant.      Descripcion                        Precio  Gramos   $");
        
         txtNoEditable.append("\n");
         txtNoEditable.append("==============================================");
         txtNoEditable.append("\n");
         txtNoEditable.append("\n");
          lblSubtotal.setText("Subtotal: $ 0.00 " );    
            lblDesc.setText("Descuento : $ "+cdesc);
            lblTotal.setText("Total: $ " + ctotal);  
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        
         sSQL ="SELECT comanda.numTicket, comanda.numMesa, comanda.numMesero, comanda.fecha"
                 + ",comanda.horainicial, comanda.horafinal, comanda.personas ,detallecomanda.cantidad"
                 + ",comanda.total, comanda.descuento, comanda.cash, comanda.card"
                 + ",detallecomanda.numPlato, detallecomanda.descPlato, detallecomanda.precio, "
                 + "detallecomanda.enviado,detallecomanda.secuencia,"
                 + "detalleComanda.kilos, detalleComanda.dinero  FROM comanda INNER JOIN detallecomanda ON comanda.numticket=detallecomanda.numTicket"
                 + " where comanda.numTicket= '" + comandaTicket + "' ORDER BY detallecomanda.numTicket,detallecomanda.secuencia";
    try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            String cgramos="   ", cimporte="   "  ;           
            while(rs.next()){              // sacar los datos de cada renglon de la BD
               
                ctotal=rs.getString("comanda.total");      
                cdesc=rs.getString("comanda.descuento");
                ccash=rs.getString("comanda.cash");
                ccard=rs.getString("comanda.card");      
                cgramos=rs.getString("detalleComanda.kilos");
                cimporte=rs.getString("detalleComanda.dinero");

                registro[0]=rs.getString("detallecomanda.cantidad");
                registro[1]=rs.getString("detallecomanda.numPlato");    
                registro[2]=rs.getString("detallecomanda.descPlato");     
                registro[3]=rs.getString("detallecomanda.precio");   
               
                cenvia=rs.getString("detallecomanda.enviado");
               
              
                
               
                 if( registro[0].equals("0") )  {  // truco para quitar los 00 y ponre blancos para
                } else {   
                 // if (cenvia.equals("1")){          // si ya gue enviado ala cocina y BD lo pone en el area de texto
                     String descri = registro[2];
                     String cant= registro[0];
                     String plato = registro[1];
                     Integer dif;
                      if(descri.length() < 25){
                       dif = 25 - descri.length();
                        for(int y=1; y<dif;y++){
                      descri = descri + "-";
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
                      
                  txtNoEditable.append(cant+"      "+ descri+"    "+registro[3]+"    "+cgramos+"    "+cimporte);
                  txtNoEditable.append("\n");
              // }
                
            }

            }
            Subtotal = Float.parseFloat(ctotal) + Float.parseFloat(cdesc);
            lblSubtotal.setText("Subtotal: $ " + Subtotal );    
            lblDesc.setText("Descuento : $ "+cdesc);
            lblTotal.setText("Total: $ " + ctotal);  
            txtefvo.setText(ccash);
            txttarj.setText(ccard);
            float resta = ((Float.parseFloat(ctotal) - Float.parseFloat(ccash)) - Float.parseFloat(ccard));
            txtresta.setText(Float.toString(resta));
            tblOrdenes.setModel(modelo);
            if(pagada.equals("NO")){
                btnPagar.setEnabled(true);
            } else {
                 btnPagar.setEnabled(false);
            }
           int y = modelo.getRowCount();
               
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }            
    }
public class FormatoTabla extends DefaultTableCellRenderer{
  String enviado="";
 public Component getTableCellRendererComponent
 (JTable table, Object value, boolean selected, boolean focused, int row, int column)
 {
        
        enviado = (String) modelo.getValueAt(row,0);    
        if (enviado.equals("si")){

   setForeground(Color.green);
   } else {
              
       setForeground(Color.red);
   }
    super.getTableCellRendererComponent(table, value, selected, focused, row, column);
 return this;     
 }
   }
            
  void borrarOrden(String cve){

 
        Boolean autoriza=false;
        
        if(pagada.equals("SI")){
            //autoriaacoppn
          Numeros superVis = new Numeros(false, this,"6",1);
           superVis.setVisible(true);  
        } else {
            autoriza = true;
        }
        if (autoriza ){
                   String sSQL = "";
        Integer found = 0;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
            sSQL = "DELETE FROM orden  WHERE numTicket = ?";
          try{   
             PreparedStatement pst = link.prepareStatement(sSQL);
             pst.setString(1, cve);
              int n = pst.executeUpdate();
              
               if(n>0){
                limpiar();
         sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
                + " pagada='" + bandera + "'  and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
               
               
                 sSQL = "DELETE FROM comanda  WHERE numTicket = ?";
          try{   
              pst = link.prepareStatement(sSQL);
             pst.setString(1, cve);
               pst.executeUpdate();
              
             
            
             }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"No se pudo borrar el Ticket de la Comanda");
            }
               
                sSQL = "DELETE FROM detallecomanda  WHERE numTicket = ?";  
               try{   
              pst = link.prepareStatement(sSQL);
             pst.setString(1, cve);
               pst.executeUpdate();
              
             
            
             }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"No se pudo borrar el Detalle de la Comanda");
            } 
               
             
           
               }
            
             }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"No se pudo borrar la Orden");
            }
        }
        
    }    
  
public void regresaDeNumeros( String cveSupervisor) {
        Boolean superValido=false;
     if(cveSupervisor!="x"){   // que no haya dado cancelar en la clase de NUmeros
        superValido =validaSupervisor(cveSupervisor);
        if(superValido == false) {
          
            JOptionPane.showMessageDialog(null,"Clave no Valida");
            
        }else {
             
                   String sSQL = "";
        Integer found = 0;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
            sSQL = "DELETE FROM orden  WHERE numTicket = ?";
          try{   
             PreparedStatement pst = link.prepareStatement(sSQL);
             pst.setString(1, ticket);
              int n = pst.executeUpdate();
              
               if(n>0){
                limpiar();
         sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
               + " pagada='" + bandera + "'  and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
               
               
                 sSQL = "DELETE FROM comanda  WHERE numTicket = ?";
          try{   
              pst = link.prepareStatement(sSQL);
             pst.setString(1, ticket);
               pst.executeUpdate();
              
             
            
             }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"No se pudo borrar el Ticket de la Comanda");
            }
               
                sSQL = "DELETE FROM detallecomanda  WHERE numTicket = ?";  
               try{   
              pst = link.prepareStatement(sSQL);
             pst.setString(1, ticket);
               pst.executeUpdate();
              
             
            
             }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"No se pudo borrar el Detalle de la Comanda");
            } 
               
             
           
               }
            
             }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
            JOptionPane.showMessageDialog(null,"No se pudo borrar la Orden");
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
              + " and  tipo = 'Admin' ";
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
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipoGroup = new javax.swing.ButtonGroup();
        mnMenu = new javax.swing.JPopupMenu();
        editar = new javax.swing.JMenuItem();
        borrar = new javax.swing.JMenuItem();
        tortilla = new javax.swing.ButtonGroup();
        jToolBar2 = new javax.swing.JToolBar();
        btnPagar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        panel1 = new javax.swing.JPanel();
        pnlScroll2 = new javax.swing.JScrollPane();
        txtNoEditable = new javax.swing.JTextArea();
        lblSubtotal = new javax.swing.JLabel();
        lblDesc = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtefvo = new javax.swing.JTextField();
        txttarj = new javax.swing.JTextField();
        txtresta = new javax.swing.JTextField();
        panle2 = new javax.swing.JPanel();
        pnlScroll = new javax.swing.JScrollPane();
        tblOrdenes = new javax.swing.JTable();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnPagada = new javax.swing.JButton();
        btnNoPagada = new javax.swing.JButton();
        lbluser = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        editar.setText("Modificar Registro");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });
        mnMenu.add(editar);

        borrar.setText("Eliminar Registro");
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarActionPerformed(evt);
            }
        });
        mnMenu.add(borrar);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jToolBar2.setRollover(true);

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pesos.jpg"))); // NOI18N
        btnPagar.setText("Pagar");
        btnPagar.setBorder(null);
        btnPagar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPagar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPagar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPagarMouseClicked(evt);
            }
        });
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnPagar);

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.jpg"))); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.setToolTipText("Ordenes sin pagar requieren Autorizacion");
        btnBorrar.setFocusable(false);
        btnBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnBorrar);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnCancelar);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/home.png"))); // NOI18N
        btnSalir.setText("Home");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSalir);

        panel1.setBackground(new java.awt.Color(255, 255, 51));
        panel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la Orden", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        txtNoEditable.setColumns(20);
        txtNoEditable.setRows(5);
        pnlScroll2.setViewportView(txtNoEditable);

        lblSubtotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(0, 0, 204));
        lblSubtotal.setText("Subtotal");

        lblDesc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDesc.setForeground(new java.awt.Color(0, 153, 0));
        lblDesc.setText("Descuento");

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 0, 204));
        lblTotal.setText("Total");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Efectivo");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Tarjeta");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Cambio");

        txtefvo.setText("                  ");
        txtefvo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtefvoActionPerformed(evt);
            }
        });

        txttarj.setText("                 ");
        txttarj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttarjActionPerformed(evt);
            }
        });

        txtresta.setText("                 ");
        txtresta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrestaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDesc)
                            .addComponent(lblSubtotal)
                            .addComponent(lblTotal))
                        .addContainerGap(206, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtefvo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtresta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(53, 53, 53)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttarj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(102, 102, 102))))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                .addComponent(pnlScroll2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSubtotal)
                .addGap(13, 13, 13)
                .addComponent(lblDesc)
                .addGap(31, 31, 31)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtefvo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttarj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtresta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        panle2.setBackground(new java.awt.Color(255, 102, 0));
        panle2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consulta de Ordenes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        tblOrdenes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblOrdenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblOrdenes.setComponentPopupMenu(mnMenu);
        tblOrdenes.setRowHeight(30);
        tblOrdenes.setRowMargin(7);
        tblOrdenes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        pnlScroll.setViewportView(tblOrdenes);

        lblBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuscar.setText("Datos de busqueda");

        txtBuscar.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnPagada.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPagada.setForeground(new java.awt.Color(0, 204, 0));
        btnPagada.setText("PAGADAS");
        btnPagada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagadaActionPerformed(evt);
            }
        });

        btnNoPagada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNoPagada.setForeground(new java.awt.Color(204, 0, 0));
        btnNoPagada.setText("NO PAGADAS");
        btnNoPagada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoPagadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panle2Layout = new javax.swing.GroupLayout(panle2);
        panle2.setLayout(panle2Layout);
        panle2Layout.setHorizontalGroup(
            panle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panle2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscar)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPagada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNoPagada, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
        );
        panle2Layout.setVerticalGroup(
            panle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panle2Layout.createSequentialGroup()
                .addGroup(panle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPagada, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNoPagada, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panle2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(panle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBuscar)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        lbluser.setFont(new java.awt.Font("Traditional Arabic", 3, 18)); // NOI18N
        lbluser.setForeground(new java.awt.Color(0, 0, 204));
        lbluser.setText("UsuARIO");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 0, 0));
        jLabel5.setText("Contol de Ordenes ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panle2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(lbluser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(109, 109, 109)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbluser)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
      double totalOrden = Double.parseDouble(ctotal);
       PagarComanda pago = new PagarComanda(this,"2",totalOrden, "1111 ");
        pago.setVisible(true);
     
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPagarActionPerformed
public void regresadePagarComanda(Boolean cancelo, String efectivo, String tarjeta, Double resta){
     if(!cancelo){
     
        txtefvo.setText(efectivo);
        txttarj.setText(tarjeta);
        txtresta.setText(resta.toString());
        Double efvo = Double.parseDouble(txtefvo.getText());  
        Double tarje = Double.parseDouble(txttarj.getText());
        Double rest = Double.parseDouble(txtresta.getText());
        if(rest<=0)    // pagotodo y se le dio cambio
        {
        
        
        
      Fecha fecha = new Fecha();      
      String horafinal = fecha.getHora();
      Conexion mysql = new Conexion();
      Connection link;        
      link = mysql.conectarDB();  
      String sSQL="";
  
      sSQL = "UPDATE comanda  SET cash = ?, card =?, descuento =?, total=?, horafinal = ? WHERE numTicket = '" + ticket + "'";
             
            try {
            PreparedStatement pst = link.prepareStatement(sSQL);
             pst.setString(1, efvo.toString());
               pst.setString(2, tarje.toString());
             pst.setString(3,cdesc);
              pst.setString(4,ctotal.toString());            
            pst.setString(5, horafinal);
           // JOptionPane.showMessageDialog(null,pst);
            pst.executeUpdate();
  
           } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con los datos de Pago del ticket") ;
          } // todo pago bien y se crea el proximo ticlet

              
             sSQL = "UPDATE orden  SET pagada = ? WHERE numTicket = '" + ticket+ "'";
             
               try {
            PreparedStatement pst = link.prepareStatement(sSQL);
             pst.setString(1, "SI");
            pst.executeUpdate();
          
  
           } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la Orden con los datos de Pago") ;
          }  
          
   
    }  
        
    
}    

    
}
    
public void limpiar(){
                  btnPagar.setEnabled(false);
               txtNoEditable.setText("");
                   lblSubtotal.setText("Subtotal: $" );    
            lblDesc.setText("Descuento : $ ");
            lblTotal.setText("Total: $ ");      
            txtefvo.setText("   ");
            txttarj.setText("   ");
            txtresta.setText("   ");
}
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
                         Cajero usuario = new Cajero();
                 
                 usuario.nombreCajero = nom;
                 usuario.claveCajero=cve;
                 usuario.cambiarTitulo();
                 usuario.setVisible(true);                
                 dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
                String valor = " ";
                limpiar();
       String   sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
                + " CONCAT(numTicket,' ',nomCajero,' ',fecha,' ',pagada,' ',total) LIKE '%"+valor+"%'   and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed
   
   
    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
     
        
        // TODO add your handling code here:
    }//GEN-LAST:event_editarActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblOrdenes.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblOrdenes.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  borrarOrden(cve);

            }
        }catch(Exception e) {
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_borrarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
       limpiar();
        String valor = txtBuscar.getText();
       String   sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
                + " CONCAT(numTicket,' ',nomCajero,' ',fecha,' ',pagada,' ',total) LIKE '%"+valor+"%'  and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
        btnBuscarActionPerformed(evt);
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed

 borrarOrden(ticket);
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnPagarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPagarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPagarMouseClicked

    private void btnPagadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagadaActionPerformed
        // TODO add your handling code here:
       limpiar();    
       bandera="SI";
       String   sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
                + " pagada='" + bandera + "'  and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
    }//GEN-LAST:event_btnPagadaActionPerformed

    private void btnNoPagadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoPagadaActionPerformed
        // TODO add your handling code here:
      limpiar();
      bandera="NO";
        String   sSQL = "SELECT numTicket, nomCajero, fecha, pagada, total from orden WHERE"
                 + " pagada='" + bandera + "'  and"
                + " fecha <= '" + hoy + "' ORDER by numTicket";
        CargarTablaOrdenes(sSQL);
    }//GEN-LAST:event_btnNoPagadaActionPerformed

    private void txtrestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrestaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtrestaActionPerformed

    private void txtefvoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtefvoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtefvoActionPerformed

    private void txttarjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttarjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttarjActionPerformed

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
            java.util.logging.Logger.getLogger(ManejaOrdenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManejaOrdenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManejaOrdenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManejaOrdenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManejaOrdenes("xxx","ÿyy").setVisible(true);
            }
        });
    }
    
 public void mouseClicked(java.awt.event.MouseEvent evt) {
     //habilitar();
     int filaSeleccionada;
        String ticket;
        
        try{
            filaSeleccionada = tblOrdenes.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                  
                 
                  modelo = (MyModelo) tblOrdenes.getModel();
                  ticket = (String) modelo.getValueAt(filaSeleccionada,0);
                   pagada = (String) modelo.getValueAt(filaSeleccionada,3);
                  //buscarOrden(ticket);
           
                   
            }
        }catch(Exception e) {
        }
       
  }
     public void mouseExited(java.awt.event.MouseEvent evt) {

  }
      public void mouseEntered(java.awt.event.MouseEvent evt) {

  }    
           public void mouseReleased(java.awt.event.MouseEvent evt) {

  }  
   public void mousePressed(java.awt.event.MouseEvent evt) {

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
           
            } else {
                // Find out which indexes are selected.
              if (isAdjusting){
               
                  btnPagar.setEnabled(true);
  
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                int y=0;

                for (int i = minIndex; i <= maxIndex; i++) { // obteber los indices de las lineas seleccionadas
                    if (lsm.isSelectedIndex(i)) {
            //        
                     String ticket = (String) modelo.getValueAt(i,0);
                     pagada = (String) modelo.getValueAt(i,3);
                    
                  buscarOrden(ticket);
                     
                    }
                }
          
            }
            }
          //  output.append(newline);
           // output.setCaretPosition(output.getDocument().getLength());
        }
    
 }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem borrar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnNoPagada;
    private javax.swing.JButton btnPagada;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JMenuItem editar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbluser;
    private javax.swing.JPopupMenu mnMenu;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panle2;
    private javax.swing.JScrollPane pnlScroll;
    private javax.swing.JScrollPane pnlScroll2;
    private javax.swing.JTable tblOrdenes;
    private javax.swing.ButtonGroup tipoGroup;
    private javax.swing.ButtonGroup tortilla;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextArea txtNoEditable;
    private javax.swing.JTextField txtefvo;
    private javax.swing.JTextField txtresta;
    private javax.swing.JTextField txttarj;
    // End of variables declaration//GEN-END:variables
}
