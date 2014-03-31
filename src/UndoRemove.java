import javax.swing.DefaultListModel;


public class UndoRemove implements Undoable{

	DefaultListModel source;
	int index;
	Object value;
	
	public UndoRemove(DefaultListModel source,	int index, Object value)
	{
		this.source=source;
		this.index=index;
		this.value=value;
	}
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		source.add(index, value);
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		source.removeElementAt(index);
	}

}
