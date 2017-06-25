package tag.player;

import java.util.Random;

import tag.Data;
import tag.Data.Point;
import tag.Player;
import tag.Viewer;

public class RandomPlayer extends Player {
	private Random random=new Random(0);
	private int seed;
	
	public void setSeed(int seed){
		this.seed=seed;
		this.random.setSeed(seed);
	}
	
	@Override
	public Point play(Viewer v) {
		this.setSeed(this.seed= (this.seed+1)%100000  );
				
		while(true){
			int x=(int)(this.random.nextDouble()*v.getWidth());
			int y=(int)(this.random.nextDouble()*v.getHeigth());
			if( v.getGrid(x, y).color==null ){
				return new Data.Point(x, y);
			}
			
		}
	}
	
	@Override
	public String toString(){
		return "Random Player";
	}

}
