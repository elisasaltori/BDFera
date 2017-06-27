package gui;
import java.sql.Connection;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class ReportPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Connection connection;
	protected JTable table;
	protected DefaultTableModel model;
	protected JScrollPane scrollTable;
	
	public ReportPanel(Connection connection){
		super();
		this.connection = connection;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public abstract String getTitle();
	public abstract String getDescription();
	protected abstract void fillTable();

	protected JTable getTable(){return table;}
	
	protected void initializeTable(String Cols[]){
		model = new DefaultTableModel();
		table = new JTable(model);
		
		table.setDefaultEditor(Object.class, null);
		for (int i=0; i<Cols.length; i++)
			model.addColumn(Cols[i]);
		
		scrollTable = new JScrollPane(table);
		scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollTable);
	}
}
