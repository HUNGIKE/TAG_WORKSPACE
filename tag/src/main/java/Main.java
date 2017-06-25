import org.neuroph.core.NeuralNetwork;

import tag.Data;
import tag.Host;
import tag.Player;
import tag.player.CNNPlayer;
import tag.player.GUIPlayer;
import tag.player.RandomPlayer;
import tag.player.SimplePlayer;
import tag.player.gametree.GameTreePlayer;
import tag.ui.MainFrame;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Host host=new Host();
		host.setGUI(new MainFrame());
		
		host.setPlayerListItem(0, getPlayerList1(host));
		host.setPlayerListItem(1, getPlayerList2(host));


	}
	
	
	public static Player[] getPlayerList1(Host host){
		Player[] players=new Player[5];
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(host.getGUI());
		players[0]=guiPlayer;
		
		GameTreePlayer gtp=new GameTreePlayer();
		players[1]=gtp;
		
		players[2]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer();
		simplePlayer.getNetwork().load("training_ANN.nn");
		players[3]=simplePlayer;
		
		CNNPlayer cnnPlayer=new CNNPlayer();
		cnnPlayer.getNetwork().load("training_CNN.nn");
		players[4]=cnnPlayer;
		
		return players;
	}
	
	public static Player[] getPlayerList2(Host host){
		Player[] players=new Player[5];
		
		
		GameTreePlayer gtp=new GameTreePlayer();
		players[0]=gtp;
		
		CNNPlayer cnnPlayer=new CNNPlayer();
		cnnPlayer.getNetwork().load("training_CNN.nn");
		players[1]=cnnPlayer;
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(host.getGUI());
		players[2]=guiPlayer;
		
		players[3]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer();
		simplePlayer.getNetwork().load("training_ANN.nn");
		players[4]=simplePlayer;
		
		return players;
	}

}
