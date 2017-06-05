import org.neuroph.core.NeuralNetwork;

import tag.Data;
import tag.Host;
import tag.player.SimplePlayer;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Host host=new Host();
		
		SimplePlayer ply=new SimplePlayer();
		ply.setNetwork(NeuralNetwork.load("trainingg1.nn"));
		
		host.setPlayer(Data.Color.BLACK,new SimplePlayer());
		host.setPlayer(Data.Color.WHITE,new SimplePlayer());
		
		host.run();

	}

}
