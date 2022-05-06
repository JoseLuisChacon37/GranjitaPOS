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
/**
 *
 * @author Intech-Torreon
 */
public class ManejaUsuarios extends javax.swing.JFrame  implements MouseListener{
MyModelo modelo;
String[] categoria = new String[100];
 String accion = "Insertar";
   ListSelectionModel listSelectionModel;
  String cve_Actualizar = "" ;   
    /**
     * Creates new form RegistroUsuarios
     */
    public ManejaUsuarios() {
        initComponents();
         setLocationRelativeTo(null);
        CargarTablaUsuarios(" ");
         llenaCategoria("No");
        inhabilitar();
        
    }
    void CargarTablaUsuarios(String valor){
        String[] encabezados={"Cve","Nombre","Categoria","Telefono","Password"};
        String[] registro = new String[5];
        String sSQL = "";
        modelo = new MyModelo();
          modelo.addColumn(encabezados[0]);    //a regar columnas
          modelo.addColumn(encabezados[1]);    //a regar columnas
          modelo.addColumn(encabezados[2]);    //a regar columnas
          modelo.addColumn(encabezados[3]);    //a regar columnas          
                modelo.addColumn(encabezados[4]);    //a regar columnas       
        listSelectionModel = tblUsuarios.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ManejaUsuarios.SharedListSelectionHandler());
        tblUsuarios.addMouseListener( this);
	tblUsuarios.setSelectionModel(listSelectionModel);
        
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
     
