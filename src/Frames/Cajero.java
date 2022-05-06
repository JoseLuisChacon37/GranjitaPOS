package Frames;
import Frames.Acceso;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class Cajero extends javax.swing.JFrame implements ActionListener {
   public  String nombreCajero;
   public   String claveCajero;
       JButton btnMesas = new JButton();
       JButton btnRecibos = new JButton();
       JButton btnSalir = new JButton();  
         JButton btnMenu = new JButton();       
       
    public Cajero (){
        super();
        this.setUndecorated(true);
     
       // setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
       //botones

       //btnMesas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/mesas.png")));
       btnRecibos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/recibos.png")));
       btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/puerta.jpg")));
       btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ordenes.png")));
         btnMenu.addActionListener(this);
       btnMesas.addActionListener(this);
       btnSalir.addActionListener(this);
       btnRecibos.addActionListener(this);
       // crear barra
       JToolBar barra = new JToolBar();
       barra.add(btnMenu);
       
     //  barra.add(btnMesas);
       barra.add(btnRecibos);
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
        if(fuente==btnMesas){
            dispose();
        } 
        if(fuente==btnSalir){
           
          Acceso login = new Acceso();
        login.setVisible(true);
        dispose();
        }
           if(fuente==btnMenu){
           
         OrdenCajero orden = new OrdenCajero(nombreCajero, claveCajero);
        orden.setVisible(true);
        dispose();
        }     
                 if(fuente==btnRecibos){
           
         ManejaOrdenes ordenes = new ManejaOrdenes(nombreCajero,claveCajero);
        ordenes.setVisible(true);
        dispose();
        }      
    }   
    public void cambiarTitulo(){
        setTitle("Bienvenido :" + claveCajero +" " +nombreCajero);
    }
 
           public static void main(String[] args) {
       Cajero texto = new Cajero();
     texto.pack();
     texto.setVisible(true);
   }
}