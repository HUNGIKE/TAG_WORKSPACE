package tag.player.gametree;

import tag.Data.Point;
import tag.Controller;
import tag.Data;
import tag.Player;
import tag.Viewer;
import tag.exception.OperationProhibitedException;
import tag.exception.OutOfBoardException;

public class GameTreePlayer extends Player {
	int DEPTH=2,WIDTH=100;

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
		
		// System.out.println(""+p.x+" ,"+p.y);
		// mem.data.print();
		
		
		return p;
	}
	
	private Point runGameTree(int depth,int width,MemBoard board,Data.Color color) throws OutOfBoardException, OperationProhibitedException{
		int score=Integer.MIN_VALUE;
		int pX=0,pY=0;
		
		int boardWidth=board.data.getWidth(),boardHeight=board.data.getHeigth();
		
		for(int x=0;x<boardWidth;x++){
			for(int y=0;y<boardHeight;y++){
				if(board.data.getGrid(x, y).color!=null)continue;
				
				int newScore=runGameTree(depth-1,width-1,board,x,y,color);
				if(newScore>=score){
					score=newScore;
					pX=x;pY=y;
				}
				
				
			}
		}
		return new Point(pX,pY);
	}
	
	private int runGameTree(int depth,int width,MemBoard board,int x,int y,Data.Color color) throws OutOfBoardException, OperationProhibitedException{
		
		if(depth==0)return countScore(board,color);
		
		
		int score = Integer.MIN_VALUE;
		int boardWidth=board.data.getWidth(),boardHeight=board.data.getHeigth();
		
		board.activate(x,y, color);

		scan: for(int nextX=0;nextX<boardWidth;nextX++){
			for(int nextY=0;nextY<boardHeight;nextY++){
				if(board.data.getGrid(nextX, nextY).color!=null)continue;
				if(width<=0)break scan;
				width-=1;
					
				score = Math.max( score,-1*runGameTree(depth-1,width,board,nextX,nextY,color.rivalColor()) );

			}
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
