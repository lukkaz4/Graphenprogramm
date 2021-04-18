package model;

import java.util.ArrayList;

public class Matrix {
	int[][] adjazenzMatrixOrigin;
	// int[][] potenzMatrix;
	int[][] potenzMatrixArtikulation;
	// int[][] matrixBuffer;
	int[][] matrixBufferArtikulation;
//	int[][] distanzMatrix;
	int[][] distanzMatrixArtikulation;
	// int[][] komponentenMatrix;
	int[][] komponentenFertig;
	int[][] newArray;
	int[][] komponentenMatrixArtikulationen;
	int[] spaltenNummern;
	String radius;
	String komponenten;
	// int amountOfComponents;
	int amountOfComponentsCopy;
	int amountOfComponentsArtikulationen;
	int[] exzentrizitaeten;
	int matrixSizeMinusOne;
	ArrayList<ArrayList<Integer>> columnNumbers;

	public Matrix(int[][] adjazenzMatrixOrigin) {
		matrixSizeMinusOne = adjazenzMatrixOrigin.length - 1;
		potenzMatrixArtikulation = new int[matrixSizeMinusOne][matrixSizeMinusOne];
		distanzMatrixArtikulation = new int[matrixSizeMinusOne][matrixSizeMinusOne];
		matrixBufferArtikulation = new int[matrixSizeMinusOne][matrixSizeMinusOne];
		newArray = new int[matrixSizeMinusOne][matrixSizeMinusOne];
		spaltenNummern = new int[adjazenzMatrixOrigin.length];
		exzentrizitaeten = new int[adjazenzMatrixOrigin.length];
		columnNumbers = new ArrayList<ArrayList<Integer>>();
		// cloneIntArray(distanzMatrix, adjazenzMatrix);
		// cloneIntArray(potenzMatrix, adjazenzMatrix);
		this.adjazenzMatrixOrigin = adjazenzMatrixOrigin;
		amountOfComponentsCopy = 0;

		amountOfComponentsArtikulationen = 0;
		komponentenMatrixArtikulationen = new int[matrixSizeMinusOne][matrixSizeMinusOne];
	}

