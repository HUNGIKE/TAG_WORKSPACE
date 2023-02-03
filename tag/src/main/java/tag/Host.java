package tag;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import tag.Data.Point;
import tag.exception.OperationProhibitedException;
import tag.exception.OutOfBoardException;
import tag.ui.MainFrame;

public class Host {
	Data data;
	Viewer viewer;
	Controller controller;
	
	
	Data.Color[] playerColor=new Data.Color[]{Data.Color.BLACK,Data.Color.WHITE};
	Player[] player=new Player[2];
	
	
	
	public Host() {
		this(10,10);
	}
	
	public Host(int w,int h){
		this.data=new Data(w,h);
		this.viewer=new Viewer(this.data);
		this.viewer.setGameInfo(new Viewer.GameInfo());
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
	
	
	public void setMaximusRound(int maximusRound){
		this.viewer.getGameInfo().maximusRound = maximusRound;
	}
	public int getMaximusRound(){
		return this.viewer.getGameInfo().maximusRound;
	}
	
	public void setRound(int round) {
		this.viewer.getGameInfo().round = round;
	}
	public int getRound(){
		return this.viewer.getGameInfo().round;
	}
	
	public void resetGame() {
		this.controller.setGameTerminated(true);
	}

	
	public void run(){
		
		
		this.setRound(0);
		this.controller.reset();
		
		int p=0;
		
		while (!this.controller.isGameTerminated()) {
			synchronized (this) {

				Player ply = this.player[p];
				this.viewer.setColor(playerColor[p]);

				List<Data.Point> closeSet = null;
				try {
					Data.Point retP = ply.play(this.viewer);
					if (retP == null) throw new OperationProhibitedException();

					closeSet = this.controller.setValue(retP.x, retP.y, this.viewer.getColor());
					this.controller.clean(closeSet); 

				} catch (OutOfBoardException | OperationProhibitedException e) {
					continue;
				} finally {
					for(Player player:this.player) {
						player.update(this.viewer,closeSet);
					}
					
				}

				p = (p + 1) % 2;
				if (p == 0) { this.setRound(this.getRound()+1); }

				if (this.getMaximusRound() > 0 && this.getRound() >= this.getMaximusRound()) {
					this.controller.setGameTerminated(true);
				}
			}
		}
		
		// System.out.print("Score: BLACK "+this.controller.getScore(Data.Color.BLACK));
		// System.out.println(",WHITE "+this.controller.getScore(Data.Color.WHITE));
	}

}
