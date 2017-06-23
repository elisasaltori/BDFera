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
        	rs = stmt.executeQuery("SELECT NOME FROM MODALIDADEESPORTIVA");
        	
        	while (rs.next())
        		dropDownMenus.elementAt(0).addItem((String)rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
        try {
        	stmt = connection.createStatement();
        	rs = stmt.executeQuery("SELECT NOME FROM MEDICO");
        	
        	while (rs.next())
        		dropDownMenus.elementAt(1).addItem(rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
        try {
        	stmt = connection.createStatement();
        	rs = stmt.executeQuery("SELECT NOME FROM PREPARADOR");
        	
        	while (rs.next())
        		dropDownMenus.elementAt(2).addItem(rs.getString("NOME"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void fillTable(){
		Statement stmt;
        ResultSet rs;
		
        try{
			stmt = connection.createStatement();

			String modalidadeString;
			if (dropDownMenus.elementAt(0).getSelectedIndex()==0){
				modalidadeString = "LIKE '%'";
			} else {
				modalidadeString = "= '" + dropDownMenus.elementAt(0).getSelectedItem()+"'";
			}
			String medicoString;
			if (dropDownMenus.elementAt(1).getSelectedIndex()==0){
				medicoString = "LIKE '%'";
			} else {
				medicoString = "= '" + dropDownMenus.elementAt(1).getSelectedItem()+"'";
			}
			String treinadorString;
			if (dropDownMenus.elementAt(2).getSelectedIndex()==0){
				treinadorString = "LIKE '%'";
			} else {
				treinadorString = "= '" + dropDownMenus.elementAt(2).getSelectedItem() + "'";
			}

			rs = stmt.executeQuery(
			"SELECT NOME, PASSAPORTE, NACAOORIGEM, DATANASCIMENTO FROM ATLETA A "+
			    "JOIN( "+
			        "(SELECT PASSAPORTEATLETA FROM ATLETAPARTICIPANTE AP "+
			            "JOIN (SELECT NACAO, MODALIDADE FROM EQUIPEOLIMPICA EO "+
			                "JOIN (SELECT CODIGO, NOME FROM MODALIDADEESPORTIVA M WHERE M.NOME " + modalidadeString +") M "+
			                "ON EO.MODALIDADE = M.CODIGO "+
			                "JOIN (SELECT CODIGO, NOME FROM PREPARADOR P WHERE P.NOME "+ treinadorString +") P "+
			                "ON P.CODIGO = EO.PREPARADOR "+
			            ") EO ON EO.NACAO = AP.NOMENACAO AND EO.MODALIDADE = AP.CODIGOMODALIDADE "+
			        ") AP "+
			        "JOIN(SELECT CODIGO, PASSAPORTEATLETA, CODIGOMEDICO FROM ATENDIMENTOLESAO AL "+
			            "JOIN (SELECT MED.CODIGO AS MED_COD, MED.NOME FROM MEDICO MED WHERE MED.NOME " + medicoString +") MED "+
			            "ON AL.CODIGOMEDICO = MED_COD "+
			        ") AL "+
			        "ON AL.PASSAPORTEATLETA = AP.PASSAPORTEATLETA "+
			    ") "+
			    "ON AL.PASSAPORTEATLETA = A.PASSAPORTE ");
			model.setRowCount(0);
			while (rs.next()) {
				for (int i=0; i<10; i++)
				model.addRow(new Object[]{rs.getString("NOME"), rs.getString("PASSAPORTE"), rs.getString("NACAOORIGEM"), rs.getString("DATANASCIMENTO").substring(0,11)});
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}
