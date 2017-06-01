package tag;

public class Host {
	Data data;
	Viewer viewer;
	Controller controller;
	Player[] player=new Player[2];
	
	
	
	public Host(){
		this.data=new Data(10,10);
		this.viewer=new Viewer(this.data);
		this.controller=new Controller(data);
	}
	
	public void setPlayer(int idx,Player player){
		this.player[idx]=player;
	}
	
	
	public void run(){
		//maybe need chess type class in the future.
		int p=0;
		
		while( ! this.controller.isGameTerminated() ){
			p=(p+1)%2;
			Player ply=this.player[p];
			
			Data.Point retP=ply.play(this.viewer);
			//error handling 
			this.controller.setValue(retP.x,retP.y, p+1);
			this.data.print();
			
			//Thread.sleep(300);
			
		}
		
	}

}
