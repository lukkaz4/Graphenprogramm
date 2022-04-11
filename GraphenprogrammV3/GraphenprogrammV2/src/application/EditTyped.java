package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.scene.control.Button;
import model.AdjacencyMatrix;

public class EditTyped {
	
	private ArrayList<Event> undoButtonClicks = new ArrayList<Event>();
	private ArrayList<Event> redoButtonClicks = new ArrayList<Event>();
	
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
	
	public Button getUndoButton() throws Exception
	{
		
		Button button;
	
		try 
		{
			
			button = (Button)getLastListItem(undoButtonClicks).getSource();
			
			redoButtonClicks.add(getLastListItem(undoButtonClicks));
			undoButtonClicks.remove(getLastListItem(undoButtonClicks));
			
			return button;
					
		} 
		
		catch (Exception e) 
		{
			
			e.printStackTrace();
			throw new Exception();	
			
		}	
	}
	
	public Button getRedoButton() throws Exception
	{
		
		Button button;
		
		try 
		{
	
			button = (Button)getLastListItem(redoButtonClicks).getSource();
			
			undoButtonClicks.add(getLastListItem(redoButtonClicks));
			redoButtonClicks.remove(getLastListItem(redoButtonClicks));
			
			return button;
		
		} 
		
		catch (Exception e) 
		{
			
			e.printStackTrace();
			throw new Exception();
			
		}		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
