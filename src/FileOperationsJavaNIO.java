import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.*;


public class FileOperationsJavaNIO extends FileOperations{
	
	public FileOperationsJavaNIO(String filename) {
		super(filename);
	}

	@Override
	public byte[] readAll() throws IOException{
		return readAllFromPosition(0);
	}

	@Override
	public byte[] readAllFromPosition(int position) throws IOException{
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		FileChannel channel = file.getChannel();
		
		byte[] buffer = _readNumberFromPositionInternal((int)channel.size()-position, position, channel);
		
		channel.close();
		file.close();
		
		return buffer;
	}

	@Override
	public byte[] readNumberFromPosition (int bytesNumber, int position) throws IOException {
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		FileChannel channel = file.getChannel();
		
		byte[] buffer = _readNumberFromPositionInternal(bytesNumber, position, channel);
		
		channel.close();
		file.close();
		
		return buffer;		
	}
	
	private byte[] _readNumberFromPositionInternal(int bytesNumber, int position, FileChannel channel) throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(bytesNumber);
		channel.position(position);
		int bytesRead;
		do {
	        bytesRead = channel.read(buffer);
	    } while (bytesRead != -1 && buffer.hasRemaining());
		
		return buffer.array();
	}

	@Override
	public void overWrite(byte[] content) throws IOException{
		RandomAccessFile file = new RandomAccessFile(filename, "w");
		FileChannel channel = file.getChannel();
		
		_writeNumberFromPositionInternal(content, (int)channel.size(), channel);
		
		channel.close();
		file.close();		
	}

	@Override
	public void append(byte[] content) throws IOException{
		RandomAccessFile file = new RandomAccessFile(filename, "w");
		FileChannel channel = file.getChannel();
		
		_writeNumberFromPositionInternal(content, (int)channel.size(), channel);
		
		channel.close();
		file.close();		
	}

	@Override
	public void writeAtPosition(byte[] content, int position) throws IOException{
		RandomAccessFile file = new RandomAccessFile(filename, "w");
		FileChannel channel = file.getChannel();
		
		_writeNumberFromPositionInternal(content, position, channel);
		
		channel.close();
		file.close();		
	}
	
	private void _writeNumberFromPositionInternal(byte[] content, int position, FileChannel channel) throws IOException{
		ByteBuffer buffer = ByteBuffer.wrap(content);
		channel.position(position);
		while (buffer.hasRemaining())
	        channel.write(buffer); 
	}

}
