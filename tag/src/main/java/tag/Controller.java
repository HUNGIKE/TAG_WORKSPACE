package tag;

import java.util.LinkedList;
import java.util.List;

import tag.Data.Color;
import tag.Data.Grid;
import tag.Data.Point;
import tag.exception.CannotFindClosedSetException;

public class Controller {
	final private int[][] OFFSET=new int[][]{{0,-1},{0,1},{-1,0},{1,0}}; 
	
	private Data data;
	public Controller(Data data){
		this.data=data;
	}
	
	private int step=0;
	private boolean isGameTerminated=false;
	public boolean isGameTerminated(){
		return this.step>=100;
	}
	
	public void reset(){
		this.data.createBoard();
		this.step=0;
		this.isGameTerminated=false;
		
	}
	
	public int getScore(Color color){
		int score=0;
		for(int i=0;i<this.data.getWidth();i++){
			for(int j=0;j<this.data.getHeigth();j++){
				Grid grid=this.data.getGrid(i,j);
				if(color.equals(grid.color)){
					score+=1;
				}
			}
		}
		
		return score;
		
	}
	
	
	public void setValue(int x,int y,Color color){
		this.step++;
		this.data.setValue(x, y, color);
		

		for(int[] offset:this.OFFSET){
			Grid grid=this.data.getGrid(x+offset[0], y+offset[1]);
			if(grid==null)continue;
			Color cNear=grid.color;
			
			if(!color.equals(cNear)){
				
				try {
					
					List closeSet=new LinkedList();
					tryToCollectClosedSet(x+offset[0], y+offset[1],closeSet);
					clean(closeSet);
				} catch (CannotFindClosedSetException e) {
					continue;
				}
			}
			
		}
		
	}
	
	
	public void clean(List<Point> points){
		// System.out.println("clean:");
		for(Point p:points){
			// System.out.print("   "+p.x+","+p.y);
			this.data.setValue(p.x,p.y,null);
		}
		
		// System.out.println();
	}
	
	public void tryToCollectClosedSet(int startX,int startY,List<Point> closedSet) throws CannotFindClosedSetException{
		Point point=new Point(startX,startY);
		Grid g=this.data.getGrid(startX, startY);
		if(g==null)return;
		
		Color c=g.color;
		if( c==null ){
			throw new CannotFindClosedSetException();
		}
		
		closedSet.add(point);
		
		for(int[] offset:this.OFFSET){
			Grid gNear=this.data.getGrid(startX+offset[0], startY+offset[1]);
			if(gNear==null)continue;
			
			Color cNear=gNear.color;
			if(cNear==null){
				throw new CannotFindClosedSetException();
			}
			
			if(closedSet.contains(new Point(startX+offset[0], startY+offset[1])))continue;
			
			if( c.equals(cNear) ){
				tryToCollectClosedSet(startX+offset[0], startY+offset[1],closedSet);
			}
			
		}
		
		
		
	}
}



