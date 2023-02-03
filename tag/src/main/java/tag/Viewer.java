package tag;

import tag.Data.Color;
import tag.Data.Grid;

public class Viewer {
	private Data data;
	private Color color;
	private GameInfo info;
	
	
	public static class GameInfo{
		public int round;
		public int maximusRound;
	}
	
	public void setGameInfo(GameInfo info) {
		this.info = info;
	}
	
	public GameInfo getGameInfo() {
		return this.info;
	}
	
	public void setColor(Color color){
		this.color=color;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public Viewer(Data data){
		this.data=data;
	}
	
	public void setData(Data data){
		this.data=data;
	}
	
	public int getWidth(){
		return this.data.getWidth();
	}
	
	public int getHeigth(){
		return this.data.getHeigth();
	}
	
	public Grid getGrid(int x,int y){
		return this.data.getGrid(x, y);
	}
	
	public Color getValue(int x,int y){
		return this.data.getGrid(x, y).color;
	}
}
