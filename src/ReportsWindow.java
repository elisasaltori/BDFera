import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportsWindow {
	
	JFrame frame;
	
	JPanel reportsPanel;
	
	ButtonGroup radio;
	Button generateButton;
	
	Vector<ReportPanel> relatoriosPanels;	
	Vector<String> textosRelatorios;
	int index;
	
	public Connection Connect(){
		 Connection connection;
		 try{
		 Class.forName("oracle.jdbc.driver.OracleDriver");
         connection = DriverManager.getConnection(
                 "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl",
                 "8551100",
                 "grupofera");
         	return connection;
		 }catch(Exception e){
			 e.printStackTrace();
			 return null;
		 }
	}
	
	private ActionListener radioListener = new ActionListener (){
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton btn = (JRadioButton) e.getSource();
            System.out.println("Selected Button = " + btn.getText());
            
            for (int i=0; i<textosRelatorios.size(); i++){
            	if (textosRelatorios.elementAt(i) == btn.getText()){
            		relatoriosPanels.elementAt(i).setVisible(true);
            		index = i;
            	} else {            		
            		relatoriosPanels.elementAt(i).setVisible(false);
            	}
            }
        }
	};
	
	private void initializeReportsPanel(){
		reportsPanel = new JPanel();
		reportsPanel.setLayout(new FlowLayout());
		
		radio = new ButtonGroup();
		for (int i=0; i<textosRelatorios.size(); i++){
			JRadioButton radioAux = new JRadioButton(textosRelatorios.elementAt(i));
			radioAux.addActionListener(radioListener);
			if (i==0) radioAux.setSelected(true);
			radio.add(radioAux);
			reportsPanel.add(radioAux);
		}
		
		generateButton = new Button ("Gerar PDF");
		generateButton.addActionListener(new ActionListener(){
			@Override
			
			
			public void actionPerformed(ActionEvent e){
				
				ReportPanel thisReport = relatoriosPanels.elementAt(index);
				
				if (writePDF(thisReport.getTable(), thisReport.getTitle(), thisReport.getDescription())){
					JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso!", "Relatório", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Relatório não pôde ser criado!", "Relatório", JOptionPane.ERROR_MESSAGE);	
				}
			}
		});
		
		reportsPanel.add(generateButton);
	}
	
	public ReportsWindow(){
		frame = new JFrame("Relatórios");
		frame.setBounds(100,100,1000,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		JPanel superPanel = new JPanel();
		superPanel.setLayout(new BoxLayout(superPanel, BoxLayout.PAGE_AXIS));
		
		JLabel title = new JLabel("Gerar relatórios");
		title.setFont(new Font(null, Font.BOLD ,25));
		JLabel instruction = new JLabel("Selecione o tipo de relatório desejado:");
		instruction.setFont(new Font(null, 0 ,18));

		JPanel aux = new JPanel(); aux.add(title); superPanel.add(aux);
		
		aux = new JPanel(); aux.add(instruction); superPanel.add(aux);
		
		
		Connection connection = Connect();
		
		relatoriosPanels = new Vector<ReportPanel>();
		textosRelatorios = new Vector<String>();

		ReportPanel relatorio1 = new RelatorioAtletas(connection);
		textosRelatorios.add(relatorio1.getTitle());
		index = 0;
		relatoriosPanels.addElement(relatorio1);

		ReportPanel relatorio2 = new RelatorioMedicos(connection);
		textosRelatorios.add(relatorio2.getTitle());
		relatorio2.setVisible(false);
		relatoriosPanels.addElement(relatorio2);
		
		ReportPanel relatorio3 = new RelatorioTreinadores(connection);
		textosRelatorios.add(relatorio3.getTitle());
		relatorio3.setVisible(false);
		relatoriosPanels.addElement(relatorio3);

		initializeReportsPanel();
		superPanel.add(reportsPanel);
		superPanel.add(relatorio1);
		superPanel.add(relatorio2);
		superPanel.add(relatorio3);
		
		frame.add(superPanel);
		
	}

	public static boolean writePDF(JTable table, String titleString, String introString) {
	    Document document = new Document();
	    try {
	    	JFileChooser fileChooser = new JFileChooser();
	    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Portable Document Format", "pdf");
	    	fileChooser.setFileFilter(filter);
	    	
	    	if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
	    		PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile()+".pdf"));
	    	} else {
	    		return false;
	    	}
	    	
	    	document.open();
	      
	    	Paragraph title = new Paragraph(titleString, FontFactory.getFont(FontFactory.TIMES_ROMAN,18, Font.BOLD, BaseColor.BLACK));
	    	title.setAlignment(Paragraph.ALIGN_CENTER);
	    	document.add(title);
	    	document.add(Chunk.NEWLINE);
		  
	    	Paragraph intro = new Paragraph(introString);
	    	document.add(intro);
	    	document.add(Chunk.NEWLINE);
		  
	    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		  
	    	PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
	    	for (int i=0; i<table.getColumnCount(); i++){
	    		pdfTable.addCell(table.getColumnName(i));
	    	}
	    	for (int i=0; i<table.getRowCount()-1; i++){
	    		for (int j=0; j<table.getColumnCount(); j++){
	    			pdfTable.addCell(table.getValueAt(i, j).toString());
	    		}
	    	}
	    	document.add(pdfTable);
	    	document.add(new Paragraph(Chunk.NEWLINE));
	    	document.add(new Paragraph("Gerado em " + dateFormat.format(Calendar.getInstance().getTime()).toString()));		  
	    	document.close();	
	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	    	return false;
	    }
	    return true;
	}
	
	public static void main (String []args){
		ReportsWindow rw = new ReportsWindow();
		System.out.println("Start");
		rw.frame.setVisible(true);
	}
}
