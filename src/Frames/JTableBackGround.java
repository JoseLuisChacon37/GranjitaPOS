import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Random;
 
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
/**
 * 
 * @author Leyer
 *
 */
public class JTableBackGround extends JFrame{
  //  private static final long serialVersionUID = -6650115843758904110L;
     
    private static final String pathImage="foto.jpg";
     
    public static final short WINDOW_WIDTH = 930;
    public static final short WINDOW_HEIGTH = 420;
 
    protected JTable     mainTable  =null;
    protected TableModel tableModel =null;
     
    public JTableBackGround(){
        super("Imagen de Fondo JTable");
        initComponents();
        this.setSize(WINDOW_WIDTH,WINDOW_HEIGTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private class TableModel extends DefaultTableModel{
         
        //private static final long serialVersionUID = 1L;
         
        public TableModel(){
            addColumn("1");addColumn("2");
            addColumn("3");addColumn("4");
            for(int index=0;index<20;index++){
                Object row[]={
                        new Random().nextInt(100001),
                        new Random().nextInt(100001),
                        new Random().nextInt(100001),
                        new Random().nextInt(100001)};
                addRow(row);
            }
        }   
    }
    private void initComponents(){
        tableModel=new TableModel();
        mainTable=new JTable(tableModel){
            private static final long serialVersionUID = 1L;
             
             ImageIcon imageBackground = new ImageIcon(pathImage);
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    final Component c = super.prepareRenderer(renderer, row, column);
                    if (c instanceof JComponent){
                        ((JComponent) c).setOpaque(false);                    
                    }
                    return c;
                }
                @Override
                public void paint(Graphics graphics) {
                    graphics.drawImage(imageBackground.getImage(), 0, 0,getWidth(),getHeight(),null);
                    super.paint(graphics);
                }
        };
        mainTable.setFillsViewportHeight(true);
        mainTable.setOpaque(false);
        mainTable.setForeground(Color.white);
        JScrollPane scrollPane= new JScrollPane(mainTable);
        getContentPane().add(scrollPane);
         
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JTableBackGround().setVisible(true);
            }
        });
    }
}