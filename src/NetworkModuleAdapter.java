public class NetworkModuleAdapter implements Observer
{
	private NetworkModule net;
	private Mediator mediator;
	
	
	public NetworkModuleAdapter(NetworkModule net, Mediator mediator)
	{
		this.net = net;
		this.mediator = mediator;
	}

	@Override
	public void notify(ObservableState observableState)
	{
		System.out.println("Event");
	}
}
