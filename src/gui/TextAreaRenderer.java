package gui;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * Classe auxiliar para a criacao de uma coluna da tabela. Estabelece uma celula de TextArea com scrollbar.
 * 
 *
 */
public class TextAreaRenderer extends JScrollPane implements TableCellRenderer
{
	   JTextArea textarea;
	  
	   public TextAreaRenderer() {
	      textarea = new JTextArea();
	      textarea.setLineWrap(true);
	      textarea.setWrapStyleWord(true);
	      textarea.setEditable(false);
	      getViewport().add(textarea);
	   }
	  
	   public Component getTableCellRendererComponent(JTable table, Object value,
	                                  boolean isSelected, boolean hasFocus,
	                                  int row, int column)
	   {
	      if (isSelected) {
	         setForeground(table.getSelectionForeground());
	         setBackground(table.getSelectionBackground());
	         textarea.setForeground(table.getSelectionForeground());
	         textarea.setBackground(table.getSelectionBackground());
	      } else {
	         setForeground(table.getForeground());
	         setBackground(table.getBackground());
	         textarea.setForeground(table.getForeground());
	         textarea.setBackground(table.getBackground());
	      }
	  
	      textarea.setText((String) value);
	      textarea.setCaretPosition(0);
	      return this;
	   }
	}
