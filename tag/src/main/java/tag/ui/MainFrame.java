package tag.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JFrame;

import tag.Data;
import tag.Data.Point;

public class MainFrame extends JFrame{
	
	private BoardPanel boardpanel;
	private ArrayBlockingQueue<Data.Point> point;
	
	public MainFrame(){
		this.point=new ArrayBlockingQueue(1);
		
		this.boardpanel=new BoardPanel(10,10){

			@Override
			protected void GridClick(int x, int y) {
				MainFrame.this.point.offer(new Data.Point(x, y));
			}
			
			
		};
		this.add(this.boardpanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
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
	
	public static void main(String[] args) {
		new MainFrame();

	}
	
	
	
}
