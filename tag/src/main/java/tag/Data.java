package tag;

public class Data {
	private int width,height;
	private Grid[][] board;
	
	
	public Data(int width,int height){
		this.width=width;
		this.height=height;
		this.createBoard();
	}
	
	public void createBoard(){
		this.board=new Grid[this.width][this.height];
		for(int i=0;i<this.board.length;i++){
			this.board[i]=new Grid[this.height];
			for(int j=0;j<this.board.length;j++){
				this.board[i][j]=new Grid();
			}
		}
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeigth(){
		return this.height;
	}
	
	public Grid getGrid(int x,int y){
		try{
			return this.board[x][y];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public void setValue(int x,int y,Color color){
		this.getGrid(x, y).color=color;
	}
	
	
	public static class Point{
		public int x,y;
		public Point(int x,int y){
			this.x=x;
			this.y=y;
		}
		
		@Override
		public boolean equals(Object obj){
			if( ! (obj instanceof Point) )return false;
			Point objP=(Point)obj;
			return objP.x==this.x && objP.y==this.y;
		}
		
		@Override
		public int hashCode(){
			return x*1000+y;
		}
		
	}
	
	public static class Grid{
		public Color color;
		
	}
	
	public static enum Color{
		BLACK,WHITE;
		
		public Color rivalColor(){
			return this.equals(BLACK)?WHITE:BLACK;
		}
	}
	
	public void print(){
		for(int i=0;i<this.width;i++){
			for(int j=0;j<this.height;j++){
				try{
					System.out.print("  "+this.board[i][j].color.ordinal());
				}catch(Exception e){

					System.out.print("  x");
				}
			}
			System.out.println();
		}
		
		System.out.println("==============");
		
	}

}
