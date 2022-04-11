package model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

public class AdjacencyMatrix extends Matrix
{

	private int[][] adjacencyMatrix;

	public AdjacencyMatrix(int size) 
	{
		
		adjacencyMatrix = new int[size][size];

	}

	//Reads the the text of the single buttons and creates a matrix out of it
	public void setAdjacencyMatrix(int[][] matrix)
	{
		
		cloneMatrix(adjacencyMatrix, matrix);
		
	}
	
	public void diagAdjacencyMatrixDigits(List<ArrayList<Button>> screenInput, int i, int j)
	{

			Integer integer = Integer.parseInt(screenInput.get(i).get(j).getText());
			adjacencyMatrix[i][j] = integer.intValue();
			adjacencyMatrix[j][i] = integer.intValue();

	}
	
	public int[][] getAdjacencyMatrix()
	{
		
		return adjacencyMatrix;
		
	}
	
	public void changeMatrixSize(int i)
	{
		
		adjacencyMatrix = new int[i][i];
		
	}
	



}
