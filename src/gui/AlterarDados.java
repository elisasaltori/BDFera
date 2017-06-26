package gui;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;


public class AlterarDados {
	
	
	
	private String resultado, data, passaporte, medico, modalidade;
	private boolean reprovado;
	private JFrame frmAdicionar;
	private JTextField diagnosticoTextField;
	private Connection c;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void janelaAlterarDados(String key){
		JTextArea resultadoTextArea;
		JFormattedTextField dataTextField;
		frmAdicionar = new JFrame();
		
		frmAdicionar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnSalvar = new JButton("Salvar");
		JLabel lblNewLabel = new JLabel("C\u00F3digo do exame");
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
		frmAdicionar.setBounds((int)(dimension.getWidth()-366)/2, (int)(dimension.getHeight()-195)/2, 653, 301);
		
		
		
		lblNewLabel.setBounds(10, 37, 125, 14);
		frmAdicionar.getContentPane().add(lblNewLabel);
		
		
		lblData.setBounds(145, 37, 46, 14);
		frmAdicionar.getContentPane().add(lblData);
		
		
		lblResultado.setBounds(23, 129, 112, 14);
		frmAdicionar.getContentPane().add(lblResultado);
		
		resultadoTextArea = new JTextArea();
		resultadoTextArea.setWrapStyleWord(true);
		resultadoTextArea.setLineWrap(true);

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
		 
		
		btnSalvar.setBounds(164, 227, 89, 23);
		frmAdicionar.getContentPane().add(btnSalvar);
		
		
		JLabel lblPassaporte = new JLabel("Passaporte");
		lblPassaporte.setBounds(256, 37, 97, 14);
		frmAdicionar.getContentPane().add(lblPassaporte);
		
		JLabel lblCodigoDoMdico = new JLabel("C\u00F3digo do m\u00E9dico");
		lblCodigoDoMdico.setBounds(374, 37, 111, 14);
		frmAdicionar.getContentPane().add(lblCodigoDoMdico);
		
		JLabel lblNewLabel_1 = new JLabel("C\u00F3digo da modalidade");
		lblNewLabel_1.setBounds(495, 37, 132, 14);
		frmAdicionar.getContentPane().add(lblNewLabel_1);
		
		JComboBox<String> passaporteComboBox = new JComboBox<String>();
		
		try{
			int index = 0;
			Statement stmt;
	        ResultSet rs;
			stmt = c.createStatement();
	        rs = stmt.executeQuery("select passaporte from atleta");
	        while(rs.next()){
	        	passaporteComboBox.insertItemAt(rs.getString("passaporte"), index++);
	        	
	        }
	    }catch(Exception e){
	     	e.printStackTrace();
	    }
		
		passaporteComboBox.setBounds(256, 62, 97, 20);
		frmAdicionar.getContentPane().add(passaporteComboBox);
		
		diagnosticoTextField = new JTextField();
		
		if(key == null){
			try{
				Statement stmt;
		        ResultSet rs;
				stmt = c.createStatement();
		        rs = stmt.executeQuery("select nvl(max(codigo)+1,0) as novocodigo from examedoping");
		        rs.next();
		        System.out.println(rs.getString("novocodigo"));
		        diagnosticoTextField.setText(rs.getString("novocodigo").toString());
		    }catch(Exception e){
		     	e.printStackTrace();
		    }
		}
		diagnosticoTextField.setEnabled(false);
		diagnosticoTextField.setEditable(false);
		diagnosticoTextField.setBounds(23, 62, 86, 20);
		frmAdicionar.getContentPane().add(diagnosticoTextField);
		diagnosticoTextField.setColumns(10);
		
		JComboBox<Object> medicoComboBox = new JComboBox<Object>();
		try{
			int index = 0;
			Statement stmt;
	        ResultSet rs;
			stmt = c.createStatement();
	        rs = stmt.executeQuery("select codigo from medico");
	        while(rs.next()){
	        	medicoComboBox.insertItemAt(rs.getString("codigo"), index++);
	        	
	        }
	    }catch(Exception e){
	     	e.printStackTrace();
	    }
		medicoComboBox.setBounds(374, 62, 85, 20);
		frmAdicionar.getContentPane().add(medicoComboBox);
		
		JComboBox<String> modalidadeComboBox = new JComboBox<String>();
		try{
			int index = 0;
			Statement stmt;
	        ResultSet rs;
			stmt = c.createStatement();
	        rs = stmt.executeQuery("select codigo from modalidadeesportiva");
	        while(rs.next()){
	        	modalidadeComboBox.insertItemAt(rs.getString("codigo"), index++);
	        }
	    }catch(Exception e){
	     	e.printStackTrace();
	    }
		modalidadeComboBox.setBounds(495, 62, 86, 20);
		frmAdicionar.getContentPane().add(modalidadeComboBox);
		if(key != null){
			diagnosticoTextField.setText(key);
			try{
				int index = 0;
				Statement stmt;
		        ResultSet rs;
				stmt = c.createStatement();
		        rs = stmt.executeQuery("select * from examedoping where codigo = "+key);
		        rs.next();
		        while(!rs.getString("passaporteatleta").equals(passaporteComboBox.getItemAt(index))) index++;
		        passaporteComboBox.setSelectedIndex(index);
		        index = 0;
		        while(!rs.getString("codigomedico").equals(medicoComboBox.getItemAt(index))) index++;
		        medicoComboBox.setSelectedIndex(index);
		        index = 0;
		        while(!rs.getString("codigomodalidade").equals(modalidadeComboBox.getItemAt(index))) index++;
		        modalidadeComboBox.setSelectedIndex(index);
		        index = 0;
		        if(rs.getString("reprovado").equals("1")) chckbxReprovado.setSelected(true); 
		        resultadoTextArea.setText(rs.getString("resultado"));
		    }catch(Exception e){
		     	e.printStackTrace();
		    }
			
		}
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Estou pressionado");
				resultado = resultadoTextArea.getText();
				reprovado = chckbxReprovado.isSelected();
				passaporte = (String) passaporteComboBox.getSelectedItem();
				medico = (String) medicoComboBox.getSelectedItem();
				modalidade = (String) modalidadeComboBox.getSelectedItem();
				try {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					Date d = df.parse(dataTextField.getText());
					data = df.format(d);
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(null, "Digite uma data no formato dd/MM/yyyy");
					data = null;
				}
				System.out.println(data);
				System.out.println(resultado);
				System.out.println(reprovado);
				System.out.println(passaporte);
				System.out.println(medico);
				System.out.println(modalidade);
				frmAdicionar.setVisible(false);
				try {
					insertQuery(key);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					frmAdicionar.setVisible(true);
				}
				return;
			}
		});
		frmAdicionar.setVisible(true);
		
		
	}
	
	public AlterarDados(Connection connection){
		c = connection;
		janelaAlterarDados(null);


	}
	public AlterarDados(Connection connection, String key){
		c = connection;
		janelaAlterarDados(key);
	}
	private void insertQuery(String key) throws SQLException{
		if(key == null){
			int ireprovado;
			if(reprovado == true) ireprovado = 1;
			else ireprovado = 0;
			Statement stmt = c.createStatement();
			stmt.execute("INSERT INTO EXAMEDOPING (codigo, dataexame, resultado, reprovado, passaporteatleta, codigomedico, codigomodalidade) SELECT NVL(MAX(CODIGO)+1, 0), to_date("+"'"+data+"', 'DD/MM/RRRR'), '"+resultado+"', "+ireprovado+", '"+passaporte+"', "+medico+", "+modalidade+" FROM EXAMEDOPING");
			stmt.close();
		}
		else{
			int ireprovado;
			if(reprovado == true) ireprovado = 1;
			else ireprovado = 0;
			Statement stmt = c.createStatement();
			stmt.execute("update examedoping set dataexame = to_date('" + data + " ', 'DD/MM/RRRR'), resultado = '" + resultado + " ', reprovado = " + ireprovado + ", passaporteatleta = '"+ passaporte + "', codigomedico = " + medico + ", codigomodalidade = "+ modalidade + " where codigo = "+ key);
			System.out.println("update examedoping set dataexame = to_date('" + data + " ', 'DD/MM/RRRR'), resultado = '" + resultado + " ', reprovado = " + ireprovado + ", passaporteatleta = '"+ passaporte + "', codigomedico = " + medico + ", codigomodalidade = "+ modalidade + " where codigo = "+ key);
			stmt.close();
		}
	}
	
	
	
}
