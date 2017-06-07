package tag.player;

import tag.Data;
import tag.Data.Point;
import tag.Player;
import tag.Viewer;

public class RandomPlayer extends Player {

	@Override
	public Point play(Viewer v) {
		
		while(true){
			int x=(int)(Math.random()*v.getWidth());
			int y=(int)(Math.random()*v.getHeigth());
			if( v.getGrid(x, y).color==null ){
				return new Data.Point(x, y);
				
			}
			
		}
	}

}
