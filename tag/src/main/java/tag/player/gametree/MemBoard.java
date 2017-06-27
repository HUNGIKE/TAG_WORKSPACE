package tag.player.gametree;

import java.util.List;
import java.util.Stack;

import tag.Controller;
import tag.Data;
import tag.Viewer;
import tag.Data.Grid;
import tag.Data.Point;
import tag.exception.OperationProhibitedException;
import tag.exception.OutOfBoardException;
import tag.player.gametree.PointFixedPriorityQueue.PointValue;

public class MemBoard{
	public Data data;
	public Controller controller;
	public Stack<ActionItem> actionStack;
	
	
	public MemBoard(int width,int height){
		this.data=new Data(width,height);
		this.controller=new Controller(this.data);
		this.actionStack=new Stack<ActionItem>();
	}
	
	public void copyData(Viewer viewer){
		int width=viewer.getWidth(),height=viewer.getHeigth();
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				this.data.setValue(x, y,viewer.getValue(x, y));
			}
		}
	}
	
	public void activate(int x,int y,Data.Color color) throws OutOfBoardException, OperationProhibitedException{

		ActionItem putActionItem=new ActionItem(Action.PUT,x,y,color);
		this.actionStack.push(putActionItem);
		List<Point> closeSet=this.controller.setValue(x, y, color);
		
		
		for(Point point:closeSet){
			ActionItem takeActionItem=new ActionItem(Action.TAKE,point.x,point.y,this.data.getGrid(point.x,point.y).color);
			this.actionStack.push(takeActionItem);
		}
		this.controller.clean(closeSet);
	}
	
	public void rollback(){
		
		ActionItem actionItem= null;
		
		do{
			actionItem=this.actionStack.pop();
			Grid grid=this.data.getGrid(actionItem.x, actionItem.y);
			
			if(Action.TAKE.equals(actionItem.action)){
				grid.color=actionItem.color;
			}else if(Action.PUT.equals(actionItem.action)){
				grid.color=null;
			}
		}while( actionItem!=null && Action.TAKE.equals(actionItem.action) );
		
	}
	
	

	final private static int[][] OFFSET=new int[][]{{0,-1},{0,1},{-1,0},{1,0}}; 
	private int getPointValue(int x,int y){
		int bc=0,wc=0,ec=0;
		
		for(int[] offset:OFFSET){
			Grid grid=this.data.getGrid(x+offset[0], y+offset[1]);
			if(grid!=null ){
				if( grid.color==null ){
					ec+=1;
				}else if(Data.Color.WHITE.equals(grid.color)){
					bc+=1;
				}else{
					wc+=1;
				}
			}
		}
		
		double av=( bc+wc+ec) /3d;
		return (int)( ( Math.pow(bc-av,2)+Math.pow(ec-av,2)+Math.pow(wc-av,2) )/av*-10000);
	}
	
	
	public PointFixedPriorityQueue getPriorityPoint(int number){
		PointFixedPriorityQueue queue=new PointFixedPriorityQueue(number);
		
		int boardWidth=data.getWidth(),boardHeight=data.getHeigth();
		
		for(int x=0;x<boardWidth;x++){
			for(int y=0;y<boardHeight;y++){
				if(data.getGrid(x, y).color!=null)continue;
				
				queue.offer(new Point(x,y), getPointValue(x,y) );
			}
		}
		
		
		return queue;
	}

}

class ActionItem{
	Action action;
	int x,y;
	Data.Color color;
	
	ActionItem(Action action,int x,int y,Data.Color color){
		this.action=action;
		this.x=x;this.y=y;
		this.color=color;
	}
	
	
}

enum Action{
	PUT,TAKE;
}
