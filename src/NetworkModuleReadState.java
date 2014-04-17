import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class NetworkModuleReadState extends NetworkModuleOperationState
{
	private ByteBuffer byteBuffer;
	private boolean reachedEndOfStream;
	
	
	public NetworkModuleReadState(SelectionKey key, boolean timedOut, boolean encounteredError, boolean reachedEndOfStream)
	{
		super((SocketChannel)key.channel(), timedOut, encounteredError);
		
		this.byteBuffer = ((NetworkModuleSelectionKeyAttachment)key.attachment()).getByteBuffer();
		
		this.reachedEndOfStream = reachedEndOfStream;
		
		Main.logger.info("Bla: " + this.encounteredError + " " + this.reachedEndOfStream + " " + this.timedOut);
	}
	
	public ByteBuffer getByteBuffer()
	{
		return byteBuffer;
	}
	
	public boolean hasReachedEndOfStream()
	{
		return reachedEndOfStream;
	}
	
	@Override
	protected void processNetworkModule(NetworkModule networkModule) throws IOException
	{
		if(this.hasTimedOut() || this.hasEncouteredError() || this.hasReachedEndOfStream())
			networkModule.disconnect(this.getSocketChannel());
	}

	@Override
	public void process(Observer observer) throws Exception
	{
		if((observer instanceof NetworkModule))
			this.processNetworkModule((NetworkModule)observer);
		else
		{
		}
	}
}
