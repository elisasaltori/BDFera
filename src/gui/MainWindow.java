package gui;

/**
 * @author Elisa Saltori Trujillo - 8551100, Cristiano di Maio Chiaramelli - 9293053, Chan Ken Chen - 9436170
 * 
 */
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * Janela principal para um programa simples de acesso a um banco de dados. 
 * Permite insercao, alteração, remoção e visualização de entidades do tipo ExameDoping.
 * @author Elisa Saltori Trujillo - 8551100, Cristiano di Maio Chiaramelli - 9293053, Chan Ken Chen - 9436170
 *
 */
public class MainWindow {

	private JFrame frame;
	private Connection connection; 
	private JTable tabelaExames;
	
	/**
	 * Ativa a aplicacao
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
	 * Cria a aplicacao
	 */
	public MainWindow() {
		initialize();
			
	}
	/**
	 * Funcao que estabelece uma conexao com o banco de dados.
	 * @return Objeto Connection se a conexao foi efetuada com sucesso; null se houve erro
	 */
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
	
	/**
	 * Apaga do banco de dados um registro ExameDoping de codigo id.
	 * @param id codigo (unico) de identificacao do registro a ser apagado
	 * @return true se o registro for apagado com sucesso; false, caso contrario
	 */
	public boolean deleteExam(int id){
		Statement stmt;
		try{
			  stmt = this.connection.createStatement();
	          stmt.executeQuery("DELETE FROM EXAMEDOPING WHERE CODIGO="+id);
	          stmt.close();
	     }catch(Exception e){
	     	e.printStackTrace();
	     }
		return true;
	}

