/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package Frames;
import BDqueries.*;
import clasesAuxiliares.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;

import java.awt.event.*;

/**
 *
 * @author Intech-Torreon
 */
public class ManejaCombos extends javax.swing.JFrame implements MouseListener{
MyModelo modelo;
String[] categoria = new String[100];
 ListSelectionModel listSelectionModel;
  String cve_Actualizar = "" ;   
  String plato ="";
    
    public ManejaCombos() {
        initComponents();
         setLocationRelativeTo(null);
        CargarTablaProductos("xyz");
         llenarCombos();
         llenarProductos();
        inhabilitar();
        
    }
    void CargarTablaProductos(String valor){
        String[] encabezados={"#Combo","codigo","prod","Cantidad"};
        String[] registro = new String[4];
        String sSQL = "";
        modelo = new MyModelo();
          modelo.addColumn(encabezados[0]);    //a regar columnas
          modelo.addColumn(encabezados[1]);    //a regar columnas
          modelo.addColumn(encabezados[2]);    //a regar columnas
          modelo.addColumn(encabezados[3]);    //a regar columnas     
        
        listSelectionModel = tblIngredientes.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ManejaCombos.SharedListSelectionHandler());
	tblIngredientes.addMouseListener( this);

        tblIngredientes.setSelectionModel(listSelectionModel);
        
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        
        sSQL = "SELECT  numCombo,numPlato,descPlato,cantidad from prodcombo WHERE numCombo = '"+ valor +"' ORDER BY numPlato";
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                registro[0] = rs.getString("numCombo");
                registro[1] = rs.getString("numPlato");
                registro[2] = rs.getString("descPlato");
                registro[3]= rs.getString("cantidad");
                modelo.addRow(registro);
              
            }
            tblIngredientes.setModel(modelo);
          
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
  
      void BuscarProductoEditar(String combo, String prod){

        String sSQL = "";
        String descombo="", desplato="", catplato="", preplato="0.00";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        
        sSQL = "SELECT  descPlato from plato WHERE numPlato = "+combo;
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
             
                descombo = rs.getString("descPlato");
                
            }
            comboCombos.setSelectedItem(combo+" "+descombo) ;    
            comboProducto.setSelectedItem(prod);
            btnGuardar.setEnabled(true);
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
      
  void BorrarPlatillo(String comb, String prod){

        String sSQL = "";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
       
      
  
        sSQL = "DELETE FROM prodcombo WHERE numPlato = ? and numCombo =?";
        try{   
         PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, prod);
            pst.setString(2, comb);     
         int n = pst.executeUpdate();
         if(n>0){
           CargarTablaProductos(comb);
           JOptionPane.showMessageDialog(null,"Platillo Eliminado exitosamente");
           
          }
            
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
       
    }    
    void habilitar(){
        comboCombos.setEnabled(true); 
        btnSalir.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnGuardar1.setEnabled(true);
        
    }

    void inhabilitar(){
      
         btnGuardar.setEnabled(false);
     
         btnGuardar1.setEnabled(false);
        btnBorrar.setEnabled(false);
        
    }
    
    void llenarCombos( ){   // llenar el combo de combos
       
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        String sSQL="";
      
           
        sSQL = "SELECT numPlato,descPlato from plato where categoria = 'Combos'";
        
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            Integer i=0;
            comboCombos.removeAllItems();
            while(rs.next()){
           comboCombos.addItem( rs.getString("numPlato")+" "+rs.getString("descPlato"));
                i = i +1;
                    
            }
            
           
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }       
      
    }	
    void llenarProductos( ){      //llenat el combo de productos
       
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        String sSQL="";
      
           
        sSQL = "SELECT numPlato, descPlato from plato";
        
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            Integer i=0;
            comboProducto.removeAllItems();
            while(rs.next()){
                comboProducto.addItem(rs.getString("numPlato")+" "+ rs.getString("descPlato"));
                i = i +1;
                    
            }
            
           
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
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

        tipoGroup = new javax.swing.ButtonGroup();
        mnMenu = new javax.swing.JPopupMenu();
        editar = new javax.swing.JMenuItem();
        borrar = new javax.swing.JMenuItem();
        panel1 = new javax.swing.JPanel();
        lblProd = new javax.swing.JLabel();
        lblCombo = new javax.swing.JLabel();
        comboCombos = new javax.swing.JComboBox<>();
        btnGuardar1 = new javax.swing.JButton();
        comboProducto = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        comboCantidad = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblIngredientes = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

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

        panel1.setBackground(new java.awt.Color(255, 255, 51));
        panel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Armar Combo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        lblProd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblProd.setText("Producto");

        lblCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCombo.setText("Combos");

        comboCombos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        comboCombos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboCombosItemStateChanged(evt);
            }
        });
        comboCombos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCombosActionPerformed(evt);
            }
        });

        btnGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok2.png"))); // NOI18N
        btnGuardar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar1ActionPerformed(evt);
            }
        });

        comboProducto.setToolTipText("");
        comboProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboProductoActionPerformed(evt);
            }
        });

        jLabel1.setText("Cantidad");

        comboCantidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "150", "250", "500", "1000", "1500", "1", "2" }));
        comboCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCantidadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProd)
                    .addComponent(lblCombo))
                .addGap(29, 29, 29)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboCombos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboProducto, 0, 150, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(212, 212, 212))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboCombos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProd)
                    .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(comboCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 102, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ingredientes Actuales del Platillo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tblIngredientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblIngredientes.setComponentPopupMenu(mnMenu);
        tblIngredientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblIngredientes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jToolBar2.setRollover(true);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.jpg"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setBorder(null);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNuevo);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok2.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnGuardar);

        btnBorrar.setIcon(new javax.swing.ImageIcon("C:\\Users\\CASA\\Documents\\NetBeansProjects\\SistemaRest\\src\\imagenes\\borrar.jpg")); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.setFocusable(false);
        btnBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnBorrar);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/puerta2.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSalir);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
    
        habilitar();
      
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoActionPerformed
   
    void guardar(){
       Conexion mysql = new Conexion();
       Connection link;        
       link = mysql.conectarDB();
       String comb,prod,cant ="";
       String sSQL = "",mensage="" ;
//  obtener valores de los  y sus prod
       comb =(String)comboCombos.getSelectedItem();
       prod = (String)comboProducto.getSelectedItem();
       cant = (String)comboCantidad.getSelectedItem();
            
 
       sSQL = "INSERT INTO prodcombo (numCombo,numPlato,descPlato,cantidad) "
               + "VALUES (?,?,?,?)";
            
             
           
                 
       String vector[] = comb.split(" ");  // arreglo con todo lo de combo
       comb = vector[0];  // para obtenter el numero de combo
       String vector2[] = prod.split(" "); // todo lo de prod (codigo y desc)
       int i = vector2.length;
       String Cadena=vector2[1];
       for(int p=2;p<i;p++){
           Cadena= Cadena + " "+ vector2[p];
       }
       try {
       PreparedStatement pst = link.prepareStatement(sSQL);
       pst.setString(1, comb);
       pst.setString(2, vector2[0]);
       pst.setString(3,Cadena);
       pst.setString(4,cant);
       
       int n = pst.executeUpdate();
       if(n>0){
           CargarTablaProductos("");
     
       }
       }
       catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con los datos del Combo") ;
       }
    }
    
  
    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
        
        // TODO add your handling code here:
    }//GEN-LAST:event_editarActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
       // int filaSeleccionada;
       // / cve;
        
      //  try{
      //      filaSeleccionada = tblIngredientes.getSelectedRow();
        //    if(filaSeleccionada == -1){
        //        JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
        //    }else {
                 
       //           modelo = (DefaultTableModel) tblIngredientes.getModel();
