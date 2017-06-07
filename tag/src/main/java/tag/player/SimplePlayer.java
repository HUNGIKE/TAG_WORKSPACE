package tag.player;

import tag.Data.Color;
import tag.Data.Point;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;

import tag.Player;
import tag.Viewer;

public class SimplePlayer extends Player {
	private static NeuralNetwork neuralNetwork;
	public SimplePlayer(){
		this.neuralNetwork=new MultiLayerPerceptron(10*10,2,2,2,2,2,10*10);
	}
	
	
	public void setNetwork(NeuralNetwork neuralNetwork){
		this.neuralNetwork=neuralNetwork;
	}
	
	public NeuralNetwork getNetwork(){
		return this.neuralNetwork;
	}
	

	@Override
	public Point play(Viewer v) {
		this.neuralNetwork.setInput(this.getDoubleArray(v));
		neuralNetwork.calculate();
		double[] output=neuralNetwork.getOutput();
		
		return getPointFromOutput(v,output);
	}
	
	private Point getPointFromOutput(Viewer v,double[] output){

		int w=v.getWidth(),h=v.getHeigth();
		double maxV=Integer.MIN_VALUE;
		int maxX=-1,maxY=-1;
		
		for(int i=0;i<output.length;i++){
			int x=i/h , y=i%h;
			
			if( v.getValue(x, y)==null && output[x*h+y]>maxV ){
				maxV=output[x*h+y];
				maxX=x;maxY=y;
			}
		}
		
		
		return new Point(maxX,maxY);
	}
	
	private double[] getDoubleArray(Viewer v){
		int w=v.getWidth(),h=v.getHeigth();
		
		double[] ret=new double[w*h];
		
		for(int i=0;i<w*h;i++){
			int x=i/h , y=i%h;
				Color c=v.getValue(x, y);
				if(c!=null){
					ret[i]=c.ordinal();
				}else{
					ret[i]=-1;
				}
		}
		
		return ret;
	}

}
