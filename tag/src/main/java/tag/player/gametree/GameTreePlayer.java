package tag.player.gametree;

import tag.Data.Point;
import tag.Controller;
import tag.Data;
import tag.Player;
import tag.Viewer;
import tag.exception.OperationProhibitedException;
import tag.exception.OutOfBoardException;
import tag.player.gametree.PointFixedPriorityQueue.PointValue;

public class GameTreePlayer extends Player {
	int DEPTH=8,WIDTH=11;

	public GameTreePlayer(int DEPTH,int WIDTH){
		this.DEPTH=DEPTH;
		this.WIDTH=WIDTH;
	}
	
	public GameTreePlayer(){}
	
	@Override
	public Point play(Viewer viewer) {
		MemBoard mem=new MemBoard(viewer.getWidth(),viewer.getHeigth());
		mem.copyData(viewer);
		
		Point p=null;
		
		try {
			p=runGameTree(DEPTH,WIDTH,mem,viewer.getColor());
		} catch (OutOfBoardException | OperationProhibitedException e) {
			e.printStackTrace();
			
			p=new Point(0,0);
		}
		
		return p;
	}
	
	private Point runGameTree(int depth,int width,MemBoard board,Data.Color color) throws OutOfBoardException, OperationProhibitedException{
		int score=(Integer.MIN_VALUE+1);
		Point retPoint=new Point(0,0);
		
		int boardWidth=board.data.getWidth(),boardHeight=board.data.getHeigth();
		
		
		PointFixedPriorityQueue breadthQueue=board.getPriorityPoint(width);
		Point point=null;
		while( ( point=breadthQueue.poll())!=null){
			if(board.data.getGrid(point.x,point.y).color!=null)continue;
			
			int newScore=runGameTree(depth-1,WIDTH,board,point.x,point.y,color,score*-1);
			if(newScore>score){
				score=newScore;
				retPoint=point;
			}
			
		}
		
		return retPoint;
	}
	
	private int runGameTree(int depth,int width,MemBoard board,int x,int y,Data.Color color,int alphabeta) throws OutOfBoardException, OperationProhibitedException{
		
		int score =(Integer.MIN_VALUE+1);
		int boardWidth=board.data.getWidth(),boardHeight=board.data.getHeigth();
		
		board.activate(x,y, color);

		if(depth==0){
			score=countScore(board,color);
		}else{
			PointFixedPriorityQueue breadthQueue=board.getPriorityPoint(width);
			Point point=null;
			while( ( point=breadthQueue.poll())!=null){
				if(board.data.getGrid(point.x,point.y).color!=null)continue;
				if(score>=alphabeta)break;
				
				int newScore=runGameTree(depth-1,WIDTH,board,point.x,point.y,color.rivalColor(),score*-1);
				score = Math.max( score, newScore);
				
			}
		
			score*=-1;
		}
		
		board.rollback();
		return score;
	}
	
	
	private int countScore(MemBoard board,Data.Color color){
		return board.controller.getScore(color)-board.controller.getScore(color.rivalColor());
	}

	@Override
	public String toString(){
		return "Game Tree";
	}
}
