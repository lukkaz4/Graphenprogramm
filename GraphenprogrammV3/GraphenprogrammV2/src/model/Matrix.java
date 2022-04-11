package model;

public class Matrix {
	
	public Matrix() {}
	
	public void cloneMatrix(int[][] copiedArray, int[][] originalArray) 
	{
		
		for (int i = 0; i < originalArray.length; i++) 
		{
			for (int j = 0; j < originalArray.length; j++) 
			{
				
				try 
				{
					
					copiedArray[i][j] = originalArray[i][j];
					
				} 
				
				catch (Exception e) 
				{
					
					e.printStackTrace();
					
				}
			}
		}
	}
	
	
}
