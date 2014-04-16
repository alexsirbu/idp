import java.io.*;
import java.nio.*;
import java.nio.channels.*;


public class NetworkModuleReadThread extends NetworkModuleOperationThread
{		
	public NetworkModuleReadThread(SelectionKey key)
	{
		super(key);
	}
	
	@Override
	public void run()
	{	
		NetworkModuleSelectionKeyAttachment attachment = (NetworkModuleSelectionKeyAttachment)key.attachment();
		ByteBuffer byteBuffer = attachment.getByteBuffer();
		SocketChannel socketChannel = (SocketChannel)this.key.channel();
		
		boolean timedOut = attachment.getTimer().hasTimedOut();
		boolean encounteredError = false;
		boolean reachedEOS = false;
		
		int noReadBytes = 0;
		
		if(!timedOut)
		{
			try
			{
				do
				{
					noReadBytes = socketChannel.read(byteBuffer);
				}
				while(noReadBytes > 0);
				
				if(noReadBytes == -1)
					reachedEOS = true;
			}
			catch(IOException e)
			{
				encounteredError = true;
				
				e.printStackTrace();
			}
		}
		
		this.notifyObservers(NetworkModuleOperationState.getReadOperationState(key, timedOut, encounteredError, reachedEOS));
		
		if(!timedOut && !encounteredError && !reachedEOS)
		{
			if(noReadBytes > 0)
				attachment.getTimer().restart();
			
			key.interestOps(SelectionKey.OP_READ);
			this.key.selector().wakeup();
		}
	}
}
