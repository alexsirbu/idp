import org.apache.log4j.*;

import sharix.SharixServerStub;
import sharix.SharixServerStub.RegisterPeer;
import sharix.SharixServerStub.GetPeersResponse;
import sharix.SharixServerStub.UpdatePeerFileList;

/*
 * 
 */
public class Main {
	static Logger logger = Logger.getLogger(Main.class);
	
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
		
		SharixServerStub serverStub = new SharixServerStub();
		RegisterPeer rp = new RegisterPeer();
		rp.setName("Testa");
		rp.setIp("127.0.0.1");
		rp.setPort(6666);
		serverStub.registerPeer(rp);
		
		GetPeersResponse gpr = serverStub.getPeers();
		String peers[] = gpr.get_return();
		
		for(int i = 0; i < peers.length; i++)
			System.out.println(peers[i]);
		
		/*UpdatePeerFileList upfl = new UpdatePeerFileList();
		upfl.setName("Test");
		upfl.setFiles(new String[] );*/
		
		
		
		System.out.println("Done!");
		
		FileAppender appender = new FileAppender();
		appender.setName("A2");
		appender.setFile(args[0]+".log");
		appender.setThreshold(Level.ALL);
	    appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
	    appender.activateOptions();
		logger.addAppender(appender);
		
		Mediator mediator = new Mediator(args[0]);
		
		GUI gui = new GUI(mediator);
		mediator.setGUI(gui);
		
		mediator.registerLocalPeer();
		
		NetworkModule netModule = new NetworkModule(mediator.getLocalPeerIP(), mediator.getLocalPeerPort());
		netModule.setLogger(Main.logger);
		
		NetworkModuleAdapter net = new NetworkModuleAdapter(netModule, mediator);
		net.setLogger(Main.logger);
		
		mediator.setNet(net);
		
		netModule.start();
		netModule.join();
	}
}
