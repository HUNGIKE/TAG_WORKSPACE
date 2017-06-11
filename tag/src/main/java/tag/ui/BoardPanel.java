package tag.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import tag.Data;

public abstract class BoardPanel extends JPanel{
	
	Grid[][] grid;
	
	public BoardPanel(int w,int h){
		this.setLayout(new GridLayout(w,h));
		this.grid=new Grid[w][h];
		
		for(int i=0;i<w;i++) {
			for(int j=0;j<h;j++){
				this.grid[i][j]=new Grid(i,j);
				this.add(this.grid[i][j]);
			}
		}
	}
	
	
	public void clean(List<Data.Point> cleanPoint){
		
		for(int i=0;i<3;i++){
			for(Data.Point point:cleanPoint){
				this.grid[point.x][point.y].setShowChess(false);
			}
			this.repaint();
			try {Thread.sleep(100); } catch (InterruptedException e) {}
			for(Data.Point point:cleanPoint){
				this.grid[point.x][point.y].setShowChess(true);
			}
			this.repaint();
			try {Thread.sleep(100); } catch (InterruptedException e) {}
		}
		
		
		for(Data.Point point:cleanPoint){
			this.grid[point.x][point.y].setColor(null);
		}
		this.repaint();
		
	}
	
	public void setColor(int x,int y,Color color){
		Color oldColor=this.grid[x][y].color;
		if(color==null && oldColor==null ||
			color!=null && color.equals(oldColor) || 
			oldColor!=null && oldColor.equals(color)){
			return;
		}
		
		for(int i=0;i<3;i++){

			this.grid[x][y].setColor(oldColor);
			this.grid[x][y].repaint();
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		
			this.grid[x][y].setColor(color);
			this.grid[x][y].repaint();
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
	}
	

	
	protected abstract void GridClick(int x,int y);



	class Grid extends JPanel{
		Color color;
		final int X,Y;
		public Grid(int X,int Y){
			this.X=X;
			this.Y=Y;
			
			this.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e){
					BoardPanel.this.GridClick(Grid.this.X,Grid.this.Y);
				}
			});
			

			this.setSize(5,5);
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1) );
			this.setBackground(Color.WHITE);
		}
		
		public void setColor(Color color){
			this.color=color;
		}
		
		private boolean isShowChess=true;
		public void setShowChess(boolean isShowChess){
			this.isShowChess=isShowChess;
		}
		
		
		@Override
		public void paint(Graphics g){
			super.paint(g);
			if(this.color==null)return;
			if( !this.isShowChess )return;
			
			this.setOpaque(true);
			
			
			g.setColor(this.color);
			g.fillOval(0, 0,this.getWidth(),this.getHeight());
			g.setColor(Color.BLACK);
			g.drawOval(0, 0,this.getWidth(),this.getHeight());
			
			
		}
		
		
	}


}

