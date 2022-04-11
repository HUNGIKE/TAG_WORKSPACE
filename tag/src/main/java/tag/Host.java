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
	MainFrame mainframe;
	
	
	Data.Color[] playerColor=new Data.Color[]{Data.Color.BLACK,Data.Color.WHITE};
	Player[] player=new Player[2];
	
	
	
	public Host() {
		this(10,10);
	}
	
	public Host(int w,int h){
		this.data=new Data(w,h);
		this.viewer=new Viewer(this.data);
		this.controller=new Controller(data);
	}
	
	public void setGUI(MainFrame mainframe){
		this.mainframe=mainframe;
		this.mainframe.setHost(this);
	}
	
	public MainFrame getGUI(){
		return this.mainframe;
	}
	
	public Viewer getViewer(){
		return this.viewer;
	}
	
	public void setPlayerListItem(int idx,Player[] players){
		this.mainframe.setPlayerListItem(idx, players);
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
	
	private int maximusRound=0;
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
	
	public void resetGame() {
		this.controller.setGameTerminated(true);
	}

	
	public void run(){
		
		
		this.round=0;
		this.controller.reset();
		
		int p=0;
		
		while (!this.controller.isGameTerminated()) {
			synchronized (this) {

				if (this.mainframe != null) {
					this.mainframe.setRoundString(this.round, this.maximusRound);
				}

				Player ply = this.player[p];
				this.viewer.setColor(playerColor[p]);

				try {
					Data.Point retP = ply.play(this.viewer);
					if (retP == null)
						throw new OperationProhibitedException();

					List<Data.Point> closeSet = this.controller.setValue(retP.x, retP.y, this.viewer.getColor());
					if (this.mainframe != null)
						this.mainframe.setColor(retP.x, retP.y, this.mainframe.toGUIColor(this.viewer.getColor()));

					if (!closeSet.isEmpty()) { this.controller.clean(closeSet); }
				} catch (OutOfBoardException | OperationProhibitedException e) {
					continue;
				} finally {
					if (this.mainframe != null) {
						this.mainframe.updadteFrame(this.viewer);
					}
				}

				p = (p + 1) % 2;
				if (p == 0) {
					this.round++;
				}

				if (this.maximusRound > 0 && this.round >= this.maximusRound) {
					this.controller.setGameTerminated(true);
				}
			}
		}
		
		int b_count=this.controller.getScore(Data.Color.BLACK);
		int w_count=this.controller.getScore(Data.Color.WHITE);
		
		JOptionPane.showMessageDialog(this.mainframe,"score - BLACK: "+b_count+" v.s WHITE: "+w_count);
		
		this.mainframe.enableResetButton();
		
		// System.out.print("Score: BLACK "+this.controller.getScore(Data.Color.BLACK));
		// System.out.println(",WHITE "+this.controller.getScore(Data.Color.WHITE));
	}

}
