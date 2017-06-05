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
		

		double score=0;
		
		for(int i=0;i<1;i++){
			
			this.host.run();
			int blackScore=this.host.getController().getScore(Color.BLACK),
				whiteScore=this.host.getController().getScore(Color.WHITE);
			score+=( blackScore - whiteScore );
			
		}
		
		return score;
		
		
	}
	
	public Training1(){
		LinkedList<DoubleChromosome> list=new LinkedList<DoubleChromosome>();
		
		for(int i=0;i<100;i++){
			list.add( DoubleChromosome.of(-100,100,2330) );
		}
		
		this.gtf=Genotype.of(list );
		this.engine=Engine.builder(this::eval, gtf).build();
		
		this.host=new Host();
		this.player=new Player[2];
		this.spl=new SimplePlayer();
		this.player[0]=this.spl;
		this.player[1]=new SimplePlayer();
		
		host.setPlayer(Color.BLACK,this.player[0]);
		host.setPlayer(Color.WHITE,this.player[1]);
		
	}
	
	public void train(){
		Genotype<DoubleGene> result = engine.stream().limit(1000).collect(EvolutionResult.toBestGenotype());
		this.spl.getNetwork().setWeights(result.getChromosome().as(DoubleChromosome.class).toArray());
	}

	public static void main(String[] args) {
		String filePath="trainingg2.nn";
		
		Training1 t1=new Training1();
		//t1.spl.getNetwork().load(filePath);
		t1.train();
		t1.spl.getNetwork().save(filePath);

	}

}
