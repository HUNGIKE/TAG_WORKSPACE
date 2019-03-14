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
		
		Host host=new Host(15,15);
		host.setGUI(new MainFrame(15,15));
		
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
		try{
			simplePlayer.getNetwork().createFromFile ("training_ANN.nn");
		}catch(Exception e){
			System.err.println(e);
		}
		players[3]=simplePlayer;
		
		CNNPlayer cnnPlayer=new CNNPlayer();
		try{
			cnnPlayer.getNetwork().createFromFile ("training_CNN.nn");
		}catch(Exception e){
			System.err.println(e);
		}
		players[4]=cnnPlayer;
		
		return players;
	}
	
	public static Player[] getPlayerList2(Host host){
		Player[] players=new Player[5];
		
		
		GameTreePlayer gtp=new GameTreePlayer();
		players[0]=gtp;
		
		CNNPlayer cnnPlayer=new CNNPlayer();
		try{
			cnnPlayer.getNetwork().createFromFile("training_CNN.nn");
		}catch(Exception e){
			System.err.println(e);
		}
		players[1]=cnnPlayer;
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(host.getGUI());
		players[2]=guiPlayer;
		
		players[3]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer();
		try{
			simplePlayer.getNetwork().createFromFile("training_ANN.nn");
		}catch(Exception e){
			System.err.println(e);
		}
		players[4]=simplePlayer;
		
		return players;
	}

}
