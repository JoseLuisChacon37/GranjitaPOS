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
public class ManejaProducto extends javax.swing.JFrame  implements MouseListener{
MyModelo modelo;
String[] categoria = new String[50];
String accion = "Insertar";
ListSelectionModel listSelectionModel;
String cve_Actualizar = "" ;   
String manejaStock ="", manejaPeso="";
    /**
     * Creates new form RegistroUsuarios
     */
    public ManejaProducto() {
        initComponents();
         setLocationRelativeTo(null);
        CargarTablaProductos(" ");
         llenaCategoria();
        inhabilitar();
        
    }
    void CargarTablaProductos(String valor){
        String[] encabezados={"Codigo","Descripcion","Categoria","Precio","Stock","Peso"};
        String[] registro = new String[6];
        String sSQL = "";
        modelo = new MyModelo();
          modelo.addColumn(encabezados[0]);    //a regar columnas
          modelo.addColumn(encabezados[1]);    //a regar columnas
          modelo.addColumn(encabezados[2]);    //a regar columnas
          modelo.addColumn(encabezados[3]);    //a regar columnas     
          modelo.addColumn(encabezados[4]);
          modelo.addColumn(encabezados[5]);
     
        listSelectionModel = tblProductos.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ManejaProducto.SharedListSelectionHandler());
        tblProductos.addMouseListener( this);
	tblProductos.setSelectionModel(listSelectionModel);
        
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        
        sSQL = "SELECT numPlato, descPlato, precio, categoria, stock, peso from plato WHERE"
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
                modelo.addRow(registro);    
            }
            tblProductos.setModel(modelo);
  
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
  
      void BuscarPlatoEditar(String cve){

        String sSQL = "";
        String numplato="", desplato="", catplato="", preplato="0.00", stockable="", pesable="";
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        stockBox.setSelected(false);
        granelBox.setSelected(false);
        sSQL = "SELECT numPlato, descPlato, categoria,precio,stock,peso from plato WHERE numPlato = "+cve;
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            while(rs.next()){
                numplato = rs.getString("numPlato");
                desplato = rs.getString("descPlato");
                catplato = rs.getString("categoria");   
                preplato = rs.getString("precio");     
                stockable = rs.getString("stock");
                pesable = rs.getString("peso");
            }
            manejaStock=stockable;
            txtNoPlato.setText(numplato);
            txtDescripcion.setText(desplato);
            txtPrecio.setText(preplato);           
            comboCatego.setSelectedItem(catplato);
            txtDescripcion.setEnabled(true);
            comboCatego.setEnabled(true);
            txtPrecio.setEnabled(true);
            btnCancelar.setEnabled(true);
            manejaPeso = pesable;
            if(manejaStock.equals("SI")){
                stockBox.setSelected(true);
            }
                if(manejaPeso.equals("SI")){
                granelBox.setSelected(true);
            }
            btnGuardar.setEnabled(true);
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
      
  void BorrarProducto(String cve){

        String sSQL = "";
        Integer found = 0;
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        sSQL = "SELECT numPlato FROM detallecomanda WHERE numPlato = " +cve;
      
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
            JOptionPane.showMessageDialog(null,"No se puede borrar el producto, tiene dependencias"
                    + " en el Archivo de Detalle Comanda");
            JOptionPane.showMessageDialog(null,"Llame al aministrador");
        } else {
        sSQL = "DELETE FROM plato WHERE numPlato = ?";
        try{   
         PreparedStatement pst = link.prepareStatement(sSQL);
         pst.setString(1, cve);
         int n = pst.executeUpdate();
         if(n>0){
           CargarTablaProductos("");
           JOptionPane.showMessageDialog(null,"Producto Eliminado exitosamente");
           
          }
            
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }
        }
    }    
    void habilitar(){
        
        txtDescripcion.setEnabled(true);
        txtPrecio.setEnabled(true);
        comboCatego.setEnabled(true);
        txtNoPlato.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        stockBox.setEnabled(true);
        btnCancelar.setEnabled(true);
      
        btnGuardar.setEnabled(true);
        txtNoPlato.requestFocus();
        
    }

    void llenaCategoria( ){
        
        Conexion mysql = new Conexion();
        Connection link = mysql.conectarDB();
        String sSQL="";
        sSQL = "SELECT descCategoria from categorias";
               
        try{
            Statement st = link.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            Integer i=0;
            
            while(rs.next()){
                
                categoria[i]=rs.getString("descCategoria");
                comboCatego.addItem( categoria[i]);
                i = i +1;
                    
            }
            
           
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex);
        }       
      
    }
    void inhabilitar(){
        txtNoPlato.setEnabled(false);
        txtDescripcion.setEnabled(false);
        txtPrecio.setEnabled(false);
        comboCatego.setEnabled(false);
        txtNoPlato.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
         btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
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
        tortilla = new javax.swing.ButtonGroup();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panel1 = new javax.swing.JPanel();
        lblNumeo = new javax.swing.JLabel();
        txtNoPlato = new javax.swing.JTextField();
        lblDescricpcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        lblCategoria = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        comboCatego = new javax.swing.JComboBox<>();
        txtPrecio = new javax.swing.JTextField();
        stockBox = new javax.swing.JCheckBox();
        granelBox = new javax.swing.JCheckBox();
        panel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        barraMenu = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
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

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

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

        lblCategoria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCategoria.setText("Categoria");

        lblPrecio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPrecio.setText("Precio");

        comboCatego.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        comboCatego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCategoActionPerformed(evt);
            }
        });

        txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioActionPerformed(evt);
            }
        });

        stockBox.setText("Maneja Stock");
        stockBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockBoxActionPerformed(evt);
            }
        });

        granelBox.setText("Venta Granel");
        granelBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                granelBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNumeo)
                                .addComponent(lblDescricpcion)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addComponent(lblCategoria)
                                    .addGap(54, 54, 54)))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(lblPrecio)
                                .addGap(35, 35, 35)))
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoPlato, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(stockBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(granelBox)))
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
                .addGap(5, 5, 5)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stockBox)
                    .addComponent(granelBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrecio))
                .addGap(33, 33, 33))
        );

        stockBox.getAccessibleContext().setAccessibleName("stockBox");

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
        tblProductos.setComponentPopupMenu(mnMenu);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        barraMenu.add(btnNuevo);

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

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.jpg"))); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.setFocusable(false);
        btnBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        barraMenu.add(btnBorrar);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancela.jpg"))); // NOI18N
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
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        panel1.getAccessibleContext().setAccessibleName("RegistroproductosPanel");
        panel1.getAccessibleContext().setAccessibleDescription("Productos");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion = "Insertar";
        txtNoPlato.setEnabled(true);
        manejaStock="NO";
        stockBox.setSelected(false);
        granelBox.setSelected(false);
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
   
    void guardar(){
       Conexion mysql = new Conexion();
       Connection link;        
       link = mysql.conectarDB();
       String cve, nom, cat, pre,sel ="";
       cve = txtNoPlato.getText();
       nom = txtDescripcion.getText();
       pre = txtPrecio.getText();
       cat =(String)comboCatego.getSelectedItem();
       
       String sSQL = "" ;
       String mensaje="";
       
       if(accion.equals("Insertar")){
         
           sSQL = "INSERT INTO plato (numPlato, descPlato,precio, categoria, stock,peso) "
               + "VALUES (?,?,?,?,?,?)";
              mensaje = "Datos Registrados correctamente";
       }
       
       else if (accion.equals("Modificar")){
           sSQL = "UPDATE plato" +
                   " SET numPlato = ?, descPlato = ?," +
                   "precio = ? , categoria = ? , stock = ? , peso=?" +
                   "WHERE numPlato = " + cve_Actualizar;
          mensaje = "Datos modificados correctamente";        
       }
       try {
   PreparedStatement pst = link.prepareStatement(sSQL);
   pst.setString(1, cve);
   pst.setString(2, nom);
   pst.setString(3, pre);
   pst.setString(4, cat);
   pst.setString(5,manejaStock);
   pst.setString(6,manejaPeso);

   int n = pst.executeUpdate();
       if(n>0){
           CargarTablaProductos("");
           JOptionPane.showMessageDialog(null,mensaje);
           inhabilitar();
           manejaStock="NO";
           manejaPeso="NO";
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
                  accion = "Modificar";
                 
                  modelo = (MyModelo) tblProductos.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BuscarPlatoEditar(cve);
                  cve_Actualizar = cve;
                  
            }
        }catch(Exception e) {
            
        }
}
    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed

        
        // TODO add your handling code here:
    }//GEN-LAST:event_editarActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblProductos.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblProductos.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BorrarProducto(cve);

            }
        }catch(Exception e) {
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_borrarActionPerformed

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

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
     int filaSeleccionada;
        String cve;
        
        try{
            filaSeleccionada = tblProductos.getSelectedRow();
            if(filaSeleccionada == -1){
                JOptionPane.showMessageDialog(null, "Seleccion un Registro Primero Por favor");
            }else {
                 
                  modelo = (MyModelo) tblProductos.getModel();
                  cve = (String) modelo.getValueAt(filaSeleccionada,0);
                  BorrarProducto(cve);

            }
        }catch(Exception e) {
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoMouseClicked

    private void granelBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_granelBoxActionPerformed
        // TODO add your handling code here:
        if(manejaPeso.equals("NO"))
        manejaPeso="SI";  //   producto manjea inventarios
        else manejaPeso="NO";
        txtPrecio.requestFocus();

    }//GEN-LAST:event_granelBoxActionPerformed

    private void stockBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockBoxActionPerformed
        if(manejaStock.equals("NO"))
        manejaStock="SI";  //   producto manjea inventarios
        else manejaStock="NO";
        txtPrecio.requestFocus();

    }//GEN-LAST:event_stockBoxActionPerformed

    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioActionPerformed
        // TODO add your handling code here:
        btnGuardarActionPerformed(evt);
    }//GEN-LAST:event_txtPrecioActionPerformed

    private void comboCategoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCategoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCategoActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        comboCatego.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void txtNoPlatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPlatoActionPerformed
        txtDescripcion.requestFocus();

        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPlatoActionPerformed

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
            java.util.logging.Logger.getLogger(ManejaProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManejaProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManejaProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManejaProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManejaProducto().setVisible(true);
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
               btnBorrar.setEnabled(true);
            }
            }
          //  output.append(newline);
           // output.setCaretPosition(output.getDocument().getLength());
        }
    
 }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraMenu;
    private javax.swing.JMenuItem borrar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> comboCatego;
    private javax.swing.JMenuItem editar;
    private javax.swing.JCheckBox granelBox;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblDescricpcion;
    private javax.swing.JLabel lblNumeo;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JPopupMenu mnMenu;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private javax.swing.JCheckBox stockBox;
    private javax.swing.JTable tblProductos;
    private javax.swing.ButtonGroup tipoGroup;
    private javax.swing.ButtonGroup tortilla;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNoPlato;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
