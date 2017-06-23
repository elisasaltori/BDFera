import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RelatorioMedicos extends ReportPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JFormattedTextField numMinAtendimentos;
	JComboBox<String> nacaoBox;
	
	public RelatorioMedicos(Connection connection) {
		super(connection);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		fillHeader();
		initializeTable(new String[] {"Nome", "Doc Identidade", "CRM", "Endereço"});
		fillTable();
		
        numMinAtendimentos.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        fillTable();
		    }
		});        
        
        nacaoBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        fillTable();
		    }
		});
	}

	@Override
	public String getTitle(){
		return "Relatório de atendimentos médicos";
	}
	@Override
	public String getDescription(){
		return "Lista dos médicos que atenderam pelo menos " + numMinAtendimentos.getValue() + " atletas da nação " + nacaoBox.getSelectedItem(); 
	}

	public void fillHeader(){
		Statement stmt;
        ResultSet rs;
        
        nacaoBox = new JComboBox<String>();

        numMinAtendimentos = new JFormattedTextField(NumberFormat.getInstance());
        numMinAtendimentos.setValue(0);
        numMinAtendimentos.setPreferredSize(new Dimension (50,25));
        
        try {
        	stmt = connection.createStatement();
        	rs = stmt.executeQuery("SELECT NOME FROM NACAO");
        	
        	while (rs.next())
        		nacaoBox.addItem((String)rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
        
        JPanel headerPanel = new JPanel (new FlowLayout());
        headerPanel.add(new JLabel("Número mínimo de atletas atendidos "));
        headerPanel.add(numMinAtendimentos);
        headerPanel.add(new JLabel("Nação "));
        headerPanel.add(nacaoBox);
        add(headerPanel);
	}

	@Override
	public void fillTable(){
		Statement stmt;
        ResultSet rs;
		
        try{
			stmt = connection.createStatement();

			rs = stmt.executeQuery("SELECT NOME, DOCIDENTIDADE, CRM, ENDERECO, COUNT(*) AS NUMATENDIMENTOS FROM MEDICO M "+
			    "JOIN (SELECT DISTINCT PASSAPORTEATLETA, CODIGOMEDICO FROM ATENDIMENTOLESAO AL "+
			        "JOIN (SELECT PASSAPORTEATLETA AS PASSAPORTE FROM ATLETAPARTICIPANTE AP "+
			            "JOIN (SELECT NOME AS NOME_NACAO FROM NACAO N WHERE N.NOME = '" + nacaoBox.getSelectedItem() + "') "+
			            "ON AP.NOMENACAO = NOME_NACAO "+
			        ")"+
			        "ON PASSAPORTEATLETA = PASSAPORTE "+
			        "GROUP BY PASSAPORTEATLETA, CODIGOMEDICO "+
			        "HAVING COUNT(*) > "+ numMinAtendimentos.getValue().toString() +") AL "+
			    "ON M.CODIGO = AL.CODIGOMEDICO "+
			    "GROUP BY NOME, DOCIDENTIDADE, CRM, ENDERECO");
			
			model.setRowCount(0);
			while (rs.next()) {
				for (int i=0; i<10; i++)
				model.addRow(new Object[]{rs.getString("NOME"), rs.getString("DOCIDENTIDADE"), rs.getString("CRM"), rs.getString("ENDERECO"), rs.getString("NUMATENDIMENTOS")});
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}