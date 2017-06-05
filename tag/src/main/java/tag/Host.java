package tag;

import java.awt.Color;

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
			p=(p+1)%2;
			Player ply=this.player[p];
			
			Data.Point retP=ply.play(this.viewer);
			//error handling 
			
			this.controller.setValue(retP.x,retP.y,playerColor[p]);
			//  this.data.print();
			
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.print("Score: BLACK "+this.controller.getScore(Data.Color.BLACK));
		System.out.println(",WHITE "+this.controller.getScore(Data.Color.WHITE));
	}

}
