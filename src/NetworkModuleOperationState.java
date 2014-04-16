import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class NetworkModuleOperationState extends ObservableState
{
	public static final int READ_OPERATION = 0;
	public static final int WRITE_OPERATION = 1;
	public static final int CONNECT_OPERATION = 2;
	public static final int ACCEPT_OPERATION = 3;
	
	
	private SocketChannel socketChannel;
	private ByteBuffer byteBuffer;
	private int operationType;
	private boolean timedOut;
	private boolean encounteredError;
	private boolean reachedEOS;
	
	
	private NetworkModuleOperationState(SelectionKey key, boolean timedOut, boolean encounteredError, boolean reachedEOS, int operationType)
	{
		this.socketChannel = (SocketChannel)key.channel();
		this.byteBuffer = (ByteBuffer)key.attachment();
		this.operationType = operationType;
		this.timedOut = timedOut;
		this.encounteredError = encounteredError;
		this.reachedEOS = reachedEOS;
	}
	
	public SocketChannel getSocketChannel()
	{
		return this.socketChannel;
	}
	
	public ByteBuffer getByteBuffer()
	{
		return this.byteBuffer;
	}
	
	public boolean hasTimedOut()
	{
		return this.timedOut;
	}
	
	public boolean hasEncouteredError()
	{
		return this.encounteredError;
	}
	
	public boolean hasReachedEOS()
	{
		return this.reachedEOS;
	}
	
	public boolean isReadOperation()
	{
		return (this.operationType == NetworkModuleOperationState.READ_OPERATION);
	}
	
	public boolean isWriteOperation()
	{
		return (this.operationType == NetworkModuleOperationState.WRITE_OPERATION);
	}
	
	public boolean isConnectOperation()
	{
		return (this.operationType == NetworkModuleOperationState.CONNECT_OPERATION);
	}
	
	public boolean isAcceptOperation()
	{
		return (this.operationType == NetworkModuleOperationState.ACCEPT_OPERATION);
	}
	
	public static NetworkModuleOperationState getReadOperationState(SelectionKey key, boolean timedOut, boolean encouteredError, boolean reachedEOS)
	{
		return new NetworkModuleOperationState(key, timedOut, encouteredError, reachedEOS, NetworkModuleOperationState.READ_OPERATION);
	}
	
	public static NetworkModuleOperationState getWriteOperationState(SelectionKey key, boolean timedOut, boolean encounteredError)
	{
		return new NetworkModuleOperationState(key, timedOut, encounteredError, false, NetworkModuleOperationState.WRITE_OPERATION);
	}
	
	public static NetworkModuleOperationState getConnectOperationState(SelectionKey key, boolean timedOut)
	{
		return new NetworkModuleOperationState(key, timedOut, false, false, NetworkModuleOperationState.CONNECT_OPERATION);
	}
	
	public static NetworkModuleOperationState getAcceptOperationState(SelectionKey key, boolean timedOut)
	{
		return new NetworkModuleOperationState(key, timedOut, false, false, NetworkModuleOperationState.ACCEPT_OPERATION);
	}
}
