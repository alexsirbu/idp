/**
 * @author cristian
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
	private Peer sender;
	/*
	 * 
	 */
	private Peer receiver;
	/*
	 * 
	 */
	private int progress;
	
	/*
	 * 
	 */
	public Transfer(File file, Peer sender, Peer receiver) {
		this.file = file;
		this.sender = sender;
		this.receiver = receiver;
		this.progress = 0;
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
	public Peer getSender() {
		return sender;
	}
	
	/*
	 * 
	 */
	public Peer getReceiver() {
		return receiver;
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
	public void setProgress(int progress) {
		assert progress >= 0 && progress <= 100;
		this.progress = progress;
	}
}
