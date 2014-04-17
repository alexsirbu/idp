import org.apache.log4j.Logger;

public class NetworkModuleAdapter implements Observer
{
	public NetworkModule networkModule;
	private Mediator mediator;
	
	private static Logger logger = Logger.getLogger(NetworkModuleAdapter.class);
	
	
	public NetworkModuleAdapter(NetworkModule networkModule, Mediator mediator)
	{
		this.networkModule = networkModule;
		this.mediator = mediator;
		
		this.networkModule.registerObserver(this);
	}
	
	public void setLogger(Logger logger)
	{
		NetworkModuleAdapter.logger = logger;
	}

	@Override
	public void notify(ObservableState observableState)
	{
		NetworkModuleAdapter.logger.info("NetworkModuleAdapter: Received notification from NetworkModule.");
		
		try
		{
			observableState.process(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
