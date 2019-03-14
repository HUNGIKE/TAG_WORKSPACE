package tag.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tag.Data;
import tag.Data.Grid;
import tag.Data.Point;
import tag.Host;
import tag.Player;
import tag.Viewer;
import tag.player.GUIPlayer;


public class MainFrame extends JFrame{
	private Host host;
	
	private BoardPanel boardpanel;
	private ControlPanel controlPanel;

	private ArrayBlockingQueue<Data.Point> point;
	
	private int w,h;
	
	public MainFrame() {
		this(10, 10);
	}
	
	public MainFrame(int w,int h){
		this.w=w;this.h=h;
		this.point=new ArrayBlockingQueue(1);
		this.setLayout(null);
		
		this.controlPanel=new ControlPanel(){

			@Override
			public void ResetClick() {
				//To Do 
				
			}

			@Override
			public void StartClick() {
				if(!this.start.isEnabled())return;
				
				Host host=MainFrame.this.host;
				
				this.list[0].setEnabled(false);
				this.list[1].setEnabled(false);
				this.start.setEnabled(false);
				
				
				host.setPlayer(Data.Color.BLACK, this.getItem(0));
				host.setPlayer(Data.Color.WHITE, this.getItem(1));
				
				MainFrame.this.boardpanel.setEnabled(true);
				MainFrame.this.boardpanel.setVisible(true);
				
				new Thread(){
					public void run(){
						MainFrame.this.host.run();
					}
					
				}.start();

			}
			
			
			
		};
		this.controlPanel.setSize(500,100);
		this.controlPanel.setLocation(10,10);
		
		this.boardpanel=new BoardPanel(w,h){

			@Override
			protected void GridClick(int x, int y) {
				if(!this.isEnabled())return;
				
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
		this.boardpanel.setEnabled(false);
		//this.boardpanel.setVisible(false);
		this.setVisible(true);
		
	}
	
	public void setHost(Host host){
		this.host=host;
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
	
	public void Clean(List<Data.Point> cleanPoint){
		this.boardpanel.clean(cleanPoint);
	}
	
	public void setPlayerListItem(int idx,Player[] players){
		MainFrame.this.controlPanel.setItems(idx, players);
	}
	
	public void setRoundString(int round,int maximusRound){
		if(maximusRound>0){
			this.controlPanel.setRoundTextString(""+ (round+1) +"/"+maximusRound);
		}else{

			this.controlPanel.setRoundTextString(""+ (round+1) +"/unlimited");
		}
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
	
//	public static void main(String[] args) {
//		new MainFrame();
//	}
	
	
	
}


abstract class ControlPanel extends JPanel{
	JButton reset,start;
	JLabel roundText;
	JComboBox<Player>[] list=new JComboBox[2];

	
	private MouseAdapter ma=new MouseAdapter(){

		@Override
		public void mouseClicked(MouseEvent e){
			Component c=e.getComponent();
			if(c==ControlPanel.this.reset){
				ControlPanel.this.ResetClick();
			}else if(c==ControlPanel.this.start){
				ControlPanel.this.StartClick();
			}
			
		}
		
	};
	
	public void setRoundTextString(String str){
		this.roundText.setText(str);
	}
	
	public ControlPanel(){
		this.setLayout(null);
		
		this.reset=new JButton("Reset");
		this.reset.setLocation(380,10);
		this.reset.setSize(100,30);	
		this.reset.setEnabled(false);
		this.reset.addMouseListener(this.ma);
		this.add(this.reset);
		
		this.start=new JButton("Start");
		this.start.setLocation(270,10);
		this.start.setSize(100,30);
		this.start.addMouseListener(this.ma);		
		this.add(this.start);
		
		this.roundText=new JLabel("");
		this.roundText.setFont(new Font("Serif", Font.BOLD, 30));
		this.roundText.setLocation(10,40);
		this.roundText.setSize(180,100);
		this.add(this.roundText);
		
		JLabel blackText=new JLabel("BLACK:");
		blackText.setLocation(10, 5);
		blackText.setSize(80,30);
		this.add(blackText);
		
		this.list[0]=new JComboBox();
		this.list[0].setLocation(60,10);
		this.list[0].setSize(180,20);
		this.add(list[0]);
		
		JLabel whiteText=new JLabel("WHITE:");
		whiteText.setLocation(10, 35);
		whiteText.setSize(80,30);
		this.add(whiteText);
		
		this.list[1]=new JComboBox();
		this.list[1].setLocation(60,40);
		this.list[1].setSize(180,20);
		this.add(list[1]);


		
	}
	
	public Player getItem(int idx){
		return (Player) this.list[idx].getSelectedItem();
	}
	
	public void setItems(int idx,Player[] players){
		for(Player p:players){
			this.list[idx].addItem(p);
		}
	}
	
	public abstract void ResetClick();
	
	public abstract void StartClick();
	
}
