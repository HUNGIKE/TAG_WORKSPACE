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
	Viewer viewer;
	
	public GUIPlayer(Viewer viewer){
		this.frame=new MainFrame();
		this.viewer=viewer;
	}
	
	private void updadteFrame(){
		int w=this.viewer.getWidth();
		int h=this.viewer.getHeigth();
		
		
		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				Grid grid=this.viewer.getGrid(x, y);
				if(grid!=null){
					this.frame.setColor(x, y, toGUIColor(grid.color) );
				}else{
					this.frame.setColor(x, y,null);
				}
				
			}
			
		}
		this.frame.repaint();
	}
	
	private Color toGUIColor(Data.Color color){
		if(color==null)return null;
		if(color.equals(Data.Color.BLACK)){
			return Color.BLACK;
		}else if(color.equals(Data.Color.WHITE)){
			return Color.WHITE;
		}else{
			return null;
		}
	}
	
	
	@Override
	public Point play(Viewer v) {
		updadteFrame();
		return frame.getPoint();
	}

}
