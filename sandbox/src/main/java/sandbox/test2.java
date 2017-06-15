package sandbox;


import org.neuroph.core.Layer;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.layer.Layer2D.Dimensions;
import org.neuroph.util.NeuronProperties;

public class test2 {

	public static void main(String[] args) {
		Kernel k=new Kernel(3,3);
		
//		ConvolutionalNetwork cn=new ConvolutionalNetwork();
//		InputMapsLayer y1=new InputMapsLayer(new Layer2D.Dimensions(3,3),1);
//		
//		FeatureMapsLayer y2=new ConvolutionalLayer(y1,k,3);
//		
//		Layer y3=new Layer(3,new NeuronProperties());
//		
//		cn.addLayer(y1);
//		cn.addLayer(y2);
//		cn.addLayer(y3);
		
		
		
		ConvolutionalNetwork.Builder cb=new ConvolutionalNetwork.Builder(new Layer2D.Dimensions(3,3),2);
		cb.withConvolutionLayer(k, 5);
		//cb.withConvolutionLayer(k, 3);
		cb.withFullConnectedLayer(100);
		ConvolutionalNetwork cn=cb.createNetwork();
		
		double[] w=new double[cn.getWeights().length];
		System.out.println(cn.getWeights().length);
		
		
		cn.setWeights(w);
		cn.setInput(new double[]{1,2,3,2,1,2,3,2,1,1,2,3,2,1,2,3,2,1,1,2,3,2,1,2,3,2,1,1,2,3,2,1,2,3,2,1});
		cn.calculate();
		
		for(double d:cn.getOutput()){
			System.out.println(d);
		}
		
	}

}
