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
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;

public class test {
	
	public NeuralNetwork neuralNetwork = new MultiLayerPerceptron(2,3,1);
	
	
	private double estimatedFunc(double x1,double x2){
		// return x1+x2;
		return Math.pow(x1,3)+Math.pow(x2,2)+x1;
	}
	
	private synchronized Double eval(Genotype<DoubleGene> gt) {
			
			
			
			neuralNetwork.setWeights(gt.getChromosome().as(DoubleChromosome.class).toArray());
			
			double bias=0;
			
			for(int i=0;i<10000;i++){
				double x1=(int)(i/100)+1,x2=i%100+1;
				// double x1=Math.random()*100,x2=Math.random()*100;
				
				neuralNetwork.setInput( intputNormailize(x1,x2) );
				neuralNetwork.calculate();
				double output=outputNormalize( neuralNetwork.getOutput()[0] );
				
				// System.out.println("output[0]="+(output[0]*10000d)+",est fun="+estimatedFunc(x1,x2));
				bias+=Math.abs( output - estimatedFunc(x1,x2) );
				
			}
			
			
			// System.out.println(bias/10000);
			return -1d*bias;
		
		
		
		
        //return (int) gt.getChromosome().as(DoubleChromosome.class).byteValue(2);
    }
	
	public DataSet getTrainingSet(){
		DataSet trainingSet = new DataSet(2, 1);

		
		for(int i=0;i<2000000;i++){
			//double x1=(int)(i/100),x2=i%100;
			double x1=Math.random()*100,x2=Math.random()*100;
			double y=estimatedFunc(x1,x2)/1010100;
			trainingSet.addRow(new DataSetRow( intputNormailize( x1,x2 ),new double[]{ y }));
		
		}
		
		
		
		
		return trainingSet;
	}
	
	
	public void taringByGA(){
		
		LinkedList<DoubleChromosome> list=new LinkedList<DoubleChromosome>();
		
		for(int i=0;i<300;i++){
			
			list.add( DoubleChromosome.of(-100,100,this.neuralNetwork.getWeights().length) );
			
		}
		
		
		Factory<Genotype<DoubleGene>> gtf = Genotype.of(list );		
		Engine<DoubleGene, Double> engine = Engine.builder(this::eval, gtf).build();		
		Genotype<DoubleGene> result = engine.stream().limit(1000).collect(EvolutionResult.toBestGenotype());
	 
	    System.out.println("Result:\n" + result);

		this.neuralNetwork.setWeights(result.getChromosome().as(DoubleChromosome.class).toArray());
		

		
	}
	
	private double[] intputNormailize(double ... input){
		input[0]/=100;
		input[1]/=100;
		
		return input;
		
	}
	
	private double outputNormalize(double output){
		return output*1010100;
	}
	
	
	public void taringByBP(){
		this.neuralNetwork.learn(getTrainingSet());
	}
	
	public void estimate(){

		
		
	    for(int i=0;i<=10000;i++){
			double x1=Math.random()*100,x2=Math.random()*100;
	    	double y1=this.estimatedFunc(x1,x2);
	    	
	    	neuralNetwork.setInput( intputNormailize(x1,x2) );
	    	neuralNetwork.calculate();
	    	
	    	double output=this.outputNormalize( neuralNetwork.getOutput()[0] );
	    	
	    	double bias=output-y1;
	    	
	    	System.out.println("x1="+x1+" , x2="+x2);
	    	System.out.println("output="+ output +",eval func="+y1);
	    	System.out.println("bias="+bias);
	    	
	    	
	    	System.out.println();
	    	System.out.println();
	    }
		
		
	}
	
	

	public static void main(String[] args) {
		test test=new test();
		
		test.taringByGA();
		
		// test.neuralNetwork.randomizeWeights();
		// test.taringByBP();
		
		test.estimate();
		
		
	}

}
