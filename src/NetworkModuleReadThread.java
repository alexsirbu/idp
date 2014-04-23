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
		int noCurrReadBytes = 0;
		
		if(!timedOut)
		{
			try
			{
				do
				{
					noCurrReadBytes = socketChannel.read(byteBuffer);
					if(noCurrReadBytes > 0)
						noReadBytes += noCurrReadBytes;
				}
				while(noCurrReadBytes > 0);
				
				if(noCurrReadBytes == -1)
					reachedEOS = true;
			}
			catch(IOException e)
			{
				encounteredError = true;
				
				e.printStackTrace();
			}
		}
		
		if(!timedOut && !encounteredError && !reachedEOS && noReadBytes > 0)
			attachment.getTimer().restart();
		
		if(noReadBytes > 0 || timedOut || encounteredError || reachedEOS)
			this.notifyObservers(new NetworkModuleReadState(key, timedOut, encounteredError, reachedEOS));
		
		if(key.isValid() && key.interestOps() == 0)
			key.interestOps(SelectionKey.OP_READ);
	}
}
