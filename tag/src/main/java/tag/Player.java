package tag;

import java.util.List;

public abstract class Player {
	
	
	public abstract Data.Point play(Viewer v);
	
	public void update(Viewer v,List<Data.Point> closeSet) {}
	public void update(Viewer v) {}

}