	/**
	 * Preenche a tabela da janela com todos os registros ExameDoping presentes no banco de dados
	 */
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
			 stmt.close();
	     }catch(Exception e){
	     	e.printStackTrace();
	     }
	}
	
	/**
	 * Esvazia totalmente a tabela
	 */
	private void emptyTable(){
		 DefaultTableModel model = (DefaultTableModel)tabelaExames.getModel();
		 model.setRowCount(0);
	}
	
	/**
	 * Inicializa o conteudo da janela
	 */
	private void initialize() {
		frame = new JFrame();
		//Ao fechar a janela, fecha conexao com o banco de dados antes de sair do programa
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try{
					connection.close();
					System.exit(0);
				}catch(Exception e){
					e.printStackTrace();
					System.exit(1);
				}
				
			}
		});
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		
		//Cria tabela de registros ExameDoping
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
		
		//Renderer e editor customizados para campo Resultado (area de texto com scrollbar)
		tabelaExames.getColumn("Resultado").setCellRenderer(new TextAreaRenderer());
		TextAreaEditor ed = new TextAreaEditor();
		tabelaExames.getColumn("Resultado").setCellEditor(ed);
		ed.setEditable(false);
		tabelaExames.setDefaultEditor(Object.class, null); //Desativa edicao
		
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JScrollPane examesScroll = new JScrollPane(tabelaExames); //Painel do tipo scroll em que esta contida a tabela
		springLayout.putConstraint(SpringLayout.WEST, examesScroll, 21, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(examesScroll);
		
		//Titulo da tabela
		JLabel title = new JLabel("Exames de Doping"); 
		springLayout.putConstraint(SpringLayout.NORTH, title, 20, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, title, 36, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, title, -12, SpringLayout.NORTH, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, title, 300, SpringLayout.WEST, frame.getContentPane());
		title.setFont(new Font("Tahoma", Font.PLAIN, 32));
		frame.getContentPane().add(title);
		
		//Titulo das opcoes de manipulacao dos exames
		JLabel tituloOpcoes = new JLabel("Exames de doping:");
		springLayout.putConstraint(SpringLayout.NORTH, examesScroll, 0, SpringLayout.NORTH, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.SOUTH, examesScroll, 347, SpringLayout.NORTH, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.EAST, examesScroll, -15, SpringLayout.WEST, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.WEST, tituloOpcoes, 797, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tituloOpcoes, -22, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, tituloOpcoes, 71, SpringLayout.NORTH, frame.getContentPane());
		tituloOpcoes.setFont(new Font("Arial", Font.BOLD, 13));
		tituloOpcoes.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(tituloOpcoes);
		
		//Botoes para manipulacao dos exames
		
		JButton botaoInserir = new JButton("Inserir novo exame");
		springLayout.putConstraint(SpringLayout.NORTH, botaoInserir, 100, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, botaoInserir, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoInserir, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tituloOpcoes, -6, SpringLayout.NORTH, botaoInserir);
		botaoInserir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		botaoInserir.setFont(new Font("Arial", Font.BOLD, 12));
		frame.getContentPane().add(botaoInserir);
		
		JButton botaoAlterar = new JButton("Alterar exame selecionado");
		springLayout.putConstraint(SpringLayout.WEST, botaoAlterar, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoAlterar, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoInserir, -6, SpringLayout.NORTH, botaoAlterar);
		botaoAlterar.setFont(new Font("Arial", Font.BOLD, 12));
		springLayout.putConstraint(SpringLayout.NORTH, botaoAlterar, 129, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoAlterar, 152, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(botaoAlterar);
		
		JButton botaoExcluir = new JButton("Excluir exame selecionado");
		springLayout.putConstraint(SpringLayout.NORTH, botaoExcluir, 6, SpringLayout.SOUTH, botaoAlterar);
		springLayout.putConstraint(SpringLayout.WEST, botaoExcluir, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoExcluir, -10, SpringLayout.EAST, frame.getContentPane());
		botaoExcluir.setFont(new Font("Arial", Font.BOLD, 12));
		botaoExcluir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row=tabelaExames.getSelectedRow();
				if(row!=-1){
					String str=(String)tabelaExames.getModel().getValueAt(row, 0); //Pega id do registro
					int id=Integer.parseInt(str);
					deleteExam(id); //Remove registo do BD
					emptyTable(); //Esvazia e preenche novamente tabela para atualizar valores
					fillTable();
				}
			}
		});
		botaoExcluir.setFont(new Font("Arial", Font.BOLD, 12));
		frame.getContentPane().add(botaoExcluir);
		
		//Botao para atualizacao da tabela
		JButton botaoAtualizar = new JButton("Recarregar tabela");
		springLayout.putConstraint(SpringLayout.NORTH, botaoAtualizar, 6, SpringLayout.SOUTH, examesScroll);
		springLayout.putConstraint(SpringLayout.WEST, botaoAtualizar, 617, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoAtualizar, 0, SpringLayout.EAST, examesScroll);
		botaoAtualizar.setFont(new Font("Arial", Font.BOLD, 12));
		botaoAtualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				emptyTable();
				fillTable();
			}
		});
		frame.getContentPane().add(botaoAtualizar);
		
		//Titulo e botoes referentes as opcoes de relatorios
		JLabel tituloRelatorio = new JLabel("Relat\u00F3rios:");
		springLayout.putConstraint(SpringLayout.WEST, tituloRelatorio, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, tituloRelatorio, -22, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoExcluir, -50, SpringLayout.NORTH, tituloRelatorio);
		springLayout.putConstraint(SpringLayout.NORTH, tituloRelatorio, 231, SpringLayout.NORTH, frame.getContentPane());
		tituloRelatorio.setFont(new Font("Arial", Font.BOLD, 13));
		tituloRelatorio.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(tituloRelatorio);
		
		JButton botaoRelAtletas = new JButton("Atletas");
		springLayout.putConstraint(SpringLayout.WEST, botaoRelAtletas, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoRelAtletas, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tituloRelatorio, -6, SpringLayout.NORTH, botaoRelAtletas);
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelAtletas, 260, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelAtletas, 283, SpringLayout.NORTH, frame.getContentPane());
		botaoRelAtletas.setFont(new Font("Arial", Font.BOLD, 12));
		botaoRelAtletas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		frame.getContentPane().add(botaoRelAtletas);
		
		JButton botaoRelMedicos = new JButton("M\u00E9dicos");
		springLayout.putConstraint(SpringLayout.WEST, botaoRelMedicos, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoRelMedicos, -10, SpringLayout.EAST, frame.getContentPane());
		botaoRelMedicos.setFont(new Font("Arial", Font.BOLD, 12));
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelMedicos, 289, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelMedicos, 312, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(botaoRelMedicos);
		
		JButton botaoRelTreinadores = new JButton("Preparadores");
		springLayout.putConstraint(SpringLayout.WEST, botaoRelTreinadores, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoRelTreinadores, -10, SpringLayout.EAST, frame.getContentPane());
		botaoRelTreinadores.setFont(new Font("Arial", Font.BOLD, 12));
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelTreinadores, 318, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelTreinadores, 341, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(botaoRelTreinadores);
		
		//Seta tamanho minimo para colunas da tabela
		tabelaExames.getColumnModel().getColumn(0).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(1).setMinWidth(50);
		tabelaExames.getColumnModel().getColumn(2).setMinWidth(400);
		tabelaExames.getColumnModel().getColumn(3).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(4).setMinWidth(45);
		tabelaExames.getColumnModel().getColumn(5).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(6).setMinWidth(5);
		
		
		this.connection=this.Connect();
		if(this.connection==null){
			System.out.println("Conexao nao estabelecida!");
			System.exit(2);
		}
		this.fillTable(); //Inicializa tabela com valores de ExameDoping

	
	}
}
