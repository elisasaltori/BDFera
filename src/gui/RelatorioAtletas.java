package gui;

import java.sql.Statement;
import java.util.Vector;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RelatorioAtletas extends ReportPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<JComboBox<String>> dropDownMenus;

	public RelatorioAtletas(Connection connection){
		super(connection);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		initializeDropDown(new String[] {"Modalidade", "Médico", "Preparador"});
		initializeTable(new String[] {"Nome", "Passaporte", "Nação", "Data de nascimento"});
		fillHeader();
		fillTable();
	}

	private void initializeDropDown(String dropMenus[]){
		dropDownMenus = new Vector<JComboBox<String>>();
		
		JPanel dropDownPanel = new JPanel(new FlowLayout());
		//Configura os valores
		for (int i=0; i<dropMenus.length; i++){
			JComboBox<String> dropDown = new JComboBox<String>(new String[] {"Todos"});
			dropDown.setVisible(true);
			dropDown.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        fillTable();
			    }
			});
			dropDownPanel.add(new JLabel(dropMenus[i]));
			dropDownPanel.add(dropDown);
			
			dropDownMenus.add(dropDown);
		}
		
		add(dropDownPanel);
	}
	
	@Override
	public String getTitle(){
		return "Relatório atletas";
	}
	@Override
	public String getDescription(){
		String stringModalidade, stringMedico, stringTreinador;
		if (dropDownMenus.elementAt(0).getSelectedIndex()==0){
			stringModalidade = "de qualquer modalidade";
		} else {
			stringModalidade = "da modalidade " + dropDownMenus.elementAt(0).getSelectedItem();
		}
		if (dropDownMenus.elementAt(1).getSelectedIndex()==0){
			stringMedico = "por qualquer médico";
		} else {
			stringMedico = "pelo médico " + dropDownMenus.elementAt(1).getSelectedItem();
		}
		if (dropDownMenus.elementAt(2).getSelectedIndex()==0){
			stringTreinador = "por qualquer treinador";
		} else {
			stringTreinador = "pelo treinador "+ dropDownMenus.elementAt(2).getSelectedItem();
		}
		
		return "Lista dos atletas " + stringModalidade + ", que já foram atendidos " + stringMedico + ", e treinados " + stringTreinador + "."; 
	}
	
	public void fillHeader(){
		Statement stmt;
        ResultSet rs;
        
        try {
        	stmt = connection.createStatement();
        	rs = stmt.executeQuery("SELECT CODIGO, NOME FROM MODALIDADEESPORTIVA ORDER BY CODIGO, NOME");
        	
        	while (rs.next())
        		dropDownMenus.elementAt(0).addItem(rs.getString("CODIGO") + " - " + rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
        try {
        	stmt = connection.createStatement();
        	rs = stmt.executeQuery("SELECT CODIGO, NOME FROM MEDICO ORDER BY CODIGO, NOME");
        	
        	while (rs.next())
        		dropDownMenus.elementAt(1).addItem(rs.getString("CODIGO") + " - " + rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
        try {
        	stmt = connection.createStatement();
        	rs = stmt.executeQuery("SELECT CODIGO, NOME FROM PREPARADOR ORDER BY CODIGO, NOME");
        	
        	while (rs.next())
        		dropDownMenus.elementAt(2).addItem(rs.getString("CODIGO") + " - " + rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private int getIndexValue(String str){
		return Integer.parseInt(str.substring(0, str.indexOf('-')-1));
	}
	
	@Override
	public void fillTable(){
		Statement stmt;
        ResultSet rs;
		
        try{
			stmt = connection.createStatement();

			String modalidadeString;
			if (dropDownMenus.elementAt(0).getSelectedIndex()==0){
				modalidadeString = "1=1";
			} else {
				modalidadeString = "ap.codigomodalidade="+getIndexValue((String) dropDownMenus.elementAt(0).getSelectedItem());
			}
			String medicoString;
			if (dropDownMenus.elementAt(1).getSelectedIndex()==0){
				medicoString = "1=1";
			} else {
				medicoString = "al.codigomedico="+ getIndexValue((String) dropDownMenus.elementAt(1).getSelectedItem()) + " or c.codigomedico="+getIndexValue((String) dropDownMenus.elementAt(1).getSelectedItem())+" or ed.codigomedico=" + getIndexValue((String) dropDownMenus.elementAt(1).getSelectedItem()+" ");
			}
			String treinadorString;
			if (dropDownMenus.elementAt(2).getSelectedIndex()==0){
				treinadorString = "1=1";
			} else {
				treinadorString = "eo.preparador=" + getIndexValue((String) dropDownMenus.elementAt(2).getSelectedItem());
			}

			rs = stmt.executeQuery("select a.nome, a.passaporte, a.nacaoorigem, a.datanascimento "+
		    "from atleta a "+
		        "left join consulta c on c.passaporteatleta=a.passaporte "+
		        "left join atendimentolesao al on al.passaporteatleta=a.passaporte "+
		        "left join examedoping ed on ed.passaporteatleta=a.passaporte "+
		        "join atletaparticipante ap on ap.passaporteatleta=a.passaporte "+
		        "join equipeolimpica eo on eo.nacao=a.nacaoorigem and eo.modalidade = ap.codigomodalidade "+
		    "where (" + medicoString + ") and " + treinadorString + " and " + modalidadeString + " " +
			"group by a.passaporte, a.nome, a.nacaoorigem, a.datanascimento ");
			
			
			
			model.setRowCount(0);
			while (rs.next()) {
				model.addRow(new Object[]{rs.getString("NOME"), rs.getString("PASSAPORTE"), rs.getString("NACAOORIGEM"), rs.getString("DATANASCIMENTO").substring(0,11)});
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}
