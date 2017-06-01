package sandbox;

import java.util.LinkedList;

import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;
import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;

public class test {
	
	private static NeuralNetwork neuralNetwork = new MultiLayerPerceptron(2,2,2);
	
	
	private static double estimatedFunc(double x1,double x2){
		return ( Math.pow(x1,2)+Math.pow(x2,2)*x1/100)/2;
	}
	
	private static synchronized Double eval(Genotype<DoubleGene> gt) {
			
			
			
			neuralNetwork.setWeights(gt.getChromosome().as(DoubleChromosome.class).toArray());
			
			double bias=0;
			
			for(int i=0;i<10000;i++){
				double x1=(int)(i/100),x2=i%100;
				
				neuralNetwork.setInput(x1/100,x2/100);
				neuralNetwork.calculate();
				double[] output=neuralNetwork.getOutput();
				
				
				bias+=( Math.abs(x1-output[0]*10000)+Math.abs(x2-output[1]*10000) );
				
			}
			
			return -1d*bias;
		
		
		
		
        //return (int) gt.getChromosome().as(DoubleChromosome.class).byteValue(2);
    }
	

	public static void main(String[] args) {
		
		
		LinkedList<DoubleChromosome> list=new LinkedList<DoubleChromosome>();
		
		for(int i=0;i<300;i++){
			
			list.add( DoubleChromosome.of(-100,100,12) );
			
		}
		
		
		
		Factory<Genotype<DoubleGene>> gtf = Genotype.of(list );		
		Engine<DoubleGene, Double> engine = Engine.builder(test::eval, gtf).build();		
		Genotype<DoubleGene> result = engine.stream().limit(1000).collect(EvolutionResult.toBestGenotype());
	 
	    System.out.println("Hello World:\n" + result);

	    

		neuralNetwork.setWeights(result.getChromosome().as(DoubleChromosome.class).toArray());
	    for(int j=0;j<10;j++){

			double x1=Math.random()*100,x2=Math.random()*100;
	    	double y1=estimatedFunc(x1,x2);
	    	
	    	neuralNetwork.setInput(x1/100,x2/100);
	    	neuralNetwork.calculate();
	    	
	    	double[] output=neuralNetwork.getOutput();
	    	
	    	System.out.println("x1="+x1+" , x2="+x2);
	    	System.out.println("y1="+output[0]*10000+",y2="+output[1]*10000);
	    	System.out.println();
	    }
		
		
	}

}
