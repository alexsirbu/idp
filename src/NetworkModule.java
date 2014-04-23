import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;


public class NetworkModule extends Thread implements Observable, Observer
{
	private static int POOL_SIZE = 10;
	private static int SELECT_TIMEOUT = 200;
	
	private String ip;
	private int port;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private ArrayList<SocketChannel> socketChannels;
	private ArrayList<Observer> observers;
	private ExecutorService executorsPool;
	
	private static Logger logger = Logger.getLogger(NetworkModule.class);
	
	
	public NetworkModule(String ip, int port) throws IOException
	{
		this.ip = ip;
		this.port = port;
		this.socketChannels = new ArrayList<SocketChannel>();
		this.observers = new ArrayList<Observer>();
		this.executorsPool = Executors.newFixedThreadPool(NetworkModule.POOL_SIZE);
	}
	
	public void setLogger(Logger logger)
	{
		NetworkModule.logger = logger;
	}
	
	@Override
	public void registerObserver(Observer observer)
	{
		NetworkModule.logger.info("NetworkModule: Registering observer.");
		
		this.observers.add(observer);
	}
	
	@Override
	public void unregisterObserver(Observer observer)
	{
		NetworkModule.logger.info("NetworkModule: Unregistering observer.");
		
		this.observers.remove(observer);
	}
	
	@Override
	public void notifyObservers(ObservableState observableState)
	{
		NetworkModule.logger.info("NetworkModule: Notifying observers.");
		
		for(Observer observer: this.observers)
			observer.notify(observableState);
	}
	
	@Override
	public void notify(ObservableState observableState)
	{
		NetworkModule.logger.info("NetworkModule: Notified by Observable.");
		
		try
		{
			observableState.process(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.notifyObservers(observableState);
	}
	
	@Override
	public void run()
	{
		NetworkModule.logger.info("NetworkModule: Running.");
		
		try
		{
			this.selector = Selector.open();
			
			this.serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(this.ip, this.port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			NetworkModule.logger.info("NetworkModule: Listening on " + this.ip + ":" + this.port + ".");
			
			do
			{
				this.selector.select(NetworkModule.SELECT_TIMEOUT);
				
				for(Iterator<SelectionKey> it = selector.keys().iterator(); it.hasNext();)
				{
					SelectionKey key = it.next();
					
					if(this.selector.selectedKeys().contains(key))
					{
						if(key.isAcceptable())
							this.accept(key);
						else if(key.isConnectable())
							this.connect(key);
						else if (key.isReadable())
							this.read(key);
						else if (key.isWritable())
							this.write(key);
						
						this.selector.selectedKeys().remove(key);
					}
					else if(key.interestOps() == SelectionKey.OP_READ)
						this.read(key);
				}
			}
			while(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public SocketChannel connect(String ip, int port) throws IOException
	{
		NetworkModule.logger.info("NetworkModule: Connecting to " + ip + ":" + port + ".");
		
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		socketChannel.connect(new InetSocketAddress(ip, port));
		
		socketChannel.register(this.selector, SelectionKey.OP_CONNECT, new NetworkModuleSelectionKeyAttachment());
		
		this.socketChannels.add(socketChannel);
		
		return socketChannel;
	}
	
	public void disconnect(SocketChannel socketChannel) throws IOException
	{
		InetSocketAddress addr = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
		NetworkModule.logger.info("NetworkModule: Disconnecting from " + addr.getHostString() + ":" + addr.getPort() + ".");
		
		socketChannel.keyFor(this.selector).cancel();
		socketChannel.close();
		
		this.socketChannels.remove(socketChannel);
	}
	
	public void read(SocketChannel socketChannel)
	{
		InetSocketAddress addr = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
		NetworkModule.logger.info("NetworkModule: Reading from " + addr.getHostString() + ":" + addr.getPort() + ".");
		
		SelectionKey key = socketChannel.keyFor(this.selector);
		
		NetworkModuleSelectionKeyAttachment attachment = (NetworkModuleSelectionKeyAttachment)key.attachment();
		attachment.getByteBuffer().clear();
		attachment.getTimer().start();
		
		Main.logger.info("Changing ops to op read in read 1.");
		key.interestOps(SelectionKey.OP_READ);		
	}
	
	public void write(SocketChannel socketChannel, byte[] src)
	{
		InetSocketAddress addr = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
		NetworkModule.logger.info("NetworkModule: Writing to " + addr.getHostString() + ":" + addr.getPort() + ".");
		
		SelectionKey key = socketChannel.keyFor(this.selector);
		
		NetworkModuleSelectionKeyAttachment attachment = (NetworkModuleSelectionKeyAttachment)key.attachment();
		attachment.getByteBuffer().clear();
		attachment.getByteBuffer().put(src);
		attachment.getByteBuffer().flip();
		attachment.getTimer().start();
		
		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	/* TODO: Not called on timeouts. */
	protected void accept(SelectionKey key) throws IOException
	{
		NetworkModule.logger.info("NetworkModule: Accepting connection.");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		if(socketChannel != null)
		{
			socketChannel.configureBlocking(false);
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			socketChannel.register(key.selector(), 0, new NetworkModuleSelectionKeyAttachment());
			
			this.socketChannels.add(socketChannel);
			
			InetSocketAddress addr = (InetSocketAddress)((SocketChannel)socketChannel.keyFor(key.selector()).channel()).socket().getRemoteSocketAddress();
			NetworkModule.logger.info("NetworkModule: Accepted connection from " + addr.getHostString() + ":" + addr.getPort() + ".");
			
			this.notifyObservers(new NetworkModuleAcceptState(socketChannel.keyFor(key.selector()), false, false));
		}
	}
	
	protected void connect(SelectionKey key) throws IOException
	{	
		key.interestOps(0);
		
		boolean encounteredError = !((SocketChannel)key.channel()).finishConnect();
		
		InetSocketAddress addr = (InetSocketAddress)((SocketChannel)key.channel()).socket().getRemoteSocketAddress();
		if(!encounteredError)
			NetworkModule.logger.info("NetworkModule: Connected to " + addr.getHostString() + ":" + addr.getPort() + ".");
		else
			NetworkModule.logger.info("NetworkModule: Connection to " + addr.getHostString() + ":" + addr.getPort() + " encountered an error.");
		
		this.notifyObservers(new NetworkModuleConnectState(key, false, encounteredError));
	}
	
	protected void read(SelectionKey key) throws IOException
	{
		key.interestOps(0);
		
		NetworkModuleOperationThread thread = new NetworkModuleReadThread(key);
		thread.registerObserver(this);
		
		this.executorsPool.execute(thread);
		
		InetSocketAddress addr = (InetSocketAddress)((SocketChannel)key.channel()).socket().getRemoteSocketAddress();
		NetworkModule.logger.info("NetworkModule: Started read thread for " + addr.getHostString() + ":" + addr.getPort() + ".");
	}
	
	protected void write(SelectionKey key) throws IOException
	{
		key.interestOps(0);
		
		NetworkModuleOperationThread thread = new NetworkModuleWriteThread(key);
		thread.registerObserver(this);
		
		this.executorsPool.execute(thread);
		
		InetSocketAddress addr = (InetSocketAddress)((SocketChannel)key.channel()).socket().getRemoteSocketAddress();
		NetworkModule.logger.info("NetworkModule: Started write thread for " + addr.getHostString() + ":" + addr.getPort() + ".");
	}
}
