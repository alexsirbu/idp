public abstract class Message {

	static int REQUEST_FILE_TYPE = 0;
	static int REQUEST_FILE_RESPONSE_TYPE = 1;
	static int REQUEST_FILE_PART_TYPE = 2;
	static int REQUEST_FILE_PART_RESPONSE_TYPE = 3;
	
	public Message(){};
	public Message(byte[] encodedMessage){};
	
	public abstract byte[] encode(); 
	public abstract int getType();
	
}