       sSQL = "SELECT clave, nombre, telefono, tipo, contrasena from usuario WHERE"
              + " CONCAT(clave,' ',nombre,' ',telefono) LIKE '%"+valor+"%'";
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                registro[0] = rs.getString("clave");
                registro[1] = rs.getString("nombre");
                registro[2] = rs.getString("tipo");
                 registro[3] = rs.getString("telefono"); 
              registro[4] = rs.getString("contrasena");    
                modelo.addRow(registro);  
            }
            tblUsuarios.setModel(modelo);
  
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
  
      void BuscarUserEditar(String cve){

        String sSQL = "";
        String clve="", nom="", tipo="", tel="", pass="";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        
        sSQL = "SELECT clave, nombre, tipo,telefono,contrasena from usuario WHERE clave = "+cve;
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                clve = rs.getString("clave");
                nom = rs.getString("nombre");
                tipo= rs.getString("tipo");   
                tel = rs.getString("telefono");     
                pass=rs.getString("contrasena"); 
                
            }
         
            txtClave.setText(clve);
            txtNombre.setText(nom);     
            txtTelefono.setText(tel);
            comboCatego.setSelectedItem(tipo);
           txtPass.setText(pass)   ;       
            txtNombre.setEnabled(true);
            comboCatego.setEnabled(true);
            txtTelefono.setEnabled(true);
            txtPass.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnGuardar2.setEnabled(true);
            btnGuardar.setEnabled(true);
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
      
  void BorrarUser(String cve){

        String sSQL = "";
        Integer found = 0;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        sSQL = "SELECT numMesero FROM comanda WHERE numMesero = " +cve;
      
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);              
            while(rs.next()){              // sacar los datos de cada renglon de la BD
              found=found+1;
              
            }

        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }  
        if (found>0){
            JOptionPane.showMessageDialog(null,"No se puede borrar el Usuario, tiene dependencias"
                    + " en el Archivo de Comanda");
            JOptionPane.showMessageDialog(null,"Llame al aministrador");
        } else {
        sSQL = "DELETE FROM usuario WHERE clave = ?";
        try{   
         PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, cve);
         int n = pst.executeUpdate();
         if(n>0){
           CargarTablaUsuarios("");
           JOptionPane.showMessageDialog(null,"Usuario Eliminado exitosamente");
           
          }
            
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
        }
    }    
    void habilitar(){
       
       // txtNoPlato.setEnabled(true);
        txtNombre.setEnabled(true);
        txtTelefono.setEnabled(true);
        comboCatego.setEnabled(true);
        txtClave.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
         txtPass.setText("");
           txtPass.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGuardar2.setEnabled(true);
        btnGuardar.setEnabled(true);
        txtClave.requestFocus();
        
    }

    void llenaCategoria( String catego){
       
                comboCatego.addItem("Admin");
           comboCatego.addItem("Cajero");       
   
    }
    void inhabilitar(){
        txtClave.setEnabled(false);
        txtNombre.setEnabled(false);
        txtTelefono.setEnabled(false);
        comboCatego.setEnabled(false);
        txtClave.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtPass.setEnabled(false);
        txtPass.setText("");
         btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar2.setEnabled(false);
        btnModificar.setEnabled(false);  
        btnBorrar.setEnabled(false);
        
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
        lblclave = new javax.swing.JLabel();
        txtClave = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblCategoria = new javax.swing.JLabel();
        kbkelefono = new javax.swing.JLabel();
        comboCatego = new javax.swing.JComboBox<>();
        txtTelefono = new javax.swing.JTextField();
        btnGuardar2 = new javax.swing.JButton();
        lblpass = new javax.swing.JLabel();
        txtPass = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
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
        panel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registro de Usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        lblclave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblclave.setText("Clave");

        txtClave.setColumns(10);
        txtClave.setText(" ");
        txtClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveActionPerformed(evt);
            }
        });

        lblNombre.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNombre.setText("Nombre");

        txtNombre.setColumns(10);
        txtNombre.setText(" ");
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        lblCategoria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCategoria.setText("Categoria");

        kbkelefono.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        kbkelefono.setText("Telefono");

        comboCatego.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        comboCatego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCategoActionPerformed(evt);
            }
        });

        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });

        btnGuardar2.setBackground(new java.awt.Color(0, 255, 0));
        btnGuardar2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok2.png"))); // NOI18N
        btnGuardar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar2ActionPerformed(evt);
            }
        });

        lblpass.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblpass.setText("Password");

        txtPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCategoria)
                    .addComponent(lblNombre)
                    .addComponent(lblclave)
                    .addComponent(kbkelefono)
                    .addComponent(lblpass))
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(comboCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(txtPass)))
                                .addGap(194, 194, 194)
                                .addComponent(btnGuardar2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblclave)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kbkelefono)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblpass)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar2)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 102, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consulta de todo el Menu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblUsuarios.setComponentPopupMenu(mnMenu);
        tblUsuarios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblUsuarios);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscar)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBuscar)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jToolBar2.setRollover(true);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.jpg"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setBorder(null);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoMouseClicked(evt);
            }
        });
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNuevo);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnGuardar);

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lapiz.jpg"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setFocusable(false);
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnModificar);

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borra1.jpg"))); // NOI18N
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
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion = "Insertar";
        txtClave.setEnabled(true);
        habilitar();
     
     
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
         inhabilitar();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveActionPerformed
       txtNombre.requestFocus();
     
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
      comboCatego.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed
   
    private void btnGuardar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar2ActionPerformed
          guardar();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar2ActionPerformed

    void guardar(){
       Conexion mysql = new Conexion();
       Connection link;        
       link = mysql.conectarDB();
       String cve, nom, cat, tel, pass ="";
       cve = txtClave.getText();
       nom = txtNombre.getText();
       tel = txtTelefono.getText();
       cat =(String)comboCatego.getSelectedItem();
       
       pass = txtPass.getText();
       String sSQL = "" ;
       String mensaje="";
       
       if(accion.equals("Insertar")){
         
           sSQL = "INSERT INTO usuario (clave, nombre ,telefono, tipo, contrasena) "
               + "VALUES (?,?,?,?,?)";
              mensaje = "Datos Registrados correctamente";
       }
       
       else if (accion.equals("Modificar")){
           sSQL = "UPDATE usuario" +
                   " SET clave = ? , nombre = ?," +
                   "telefono = ? , tipo = ? , contrasena = ? " +
                   "WHERE clave = " + cve_Actualizar;
          mensaje = "Datos modificados correctamente";        
       }
       try {
       PreparedStatement pst = link.prepareStatement(sSQL);
            pst.setString(1, cve);
       pst.setString(2, nom);
       pst.setString(3, tel);
       pst.setString(4, cat);
       pst.setString(5,pass);
       
       int n = pst.executeUpdate();
       if(n>0){
           CargarTablaUsuarios("");
           JOptionPane.showMessageDialog(null,mensaje);
           inhabilitar();
           
       }
       }
       catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,ex);
           JOptionPane.showMessageDialog(null,"Ërror al acutlizar la BD con los datos del Usuario") ;
       }
    }
    void modificar(){
        int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblUsuarios.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                  accion = "Modificar";
                 
                  modelo = (MyModelo) tblUsuarios.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BuscarUserEditar(cve);
                  cve_Actualizar = cve;
                  
            }
        }catch(Exception e) {
            
        }
}
    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
        modificar();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_editarActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblUsuarios.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblUsuarios.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BorrarUser(cve);

            }
        }catch(Exception e) {
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_borrarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String valor = txtBuscar.getText();
        CargarTablaUsuarios(valor);
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

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
     int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblUsuarios.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblUsuarios.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BorrarUser(cve);

            }
        }catch(Exception e) {
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        modificar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoMouseClicked

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
        txtPass.requestFocus();
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void txtPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassActionPerformed
        // TODO add your handling code here:
          btnGuardarActionPerformed(evt);
    }//GEN-LAST:event_txtPassActionPerformed

    private void comboCategoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCategoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCategoActionPerformed

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
            java.util.logging.Logger.getLogger(ManejaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManejaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManejaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManejaUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManejaUsuarios().setVisible(true);
            }
        });
    }
    
 public void mouseClicked(java.awt.event.MouseEvent evt) {
     //habilitar();
     int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblUsuarios.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                  accion = "Modificar";
                 
                  modelo = (MyModelo) tblUsuarios.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BuscarUserEditar(cve);
                  cve_Actualizar = cve;
                  txtNombre.requestFocus();
                  
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
               btnModificar.setEnabled(true);
               btnBorrar.setEnabled(true);
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
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardar2;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> comboCatego;
    private javax.swing.JMenuItem editar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel kbkelefono;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblclave;
    private javax.swing.JLabel lblpass;
    private javax.swing.JPopupMenu mnMenu;
    private javax.swing.JPanel panel1;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.ButtonGroup tipoGroup;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtClave;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
