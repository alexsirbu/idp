import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class NetworkModuleWriteState extends NetworkModuleOperationState
{
	private ByteBuffer byteBuffer;
	
	
	public NetworkModuleWriteState(SelectionKey key, boolean timedOut, boolean encounteredError)
	{
		super((SocketChannel)key.channel(), timedOut, encounteredError);
		
		this.byteBuffer = ((NetworkModuleSelectionKeyAttachment)key.attachment()).getByteBuffer();
	}
	
	public ByteBuffer getByteBuffer()
	{
		return byteBuffer;
	}

	@Override
	public void process(Observer observer) throws Exception
	{
		if((observer instanceof NetworkModule))
			this.processNetworkModule((NetworkModule)observer);
		else
			((NetworkModuleAdapter)observer).processNetworkModuleWrite(this);
	}
}