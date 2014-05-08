package server;


import server.SharixServerStub.RegisterPeer;


public class Main
{
	public static void main(String[] args) throws Exception
	{
		SharixServerStub serverStub = new SharixServerStub();
		RegisterPeer rp = new RegisterPeer();
		rp.setName("Test");
		rp.setIp("127.0.0.1");
		rp.setPort(6666);
		serverStub.registerPeer(rp);
		
		System.out.println("Done!");
	}
}
