package gui;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class MainWindow {

	private JFrame frame;
	private Connection connection;
	private JTable tabelaExames;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		
		
	}
	
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

	
	private void fillTable(){
		Statement stmt;
        ResultSet rs;
        DefaultTableModel model = (DefaultTableModel)tabelaExames.getModel();
		try{
			  stmt = this.connection.createStatement();
	          rs = stmt.executeQuery("SELECT * FROM EXAMEDOPING");
			  while (rs.next()) {       
	            model.addRow(new Object[]{rs.getString("CODIGO"), rs.getString("DATAEXAME").substring(0, 10), rs.getString("RESULTADO"), rs.getString("REPROVADO"),
	            rs.getString("PASSAPORTEATLETA"), rs.getString("CODIGOMEDICO"),rs.getString("CODIGOMODALIDADE")});
	         	
	         }
	     }catch(Exception e){
	     	e.printStackTrace();
	     }
	}
	
	private void emptyTable(){
		 DefaultTableModel model = (DefaultTableModel)tabelaExames.getModel();
		 model.setRowCount(0);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 479);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		DefaultTableModel model = new DefaultTableModel();
		this.tabelaExames = new JTable(model);
		tabelaExames.setRowHeight(40);
		tabelaExames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model.addColumn("Código"); 
		model.addColumn("Data"); 
		model.addColumn("Resultado"); 
		model.addColumn("Rep."); 
		model.addColumn("Atleta"); 
		model.addColumn("C. Médico"); 
		model.addColumn("C. Modalidade"); 
		tabelaExames.getColumn("Resultado").setCellRenderer(new TextAreaRenderer());
		TextAreaEditor ed = new TextAreaEditor();
		tabelaExames.getColumn("Resultado").setCellEditor(ed);
		ed.setEditable(false);
		tabelaExames.setDefaultEditor(Object.class, null);
		
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JScrollPane examesScroll = new JScrollPane(tabelaExames);
		springLayout.putConstraint(SpringLayout.NORTH, examesScroll, 56, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, examesScroll, 32, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, examesScroll, 402, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, examesScroll, 813, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(examesScroll);
		
		JLabel title = new JLabel("Exames de Doping");
		springLayout.putConstraint(SpringLayout.NORTH, title, 11, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, title, 72, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, title, 50, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, title, 336, SpringLayout.WEST, frame.getContentPane());
		title.setFont(new Font("Tahoma", Font.PLAIN, 32));
		frame.getContentPane().add(title);
		
		JLabel tituloOpcoes = new JLabel("Exames de doping:");
		springLayout.putConstraint(SpringLayout.WEST, tituloOpcoes, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, tituloOpcoes, 94, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tituloOpcoes, -10, SpringLayout.EAST, frame.getContentPane());
		tituloOpcoes.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, tituloOpcoes, 80, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(tituloOpcoes);
		
		JButton botaoInserir = new JButton("Inserir novo exame");
		springLayout.putConstraint(SpringLayout.NORTH, botaoInserir, 6, SpringLayout.SOUTH, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.WEST, botaoInserir, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoInserir, 29, SpringLayout.SOUTH, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.EAST, botaoInserir, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(botaoInserir);
		
		JButton botaoAlterar = new JButton("Alterar exame selecionado");
		springLayout.putConstraint(SpringLayout.NORTH, botaoAlterar, 129, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, botaoAlterar, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoAlterar, 152, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoAlterar, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(botaoAlterar);
		
		JButton botaoExcluir = new JButton("Excluir exame selecionado");
		springLayout.putConstraint(SpringLayout.NORTH, botaoExcluir, 158, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, botaoExcluir, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoExcluir, 181, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoExcluir, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(botaoExcluir);
		
		JButton botaoAtualizar = new JButton("Recarregar tabela");
		springLayout.putConstraint(SpringLayout.NORTH, botaoAtualizar, 6, SpringLayout.SOUTH, examesScroll);
		springLayout.putConstraint(SpringLayout.WEST, botaoAtualizar, 644, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoAtualizar, 809, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(botaoAtualizar);
		
		JLabel tituloRelatorio = new JLabel("Relat\u00F3rios:");
		springLayout.putConstraint(SpringLayout.NORTH, tituloRelatorio, 59, SpringLayout.SOUTH, botaoExcluir);
		springLayout.putConstraint(SpringLayout.WEST, tituloRelatorio, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, tituloRelatorio, -196, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tituloRelatorio, -10, SpringLayout.EAST, frame.getContentPane());
		tituloRelatorio.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(tituloRelatorio);
		
		JButton botaoRelAtletas = new JButton("Atletas");
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelAtletas, 260, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, botaoRelAtletas, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelAtletas, 29, SpringLayout.SOUTH, tituloRelatorio);
		springLayout.putConstraint(SpringLayout.EAST, botaoRelAtletas, -10, SpringLayout.EAST, frame.getContentPane());
		botaoRelAtletas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		frame.getContentPane().add(botaoRelAtletas);
		
		JButton botaoRelMedicos = new JButton("M\u00E9dicos");
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelMedicos, 289, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, botaoRelMedicos, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelMedicos, 312, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoRelMedicos, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(botaoRelMedicos);
		
		JButton botaoRelTreinadores = new JButton("Preparadores");
		springLayout.putConstraint(SpringLayout.WEST, botaoRelTreinadores, 6, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelTreinadores, 318, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelTreinadores, 341, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoRelTreinadores, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(botaoRelTreinadores);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	
		tabelaExames.getColumnModel().getColumn(0).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(1).setMinWidth(50);
		tabelaExames.getColumnModel().getColumn(2).setMinWidth(400);
		tabelaExames.getColumnModel().getColumn(3).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(4).setMinWidth(45);
		tabelaExames.getColumnModel().getColumn(5).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(6).setMinWidth(5);
		
		this.connection=this.Connect();
		this.fillTable();
		this.fillTable();
	
	}
}
