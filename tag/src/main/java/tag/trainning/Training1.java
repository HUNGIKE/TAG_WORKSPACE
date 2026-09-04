package tag.trainning;

import java.util.LinkedList;

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import org.neuroph.core.NeuralNetwork;

import tag.Data.Color;
import tag.Host;
import tag.Player;
import tag.player.CNNPlayer;
import tag.player.RandomPlayer;
import tag.player.SimplePlayer;
import tag.player.gametree.GameTreePlayer;
import tag.ui.MainFrame;


public class Training1 {
	private Factory<Genotype<DoubleGene>> gtf;	
	private Engine<DoubleGene, Double> engine;
	
	private Host host;
	private SimplePlayer trainPlayer;
	private Player rivalPlayer;
	
	public void setTrainPlayer(SimplePlayer trainPlayer){
		this.trainPlayer=trainPlayer;
		
		int weightLen=this.trainPlayer.getNetwork().getWeights().length;
		
		// Fix #1: 單一個體 = 1 條染色體 (原 30 條是誤把 population 當 chromosome)
		this.gtf=Genotype.of(DoubleChromosome.of(-1000,1000,weightLen));
		this.engine=Engine.builder(this::eval, this.gtf)
			.populationSize(30)
			.build();
	}
	
	public void setRivalPlayer(Player rivalPlayer){
		this.rivalPlayer=rivalPlayer;
	}
	
	public synchronized Double eval(Genotype<DoubleGene> gt){
		NeuralNetwork neuralNetwork=this.trainPlayer.getNetwork();
		neuralNetwork.setWeights(gt.getChromosome().as(DoubleChromosome.class).toArray());

		double score=0;
		// Fix #2: 測試時誤上傳為 1 盤，訓練時原意為多盤平均以壓低隨機性
		int evalGames = 5;
		for(int i=0;i<evalGames;i++){
			this.host.run();
			int blackScore=this.host.getController().getScore(Color.BLACK),
				whiteScore=this.host.getController().getScore(Color.WHITE);
			score+=( blackScore - whiteScore );
			
		}
		System.out.println("total:"+score+" avg:"+(score/evalGames));
		return score;
	}
	
	public Training1(){
		this.host=new Host();
		//MainFrame mainframe=new MainFrame();
		//this.host.setGUI(mainframe);
		this.host.setMaximusRound(10);
	}
	
	public void train(){
		
		this.host.setPlayer(Color.BLACK,this.trainPlayer);
		this.host.setPlayer(Color.WHITE,this.rivalPlayer);
		
		
		Genotype<DoubleGene> result = engine.stream().limit(10).collect(EvolutionResult.toBestGenotype());
		this.trainPlayer.getNetwork().setWeights(result.getChromosome().as(DoubleChromosome.class).toArray());
	}

	public static void main(String[] args) {

		String filePath_ANN="training_ANN.nn";
		String filePath_CNN="training_CNN.nn";
		
		Training1 t1=new Training1();
		
		CNNPlayer p1=new CNNPlayer();
		try{ p1.getNetwork().createFromFile (filePath_CNN); }catch(Exception e){ System.err.println("no pretrain CNN, use random: "+e.getMessage()); }
		
		SimplePlayer p2=new SimplePlayer();
		try{ p2.getNetwork().createFromFile (filePath_ANN); }catch(Exception e){ System.err.println("no pretrain ANN, use random: "+e.getMessage()); }
		
		RandomPlayer p3=new RandomPlayer();
		
		// 折衷: 原 depth 8/width 11 過慢，訓練用輕量 GameTree 仍具強度
		GameTreePlayer p4=new GameTreePlayer(3,5);
		
		t1.setTrainPlayer(p1);
		t1.setRivalPlayer(p4);
		
		
		t1.train();
		
		try{ p1.getNetwork().save(filePath_CNN); System.out.println("saved "+filePath_CNN); }catch(Throwable e){ System.err.println("save CNN failed (Java21 Neuroph bug): "+e); e.printStackTrace(); }
		try{ p2.getNetwork().save(filePath_ANN); System.out.println("saved "+filePath_ANN); }catch(Throwable e){ System.err.println("save ANN failed: "+e); }

	}

}
