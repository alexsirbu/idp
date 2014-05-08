import javax.swing.SwingWorker;

import java.util.List;
import java.security.SecureRandom;

/**
 * @author cristian
 *
 */
public class GUITestWorker extends SwingWorker<Integer, Integer> {
	/*
	 * 
	 */
	private static int NO_EVENTS = 100;
	/*
	 * 
	 */
	private static int SLEEP_TIME_MILLISEC = 500;
	/*
	 * 
	 */
	private static int NO_EVENT_TYPES = 5;
	/*
	 * 
	 */
	private static final int EVENT_ADD_PEER = 0;
	/*
	 * 
	 */
	private static final int EVENT_DELETE_PEER = 1;
	/*
	 * 
	 */
	private static final int EVENT_UPDATE_PEER = 2;
	/*
	 * 
	 */
	private static final int EVENT_INITIATE_TRANSFER = 3;
	/*
	 * 
	 */
	private static final int EVENT_CONTINUE_TRANSFER = 4;
	
	
	/*
	 * 
	 */
	private GUITest guiTest;
	/*
	 * 
	 */
	private int noEvents;
	/*
	 * 
	 */
	private SecureRandom random;
	
	
	/*
	 * 
	 */
	public GUITestWorker(GUITest guiTest) {
		this.guiTest = guiTest;
		this.noEvents = 0;
		this.random = new SecureRandom();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		guiTest.setUp();
		
		do {
			publish(Math.abs(random.nextInt() % NO_EVENT_TYPES));
			
			Thread.sleep(SLEEP_TIME_MILLISEC);
		}
		while((++noEvents) < NO_EVENTS);
		
		guiTest.tearDown();
		
		return null;
	}
	
	protected void process(List<Integer> eventTypes) {
		for(int i = 0; i < eventTypes.size(); i++) {
			switch(eventTypes.get(i)) {
				case EVENT_ADD_PEER:
					guiTest.addPeer();
					break;
					
				case EVENT_DELETE_PEER:
					guiTest.deletePeer();
					break;
					
				case EVENT_UPDATE_PEER:
					guiTest.updatePeer();
					break;
					
				case EVENT_INITIATE_TRANSFER:
					guiTest.initiateTransfer();
					break;
					
				case EVENT_CONTINUE_TRANSFER:
					guiTest.continueTransfer();
					break;
					
				default:
					break;
			}
		}
	}
}
