package tag;

public class Controller {
	private Data data;
	public Controller(Data data){
		this.data=data;
	}

	
	public boolean isGameTerminated(){
		return false;
	}
	
	
	public void setValue(int x,int y,int value){
		this.data.setValue(x, y, value);
	}
}
