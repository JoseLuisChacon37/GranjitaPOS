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
public class ManejaStocks extends javax.swing.JFrame  implements MouseListener{
MyModelo modelo;
String[] categoria = new String[50];
String accion = "Insertar";
ListSelectionModel listSelectionModel;
String cve_Actualizar = "" ;   
String manejaStock ="", manejaPeso="";
  String numplato="", desplato="",  inventa="";
  
    public ManejaStocks() {
        initComponents();
         setLocationRelativeTo(null);
        CargarTablaProductos(" ");
        // llenaCategoria();
        inhabilitar();
        
    }
    void CargarTablaProductos(String valor){
        String[] encabezados={"Codigo","Descripcion","Categoria","Precio","Stock","Peso","Inventario"};
        String[] registro = new String[7];
        String sSQL = "";
        modelo = new MyModelo();
          modelo.addColumn(encabezados[0]);    //a regar columnas
          modelo.addColumn(encabezados[1]);    //a regar columnas
          modelo.addColumn(encabezados[2]);    //a regar columnas
          modelo.addColumn(encabezados[3]);    //a regar columnas     
          modelo.addColumn(encabezados[4]);
          modelo.addColumn(encabezados[5]);
              modelo.addColumn(encabezados[6]);
 
        listSelectionModel = tblProductos.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ManejaStocks.SharedListSelectionHandler());
        tblProductos.addMouseListener( this);
	tblProductos.setSelectionModel(listSelectionModel);
        
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        
        sSQL = "SELECT numPlato, descPlato, precio, categoria, stock, peso, inventario from plato WHERE"
                + " CONCAT(numPlato,' ',descPlato,' ',precio,' ',categoria) LIKE '%"+valor+"%'";
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                registro[0] = rs.getString("numPlato");
                registro[1] = rs.getString("descPlato");
                registro[2] = rs.getString("categoria");
                registro[3] = rs.getString("precio"); 
                registro[4] = rs.getString("stock"); 
                registro[5] = rs.getString("peso");
                registro[6] = rs.getString("inventario");

                modelo.addRow(registro);    
            }
            tblProductos.setModel(modelo);
  
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
  
      void BuscarPlatoEditar(String cve){

        String sSQL = "";
      
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        sSQL = "SELECT numPlato, descPlato,inventario from plato WHERE numPlato = "+cve;
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                numplato = rs.getString("numPlato");
                desplato = rs.getString("descPlato");
                inventa = rs.getString("inventario");
            }
           
            txtNoPlato.setText(numplato);
            txtDescripcion.setText(desplato);
            txtInventa.setText(inventa);           
            txtDescripcion.setEnabled(false);
            txtInventa.setEnabled(true);
            btnCancelar.setEnabled(true);            
            btnGuardar.setEnabled(true);
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
      
      
    void habilitar(){
        
        txtDescripcion.setEnabled(true);
        txtInventa.setEnabled(true);
        txtNoPlato.setText("");
        txtDescripcion.setText("");
        txtInventa.setText("");
        btnCancelar.setEnabled(true);
      
        btnGuardar.setEnabled(true);        
    }

  
    void inhabilitar(){
        txtNoPlato.setEnabled(false);
        txtDescripcion.setEnabled(false);
        txtInventa.setEnabled(false);
        txtNoPlato.setText("");
        txtDescripcion.setText("");
        txtInventa.setText("");
         btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new javax.swing.JPanel();
        lblNumeo = new javax.swing.JLabel();
        txtNoPlato = new javax.swing.JTextField();
        lblDescricpcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        lblPrecio = new javax.swing.JLabel();
        txtInventa = new javax.swing.JTextField();
        panel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        barraMenu = new javax.swing.JToolBar();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panel1.setBackground(new java.awt.Color(255, 255, 51));
        panel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registro de Productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        lblNumeo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNumeo.setText("# Plato");

        txtNoPlato.setColumns(10);
        txtNoPlato.setText(" ");
        txtNoPlato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoPlatoActionPerformed(evt);
            }
        });

        lblDescricpcion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDescricpcion.setText("Descripcion");

        txtDescripcion.setColumns(10);
        txtDescripcion.setText(" ");
        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });

        lblPrecio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPrecio.setText("Inventario");

        txtInventa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInventaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNumeo)
                    .addComponent(lblDescricpcion)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblPrecio)))
                .addGap(42, 42, 42)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoPlato, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInventa, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(461, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumeo)
                    .addComponent(txtNoPlato, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescricpcion))
                .addGap(18, 18, 18)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrecio)
                    .addComponent(txtInventa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel2.setBackground(new java.awt.Color(255, 102, 0));
        panel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consulta de todo el Menu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblProductos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblProductos);

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

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBuscar)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(281, 281, 281))
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBuscar)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        barraMenu.setRollover(true);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok2.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        barraMenu.add(btnGuardar);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        barraMenu.add(btnCancelar);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/puerta2.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        barraMenu.add(btnSalir);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(barraMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );

        panel1.getAccessibleContext().setAccessibleName("RegistroproductosPanel");
        panel1.getAccessibleContext().setAccessibleDescription("Productos");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
         inhabilitar();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtNoPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPlatoActionPerformed
       txtDescripcion.requestFocus();
     
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPlatoActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed
   
    void guardar(){
       Conexion mysql = new Conexion();
       Connection link;        
       link = mysql.conectarDB();
       String inv ="";
       inv = txtInventa.getText();
       String sSQL = "" ;
       Double InvTotal=0.00;
       System.out.println(inventa);
       InvTotal = Double.parseDouble(inventa)+ Integer.parseInt(inv);
           sSQL = "UPDATE plato set inventario=?" +
                   "WHERE numPlato = " + cve_Actualizar;
       
       try {
       PreparedStatement pst = link.prepareStatement(sSQL);
       pst.setString(1, InvTotal.toString());
       
       int n = pst.executeUpdate();
       if(n>0){
           CargarTablaProductos("");
           inhabilitar();
       }
       }
       catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con los datos del platillo") ;
       }
    
    }  
    void modificar(){
        int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblProductos.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblProductos.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BuscarPlatoEditar(cve);
                  cve_Actualizar = cve;
                  
            }
        }catch(Exception e) {
            
        }
}
    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String valor = txtBuscar.getText();
        CargarTablaProductos(valor);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
        btnBuscarActionPerformed(evt);
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtInventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInventaActionPerformed
        // TODO add your handling code here:
        btnGuardarActionPerformed(evt);
    }//GEN-LAST:event_txtInventaActionPerformed

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
            java.util.logging.Logger.getLogger(ManejaStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManejaStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManejaStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManejaStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ManejaStocks().setVisible(true);
            }
        });
    }
    
 public void mouseClicked(java.awt.event.MouseEvent evt) {
     //habilitar();
     int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblProductos.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                  accion = "Modificar";
                 
                  modelo = (MyModelo) tblProductos.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BuscarPlatoEditar(cve);
                  cve_Actualizar = cve;
                  txtDescripcion.requestFocus();
                  
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
              // btnBorrar.setEnabled(true);
            }
            }
          //  output.append(newline);
           // output.setCaretPosition(output.getDocument().getLength());
        }
    
 }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraMenu;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblDescricpcion;
    private javax.swing.JLabel lblNumeo;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtInventa;
    private javax.swing.JTextField txtNoPlato;
    // End of variables declaration//GEN-END:variables
}
