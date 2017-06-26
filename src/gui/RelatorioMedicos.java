package gui;
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
		initializeTable(new String[] {"Código", "Nome", "Doc Identidade", "CRM", "N. Total de Atletas Atendidos"});
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
        	nacaoBox.addItem("Todos");
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
			
			String nacaoString;
			if (nacaoBox.getSelectedIndex()==0){
				nacaoString = "1=1";
			} else {
				nacaoString = "a.nacaoorigem= '" + nacaoBox.getSelectedItem() + "' ";
			}

			rs = stmt.executeQuery("select m.codigo, m.nome, m.docidentidade, m.crm, count(distinct a2.passaporte) as Nro_Atletas "+
			    "from medico m "+
			        "left join consulta c on c.CODIGOMEDICO = m.codigo "+
			        "left join atendimentolesao al on al.CODIGOMEDICO=m.codigo "+
			        "left join examedoping ed on ed.CODIGOMEDICO=m.codigo "+
			        "left join atleta a2 on (al.passaporteatleta=a2.passaporte or ed.passaporteatleta=a2.passaporte or c.passaporteatleta=a2.passaporte)"+
			        "left join atleta a on " + nacaoString + " and "+ 
			            "(al.passaporteatleta=a.passaporte or ed.passaporteatleta=a.passaporte or c.passaporteatleta=a.passaporte) "+
			    "group by m.codigo, m.nome, m.docidentidade, m.crm having count(distinct a.passaporte)>=" + numMinAtendimentos.getValue()
			    +"order by Nro_Atletas desc");


			
			
			model.setRowCount(0);
			while (rs.next()) {
				model.addRow(new Object[]{rs.getString("CODIGO"), rs.getString("NOME"), rs.getString("DOCIDENTIDADE"), rs.getString("CRM"), rs.getString("NRO_ATLETAS")});
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}