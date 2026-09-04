package tag.player;

import tag.Data.Color;
import tag.Data.Point;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import tag.Player;
import tag.Viewer;

public class SimplePlayer extends Player {
	private NeuralNetwork neuralNetwork;
	protected int boardWidth;
	protected int boardHeight;

	public SimplePlayer(){
		this(10,10);
	}

	public SimplePlayer(int w,int h){
		this.boardWidth=w;
		this.boardHeight=h;
		this.neuralNetwork=this.createNetwork();
	}
	
	
	protected NeuralNetwork createNetwork(){
		return createNetwork(this.boardWidth, this.boardHeight);
	}

	protected NeuralNetwork createNetwork(int w,int h){
		return new MultiLayerPerceptron(w*h*2,5,w*h);
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
				
				// if(y==0)System.out.println();
				// System.out.print(" "+((int)(output[x*h+y]*100))/100.0);
		}
		// System.out.println();
		
		return new Point(maxX,maxY);
	}
	
	private double[] getDoubleArray(Viewer v){
		int w=v.getWidth(),h=v.getHeigth();
		
		double[] ret=new double[w*h*2];
		
		for(int i=0;i<w*h;i++){
			int x=i/h , y=i%h;
			Color c=v.getValue(x, y);
			if(c!=null){
				// Fix #3: 原 i*2 應為 w*h+i (雙平面: 我方/敵方)
				ret[c.equals(v.getColor())?i:w*h+i]=1;
			}
		}
		
		return ret;
	}
	
	
	@Override
	public String toString(){
		return "Simple AI Player";
	}

}
