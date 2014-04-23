/**
 *
 */
public class Transfer {
	/*
	 * 
	 */
	private File file;
	/*
	 * 
	 */
	private Peer sendingPeer;
	/*
	 * 
	 */
	private Peer receivingPeer;
	/*
	 * 
	 */
	private int progress;
	/*
	 * 
	 */
	private int progressBytes;
	
	/*
	 * 
	 */
	public Transfer(File file, Peer sender, Peer receiver) {
		this.file = file;
		this.sendingPeer = sender;
		this.receivingPeer = receiver;
		this.progress = 0;
		this.progressBytes = 0;
	}
	
	/*
	 * 
	 */
	public File getFile() {
		return file;
	}
	
	/*
	 * 
	 */
	public Peer getSendingPeer() {
		return sendingPeer;
	}
	
	/*
	 * 
	 */
	public Peer getReceivingPeer() {
		return receivingPeer;
	}

	/*
	 * 
	 */
	public int getProgress() {
		return progress;
	}
	
	/*
	 * 
	 */
	public int getProgressBytes(){
		return progressBytes;
	}

	/*
	 * 
	 */
	public void updateProgress(int chunkSize) {
		progressBytes += chunkSize;
		progress = progressBytes * 100 / file.getSize();
		progress = Math.min(progress, 100);
	}
}
