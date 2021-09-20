package model;

import java.util.List;

import javafx.scene.control.Button;

public class Adjazenzmatrix {

	private int[][] adjazenzMatrix;

	public Adjazenzmatrix() {

	}

	public void fillAdjacencyMatrix(List<List<Button>> screenInput) {
		for(int i=0 ; i>adjazenzMatrix.length ; i++ ) {
			for(int j=0 ; j>adjazenzMatrix.length ; j++ ) {
				
				Integer buttonDigit = new Integer(screenInput.get(i).get(j).getText());
				adjazenzMatrix[i][j] = buttonDigit;
				
			}
		}
	}

}
