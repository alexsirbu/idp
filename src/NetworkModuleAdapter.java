import java.util.HashMap;
import java.nio.ByteBuffer;
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
		this.transfers = new HashMap<SocketChannel, Transfer>();
		
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
			ByteBuffer byteBuffer = readState.getByteBuffer();
			
			Main.logger.info("The buffer is "+byteBuffer.toString());
			
			byte buffer[] = new byte[byteBuffer.position()]; 
			byteBuffer.flip();
			byteBuffer.get(buffer);
			NetworkMessage netMsg = new NetworkMessage(buffer);
			
			NetworkModuleAdapter.logger.info("NetworkModuleAdapter: Received message " + buffer.length + "." + netMsg.getComplete());
			
			/* If the message is not complete keep reading. */
			if(!netMsg.getComplete())
				return;
			
			/* If a transfer has not been mapped, the message contains an incoming file request. */
			if(!this.transfers.containsKey(socketChannel))
			{
				MessageRequestFile msg = (MessageRequestFile)netMsg.getMessage();
				
				File file = this.mediator.getLocalPeer().getSharedFile(msg.filename);
				Peer receiver = this.mediator.getPeer(msg.getRequester());
				Peer sender = this.mediator.getLocalPeer();
				Transfer transfer = new Transfer(file, sender, receiver);
				
				this.transfers.put(socketChannel, transfer);
				this.mediator.addTransferIncomingRequest(transfer);
				
				MessageRequestFileResponse respMsg = new MessageRequestFileResponse(file.getName(), file.getSize());
				NetworkMessage respNetMsg = new NetworkMessage(respMsg);
				this.networkModule.write(socketChannel, respNetMsg.encode());
			}
			else
			{
				Transfer transfer = this.transfers.get(socketChannel);
				
				/* If the local peer is the receiver the message contains a file part reponse. */
				if(transfer.getReceivingPeer().getName().equals(this.mediator.getLocalPeer().getName()))
				{
					if(netMsg.getMessage().getType() == Message.REQUEST_FILE_PART_RESPONSE_TYPE)
					{
						MessageRequestFilePartResponse msg = (MessageRequestFilePartResponse)netMsg.getMessage();
						
						FileOperationsJavaNIO fop = null;
						
						try
						{
							fop = new FileOperationsJavaNIO(this.mediator.getLocalPeerRealName()+"/"+msg.getFilename()); 
							fop.append(msg.getContent());
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						this.mediator.updateTransferProgress(transfer, msg.getLength());
						
						if(transfer.getProgress() == 100)
						{
							try
							{
								this.networkModule.disconnect(socketChannel);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							MessageRequestFilePart respMsg = new MessageRequestFilePart(
								msg.getFilename(),
								msg.getPosition() + msg.getLength(),
								Math.min(
										msg.getLength(),
										transfer.getFile().getSize() - msg.getPosition() - msg.getLength()
								),
								this.mediator.getLocalPeer().getName()
							);
							NetworkMessage respNetMsg = new NetworkMessage(respMsg);
							this.networkModule.write(socketChannel, respNetMsg.encode());
						}
					}
					else
					{
						MessageRequestFileResponse msg = (MessageRequestFileResponse)netMsg.getMessage();
						
						File file = transfer.getFile();
						file.setSize(msg.getFilesize());
						
						MessageRequestFilePart respMsg = new MessageRequestFilePart(
							msg.getFilename(),
							0,
							Math.min(
									512,
									transfer.getFile().getSize()
							),
							this.mediator.getLocalPeer().getName()
						);
						NetworkMessage respNetMsg = new NetworkMessage(respMsg);
						this.networkModule.write(socketChannel, respNetMsg.encode());
					}
				}
				/* The message contains a file part request. */
				else
				{
					MessageRequestFilePart msg = (MessageRequestFilePart)netMsg.getMessage();
					
					FileOperationsJavaNIO fop = null;
					byte content[] = null;
					
					try
					{
						fop = new FileOperationsJavaNIO(this.mediator.getLocalPeerRealName()+"/"+msg.getFilename()); 
						content = fop.readNumberFromPosition(msg.getLength(), msg.getPosition());
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					MessageRequestFilePartResponse respMsg = new MessageRequestFilePartResponse(msg.getFilename(), msg.getPosition(), msg.getLength(), content); 
					NetworkMessage respNetMsg = new NetworkMessage(respMsg);
					this.networkModule.write(socketChannel, respNetMsg.encode());
					
					this.mediator.updateTransferProgress(transfer, msg.getLength());
				}
			}
		}
	}
	
	public void processNetworkModuleWrite(NetworkModuleWriteState writeState)
	{
		NetworkModuleAdapter.logger.info("Write notification.");
		
		if(writeState.hasTimedOut() || writeState.hasEncouteredError())
		{
			/* TODO: Handle error. */
		}
		else
			this.networkModule.read(writeState.getSocketChannel());
	}
	
	public void processNetworkModuleConnect(NetworkModuleConnectState connectState)
	{
		SocketChannel socketChannel = connectState.getSocketChannel();
		Transfer transfer = this.transfers.get(socketChannel);
		
		MessageRequestFile msg = new MessageRequestFile(transfer.getFile().getName(), transfer.getReceivingPeer().getName());
		NetworkMessage netMsg = new NetworkMessage(msg);
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
			this.transfers.put(socketChannel, transfer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
