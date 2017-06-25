package gui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 * Classe auxiliar para a criacao de uma coluna da tabela. Estabelece um editor para umaa celula de TextArea com scrollbar.
  * Codigo original por: Joris Van den Bogaert
 *  Modificado por Elisa Saltori Trujillo
 */
public class TextAreaEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	protected JScrollPane scrollpane;
	   protected JTextArea textarea;
  
	   public TextAreaEditor() {
	      super(new JCheckBox());
	      scrollpane = new JScrollPane();
	      textarea = new JTextArea(); 
	      textarea.setLineWrap(true);
	      textarea.setWrapStyleWord(true);
	      scrollpane.getViewport().add(textarea);
	  
	   }
	   
	   public void setEditable(boolean op){
		   textarea.setEditable(op);
	   }
	  
	   public Component getTableCellEditorComponent(JTable table, Object value,
	                                   boolean isSelected, int row, int column) {
	      textarea.setText((String) value);
	   
	      return scrollpane;
	   }
	  
	   public Object getCellEditorValue() {
	      return textarea.getText();
	   }
	}