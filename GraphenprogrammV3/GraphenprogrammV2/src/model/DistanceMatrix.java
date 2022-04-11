package model;

public class DistanceMatrix extends Matrix {
	
	private int[][] distanceMatrix;
	public DistanceMatrix(int[][] adjacencyMatrix)
	{
		
		setDistanceMatrix(adjacencyMatrix);
		
	}
	
	//Calculates the distance-matrix
	public void setDistanceMatrix(int[][] adjacencyMatrix) 
	{
		int[][] potencyMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
	    distanceMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
		cloneMatrix(distanceMatrix, adjacencyMatrix);
		cloneMatrix(potencyMatrix, adjacencyMatrix);
		
		for (int power = 2; power < adjacencyMatrix.length; power++) 
		{
			
			cloneMatrix(potencyMatrix, multiply(potencyMatrix, adjacencyMatrix));
			updatedistanceMatrix(distanceMatrix, potencyMatrix, power);
			
		}
	}
	
	public int[][] getDistanceMatrix() 
	{
		return distanceMatrix;
	}
	
	//clones a array
//	public void cloneIntArray(int[][] copiedArray, int[][] originalArray) {
//		for (int i = 0; i < originalArray.length; i++) {
//			for (int j = 0; j < originalArray.length; j++) {
//				copiedArray[i][j] = originalArray[i][j];
//			}
//		}
//	}
	
	
	public void updatedistanceMatrix(int[][] distanceMatrix, int[][] potenzMatrix, int potenz) 
	{
		for (int i = 0; i < potenzMatrix.length; i++) 
		{
			for (int j = 0; j < potenzMatrix.length; j++) {
				if (distanceMatrix[i][j] == 0 && i != j && potenzMatrix[i][j] > 0) {
					distanceMatrix[i][j] = potenz;

				}
			}
		}

	}
	
	public int[][] multiply(int[][] matrix1, int[][] matrix2)
	{
		
		int[][] resultMatrix = new int[matrix1.length][matrix1.length];

		for (int i = 0; i < matrix1.length; i++) 
		{
			for (int j = 0; j < matrix1.length; j++) 
			{
				int sum = 0;
				for (int k = 0; k < matrix1.length; k++) 
				{
					
					try 
					{
						
						sum += matrix1[i][k] * matrix2[k][j];
						
					} 
					
					catch (IndexOutOfBoundsException e) 
					{
						
						e.printStackTrace();
						
					}
					
				}
				resultMatrix[i][j] = sum;
			}
		}
		return resultMatrix;
	}
	

}
