import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class NetworkModuleConnectState extends NetworkModuleOperationState
{
	private String ip;
	private int port;
	
	
	public NetworkModuleConnectState(SelectionKey key, boolean timedOut, boolean encounteredError)
	{
		super((SocketChannel)key.channel(), timedOut, encounteredError);
		
		InetSocketAddress addr = (InetSocketAddress)this.socketChannel.socket().getRemoteSocketAddress();
		this.ip = addr.getHostString();
		this.port = addr.getPort();
	}

	public String getIp()
	{
		return ip;
	}

	public int getPort()
	{
		return port;
	}

	@Override
	public void process(Observer observer) throws Exception
	{	
		if((observer instanceof NetworkModule))
			this.processNetworkModule((NetworkModule)observer);
		else
			((NetworkModuleAdapter)observer).processNetworkModuleConnect(this);
	}
}
