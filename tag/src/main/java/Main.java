import org.neuroph.core.NeuralNetwork;

import tag.Data;
import tag.Host;
import tag.Player;
import tag.player.CNNPlayer;
import tag.player.GUIPlayer;
import tag.player.RandomPlayer;
import tag.player.SimplePlayer;
import tag.ui.MainFrame;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Host host=new Host();
		host.setGUI(new MainFrame());
		
		host.setPlayerListItem(0, getPlayerList1(host));
		host.setPlayerListItem(1, getPlayerList2(host));


	}
	
	
	public static Player[] getPlayerList1(Host host){
		Player[] players=new Player[4];
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(host.getGUI());
		players[0]=guiPlayer;
		
		players[1]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer();
		simplePlayer.getNetwork().load("training1.nn");
		players[2]=simplePlayer;
		
		CNNPlayer cnnPlayer=new CNNPlayer();
		cnnPlayer.getNetwork().load("training1.nn");
		players[3]=cnnPlayer;
		
		return players;
	}
	
	public static Player[] getPlayerList2(Host host){
		Player[] players=new Player[4];
		
		CNNPlayer cnnPlayer=new CNNPlayer();
		cnnPlayer.getNetwork().load("training1.nn");
		players[0]=cnnPlayer;
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(host.getGUI());
		players[1]=guiPlayer;
		
		players[2]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer();
		simplePlayer.getNetwork().load("training1.nn");
		players[3]=simplePlayer;
		

		
		return players;
	}

}
