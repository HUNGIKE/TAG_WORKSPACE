import org.neuroph.core.NeuralNetwork;

import tag.Data;
import tag.Host;
import tag.player.GUIPlayer;
import tag.player.SimplePlayer;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Host host=new Host();
		
		SimplePlayer p1=new SimplePlayer();
		p1.setNetwork(NeuralNetwork.load("trainingg1.nn"));
		
		GUIPlayer p2=new GUIPlayer(host.getViewer());
		
		host.setPlayer(Data.Color.BLACK,p1);
		host.setPlayer(Data.Color.WHITE,p2);
		
		host.run();

	}

}
