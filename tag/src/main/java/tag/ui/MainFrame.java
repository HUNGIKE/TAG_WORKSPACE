package tag.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;

import javax.swing.JFrame;

public class MainFrame extends JFrame{

	MouseAdapter ma=new MouseAdapter(){
		
	};
	
	BoardPanel boardpanel;
	
	public MainFrame(){
		this.setSize(500,500);
		this.boardpanel=new BoardPanel(10,10);
		this.add(this.boardpanel);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		new MainFrame();

	}

	
	
	
}
