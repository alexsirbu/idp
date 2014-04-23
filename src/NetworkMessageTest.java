import junit.framework.TestCase;

public class NetworkMessageTest extends TestCase {
	
	public void testNetworkMessageFunctionality()
	{
		Message msg = new MessageRequestFile("filename", "requester");
		NetworkMessage nmsg = new NetworkMessage(msg);
		assertTrue("Correct message type", nmsg.getMessage().getType() == Message.REQUEST_FILE_TYPE);
		NetworkMessage decoded = new NetworkMessage(nmsg.encode());
		assertTrue("Complete message", decoded.getComplete());
		assertTrue("Correct message type", decoded.getMessage().getType() == Message.REQUEST_FILE_TYPE);
	}

}
