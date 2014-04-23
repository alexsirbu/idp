import java.io.*;
import java.nio.*;
import java.nio.channels.*;


public class NetworkModuleWriteThread extends NetworkModuleOperationThread
{
	public NetworkModuleWriteThread(SelectionKey key)
	{
		super(key);
	}
	
	@Override
	public void run()
	{
		NetworkModuleSelectionKeyAttachment attachment = (NetworkModuleSelectionKeyAttachment)key.attachment();
		ByteBuffer byteBuffer = attachment.getByteBuffer();
		SocketChannel socketChannel = (SocketChannel)this.key.channel();
		
		boolean timedOut = false;
		boolean encounteredError = false;
		
		try
		{
			do
			{
				int noWrittenBytes = socketChannel.write(byteBuffer);
				Main.logger.info("Wrote " + noWrittenBytes + "bytes.");
				
				timedOut = attachment.getTimer().hasTimedOut();
			}
			while(!timedOut && byteBuffer.hasRemaining());
		}
		catch(IOException e)
		{
			encounteredError = true;
			
			e.printStackTrace();
		}
		
		Main.logger.info("Wrote message with timeout variable value " + timedOut + ".");
		
		this.notifyObservers(new NetworkModuleWriteState(key, timedOut, encounteredError));
	}
}
