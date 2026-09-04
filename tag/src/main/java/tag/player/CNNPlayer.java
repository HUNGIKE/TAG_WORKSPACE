package tag.player;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;

public class CNNPlayer extends SimplePlayer {

	public CNNPlayer(){
		super();
	}

	public CNNPlayer(int w,int h){
		super(w,h);
	}
	
	@Override
	protected NeuralNetwork createNetwork(){
		// Fix #4: 使用父類 boardWidth/boardHeight，預設 10x10 保持相容
		return createNetwork(this.boardWidth, this.boardHeight);
	}

	protected NeuralNetwork createNetwork(int w,int h){
		Kernel k=new Kernel(3,3);
		// Deeper: 3 conv layers 8/16/32 maps (2.92 Builder無activation參數，預設Sigmoid，仍加深)
		ConvolutionalNetwork.Builder cb=new ConvolutionalNetwork.Builder(new Layer2D.Dimensions(w,h),2);
		cb.withConvolutionLayer(k, 8);
		cb.withConvolutionLayer(k, 16);
		cb.withConvolutionLayer(k, 32);
		cb.withFullConnectedLayer(w*h);
		return cb.createNetwork();
	}
	
	
	
	@Override
	public String toString(){
		return "CNN AI Player";
	}

}
