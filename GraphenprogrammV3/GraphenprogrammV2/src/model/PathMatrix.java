package model;

public class PathMatrix extends Matrix {
	
	public int[][] pathMatrix;
	
	public PathMatrix(int matrixSize, DistanceMatrix distanceMatrix) 
	{
		
		pathMatrix = new int[matrixSize][matrixSize];
		setPathMatrix(distanceMatrix.getDistanceMatrix());
		
	}
	
	public void setPathMatrix(int[][] distanceMatrix)
	{
		
		for(int i = 0; i < distanceMatrix.length; i++)
		{
			
			for(int j = 0; j < distanceMatrix.length; j++)
			{
				
				try 
				{
					
					if(distanceMatrix[i][j] == 0 && i != j)
						pathMatrix[i][j] = 0;
					
					else
						pathMatrix[i][j] = 1;
					
				} 
				
				catch (Exception e) 
				{

					e.printStackTrace();
					
				}
				
			}
		}
	}
	
	public int[][] getPathMatrix()
	{
		
		return pathMatrix;
		
	}
}
