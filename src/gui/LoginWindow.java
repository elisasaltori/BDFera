package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Uma tela de login simples que realiza a conexao com o BD.
 * @author Elisa Saltori Trujillo - 8551100, Cristiano di Maio Chiaramelli - 9293053, Chan Ken Chen - 9436170
 *
 */
public class LoginWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField usuario;
	private JPasswordField senha;
	private JLabel erro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginWindow dialog = new LoginWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
                 usuario.getText(),
                 String.valueOf(senha.getPassword()));
         	return connection;
		 }catch(Exception e){
			 return null;
		 }
	}

	/**
	 * Create the dialog.
	 */
	public LoginWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setBounds((int)((width-326.0)/2.0), (int)((height-200.0)/3.0f), 326, 200);
		this.setResizable(false);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		usuario = new JTextField();
		contentPanel.add(usuario);
		usuario.setColumns(10);
		
		senha = new JPasswordField();
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, usuario, -23, SpringLayout.NORTH, senha);
		sl_contentPanel.putConstraint(SpringLayout.EAST, usuario, 0, SpringLayout.EAST, senha);
		sl_contentPanel.putConstraint(SpringLayout.WEST, senha, 106, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, senha, -21, SpringLayout.SOUTH, contentPanel);
		contentPanel.add(senha);
		senha.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Usu\u00E1rio:");
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewLabel, 69, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblNewLabel, -6, SpringLayout.NORTH, usuario);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Senha:");
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewLabel_1, 0, SpringLayout.WEST, lblNewLabel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -6, SpringLayout.NORTH, senha);
		contentPanel.add(lblNewLabel_1);
		
		erro = new JLabel("");
		sl_contentPanel.putConstraint(SpringLayout.WEST, erro, 10, SpringLayout.WEST, senha);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, erro, 0, SpringLayout.SOUTH, contentPanel);
		erro.setForeground(Color.RED);
		contentPanel.add(erro);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Connection connection = Connect();
						if(connection==null){
							erro.setText("Erro na conexão!");
						}else{
							erro.setText("");
							MainWindow.runWindow(connection);
							dispose();
							
							
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
