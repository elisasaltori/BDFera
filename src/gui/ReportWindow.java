package gui;

import javax.swing.JFrame;

public class ReportWindow {
	
	JFrame frame;
	
	void initialize(){
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 479);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
	}
}
