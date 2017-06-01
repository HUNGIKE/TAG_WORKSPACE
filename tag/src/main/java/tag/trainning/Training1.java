package tag.trainning;

import java.util.LinkedList;

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import org.neuroph.core.NeuralNetwork;

import tag.Host;
import tag.Player;
import tag.player.SimplePlayer;


public class Training1 {
	private Factory<Genotype<DoubleGene>> gtf;	
	private Engine<DoubleGene, Double> engine;
	
	private Host host;
	private SimplePlayer spl;
	private Player[] player;
	
	public synchronized Double eval(Genotype<DoubleGene> gt){
		NeuralNetwork neuralNetwork=this.spl.getNetwork();
		neuralNetwork.setWeights(gt.getChromosome().as(DoubleChromosome.class).toArray());
		
		double bias=0;
		
		for(int i=0;i<10000;i++){
			int score=0;
			
			this.host.run();
			//gather score 
			
			bias-=score;
			
		}
		
		return bias;
		
		
	}
	
	public Training1(){
		LinkedList<DoubleChromosome> list=new LinkedList<DoubleChromosome>();
		
		for(int i=0;i<300;i++){
			list.add( DoubleChromosome.of(-100,100,12) );
		}
		
		this.gtf=Genotype.of(list );
		this.engine=Engine.builder(this::eval, gtf).build();
		
		this.host=new Host();
		this.player=new Player[2];
		this.spl=new SimplePlayer();
		this.player[0]=this.spl;
		this.player[1]=new SimplePlayer();
		
		host.setPlayer(0,this.player[0]);
		host.setPlayer(1,this.player[1]);
		
	}
	
	public void train(){
		Genotype<DoubleGene> result = engine.stream().limit(1000).collect(EvolutionResult.toBestGenotype());
		this.spl.getNetwork().setWeights(result.getChromosome().as(DoubleChromosome.class).toArray());
	}

	public static void main(String[] args) {
		//To Do
		String filePath="traing1.nn";
		
		Training1 t1=new Training1();
		t1.train();
		
		t1.spl.getNetwork().save(filePath);

	}

}
