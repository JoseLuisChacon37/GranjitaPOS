/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesAuxiliares;

import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Intech-Torreon
 */
    public class MyModelo extends DefaultTableModel {
 
       public boolean isCellEditable (int row, int column) {
       // Aquí devolvemos true o false según queramos que una celda
       // identificada por fila,columna (row,column), sea o no editable

       return false;
       }
    }
