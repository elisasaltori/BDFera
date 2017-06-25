import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RelatorioTreinadores extends ReportPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RelatorioTreinadores(Connection connection) {
		super(connection);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel jp = new JPanel();
		jp.add(new JLabel(getDescription()));
		add(jp);
		initializeTable(new String[] {"Nome", "Atletas irregulares", "Resultado"});
		fillTable();
		
	}

	@Override
	public String getTitle() {
		return "Relatório de treinadores e doping";
	}

	@Override
	public String getDescription() {
		
		return "Lista dos treinadores que já treinaram mais atletas que estão em situação irregular devido ao doping";
	}

	@Override
	public void fillTable(){
		Statement stmt;
        ResultSet rs;
		
        try{
			stmt = connection.createStatement();

			rs = stmt.executeQuery("select p.codigo, p.nome, count(distinct ad.passaporte) as Nro_Atletas_Irreg,count(distinct ad.passaporte)/count(distinct ap2.passaporteatleta) as PCT_IRREGULAR "+ 
		    "from preparador p "+
		    "join equipeolimpica ep on ep.preparador=p.codigo "+
		    "join atletaparticipante ap on ap.codigomodalidade=ep.modalidade "+
		    "join atleta a on a.nacaoorigem=ep.nacao and a.passaporte=ap.passaporteatleta "+
		    "left join (select a.passaporte, a.nome, count(*), max(ed.dataexame) "+
		    "from atleta a "+
		    "join examedoping ed on ed.passaporteatleta=a.passaporte and ed.reprovado=1 "+
		    "group by a.passaporte, a.nome having (SYSDATE-max(ed.dataexame))<90 or count(*)>2 "+
		    ") ad on ad.passaporte=ap.passaporteatleta "+
		    "join atletaparticipante ap2 on ap2.codigomodalidade=ep.modalidade "+
		    "join atleta a2 on a2.nacaoorigem=ep.nacao and a2.passaporte=ap2.passaporteatleta "+
		    "group by p.codigo, p.nome "+
		    "order by PCT_IRREGULAR desc ");
			
			model.setRowCount(0);
			while (rs.next()) {
				DecimalFormat df = new DecimalFormat("#.##");
				model.addRow(new Object[]{rs.getString("NOME"), rs.getString("Nro_Atletas_Irreg"), df.format(100*Float.parseFloat(rs.getString("PCT_IRREGULAR")))+" %"});
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
