import java.util.ArrayList;

/*
 * 
 */
public class Main {
	/*
	 * 
	 */
	protected static String MY_PEER_NAME = "__MYSELF__";
	
	/*
	 * 
	 */
	public static void main(String[] args) {
		Mediator mediator = new Mediator();
		mediator.addPeer(MY_PEER_NAME, new ArrayList<File>());
		
		GUI gui = new GUI(mediator);
		mediator.setGUI(gui);
	}
}
