package tag.player;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;

public class CNNPlayer extends SimplePlayer {
	
	@Override
	protected NeuralNetwork createNetwork(){
		Kernel k=new Kernel(5,5);
		
		ConvolutionalNetwork.Builder cb=new ConvolutionalNetwork.Builder(new Layer2D.Dimensions(10,10),2);
		cb.withConvolutionLayer(k, 3);
		cb.withConvolutionLayer(k, 3);
		cb.withConvolutionLayer(k, 3);
		cb.withConvolutionLayer(k, 3);
		cb.withFullConnectedLayer(100);
		return cb.createNetwork();
	}

}
