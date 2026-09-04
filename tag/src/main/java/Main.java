import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

	// Fix #4: 參數化棋盤尺寸，訓練時為 10x10 (Host 默認)，15x15 僅為 UI 測試殘留
	private static final int BOARD_W = 10;
	private static final int BOARD_H = 10;

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
		if(!tryLoadWeights(simplePlayer.getNetwork(), "training_ANN.nn")){
			try{ simplePlayer.getNetwork().createFromFile ("training_ANN.nn"); }catch(Exception e){ System.err.println(e); }
		}
		players[3]=simplePlayer;
		
		CNNPlayer cnnPlayer=new CNNPlayer(BOARD_W,BOARD_H);
		if(!tryLoadWeights(cnnPlayer.getNetwork(), "training_CNN.nn")){
			try{ cnnPlayer.getNetwork().createFromFile ("training_CNN.nn"); }catch(Exception e){ System.err.println(e); }
		}
		players[4]=cnnPlayer;
		
		return players;
	}
	
	public static Player[] getPlayerList2(Host host,MainFrame frame){
		Player[] players=new Player[5];
		
		
		GameTreePlayer gtp=new GameTreePlayer();
		players[0]=gtp;
		
		CNNPlayer cnnPlayer=new CNNPlayer(BOARD_W,BOARD_H);
		if(!tryLoadWeights(cnnPlayer.getNetwork(), "training_CNN.nn")){
			try{ cnnPlayer.getNetwork().createFromFile("training_CNN.nn"); }catch(Exception e){ System.err.println(e); }
		}
		players[1]=cnnPlayer;
		
		GUIPlayer guiPlayer=new GUIPlayer();
		guiPlayer.setGUI(frame);
		players[2]=guiPlayer;
		
		players[3]=new RandomPlayer();
		
		SimplePlayer simplePlayer=new SimplePlayer(BOARD_W,BOARD_H);
		if(!tryLoadWeights(simplePlayer.getNetwork(), "training_ANN.nn")){
			try{ simplePlayer.getNetwork().createFromFile("training_ANN.nn"); }catch(Exception e){ System.err.println(e); }
		}
		players[4]=simplePlayer;
		
		return players;
	}

	private static boolean tryLoadWeights(NeuralNetwork n, String basePath){
		String path = basePath + ".weights";
		File f = new File(path);
		if(!f.exists()) return false;
		try(DataInputStream dis = new DataInputStream(new FileInputStream(f))){
			int len = dis.readInt();
			double[] w = new double[len];
			for(int i=0;i<len;i++) w[i]=dis.readDouble();
			if(w.length==n.getWeights().length){ n.setWeights(w); System.out.println("loaded weights "+path); return true; }
			System.err.println("weights length mismatch "+path);
			return false;
		}catch(IOException e){ System.err.println("load weights failed "+path+": "+e); return false; }
	}

}
