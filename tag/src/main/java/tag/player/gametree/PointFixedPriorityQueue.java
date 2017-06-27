package tag.player.gametree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import tag.Data;

public class PointFixedPriorityQueue {
	private int fixedLen;
	private List<PointValue> list;
	private Comparator<PointValue> comparator=new Comparator<PointValue>(){

		@Override
		public int compare(PointValue o1, PointValue o2) {
			return Integer.compare(o1.value,o2.value);
		}};
	

	public PointFixedPriorityQueue(int fixedLen){
		this.fixedLen=fixedLen;
		this.list=new LinkedList<PointValue>();
		
	}
	
	
	public void offer(PointValue pv){
		this.list.add(pv);
		this.list.sort(this.comparator);
		while(this.list.size()>this.fixedLen){
			this.list.remove(0);
		}
	}
	
	public int size(){
		return this.list.size();
		
	}
	
	public PointValue poll(){
		try{
			return this.list.remove(this.list.size()-1);
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	
	public static class PointValue{
		public Data.Point point;
		public int value;
		
		public PointValue(Data.Point point,int value){
			this.point=point;
			this.value=value;
		}
		
		public PointValue(){}
		
		
	}
	
	
}
