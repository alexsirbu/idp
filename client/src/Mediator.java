import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import org.apache.axis2.AxisFault;

import sharix.SharixServerStub;
import sharix.SharixServerStub.*;

/*
 * 
 */
public class Mediator {
	/*
	 * 
	 */
	public GUI gui;
	/*
	 * 
	 */
	private NetworkModuleAdapter net;
	/*
	 * 
	 */
	public ArrayList<Peer> peers;
	/*
	 * 
	 */
	private ArrayList<Transfer> transfers;
	/*
	 * 
	 */
	private Peer localPeer;
	/*
	 * 
	 */
	private String localPeerRealName;
	
	/*
	 * 
	 */
	private String localPeerIP;
	
	/*
	 * 
	 */
	private int localPeerPort;
	
	/*
	 * Temporary, for stage 2 only;
	 */
	private String usersFolder;
	
	/*
	 * 
	 */
	public Mediator(String localPeerName) {
		peers = new ArrayList<Peer>();
		transfers = new ArrayList<Transfer>();
		localPeerRealName = localPeerName;
	}
	
	/*
	 * 
	 */
	public void registerLocalPeer() throws IOException{
		
		FileOperations fo = new FileOperationsJavaNIO(localPeerRealName+".txt");
		
		String configFile = new String(fo.readAll());
		
		String[] configs = configFile.split("\n");
		
		for (int i=0; i<configs.length; i++)
		{
			String[] configMembers = configs[i].split(": ");
			if (configMembers[0].equals("user-ip"))
				this.localPeerIP=configMembers[1];
			else if (configMembers[0].equals("user-port"))
				this.localPeerPort=Integer.parseInt(configMembers[1]);
			else if (configMembers[0].equals("users-folder"))
				this.usersFolder=configMembers[1];
		}
		
		ArrayList<File> files = new ArrayList<File>();
		java.io.File folder = new java.io.File(localPeerRealName);
		ArrayList<String> fileNames = new ArrayList<String>();
		for(java.io.File file : folder.listFiles())
		{
			files.add(new File(file.getName(), (int)file.length()));
			fileNames.add(file.getName());
		}
			
		addLocalPeer(this.getLocalPeerRealName(), files);
		
		final Mediator mediator = this;
		final String[] localFileNames = fileNames.toArray(new String[0]);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("register");
					SharixServerStub serverStub = new SharixServerStub();
					RegisterPeer rp = new RegisterPeer();
					rp.setName(mediator.getLocalPeerRealName());
					rp.setIp(mediator.getLocalPeerIP());
					rp.setPort(mediator.getLocalPeerPort());
					serverStub.registerPeer(rp);
					UpdatePeerFileList upfl = new UpdatePeerFileList();
					upfl.setName(mediator.getLocalPeerRealName());
					upfl.setFiles(localFileNames);
					serverStub.updatePeerFileList(upfl);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// TODO Stage 3: here or in addLocalPeer, send info to server with my files
		getPeers();		
	}
	
	/*
	 * this will change in stage 3
	 */
	public void getPeers() throws IOException
	{
		final Mediator mediator = this;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					SharixServerStub serverStub = new SharixServerStub();
					GetPeersResponse resp = serverStub.getPeers();
					String[] peersInfo = resp.get_return();
					mediator.peers.clear();
					mediator.gui.peersModel.clear();
					
					for (int i=0; i<peersInfo.length/3; i++)
					{
						String name = peersInfo[i*3];
						String ip = peersInfo[i*3+1];
						int port = Integer.parseInt(peersInfo[i*3+2]);
						
						GetPeerFiles gpf = new GetPeerFiles();
						gpf.setName(name);
						GetPeerFilesResponse gpfr = serverStub.getPeerFiles(gpf);
						String[] filenames = gpfr.get_return();
						ArrayList<File> files = new ArrayList<File>();
						for(int j=0; j<filenames.length; j++)
							files.add(new File(filenames[j], 0));
						
						mediator.addPeer(name, ip, port, files);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * 
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	/*
	 * 
	 */
	public void setNet(NetworkModuleAdapter net)
	{
		this.net = net;
	}
	
	/*
	 * 
	 */
	public Peer getLocalPeer() {
		return localPeer;
	}
	
	/*
	 * 
	 */
	public String getLocalPeerRealName() {
		return localPeerRealName;
	}
	
	/*
	 * 
	 */
	public String getLocalPeerIP(){
		return localPeerIP;
	}
	
	/*
	 * 
	 */
	public int getLocalPeerPort(){
		return localPeerPort;
	}
	
	/*
	 * 
	 */
	public Peer getPeer(String peerName) {
		for(int i = 0; i < peers.size(); i++)
			if (peers.get(i).getName().equals(peerName))
				return peers.get(i);
	
		return null;
	}
	
	/*
	 * 
	 */
	public void addPeer(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = new Peer(peerName, sharedFiles);
		
		peers.add(peer);
		gui.addPeer(peer);
	}
	
	/*
	 * 
	 */
	public void addPeer(String peerName, String IP, int port, ArrayList<File> sharedFiles) {
		Peer peer = new Peer(peerName, IP, port, sharedFiles);
		
		peers.add(peer);
		gui.addPeer(peer);
	}
	
	/*
	 * 
	 */
	public void addLocalPeer(String localPeerName, ArrayList<File> sharedFiles) {
		assert localPeer == null;
		
		addPeer(localPeerName, sharedFiles);
		localPeer = getPeer(localPeerName);
	}
	
	/*
	 * 
	 */
	public void deletePeer(String peerName) {
		Peer peer = getPeer(peerName);
		if(peer == null)
			return;
		
		gui.deletePeer(peer);
		peers.remove(peer);
	}
	
	/*
	 * 
	 */
	public void updatePeerSharedFiles(String peerName, ArrayList<File> sharedFiles) {
		Peer peer = getPeer(peerName);
		if(peer == null)
			return;
		
		peer.setSharedFiles(sharedFiles);
		gui.updatePeerFiles(peer);
	}
	
	/*
	 * 
	 */
	public void addTransferOutgoingRequest(Transfer transfer) {
		transfers.add(transfer);
		this.net.initOutgoingTransferRequest(transfer);
	}
	
	/*
	 * 
	 */
	public void addTransferIncomingRequest(Transfer transfer) {
		transfers.add(transfer);
		gui.addTransferIncomingRequest(transfer);
	}
	
	/*
	 * 
	 */
	public void updateTransferProgress(String sendingPeerName, String recevingPeerName, String fileName, int chunkSize) {
		Transfer transfer = null;
		
		for(int i = 0; i < transfers.size(); i++) {
			if(transfers.get(i).getFile().getName().equals(fileName)
					&& transfers.get(i).getSendingPeer().getName().equals(sendingPeerName)
					&& transfers.get(i).getReceivingPeer().getName().equals(recevingPeerName))
				transfer = transfers.get(i);
		}
		
		if(transfer == null)
			return;
		
		transfer.updateProgress(chunkSize);
		gui.updateTransferProgress(transfer);
	}
	
	/*
	 * 
	 */
	public void updateTransferProgress(Transfer transfer, int chunkSize) {
		transfer.updateProgress(chunkSize);
		gui.updateTransferProgress(transfer);
	}
}
