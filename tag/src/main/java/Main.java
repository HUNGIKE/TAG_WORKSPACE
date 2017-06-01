import tag.Host;
import tag.player.SimplePlayer;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Host host=new Host();
		
		host.setPlayer(0,new SimplePlayer());
		host.setPlayer(1,new SimplePlayer());
		
		host.run();

	}

}
