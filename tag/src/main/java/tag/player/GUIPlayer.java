package tag.player;

import java.awt.Color;

import tag.Data;
import tag.Data.Grid;
import tag.Data.Point;
import tag.Host;
import tag.Player;
import tag.Viewer;
import tag.ui.MainFrame;

public class GUIPlayer extends Player {
	MainFrame frame;
	
	public GUIPlayer(){
	}
	
	public void setGUI(MainFrame frame){
		this.frame=frame;
	}
	
	@Override
	public Point play(Viewer v) {
		this.frame.updadteFrame(v);
		
		Point point=frame.getPoint();
		Grid grid=v.getGrid(point.x,point.y);
		if( grid==null || grid.color!=null )return null;
		
		this.frame.setColor(point.x,point.y,this.frame.toGUIColor( v.getColor()) );
		return point;
	}

}
