import java.util.ArrayList;

/*
 * 
 */
public class Main {
	/*
	 * 
	 */
	protected static String LOCAL_PEER_NAME = "__MYSELF__";
	
	
	/*
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Mediator mediator = new Mediator(LOCAL_PEER_NAME);
		
		GUI gui = new GUI(mediator);
		mediator.setGUI(gui);
		ArrayList<File> files = new ArrayList<File>();
		files.add(new File(new String("aha"), 15));
		mediator.addLocalPeer(LOCAL_PEER_NAME, files);
	}
}
