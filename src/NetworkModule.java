import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


public class NetworkModule extends Thread implements Observable, Observer
{
	private static int POOL_SIZE = 10;
	private static int SELECT_TIMEOUT = 100;
	
	private String ip;
	private int port;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private ArrayList<SocketChannel> socketChannels;
	private ArrayList<Observer> observers;
	private ExecutorService executorsPool;
	
	
	public NetworkModule() throws IOException
	{
		this.socketChannels = new ArrayList<SocketChannel>();
		this.observers = new ArrayList<Observer>();
		this.executorsPool = Executors.newFixedThreadPool(NetworkModule.POOL_SIZE);
	}
	
	public NetworkModule(String ip, int port) throws IOException
	{
		this.ip = ip;
		this.port = port;
	}
	
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	@Override
	public void registerObserver(Observer observer)
	{
		this.observers.add(observer);
	}
	
	@Override
	public void unregisterObserver(Observer observer)
	{
		this.observers.remove(observer);
	}
	
	@Override
	public void notifyObservers(ObservableState observableState)
	{
		for(Observer observer: this.observers)
			observer.notify(observableState);
	}
	
	@Override
	public void notify(ObservableState observableState)
	{	
		try
		{
			NetworkModuleOperationState opState = (NetworkModuleOperationState)observableState;
			if(opState.hasTimedOut() || opState.hasEncouteredError() || opState.hasReachedEOS())
				this.disconnect(opState.getSocketChannel());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.notifyObservers(observableState);
	}
	
	@Override
	public void run()
	{
		try
		{
			this.selector = Selector.open();
			
			this.serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(this.ip, this.port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			do
			{
				this.selector.select(NetworkModule.SELECT_TIMEOUT);
				
				for(Iterator<SelectionKey> it = selector.keys().iterator(); it.hasNext();)
				{
					SelectionKey key = it.next();
					it.remove();
					
					if(key.isAcceptable())
						this.accept(key);
					else if(key.isConnectable())
						this.connect(key);
					else if (key.isReadable())
						this.read(key);
					else if (key.isWritable())
						this.write(key);
					else if((key.interestOps() & SelectionKey.OP_READ) > 0)
						this.read(key);
					else if((key.interestOps() & SelectionKey.OP_WRITE) > 0)
						this.write(key);
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
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress(ip, port));
		socketChannel.register(this.selector, SelectionKey.OP_CONNECT, new NetworkModuleSelectionKeyAttachment());
		
		this.socketChannels.add(socketChannel);
		
		return socketChannel;
	}
	
	public void disconnect(SocketChannel socketChannel) throws IOException
	{
		assert this.socketChannels.contains(socketChannel);
		
		socketChannel.close();
		
		this.socketChannels.remove(socketChannel);
	}
	
	public void read(SocketChannel socketChannel)
	{
		assert this.socketChannels.contains(socketChannel);
		
		SelectionKey key = socketChannel.keyFor(this.selector);
		
		NetworkModuleSelectionKeyAttachment attachment = (NetworkModuleSelectionKeyAttachment)key.attachment();
		attachment.getByteBuffer().clear();
		attachment.getTimer().start();
		
		key.interestOps(SelectionKey.OP_READ);
	}
	
	public void write(SocketChannel socketChannel, byte[] src)
	{
		assert this.socketChannels.contains(socketChannel);
		
		SelectionKey key = socketChannel.keyFor(this.selector);
		
		NetworkModuleSelectionKeyAttachment attachment = (NetworkModuleSelectionKeyAttachment)key.attachment();
		attachment.getByteBuffer().clear();
		attachment.getByteBuffer().put(src);
		attachment.getTimer().start();
		
		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	/* TODO: Not called on timeouts. */
	protected void accept(SelectionKey key) throws IOException
	{
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), 0, new NetworkModuleSelectionKeyAttachment());
		
		this.socketChannels.add(socketChannel);
		
		this.notifyObservers(NetworkModuleOperationState.getAcceptOperationState(key, false));
	}
	
	/* TODO: Not called on timeouts. */
	protected void connect(SelectionKey key) throws IOException
	{
		key.interestOps(0);
		
		((SocketChannel)key.channel()).finishConnect();
		
		this.notifyObservers(NetworkModuleOperationState.getConnectOperationState(key, false));
	}
	
	protected void read(SelectionKey key) throws IOException
	{
		key.interestOps(0);
		
		NetworkModuleOperationThread thread = new NetworkModuleReadThread(key);
		thread.registerObserver(this);
		
		this.executorsPool.execute(thread);
	}
	
	protected void write(SelectionKey key) throws IOException
	{
		key.interestOps(0);
		
		NetworkModuleOperationThread thread = new NetworkModuleWriteThread(key);
		thread.registerObserver(this);
		
		this.executorsPool.execute(thread);
	}
}
