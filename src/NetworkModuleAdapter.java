import java.util.HashMap;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;


public class NetworkModuleAdapter implements Observer
{
	private NetworkModule networkModule;
	private Mediator mediator;
	private HashMap<SocketChannel, Transfer> transfers;
	
	private static Logger logger = Logger.getLogger(NetworkModuleAdapter.class);
	
	
	public NetworkModuleAdapter(NetworkModule networkModule, Mediator mediator)
	{
		this.networkModule = networkModule;
		this.mediator = mediator;
		
		this.networkModule.registerObserver(this);
	}
	
	public void setLogger(Logger logger)
	{
		NetworkModuleAdapter.logger = logger;
	}

	@Override
	public void notify(ObservableState observableState)
	{
		NetworkModuleAdapter.logger.info("NetworkModuleAdapter: Received notification from NetworkModule.");
		
		try
		{
			observableState.process(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void processNetworkModuleRead(NetworkModuleReadState readState)
	{
		if(readState.hasTimedOut() || readState.hasEncouteredError() || readState.hasReachedEndOfStream())
		{
			/* TODO: Handle error. */
		}
		else
		{
			SocketChannel socketChannel = readState.getSocketChannel();
			
			/* TODO: check if the whole message has been received. */
			
			/* If a transfer has not been mapped, the message contains an incoming file request. */
			if(!this.transfers.containsKey(socketChannel))
			{
				
			}
			else
			{
				
			}
		}
		
	}
	
	public void processNetworkModuleWrite(NetworkModuleWriteState writeState)
	{
		
	}
	
	public void processNetworkModuleConnect(NetworkModuleConnectState connectState)
	{
		SocketChannel socketChannel = connectState.getSocketChannel();
		Transfer transfer = this.transfers.get(socketChannel);
		
		MessageRequestFile msg = new MessageRequestFile(transfer.getFile().getName(), transfer.getReceivingPeer().getName());
		NetworkMessage netMsg = new NetworkMessage(msg.encode().length, msg);
		this.networkModule.write(socketChannel, netMsg.encode());
	}
	
	public void processNetworkModuleAccept(NetworkModuleAcceptState acceptState)
	{
		SocketChannel socketChannel = acceptState.getSocketChannel();
		this.networkModule.read(socketChannel);
	}
	
	public void initOutgoingTransferRequest(Transfer transfer)
	{
		SocketChannel socketChannel = null;
		
		try
		{
			socketChannel = this.networkModule.connect(transfer.getSendingPeer().getIP(), transfer.getSendingPeer().getPort());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.transfers.put(socketChannel, transfer);
	}
	
	public void initIncomingTransferRequest()
	{
		
	}
}