//                  BorrarPlatillo(cve);

         //   }
      //  }catch(Exception e) {
            
       // }
        // TODO add your handling code here:
    }//GEN-LAST:event_borrarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
       
        guardar();
        CargarTablaProductos(plato);
       
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
     int filaSeleccionada;
        String comboborrar, productoborrar;
        
        try{
            filaSeleccionada = tblIngredientes.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblIngredientes.getModel();
                  comboborrar = (String) modelo.getValueAt(filaSeleccionada,0);
                  productoborrar = (String) modelo.getValueAt(filaSeleccionada,1);
                  BorrarPlatillo(comboborrar,productoborrar);
                    btnBorrar.setEnabled(false);

            }
        }catch(Exception e) {
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar1ActionPerformed
        // TODO add your handling code here:
     
               
         guardar();  // tratar de guardar en bd
        CargarTablaProductos(plato);  // desplega en tabla el producto agragado
        
    }//GEN-LAST:event_btnGuardar1ActionPerformed

    private void comboCombosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboCombosItemStateChanged
        // TODO add your handling code here:
        plato= (String)comboCombos.getSelectedItem();
        if(plato!=null){
        String vector[] = plato.split(" ");  // se crea un arreglo con todas las palabras del accion command
         CargarTablaProductos(vector[0]);
         plato=vector[0];
          btnBorrar.setEnabled(false);

        }
    }//GEN-LAST:event_comboCombosItemStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void comboProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboProductoActionPerformed

    private void comboCombosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCombosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCombosActionPerformed

    private void comboCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCantidadActionPerformed

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
            java.util.logging.Logger.getLogger(ManejaCombos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManejaCombos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManejaCombos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManejaCombos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ManejaCombos().setVisible(true);
            }
        });
    }

     public void mouseClicked(java.awt.event.MouseEvent evt) {
       int filaSeleccionada;
            String comb,prod;
        
        try{
            filaSeleccionada = tblIngredientes.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                  
                  modelo = (MyModelo) tblIngredientes.getModel();
                  comb = (String) modelo.getValueAt(filaSeleccionada,0);
                  prod = (String) modelo.getValueAt(filaSeleccionada,1) +
                          " " + (String) modelo.getValueAt(filaSeleccionada,2);
                  BuscarProductoEditar(comb,prod);
            
                  
            }
                    }catch(Exception e) {
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
           
            } else {
                // Find out which indexes are selected.
              if (isAdjusting){
           
               btnBorrar.setEnabled(true);
            }
            }
          //  output.append(newline);
           // output.setCaretPosition(output.getDocument().getLength());
        }
        }
         public void mouseExited(java.awt.event.MouseEvent evt) {
         }
      public void mouseReleased(java.awt.event.MouseEvent evt){ }
      public void mousePressed(java.awt.event.MouseEvent evt){ }
      public void mouseEntered(java.awt.event.MouseEvent evt){ }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem borrar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardar1;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> comboCantidad;
    private javax.swing.JComboBox<String> comboCombos;
    private javax.swing.JComboBox<String> comboProducto;
    private javax.swing.JMenuItem editar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblCombo;
    private javax.swing.JLabel lblProd;
    private javax.swing.JPopupMenu mnMenu;
    private javax.swing.JPanel panel1;
    private javax.swing.JTable tblIngredientes;
    private javax.swing.ButtonGroup tipoGroup;
    // End of variables declaration//GEN-END:variables
}
