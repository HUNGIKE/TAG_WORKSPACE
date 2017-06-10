package tag;

import java.awt.Color;

import tag.exception.OperationProhibitedException;
import tag.exception.OutOfBoardException;
import tag.ui.MainFrame;

public class Host {
	Data data;
	Viewer viewer;
	Controller controller;
	MainFrame mainframe;
	
	
	Data.Color[] playerColor=new Data.Color[]{Data.Color.BLACK,Data.Color.WHITE};
	Player[] player=new Player[2];
	
	
	public Host(){
		this.data=new Data(10,10);
		this.viewer=new Viewer(this.data);
		this.controller=new Controller(data);
	}
	
	public void setGUI(MainFrame mainframe){
		this.mainframe=mainframe;
	}
	
	public MainFrame getGUI(){
		return this.mainframe;
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
	
	private int maximusRound=30;
	public void setMaximusRound(int maximusRound){
		this.maximusRound=maximusRound;
	}
	public int getMaximusRound(){
		return this.maximusRound;
	}
	
	private int round=0;
	public int getRound(){
		return this.round;
	}
	
	public void run(){
		this.controller.reset();		
		
		int p=0;
		
		while( ! this.controller.isGameTerminated() ){
			Player ply=this.player[p];
			this.viewer.setColor(playerColor[p]);
			
			try {
				Data.Point retP=ply.play(this.viewer);
				this.controller.setValue(retP.x,retP.y,this.viewer.getColor());
			} catch (OutOfBoardException | OperationProhibitedException e) {
				continue;
			}finally{
				if(this.mainframe!=null){
					this.mainframe.updadteFrame(this.viewer,true);
				}
			}
			
			
			p=(p+1)%2;
			if(p==0){ this.round++; }
			
			if(this.round>=this.maximusRound){
				this.controller.setGameTerminated(true);
			}
			
			
		}
		
		
		// System.out.print("Score: BLACK "+this.controller.getScore(Data.Color.BLACK));
		// System.out.println(",WHITE "+this.controller.getScore(Data.Color.WHITE));
	}

}
