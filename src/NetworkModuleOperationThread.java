import java.util.ArrayList;
import java.nio.channels.SelectionKey;


public abstract class NetworkModuleOperationThread extends Thread implements Observable
{
	protected SelectionKey key;
	protected ArrayList<Observer> observers;
	
	
	protected NetworkModuleOperationThread(SelectionKey key)
	{
		this.key = key;
		this.observers = new ArrayList<Observer>();
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
}
