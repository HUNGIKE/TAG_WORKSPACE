package tag;

public class Viewer {
	private Data data;
	
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
	
	
	public int getValue(int x,int y){
		return this.data.getValue(x, y);
	}
}
