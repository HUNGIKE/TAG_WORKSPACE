package tag.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class BoardPanel extends JPanel{
	
	Grid[][] grid;
	
	public BoardPanel(int w,int h){
		this.setLayout(new GridLayout(w,h));
		this.grid=new Grid[w][h];
		
		for(int i=0;i<w;i++) {
			for(int j=0;j<h;j++){
				this.grid[i][j]=new Grid(Color.BLACK);
				this.add(this.grid[i][j]);
			}
		}
	}
	
	
	public void setColor(int x,int y,Color color){
		this.grid[x][y].setColor(color);
		this.grid[x][y].repaint();
	}

}


class Grid extends JPanel{
	Color color;
	
	public Grid(){
		this.setSize(5,5);
	}
	
	public Grid(Color color){
		this();
		this.color=color;
	}
	
	public void setColor(Color color){
		this.color=color;
	}
	
	@Override
	public void paint(Graphics g){
		this.setBackground(Color.WHITE);
		if(this.color==null)return;
		
		g.fillOval(0, 0,this.getWidth(),this.getHeight());
		
		
	}
	
	
}


