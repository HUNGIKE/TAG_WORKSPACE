package tag;

public class Data {
	private int width,height;
	private int[][] board;
	
	
	public Data(int width,int height){
		this.width=width;
		this.height=height;
		this.createBoard(width, height);
	}
	
	private void createBoard(int width,int height){
		this.board=new int[width][];
		for(int i=0;i<this.board.length;i++){
			this.board[i]=new int[height];
		}
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeigth(){
		return this.height;
	}
	
	public int getValue(int x,int y){
		return this.board[x][y];
	}
	
	public void setValue(int x,int y,int value){
		this.board[x][y]=value;
	}
	
	
	public static class Point{
		public int x,y;
		public Point(int x,int y){
			this.x=x;
			this.y=y;
		}
		
	}
	
	public void print(){
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				System.out.print("  "+this.board[i][j]);
			}
			System.out.println();
		}
		
		
		
		
	}

}
