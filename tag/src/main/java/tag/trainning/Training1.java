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


public class Training1 {
	private Factory<Genotype<DoubleGene>> gtf;	
	private Engine<DoubleGene, Double> engine;
	
	private Host host;
	private SimplePlayer trainPlayer;
	private Player rivalPlayer;
	
	public void setTrainPlayer(SimplePlayer trainPlayer){
		this.trainPlayer=trainPlayer;
	}
	
	public void setRivalPlayer(Player rivalPlayer){
		this.rivalPlayer=rivalPlayer;
	}
	
	public synchronized Double eval(Genotype<DoubleGene> gt){
		NeuralNetwork neuralNetwork=this.trainPlayer.getNetwork();
		neuralNetwork.setWeights(gt.getChromosome().as(DoubleChromosome.class).toArray());

		double score=0;
		
		for(int i=0;i<30;i++){
			this.host.run();
			int blackScore=this.host.getController().getScore(Color.BLACK),
				whiteScore=this.host.getController().getScore(Color.WHITE);
			score+=( blackScore - whiteScore );
			
		}
		System.out.println("total:"+score);
		return score;
	}
	
	public Training1(){
		this.trainPlayer=new CNNPlayer();
		int weightLen=this.trainPlayer.getNetwork().getWeights().length;
		
		LinkedList<DoubleChromosome> list=new LinkedList<DoubleChromosome>();
		
		for(int i=0;i<10;i++){
			list.add( DoubleChromosome.of(-1000,1000,weightLen) );
		}
		
		this.gtf=Genotype.of(list );
		this.engine=Engine.builder(this::eval, this.gtf).build();
		

		
	}
	
	public void train(){
		this.host=new Host();
		this.host.setMaximusRound(30);
		
		this.host.setPlayer(Color.BLACK,this.trainPlayer);
		this.host.setPlayer(Color.WHITE,this.rivalPlayer);
		
		
		Genotype<DoubleGene> result = engine.stream().limit(100).collect(EvolutionResult.toBestGenotype());
		this.trainPlayer.getNetwork().setWeights(result.getChromosome().as(DoubleChromosome.class).toArray());
	}

	public static void main(String[] args) {
		String filePath="training1.nn";
		
		Training1 t1=new Training1();
		t1.setRivalPlayer(new RandomPlayer());
		t1.train();
		
		
		t1.trainPlayer.getNetwork().save(filePath);

	}

}
