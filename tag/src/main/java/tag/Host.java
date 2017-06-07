package tag;

import java.awt.Color;

import tag.exception.OperationProhibitedException;
import tag.exception.OutOfBoardException;

public class Host {
	Data data;
	Viewer viewer;
	Controller controller;
	
	
	Data.Color[] playerColor=new Data.Color[]{Data.Color.BLACK,Data.Color.WHITE};
	Player[] player=new Player[2];
	
	
	public Host(){
		this.data=new Data(10,10);
		this.viewer=new Viewer(this.data);
		this.controller=new Controller(data);
	}
	
	public Viewer getViewer(){
		return this.viewer;
	}
	
	public void setPlayer(Data.Color playerColor,Player player){
		for(int i=0;i<this.playerColor.length;i++){
			if(this.playerColor[i].equals(playerColor)){
				this.player[i]=player;
				
			}
		}
	}
	
	
	public Controller getController(){
		return this.controller;
	}
	
	public void run(){
		this.controller.reset();		
		
		int p=0;
		
		while( ! this.controller.isGameTerminated() ){
			Player ply=this.player[p];
			
			Data.Point retP=ply.play(this.viewer);
			//error handling 
			
			try {
				this.controller.setValue(retP.x,retP.y,playerColor[p]);
			} catch (OutOfBoardException | OperationProhibitedException e) {
				continue;
			}
			p=(p+1)%2;
			
		}
		// System.out.print("Score: BLACK "+this.controller.getScore(Data.Color.BLACK));
		// System.out.println(",WHITE "+this.controller.getScore(Data.Color.WHITE));
	}

}
