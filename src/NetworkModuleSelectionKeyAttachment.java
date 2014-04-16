import java.nio.ByteBuffer;


public class NetworkModuleSelectionKeyAttachment
{
	private static int BUFFER_SIZE = 1024;
	
	private ByteBuffer byteBuffer;
	private NetworkModuleTimer timer;
	
	
	public NetworkModuleSelectionKeyAttachment()
	{
		this.byteBuffer = ByteBuffer.allocateDirect(NetworkModuleSelectionKeyAttachment.BUFFER_SIZE);
		this.timer = new NetworkModuleTimer();
	}
	
	public ByteBuffer getByteBuffer()
	{
		return this.byteBuffer;
	}
	
	public NetworkModuleTimer getTimer()
	{
		return this.timer;
	}
}
