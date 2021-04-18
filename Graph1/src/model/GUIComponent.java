package model;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

public class GUIComponent
{
	public Button[][] buttonMatrix;
	

	public GUIComponent(int matrixSize)
	{
		buttonMatrix = new Button[matrixSize][matrixSize];
		initButtons(buttonMatrix);
		defaultButtonMatrix();
		matrixLambdas();
	}


	public void defaultButtonMatrix()
	{
		int y;
		int x = 200;
		for (int i = 0; i < buttonMatrix.length; i++)
		{
			y = 200;
			for (int j = 0; j < buttonMatrix[i].length; j++)
			{
				buttonMatrix[i][j].setTranslateX(x);
				buttonMatrix[i][j].setTranslateY(y);
				y += 50;
			}
			x += 50;
		}
	}

	public Button[][] getButtonMatrix()
	{
		return buttonMatrix;
	}

	public void fillDropDownMenue(ComboBox dropDownMenue)
	{
		for (int i = 0; i < 15; i++)
		{
			Integer boxItem = new Integer(i + 1);
			dropDownMenue.getItems().add(boxItem);
		}
		dropDownMenue.setValue(10);
	}

	public GridPane buttonsToLayout(GridPane layout)
	{
		for (Button[] buttonArray : buttonMatrix)
		{
			for (Button button : buttonArray)
			{
				layout.getChildren().add(button);

			}
		}
		return layout;
	}

	public void flipThatBit(Button button)
	{
		if (button.getText() == "0")
			button.setText("1");
		else
			button.setText("0");
	}

	public void matrixLambdas()
	{
		int i = 0;
		int j;
		for (Button[] buttonArray : buttonMatrix)
		{
			j = 0;
			for (Button button : buttonArray)
			{
				if (i != j)
					button.setOnAction(e ->
					{
						flipThatBit(button);
						flipThatBit(symmetricButton(button));
					});
				j++;
			}
			i++;
		}
	}

	public Button symmetricButton(Button button)
	{
		Button symmetricButton = new Button("0");
		for (int i = 0; i < buttonMatrix.length; i++)
		{
			for (int j = 0; j < buttonMatrix[i].length; j++)
			{
				if (buttonMatrix[i][j] == button)
					symmetricButton = buttonMatrix[j][i];
			}
		}
		return symmetricButton;
	}

	public int[][] getAdjazenzMatrix()
	{
		int[][] adjazenzMatrix = new int[buttonMatrix.length][buttonMatrix[0].length];
		for (int i = 0; i < buttonMatrix.length; i++)
		{
			for (int j = 0; j < buttonMatrix[i].length; j++)
			{
				if (buttonMatrix[i][j].getText() == "0")
					adjazenzMatrix[i][j] = 0;
				else
					adjazenzMatrix[i][j] = 1;
			}
		}
		return adjazenzMatrix;
	}

	public Label[][] matrixToLabel(int[][] intMatrix, boolean wegMatrix)
	{
		
		Label[][] buffer= new Label[intMatrix.length][intMatrix.length];
		initLabels(buffer);
		for (int i = 0; i < intMatrix.length; i++)
		{
			// y=yCopy;
			// x+=40; âˆž
			for (int j = 0; j < intMatrix.length; j++)
			{
				String s;
				if (intMatrix[i][j] == 0 && i!=j && wegMatrix==true)
				{
					s = "0";
				}else if(intMatrix[i][j] == 0 && i!=j && wegMatrix==false){
					s = "∞";
				}else
				{
					s = "" + intMatrix[i][j];
				}
				buffer[i][j].setText(s);
				// labelMatrix[i][j].setLocation(x, y);
				// y+=40;
			}
		}
		return buffer;
	}

	public void nodesToLayout(GridPane layout, Node[][] label, int x, int y)
	{
		int yCopy = y;
		for (Node[] labelArray : label)
		{
			y = yCopy;

			for (Node s : labelArray)
			{
				s.setTranslateX(x);
				s.setTranslateY(y);
				layout.getChildren().add(s);
				y += 40;
			}
			x += 40;
		}
	}

	public void initLabels(Label[][] labels)
	{
		for (int i = 0; i < labels.length; i++)
		{
			for (int j = 0; j < labels.length; j++)
			{
				labels[i][j] = new Label("0");
			}
		}
	}
	
	public void initButtons(Button[][] button)
	{
		for (int i = 0; i < button.length; i++)
		{
			for (int j = 0; j < button.length; j++)
			{
				button[i][j] = new Button("0");
			}
		}
	}
	
	public void cloneLabelArray(Label[][] copiedArray, Label[][] originalArray)
	{
		for (int i = 0; i < originalArray.length; i++)
		{
			copiedArray[i] = originalArray[i].clone();
		}
	}

}
