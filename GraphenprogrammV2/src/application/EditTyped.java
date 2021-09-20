package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.scene.control.Button;

public class EditTyped {
	private List<Event> undoButtonClicks = new ArrayList<Event>();
	
	private List<Event> redoButtonClicks = new ArrayList<Event>();
	private List<Event> undoMenuChoices = new ArrayList<Event>();
	private List<Event> redoMenuChoices = new ArrayList<Event>();

	
	public EditTyped() {}

	public void addEvent(Event event, int index) 
	{
		
		switch(index) 
		{
		
		case 1:
			undoButtonClicks.add(event);
			break;
			
		case 2:
			redoButtonClicks.add(event);
			break;
			
		case 3:
			undoMenuChoices.add(event);
			break;
			
		case 4:
			redoMenuChoices.add(event);
			break;
		}
	}
	
	public Event getLastListItem(List <Event> events) throws IndexOutOfBoundsException
	{
		//Event event;
			try 
			{
				
				return events.get(events.size()-1);
				
			} 
			
			catch (IndexOutOfBoundsException e) 
			{
				
				throw new IndexOutOfBoundsException();
				
			}
		
		
	}
	
	public Button getUndoButton() throws IndexOutOfBoundsException
	{
		
		Button button = (Button)getLastListItem(undoButtonClicks).getSource();
		
		redoButtonClicks.add(getLastListItem(undoButtonClicks));
		undoButtonClicks.remove(getLastListItem(undoButtonClicks));
		
		return button;
		
	}
	
	public Button getRedoButton() throws IndexOutOfBoundsException
	{
		
		Button button = (Button)getLastListItem(redoButtonClicks).getSource();
		
		undoButtonClicks.add(getLastListItem(redoButtonClicks));
		redoButtonClicks.remove(getLastListItem(redoButtonClicks));
		
		return button;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