	public int[][] multiply(int[][] matrix1, int[][] matrix2) {
		int[][] resultMatrix = new int[matrix1.length][matrix1.length];

		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				int sum = 0;
				for (int k = 0; k < matrix1.length; k++) {
					sum += matrix1[i][k] * matrix2[k][j];
				}
				resultMatrix[i][j] = sum;
			}
		}
		// printDistanz(resultMatrix);
		return resultMatrix;
	}

	public void updateDistanzMatrix(int[][] distanzMatrix, int[][] potenzMatrix, int potenz) {

		for (int i = 0; i < potenzMatrix.length; i++) {
			for (int j = 0; j < potenzMatrix.length; j++) {
				if (distanzMatrix[i][j] == 0 && i != j && potenzMatrix[i][j] > 0) {
					distanzMatrix[i][j] = potenz;

				}
			}
		}

	}

	public int[][] getDistanzMatrix(int[][] adjazenzMatrix) {
		int[][] potenzMatrix = new int[adjazenzMatrix.length][adjazenzMatrix.length];
		int[][] distanzMatrix = new int[adjazenzMatrix.length][adjazenzMatrix.length];
		cloneIntArray(distanzMatrix, adjazenzMatrix);
		cloneIntArray(potenzMatrix, adjazenzMatrix);
		for (int power = 2; power < adjazenzMatrix.length; power++) {
			cloneIntArray(potenzMatrix, multiply(potenzMatrix, adjazenzMatrix));
			updateDistanzMatrix(distanzMatrix, potenzMatrix, power);

			// printDistanz(potenzMatrix);

		}

		return distanzMatrix;
	}

	public void printDistanz(int[][] distanzMatrix) {
		for (int i = 0; i < distanzMatrix.length; i++) {
			for (int j = 0; j < distanzMatrix.length; j++) {
				System.out.print(distanzMatrix[i][j]);
			}
			System.out.println();
		}
	}

	public void cloneIntArray(int[][] copiedArray, int[][] originalArray) {
		for (int i = 0; i < originalArray.length; i++) {
			for (int j = 0; j < originalArray.length; j++) {
				copiedArray[i][j] = originalArray[i][j];
			}
		}
	}

	public int[][] getWegMatrix(int[][] distanzMatrix) {
		int[][] matrixBuffer = new int[distanzMatrix.length][distanzMatrix.length];
		for (int i = 0; i < distanzMatrix.length; i++) {
			for (int j = 0; j < distanzMatrix.length; j++) {
				if (distanzMatrix[i][j] == 0 && i != j) {
					matrixBuffer[i][j] = 0;
				} else {
					matrixBuffer[i][j] = 1;
				}
			}

		}
		return matrixBuffer;
	}

	public char getColumnName(int columnNumber) {
		int i = 65 + columnNumber;
		char c = (char) i;
		return c;
	}

	public void printArray(ArrayList<ArrayList<Integer>> komponenten) { // testMethode
		System.out.print(komponenten.size());
	}

	public void printArrays(ArrayList<ArrayList<Integer>> komponenten) { // testMethode
		for (int i = 0; i < komponenten.size(); i++) {

			for (int j = 0; j < komponenten.get(i).size(); j++) {
				System.out.print(komponenten.get(i).get(j));
			}
			System.out.println();
		}

	}

	public void printArrays(int[][] arrays) { // testMethode
		for (int i = 0; i < arrays.length; i++) {

			for (int j = 0; j < arrays.length; j++) {
				System.out.print(arrays[j][i]);
			}
			System.out.println();
		}

	}

	public String getKnotengrade() {
		String s = "KNOTENGRADE:\n";
		for (int i = 0; i < adjazenzMatrixOrigin.length; i++) {
			int kantenZähler = 0;
			s += "Knoten " + (i + 1) + ":";
			for (int j = 0; j < adjazenzMatrixOrigin.length; j++) {
				if (adjazenzMatrixOrigin[i][j] == 1) {
					kantenZähler++;
				}
			}
			s += kantenZähler;
			s += "\n";
		}
		System.out.println(s);
		return s;
	}

	public String getExzentrizitaeten() {
		int[][] distanzMatrix = getDistanzMatrix(adjazenzMatrixOrigin);
		String s = "EXZENTRIZITÄTEN:\n";
		for (int i = 0; i < adjazenzMatrixOrigin.length; i++) {
			int exZaehler = 0;
			s += "Knoten " + (i + 1) + ":";
			for (int j = 0; j < adjazenzMatrixOrigin.length; j++) {
				if (distanzMatrix[i][j] > exZaehler) {
					exZaehler = distanzMatrix[i][j];
				}
			}
			exzentrizitaeten[i] = exZaehler;
			s += exZaehler;
			s += "\n";
		}
		System.out.println(s);
		return s;
	}

	public String readZentrum() {
		ArrayList<Integer> zentrum = new ArrayList<Integer>();
		int lowest = 15;
		for (int i = 0; i < exzentrizitaeten.length; i++) {
			if (exzentrizitaeten[i] < lowest && exzentrizitaeten[i] != 0) {
				lowest = exzentrizitaeten[i];

			}
		}

		for (int i = 0; i < exzentrizitaeten.length; i++) {
			if (exzentrizitaeten[i] == lowest) {
				zentrum.add(i);
			}
		}
		radius = "RADIUS: " + lowest;
		String s = "ZENTRUM: \nKnoten: [";
		for (int i = 0; i < zentrum.size(); i++) {
			if (i == zentrum.size() - 1) {
				s += (1 + zentrum.get(i));
			} else
				s += (1 + zentrum.get(i)) + ", ";
		}

		s += "]";
		return s;
	}

	public String readDurchmesser() {
		int largestExz = 0;
		for (int i = 0; i < exzentrizitaeten.length; i++) {
			if (exzentrizitaeten[i] > largestExz)
				largestExz = exzentrizitaeten[i];
		}
		return "DURCHMESSER: " + largestExz;
	}

	public String getDurchmesser() {
		return readDurchmesser();
	}

	public String getRadius() {
		return radius;
	}

	public String getZentrum() {
		return readZentrum();
	}

	
	
	private boolean checkForEmptyArray(int[] aray) {
		for (int i = 0; i < aray.length; i++) {
			if (aray[i] != 0 || aray[i]!=-100)
				return false;
		}
		return true;
	}

	public String komponentenZusammengefasst(int[] fromKompMatrix) {
		String s = "";
		if (checkForEmptyArray(fromKompMatrix))
			return null;
		int gleich = 0;
		int[][] bufferWegMatrix = getWegMatrix(getDistanzMatrix(adjazenzMatrixOrigin));
		for (int i = 0; i < fromKompMatrix.length; i++) {
			for (int j = 0; j < fromKompMatrix.length; j++) {
				if (bufferWegMatrix[i][j] == fromKompMatrix[j]) {
					gleich++;
				}
				if (gleich == fromKompMatrix.length) {
					//System.out.println("sss" + gleich);
					s += (i + 1) + ", ";

				}

			}

			gleich = 0;

		}
		s = entferneLetztenChar(s);

		s = entferneLetztenChar(s);

		//System.out.println("STIMMT DAS==!=!=?!??!?!?!" + s);
		return s;

	}

	public String entferneLetztenChar(String str) {
		if (str != null && str.length() > 0) {
			str = str.substring(0, str.length() - 1);

		}
		return str;
	}
	
	public boolean checkForZerosOnly(int[] param) {
		for(int i=0; i < param.length; i++){
			if(param[i]!=0)
				return true;
	}
		return false;
	}

	public int leseKomps(int[][] wegMatrix, boolean fuerKomponentenString) {
		int kompZaehler = 1;
		boolean istDabei;
		int[][] komponentenMatrix = new int[wegMatrix.length][wegMatrix.length];
		int[][] spaltenNummern = new int[wegMatrix.length][wegMatrix.length];
		//for (int i = 0; i < wegMatrix.length; i++) {
			// System.out.println("-----" + i + ". " +
			// compareMatricesSingleArray(wegMatrix[i]));

			/**for (int j = 0; j < wegMatrix.length; j++) {
				komponentenMatrix[0][j] = wegMatrix[0][j];
			}**/

			for (int h = 0; h < wegMatrix.length; h++) {
				int gleich = 0;
				istDabei=false;
				for (int a = 0; a < kompZaehler; a++) {
					for (int j = 0; j < wegMatrix.length; j++) {
						if (komponentenMatrix[a][j] == wegMatrix[h][j]) {
							gleich++;
						}
						if (gleich == wegMatrix.length) {
							// System.out.println("sss" + gleich);
							istDabei=true;
													
						}
					}

					gleich = 0;
					
					if(istDabei==true) {
						
						//for (int j = 0; j < wegMatrix.length; j++) {
							
							spaltenNummern[a][getNextFreeField(spaltenNummern[a])]=1+h;									
						//}
					
				}

				}
						if(istDabei==false) {
							//for (int a = 0; a < kompZaehler; a++) {
								for (int j = 0; j < wegMatrix.length; j++) {
									komponentenMatrix[kompZaehler-1][j]=wegMatrix[h][j];									
								}								
						//	}
							kompZaehler++;
						}
						
						

			}  //hier endet H

		//}

		// artikulationenToString();
		// komponentenString();

		/*
		 * int gleich = 0; for (int h = 0; h < wegMatrix.length; h++) { for (int i = 0;
		 * i < kompZaehler; i++) { for (int j = 0; j < wegMatrix.length; j++) { if
		 * (komponentenMatrix[i][j] == wegMatrix[h][j]) { gleich++; } if (gleich ==
		 * wegMatrix.length) { // System.out.println("sss" + gleich); for (int k = 0; k
		 * < wegMatrix.length; k++) { if (spaltenNummern[h] == 0) { spaltenNummern[h] =
		 * i; // System.out.print("+" + spaltenNummern[index]); break; } } } }
		 * 
		 * gleich = 0;
		 * 
		 * } // komponentenZusammengefasst(); }
		 */

		if (fuerKomponentenString) {

			amountOfComponentsCopy = kompZaehler-1;
			System.out.println("Anzahl der Komponenten: " + (kompZaehler-1));
			komponenten = "";
			for (int i = 0; i < kompZaehler-1; i++) {
				komponenten += "Komponente " + (i+1) + ": [" + komponentenZusammengefasst(komponentenMatrix[i])
						+ "]\n";
			}
			System.out.println(komponenten);
		}
		return kompZaehler;

		// printArrays(komponentenMatrix);
	}

	
	public int getNextFreeField(int[] column) {
		
		for (int i=0; i<column.length;i++) {
			if(column[i]==0) {
				System.out.println(column[i]);
				return i;

			} 
		}
		System.out.println("getNextFreeField-Exception");
		return -1;
	}
	
	
	public void komponentenZusammengefasst() {
		for (int i = 0; i < adjazenzMatrixOrigin.length; i++) {
			// printArrays(komponentenFertig);
			System.out.print("DDDDDDD" + (spaltenNummern[i]));
		}
	}

	public void addToArray(int number, int index) {
		for (int k = 0; k < adjazenzMatrixOrigin.length; k++) {
			if (komponentenFertig[index][k] == 0)
				komponentenFertig[index][k] = number;
		}
	}

	public int compareMatrices(int[][] column1, int[] column2) {
		int allEqualCounter = 0;
		// System.out.println(column1.size());

		for (int i = 0; i < column2.length; i++) {
			for (int j = 0; j < column1.length; j++) {
				if (column1[i][j] == column2[j])
					allEqualCounter++;
			}
			if (allEqualCounter == column2.length) {
				return i;
			}
		}
		return -1;
	}

	public int[][] loescheKnoten(int knoten) {
		//System.out.println("OOOOMMMMMMGGGGG");
		int[][] adjazenzMatrix1 = new int[this.adjazenzMatrixOrigin.length][this.adjazenzMatrixOrigin.length];
		int[][] newArray = new int[matrixSizeMinusOne][matrixSizeMinusOne];
		cloneIntArray(adjazenzMatrix1, this.adjazenzMatrixOrigin);
		// printArrays(newArray);
		for (int i = 0; i < adjazenzMatrix1.length; i++) {
			for (int j = 0; j < adjazenzMatrix1.length; j++) {
				if (i == knoten || j == knoten) {
					adjazenzMatrix1[i][j] = -100;
				}

			}
		}
		int x = 0;
		int y = 0;
		printArrays(adjazenzMatrix1);
		for (int i = 0; i < (adjazenzMatrix1.length); i++) {
			//printArrays(newArray);
			System.out.println("-----------------------");
			for (int j = 0; j < adjazenzMatrix1.length; j++) {
				
				if (adjazenzMatrix1[i][j] != -100) {
					newArray[y][x] = adjazenzMatrix1[i][j];
					// System.out.println(adjazenzMatrix[i][j]);
					x++;
					//System.out.println("11");
					
					//System.out.println("WTF"+x+""+y);
					
				}
				if (x == matrixSizeMinusOne || checkForEmptyArray(adjazenzMatrix1[i])) {
					x = 0;
					y++;
				}
			}
			if (!(checkForEmptyArray(adjazenzMatrix1[i]))) {
				
				
			}
			

		}
		printArrays(newArray);
		return newArray;
	}

	public int[][] loescheBruecke(int i, int j) {
		int[][] adjazenzMatrix1 = new int[this.adjazenzMatrixOrigin.length][this.adjazenzMatrixOrigin.length];
		
		cloneIntArray(adjazenzMatrix1, this.adjazenzMatrixOrigin);
		// printArrays(newArray);
		
		adjazenzMatrix1[i][j] = 0;
		adjazenzMatrix1[j][i] = 0;
		//printArrays(adjazenzMatrix1);
		return adjazenzMatrix1;
	}
	
	
	
	
	public boolean lastTry(int i) {
		
			if ((leseKomps(getWegMatrix(getDistanzMatrix((loescheKnoten(i)))), false) -1) != amountOfComponentsCopy) {
				System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKK");
				return true;
			} 
		
		return false;
	}

	public String artikulationenString() {
		String s = "Artikulationen: [";
		for (int i = 0; i < adjazenzMatrixOrigin.length; i++) {
			//System.out.println("DAS SOLLTE SO NICHT SEIN"+i);
			if (lastTry(i)) {
				s += i +1+ ", ";
			}
		}
		return s;
		}
		
		public String brueckenString() {
			String s = "Bruecken: ";
			for (int i = 0; i < adjazenzMatrixOrigin.length; i++) {
				for (int j = 0; j < adjazenzMatrixOrigin.length; j++) {
					//System.out.println("DAS SOLLTE SO NICHT SEIN" + i);
					if (lastTryBruecken(i,j)) {
						s += "["+(i+1) + ", "+(j+1)+"], ";
					} 
				}
			}
	
		return s;
	}

		
		public boolean lastTryBruecken(int i, int j) {
			
			if ((leseKomps(getWegMatrix(getDistanzMatrix((loescheBruecke(i,j)))), false) -1) != amountOfComponentsCopy) {
				System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
				return true;
			} 
		
		return false;
	}
		
		
	public boolean checkeZugehörigkeit(int[][] matrix, int index) {
		for (int i = 0; i < matrixSizeMinusOne; i++) {
			if (matrix[index][i] == 1)
				return true;
		}
		return false;
	}


	
	
}