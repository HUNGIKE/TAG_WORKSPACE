package tag.player.gametree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import tag.Data;
import tag.Data.Point;

public class PointFixedPriorityQueue {
	private int fixedLen;
	private List<PointValue> list;
	private Comparator<PointValue> comparator=new Comparator<PointValue>(){

		@Override
		public int compare(PointValue o1, PointValue o2) {
			return Integer.compare(o1.priorityValue,o2.priorityValue);
		}};
	

	public PointFixedPriorityQueue(int fixedLen){
		this.fixedLen=fixedLen;
		this.list=new LinkedList<PointValue>();
		
	}
	
	
	public void offer(Point point,int priorityValue){
		this.list.add(new PointValue(point,priorityValue));
		this.list.sort(this.comparator);
		while(this.list.size()>this.fixedLen){
			this.list.remove(0);
		}
	}
	
	public int size(){
		return this.list.size();
		
	}
	
	public Point poll(){
		try{
			return this.list.remove(this.list.size()-1).point;
		}catch(IndexOutOfBoundsException | NullPointerException e){
			return null;
		}
	}
	
	
	public static class PointValue{
		public Data.Point point;
		public int priorityValue;
		
		public PointValue(Data.Point point,int priorityValue){
			this.point=point;
			this.priorityValue=priorityValue;
		}
		
		public PointValue(){}
		
		
	}
	
	
}
