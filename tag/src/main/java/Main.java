import org.neuroph.core.NeuralNetwork;

import tag.Data;
import tag.Host;
import tag.Player;
import tag.player.GUIPlayer;
import tag.player.RandomPlayer;
import tag.player.SimplePlayer;
import tag.ui.MainFrame;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Host host=new Host();
		host.setGUI(new MainFrame());
		host.setMaximusRound(10000);

		GUIPlayer p1=new GUIPlayer();
		p1.setGUI(host.getGUI());		
		
		GUIPlayer p2=new GUIPlayer();
		p2.setGUI(host.getGUI());
		
		SimplePlayer p3=new SimplePlayer();
		p3.setNetwork(NeuralNetwork.load("training1.nn"));
		
		Player p4=new RandomPlayer();
		
		host.setPlayer(Data.Color.BLACK,p1);
		host.setPlayer(Data.Color.WHITE,p4);
		
		host.run();

	}

}
