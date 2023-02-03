package tag.player.gametree;

import tag.Data.Point;

import java.util.concurrent.atomic.AtomicReference;

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
		
		AtomicReference<Point> pointRef=new AtomicReference<Point>();
		
		try {
			runGameTree(DEPTH,WIDTH,mem,viewer.getColor(),Integer.MIN_VALUE+1,Integer.MAX_VALUE,pointRef);
		} catch (OutOfBoardException | OperationProhibitedException e) {
			e.printStackTrace();
			return new Point(0,0);
		}
		
		return pointRef.get();
	}

	
	private int runGameTree(int depth,int width,MemBoard board,Data.Color color,int alpha,int beta,AtomicReference<Point> pointRef) throws OutOfBoardException, OperationProhibitedException{
		if(depth==0)return countScore(board,color);		

		PointFixedPriorityQueue breadthQueue=board.getPriorityPoint(width);
		int score =Integer.MIN_VALUE;
		Point point=null;

		while( ( point=breadthQueue.poll() )!=null && 
				board.data.getGrid(point.x,point.y).color==null 
		){
			
			board.activate(point.x,point.y, color);
			int newScore=runGameTree(depth-1,width,board,color.rivalColor(),beta*-1,alpha*-1,null)*-1;
			board.rollback();
			
			if( newScore>score ){
				score=newScore;
				if(pointRef!=null){ pointRef.set(point);}
				
				alpha=Math.max(alpha,score);
				if(alpha>=beta)break;
			}
		}
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
