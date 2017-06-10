package tag.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tag.Data;
import tag.Data.Grid;
import tag.Data.Point;
import tag.Host;
import tag.Viewer;

public class MainFrame extends JFrame{
	
	private BoardPanel boardpanel;
	private ControlPanel controlPanel;
	

	private ArrayBlockingQueue<Data.Point> point;
	
	public MainFrame(){
		this.point=new ArrayBlockingQueue(1);
		this.setLayout(null);
		
		this.controlPanel=new ControlPanel(){

			@Override
			public void ResetClick() {
				//To Do 
				
			}
			
			
			
		};
		this.controlPanel.setSize(500,100);
		this.controlPanel.setLocation(10,10);
		
		this.boardpanel=new BoardPanel(10,10){

			@Override
			protected void GridClick(int x, int y) {
				MainFrame.this.point.offer(new Data.Point(x, y));
			}
			
			
		};
		this.boardpanel.setSize(500,500);
		this.boardpanel.setLocation(10,120);
		
		this.add(this.controlPanel);
		this.add(this.boardpanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(530,670);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	public Point getPoint(){
		try {
			return this.point.take();
		} catch (InterruptedException e) {
			return null;
		}
		
	}
	
	public void setColor(int x,int y,Color color){
		this.boardpanel.setColor(x, y, color);
	}
	
	public void updadteFrame(Viewer v){
		int w=v.getWidth();
		int h=v.getHeigth();
		
		
		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				Grid newGrid=v.getGrid(x, y);
				Color newColor=toGUIColor(newGrid.color);
				Color oldColor=this.boardpanel.grid[x][y].color;
				
				if( (newColor!=null && !newColor.equals(oldColor) ) ||
					(oldColor!=null && !oldColor.equals(newColor) )  ){
					this.setColor(x, y, newColor);
					
				}
			}
			
		}
		this.repaint();
	}
	
	
	public Color toGUIColor(Data.Color color){
		if(color==null)return null;
		if(color.equals(Data.Color.BLACK)){
			return Color.BLACK;
		}else if(color.equals(Data.Color.WHITE)){
			return Color.WHITE;
		}else{
			return null;
		}
	}
	
	public static void main(String[] args) {
		new MainFrame();

	}
	
}


abstract class ControlPanel extends JPanel{
	private JButton reset;
	
	
	private MouseAdapter ma=new MouseAdapter(){

		@Override
		public void mouseClicked(MouseEvent e){
			Component c=e.getComponent();
			if(c==ControlPanel.this.reset){
				ControlPanel.this.ResetClick();
			}
			
		}
		
	};
	
	public ControlPanel(){
		this.setBackground(Color.RED);
		this.setLayout(null);
		this.reset=new JButton("Reset");
		this.reset.setLocation(400,10);
		this.reset.setSize(100,30);
		
		this.add(this.reset);
		this.addMouseListener(this.ma);
		
	}
	
	public abstract void ResetClick();
	
}
