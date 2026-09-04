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
		// Fix #2: 先前誤上傳為 1 盤，改為多盤平均以壓低隨機性 (原設計應為 N>1)
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
		p1.getNetwork().createFromFile (filePath_CNN);
		
		SimplePlayer p2=new SimplePlayer();
		p2.getNetwork().createFromFile (filePath_ANN);
		
		RandomPlayer p3=new RandomPlayer();
		
		GameTreePlayer p4=new GameTreePlayer();
		
		t1.setTrainPlayer(p1);
		t1.setRivalPlayer(p4);
		
		
		t1.train();
		
		p1.getNetwork().save(filePath_CNN);
		p2.getNetwork().save(filePath_ANN);

	}

}
