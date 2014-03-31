
public class UndoManager {

	Undoable[] elements;
	int last;
	int current;
	
	public UndoManager()
	{
		elements = new Undoable[100];
		current = 0;
		last = -1;
	}
	
	public void addCommand(Undoable x)
	{
		elements[++last]=x;
		current=last;		
	}
	
	public void undo()
	{
		if (last>=0)
			elements[last--].undo();
	}
	
	public void redo()
	{
		if (last<current)
			elements[++last].redo();
	}
	
}
