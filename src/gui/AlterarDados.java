package gui;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Dimension;
import java.awt.Toolkit;
//import java.awt.Dimension;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


//https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
//https://coderanch.com/t/341412/java/lock-disable-JTextField
//insert into examedopping (...) select nvl(max(codigo)+1), ... from examedopping
public class AlterarDados {
	
	
	private JFrame frmAdicionar;
	private String resultado, data;
	private boolean reprovado;
	private JTextField diagnosticoTextField;
	private JTextField passaporteTextField;
	private JTextField medicoTextField;
	private JTextField modalidadeTextField;
	private JTextArea resultadoTextArea;
	private JFormattedTextField dataTextField;
	private Connection c;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void janelaAlterarDados(){
		frmAdicionar = new JFrame();
		
		JButton btnSalvar = new JButton("Salvar");
		JLabel lblNewLabel = new JLabel("C\u00F3digo do diagn\u00F3stico");
		JLabel lblData = new JLabel("Data");
		JLabel lblResultado = new JLabel("Resultado");
		JCheckBox chckbxReprovado = new JCheckBox("Reprovado?");
		
		frmAdicionar.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmAdicionar.setVisible(false);
				frmAdicionar.dispose();
			}
		});
		frmAdicionar.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frmAdicionar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdicionar.setBounds((int)(dimension.getWidth()-366)/2, (int)(dimension.getHeight()-195)/2, 653, 301);
		
		
		
		lblNewLabel.setBounds(23, 37, 112, 14);
		frmAdicionar.getContentPane().add(lblNewLabel);
		
		
		lblData.setBounds(145, 37, 46, 14);
		frmAdicionar.getContentPane().add(lblData);
		
		
		lblResultado.setBounds(23, 129, 70, 14);
		frmAdicionar.getContentPane().add(lblResultado);
		
		resultadoTextArea = new JTextArea();
		resultadoTextArea.setBounds(23, 154, 446, 47);
		frmAdicionar.getContentPane().add(resultadoTextArea);
		
		
		chckbxReprovado.setBounds(506, 165, 97, 23);
		frmAdicionar.getContentPane().add(chckbxReprovado);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		dataTextField = new JFormattedTextField(df);
		dataTextField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		dataTextField.setValue(new Date());
		
		dataTextField.setBounds(145, 62, 86, 20);
		frmAdicionar.getContentPane().add(dataTextField);
		dataTextField.setColumns(10);
		 
		//botar aqui a sql querry
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Estou pressionado");
				//data = dataTextField.getText();
				resultado = resultadoTextArea.getText();
				reprovado = chckbxReprovado.isSelected();
				try {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					Date d = df.parse(dataTextField.getText());
					data = df.format(d);
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(null, "Digite uma data no formato dd/MM/yyyy");
				}
				System.out.println(data);
				System.out.println(resultado);
				System.out.println(reprovado);


			}
		});
		btnSalvar.setBounds(164, 227, 89, 23);
		frmAdicionar.getContentPane().add(btnSalvar);
		
		diagnosticoTextField = new JTextField();
		diagnosticoTextField.setEditable(false);
		diagnosticoTextField.setEnabled(false);
		diagnosticoTextField.setBounds(23, 62, 86, 20);
		frmAdicionar.getContentPane().add(diagnosticoTextField);
		diagnosticoTextField.setColumns(10);
		
		JLabel lblPassaporte = new JLabel("Passaporte");
		lblPassaporte.setBounds(256, 37, 62, 14);
		frmAdicionar.getContentPane().add(lblPassaporte);
		
		passaporteTextField = new JTextField();
		passaporteTextField.setBounds(256, 62, 86, 20);
		frmAdicionar.getContentPane().add(passaporteTextField);
		passaporteTextField.setColumns(10);
		
		medicoTextField = new JTextField();
		medicoTextField.setBounds(374, 62, 86, 20);
		frmAdicionar.getContentPane().add(medicoTextField);
		medicoTextField.setColumns(10);
		
		JLabel lblCodigoDoMdico = new JLabel("C\u00F3digo do m\u00E9dico");
		lblCodigoDoMdico.setBounds(374, 37, 95, 14);
		frmAdicionar.getContentPane().add(lblCodigoDoMdico);
		
		modalidadeTextField = new JTextField();
		modalidadeTextField.setBounds(495, 62, 86, 20);
		frmAdicionar.getContentPane().add(modalidadeTextField);
		modalidadeTextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("C\u00F3digo da modalidade");
		lblNewLabel_1.setBounds(495, 37, 108, 14);
		frmAdicionar.getContentPane().add(lblNewLabel_1);
	}
	/*public static boolean AlterarDados(int key){
		return true;
	}*/
	public AlterarDados(Connection connection){
		Statement stmt;
        ResultSet rs;
		c = connection;
		try{
			  stmt = c.createStatement();
	          rs = stmt.executeQuery("select nvl(max(codigo)+1) from examedopping");
	          diagnosticoTextField.setText(arg0);
	     }catch(Exception e){
	     	e.printStackTrace();
	     }
		
		janelaAlterarDados();
	}
	public static void main(String args[]) throws InterruptedException{
		//AlterarDados j = new AlterarDados();
		//j.frmAdicionar.setVisible(true);
		while(true){
			Thread.sleep(1000);
		}
	}
}
