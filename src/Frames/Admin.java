package Frames;
//import Frames.Acceso;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class Admin extends javax.swing.JFrame implements ActionListener {
   public  String nombreUsuario;
   public   String claveUsuario;
       JButton btnAdmin = new JButton();
       JButton btnOrdenes = new JButton();
       JButton btnSalir = new JButton();  
       JButton btnCorte = new JButton();
         JButton btnMenu = new JButton();       
       
    public Admin (){
        super();
        this.setUndecorated(true);
     
       // setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
       //botones

       btnAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/admin.png")));
       btnOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/comanda.png")));
       btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/puerta.jpg")));
       btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menu2.jpg")));
       btnCorte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/corte.png")));
       btnMenu.addActionListener(this);
       btnAdmin.addActionListener(this);
       btnSalir.addActionListener(this);
       btnOrdenes.addActionListener(this);
          btnCorte.addActionListener(this);

       // crear barra
       JToolBar barra = new JToolBar();
       barra.add(btnMenu);
        barra.add(btnOrdenes);
             barra.add(btnCorte);

       barra.add(btnAdmin);
       barra.add(btnSalir);

     
     //  JTextArea editar = new JTextArea(8,40);
     //  JScrollPane scroll = new JScrollPane(editar);
       BorderLayout borde = new BorderLayout();
       setLayout(borde);
       add("North", barra);
      // add("Center",scroll);
       pack();
       setVisible(true);
     
    }
   public void actionPerformed(ActionEvent evt){
        Object fuente = evt.getSource();
        if(fuente==btnAdmin){
            Configuracion config = new Configuracion();
            config.cveUsuario=claveUsuario;
            config.Usuario=nombreUsuario;
            config.setVisible(true);
            dispose();
        } 
        if(fuente==btnSalir){
           
          Acceso login = new Acceso();
        login.setVisible(true);
        dispose();
        }
           if(fuente==btnMenu){
           
         OrdenCajero orden = new OrdenCajero(nombreUsuario, claveUsuario);
        orden.setVisible(true);
        dispose();
        }     
          if(fuente==btnOrdenes){
           
         ManejaOrdenes ordenes = new ManejaOrdenes(nombreUsuario,claveUsuario);
        ordenes.setVisible(true);
        dispose();
        }   
         if(fuente==btnCorte){
           
         CorteDia corteDia = new CorteDia(nombreUsuario,claveUsuario,true);
        corteDia.setVisible(true);
        dispose();
        }   
    }   
    public void cambiarTitulo(){
        setTitle("Bienvenido :" + nombreUsuario +" " +claveUsuario);
    }
 
           public static void main(String[] args) {
       Admin texto = new Admin();
     texto.pack();
     texto.setVisible(true);
   }
}