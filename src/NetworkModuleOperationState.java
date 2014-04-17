import java.io.IOException;
import java.nio.channels.SocketChannel;


public abstract class NetworkModuleOperationState extends ObservableState
{
	protected SocketChannel socketChannel;
	protected boolean timedOut;
	protected boolean encounteredError;
	
	
	protected NetworkModuleOperationState(SocketChannel socketChannel, boolean timedOut, boolean encounteredError)
	{
		this.socketChannel = socketChannel;
		this.timedOut = timedOut;
		this.encounteredError = encounteredError;
	}
	
	public SocketChannel getSocketChannel()
	{
		return this.socketChannel;
	}
	
	public boolean hasTimedOut()
	{
		return this.timedOut;
	}
	
	public boolean hasEncouteredError()
	{
		return this.encounteredError;
	}
	
	protected void processNetworkModule(NetworkModule networkModule) throws IOException
	{
		if(this.hasTimedOut() || this.hasEncouteredError())
			networkModule.disconnect(this.getSocketChannel());
	}
}
