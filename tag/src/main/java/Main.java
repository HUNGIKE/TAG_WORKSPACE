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

	// Fix #4: 統一棋盤尺寸來源，避免 NN 寫死 10x10 與 Host 15x15 不一致
	private static final int BOARD_W = 15;
	private static final int BOARD_H = 15;

	public static void main(String[] args) throws InterruptedException {
		Host host=new Host(BOARD_W,BOARD_H);
		host.setMaximusRound(100);
		
		MainFrame frame = new MainFrame(BOARD_W,BOARD_H);
		frame.setHost(host);
		frame.setPlayerListItem(0, getPlayerList1(host, frame));
		frame.setPlayerListItem(1, getPlayerList2(host, frame));


	}
	
	
	public static Player[] getPlayerList1(Host host,MainFrame frame){
		Player[] players=new Player[5];
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(frame);
		players[0]=guiPlayer;
		
		GameTreePlayer gtp=new GameTreePlayer();
		players[1]=gtp;
		
		players[2]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer(BOARD_W,BOARD_H);
		try{
			simplePlayer.getNetwork().createFromFile ("training_ANN.nn");
		}catch(Exception e){
			System.err.println(e + " (board "+BOARD_W+"x"+BOARD_H+" vs saved 10x10,需重訓)");
		}
		players[3]=simplePlayer;
		
		CNNPlayer cnnPlayer=new CNNPlayer(BOARD_W,BOARD_H);
		try{
			cnnPlayer.getNetwork().createFromFile ("training_CNN.nn");
		}catch(Exception e){
			System.err.println(e + " (board "+BOARD_W+"x"+BOARD_H+" vs saved 10x10,需重訓)");
		}
		players[4]=cnnPlayer;
		
		return players;
	}
	
	public static Player[] getPlayerList2(Host host,MainFrame frame){
		Player[] players=new Player[5];
		
		
		GameTreePlayer gtp=new GameTreePlayer();
		players[0]=gtp;
		
		CNNPlayer cnnPlayer=new CNNPlayer(BOARD_W,BOARD_H);
		try{
			cnnPlayer.getNetwork().createFromFile("training_CNN.nn");
		}catch(Exception e){
			System.err.println(e + " (board "+BOARD_W+"x"+BOARD_H+" vs saved 10x10,需重訓)");
		}
		players[1]=cnnPlayer;
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(frame);
		players[2]=guiPlayer;
		
		players[3]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer(BOARD_W,BOARD_H);
		try{
			simplePlayer.getNetwork().createFromFile("training_ANN.nn");
		}catch(Exception e){
			System.err.println(e + " (board "+BOARD_W+"x"+BOARD_H+" vs saved 10x10,需重訓)");
		}
		players[4]=simplePlayer;
		
		return players;
	}

}
