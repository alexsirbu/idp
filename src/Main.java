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
		if (args.length < 1)
			throw new Exception("Not enough parameters given.\nParameters required: user_name");
			
		Mediator mediator = new Mediator(args[0]);
		
		GUI gui = new GUI(mediator);
		mediator.setGUI(gui);
		
		mediator.registerLocalPeer();
		
		//GUITest guiTest = new GUITest(mediator);
		//guiTest.run();
	}
}
