package gui;

/**
 * @author Elisa Saltori Trujillo - 8551100, Cristiano di Maio Chiaramelli - 9293053, Chan Ken Chen - 9436170
 * 
 */
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
	public static void runWindow(Connection connection) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow(connection);
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
	public MainWindow(Connection connection) {
		this.connection=connection;
		initialize();
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
	          rs = stmt.executeQuery("SELECT * FROM EXAMEDOPING ORDER BY DATAEXAME DESC");
			  while (rs.next()) {       
	            model.addRow(new Object[]{rs.getString("CODIGO"), rs.getString("DATAEXAME").substring(0, 10), rs.getString("RESULTADO"), rs.getString("REPROVADO"),
	            rs.getString("PASSAPORTEATLETA"), rs.getString("CODIGOMEDICO"),rs.getString("CODIGOMODALIDADE")});
	         	
	         }
			 stmt.close();
	     }catch(Exception e){
	     	e.printStackTrace();
	     }
	}
	
	public void updateTable(){
		emptyTable();
		fillTable();
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
		
		JScrollPane examesScroll = new JScrollPane(tabelaExames);
		springLayout.putConstraint(SpringLayout.WEST, examesScroll, 21, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, examesScroll, -212, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(examesScroll);
		
		//Titulo da tabela
		JLabel title = new JLabel("Exames de Doping");
		springLayout.putConstraint(SpringLayout.SOUTH, title, -412, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, examesScroll, 12, SpringLayout.SOUTH, title);
		springLayout.putConstraint(SpringLayout.NORTH, title, 20, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, title, 36, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, title, 300, SpringLayout.WEST, frame.getContentPane());
		title.setFont(new Font("Tahoma", Font.PLAIN, 32));
		frame.getContentPane().add(title);
		
		//Titulo das opcoes de manipulacao dos exames
		JLabel tituloOpcoes = new JLabel("Exames de doping:");
		springLayout.putConstraint(SpringLayout.NORTH, tituloOpcoes, 102, SpringLayout.NORTH, frame.getContentPane());
		tituloOpcoes.setFont(new Font("Arial", Font.BOLD, 13));
		tituloOpcoes.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(tituloOpcoes);
		
		//Botoes para manipulacao dos exames
		
		JButton botaoInserir = new JButton("Inserir novo exame");
		springLayout.putConstraint(SpringLayout.NORTH, botaoInserir, 131, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, botaoInserir, -317, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tituloOpcoes, 0, SpringLayout.WEST, botaoInserir);
		springLayout.putConstraint(SpringLayout.WEST, botaoInserir, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoInserir, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tituloOpcoes, -6, SpringLayout.NORTH, botaoInserir);
		springLayout.putConstraint(SpringLayout.EAST, tituloOpcoes, 0, SpringLayout.EAST, botaoInserir);
		botaoInserir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new AlterarDados(connection);
			}
		});
		botaoInserir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		botaoInserir.setFont(new Font("Arial", Font.BOLD, 12));
		frame.getContentPane().add(botaoInserir);
		
		JButton botaoAlterar = new JButton("Alterar exame selecionado");
		springLayout.putConstraint(SpringLayout.NORTH, botaoAlterar, 6, SpringLayout.SOUTH, botaoInserir);
		springLayout.putConstraint(SpringLayout.WEST, botaoAlterar, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoAlterar, -288, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoAlterar, 0, SpringLayout.EAST, tituloOpcoes);
		botaoAlterar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row=tabelaExames.getSelectedRow();
				if(row!=-1){
					String str=(String)tabelaExames.getModel().getValueAt(row, 0); //Pega id do registro
					//int id=Integer.parseInt(str);
					new AlterarDados(connection, str);
				}
			}
		});
		botaoAlterar.setFont(new Font("Arial", Font.BOLD, 12));
		frame.getContentPane().add(botaoAlterar);
		
		JButton botaoExcluir = new JButton("Excluir exame selecionado");
		springLayout.putConstraint(SpringLayout.NORTH, botaoExcluir, 6, SpringLayout.SOUTH, botaoAlterar);
		springLayout.putConstraint(SpringLayout.WEST, botaoExcluir, 15, SpringLayout.EAST, examesScroll);
		springLayout.putConstraint(SpringLayout.EAST, botaoExcluir, 0, SpringLayout.EAST, tituloOpcoes);
		botaoExcluir.setFont(new Font("Arial", Font.BOLD, 12));
		botaoExcluir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row=tabelaExames.getSelectedRow();
				if(row!=-1){
					String str=(String)tabelaExames.getModel().getValueAt(row, 0); //Pega id do registro
					int id=Integer.parseInt(str);
					deleteExam(id); //Remove registo do BD
					updateTable();
				}
			}
		});
		botaoExcluir.setFont(new Font("Arial", Font.BOLD, 12));
		frame.getContentPane().add(botaoExcluir);
		
		//Botao para atualizacao da tabela
		JButton botaoAtualizar = new JButton("Recarregar tabela");
		springLayout.putConstraint(SpringLayout.NORTH, botaoAtualizar, 424, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, examesScroll, -6, SpringLayout.NORTH, botaoAtualizar);
		springLayout.putConstraint(SpringLayout.WEST, botaoAtualizar, 617, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoAtualizar, 0, SpringLayout.EAST, examesScroll);
		botaoAtualizar.setFont(new Font("Arial", Font.BOLD, 12));
		botaoAtualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				updateTable();
			}
		});
		frame.getContentPane().add(botaoAtualizar);
		
		//Titulo e botoes referentes as opcoes de relatorios
		JLabel tituloRelatorio = new JLabel("Relat\u00F3rios:");
		springLayout.putConstraint(SpringLayout.SOUTH, botaoExcluir, -64, SpringLayout.NORTH, tituloRelatorio);
		springLayout.putConstraint(SpringLayout.NORTH, tituloRelatorio, 276, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tituloRelatorio, -172, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tituloRelatorio, 0, SpringLayout.EAST, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.WEST, tituloRelatorio, 15, SpringLayout.EAST, examesScroll);
		tituloRelatorio.setFont(new Font("Arial", Font.BOLD, 13));
		tituloRelatorio.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(tituloRelatorio);
		
		JButton botaoRelatorios = new JButton("Gerar Novo Relat\u00F3rio");
		springLayout.putConstraint(SpringLayout.NORTH, botaoRelatorios, 6, SpringLayout.SOUTH, tituloRelatorio);
		springLayout.putConstraint(SpringLayout.WEST, botaoRelatorios, 0, SpringLayout.WEST, tituloOpcoes);
		springLayout.putConstraint(SpringLayout.SOUTH, botaoRelatorios, -143, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, botaoRelatorios, 0, SpringLayout.EAST, tituloOpcoes);
		botaoRelatorios.setFont(new Font("Arial", Font.BOLD, 12));
		botaoRelatorios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ReportsWindow.runReportsWindow();
			}
		});
		frame.getContentPane().add(botaoRelatorios);
		
		//Seta tamanho minimo para colunas da tabela
		tabelaExames.getColumnModel().getColumn(0).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(1).setMinWidth(50);
		tabelaExames.getColumnModel().getColumn(2).setMinWidth(400);
		tabelaExames.getColumnModel().getColumn(3).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(4).setMinWidth(45);
		tabelaExames.getColumnModel().getColumn(5).setMinWidth(5);
		tabelaExames.getColumnModel().getColumn(6).setMinWidth(5);
		
		
		
		if(this.connection==null){
			System.out.println("Conexao nao estabelecida!");
			System.exit(2);
		}
		this.fillTable(); //Inicializa tabela com valores de ExameDoping

	
	}
}
