import javax.swing.DefaultListModel;
import javax.swing.event.*;

import java.util.Enumeration;

public class ReverseListModel extends DefaultListModel {
	
	DefaultListModel decorated;
	
	public ReverseListModel(DefaultListModel decorated)
	{
		super();
		this.decorated = decorated;
		final ReverseListModel current = this;
		this.decorated.addListDataListener(new ListDataListener() {

			@Override
			public void intervalAdded(ListDataEvent e) {
				// TODO Auto-generated method stub
				current.fireIntervalAdded(e.getSource(),e.getIndex0(),e.getIndex1());				
			}

			@Override
			public void intervalRemoved(ListDataEvent e) {
				// TODO Auto-generated method stub
				current.fireIntervalRemoved(e.getSource(),e.getIndex0(),e.getIndex1());				
			}

			@Override
			public void contentsChanged(ListDataEvent e) {
				// TODO Auto-generated method stub
				current.fireContentsChanged(e.getSource(),e.getIndex0(),e.getIndex1());
			}
			
		}
				);
	}
	
	@Override
	public Object get(int index)
	{
		if (this.decorated.getSize()==0) return null;
		return this.decorated.get(this.decorated.getSize()-index-1);
	}
	
	@Override
	public Object elementAt(int index)
	{
		if (this.decorated.getSize()==0) return null;
		return this.decorated.get(this.decorated.getSize()-index-1);
	}
	
	@Override
	public Object getElementAt(int index)
	{
		if (this.decorated.getSize()==0) return null;
		return this.decorated.get(this.decorated.getSize()-index-1);
	}

	@Override
	public int getSize()
	{
		return this.decorated.getSize();
	}
	
	
	/*@Override
	protected void fireIntervalAdded(Object source,
            int index0,
            int index1)
	{
		for(int i=index0; i<index1; i++)
			if (this.decorated.contains(((DefaultListModel)source).get(i)))
				return;
		
					
	}*/
}
