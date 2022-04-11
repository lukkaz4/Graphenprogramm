package application;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.oracle.webservices.internal.api.EnvelopeStyle.Style;
import com.sun.javafx.css.CalculatedValue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AdjacencyMatrix;
import model.DistanceMatrix;
import model.PathMatrix;

public class Main extends Application {

	private Stage primaryStage;
	private int fontSize = 17;
	private int matrixSize = 5;
	private int sceneWidth = 1380;
	private VBox vBoxCenter;
	private EditTyped editTyped;
	private ArrayList<ArrayList<Button>> buttons;
	private AdjacencyMatrix adjacencyMatrix = new AdjacencyMatrix(matrixSize);
	private Font font = new Font(fontSize);
	private BorderPane pane;
	private ComboBox<String> sizeMenu = null;
	private DistanceMatrix distanceMatrix;
	private PathMatrix pathMatrix;

	
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("GraphsAreFun");
		mainWindow();

		
	}

	public void mainWindow() {

		//Setting Children for error layout
		Label errorLabel = new Label("I found a bug.");
		errorLabel.setFont(font);
		
		Button errorOkButton = new Button("Ok");
		errorOkButton.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: white");
		errorOkButton.setOnAction(event ->
		{
			
			errorLabel.setText("I found a bug.");
			
			Stage stage = (Stage) errorOkButton.getScene().getWindow();
			stage.close();
			
		});
		
		//Setting error layout
		VBox errorPane = new VBox();
		errorPane.setAlignment(Pos.CENTER);
		errorPane.setStyle("-fx-background-color:a9a9a9");
		errorPane.setSpacing(10);
		errorPane.getChildren().addAll(errorLabel, errorOkButton);
		
		//Setting main-layout
		pane = new BorderPane();
		
		//Setting error scene
		final int errorSceneWidth = 300;
		Scene errorScene = new Scene(errorPane, errorSceneWidth, 100);
		
		//Setting error stage
		Stage errorStage = new Stage();
		errorStage.setTitle("Error");
		errorStage.setResizable(false);
		errorStage.initOwner(primaryStage);
		errorStage.initModality(Modality.APPLICATION_MODAL);
		errorStage.setScene(errorScene);

		//Setting file menu
		Menu fileMenu = new Menu("_File");

		//Setting items and shortcuts for file menu
		MenuItem newProject = new MenuItem("New Project...");
		
		MenuItem newFile = new MenuItem("New File...");
		KeyCombination newFileCombi = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN); 
		newFile.setAccelerator(newFileCombi);
		newFile.setOnAction(e->{
			
			callDefaultScreen();
			
		});
		MenuItem loadProject = new MenuItem("Load Project...");
		
		MenuItem loadFile = new MenuItem("Load File...");
		loadFile.setOnAction(e ->
		{
			
			FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All Files", "*.*");
			FileChooser.ExtensionFilter extFilterDat = new FileChooser.ExtensionFilter("DAT files", "*.dat");

			FileChooser loadFileChooser = new FileChooser();
			loadFileChooser.getExtensionFilters().addAll(extFilterAll, extFilterDat);
			
			File file = loadFileChooser.showOpenDialog(primaryStage);
			
			FileInputStream fis = null;
			ObjectInputStream ois = null;
			
			try 
			{
				
				fis = new FileInputStream(file);
		        ois = new ObjectInputStream(fis);	
		        
		        @SuppressWarnings("unchecked")
				ArrayList<Object> loadedData = (ArrayList<Object>) ois.readObject();
		        
		        sizeMenu.setValue((String) loadedData.get(0));
		        
		        adjacencyMatrix.changeMatrixSize(((int[][]) loadedData.get(1)).length);
		        adjacencyMatrix.setAdjacencyMatrix((int[][]) loadedData.get(1));
		        printMatrix(adjacencyMatrix.getAdjacencyMatrix());
		        
		        refreshButtonMatrix(adjacencyMatrix.getAdjacencyMatrix());
		        refreshMatrices();
		         
			} 
			
			catch (NullPointerException | IOException | ClassNotFoundException ex1) 
			{
				
				errorLabel.setText("File could not be loaded.");
				errorStage.show();
				
				ex1.printStackTrace();
				
			}
			
			finally
			{
				
					try 
					{
						
						if(ois != null)
							ois.close();
						
						if(fis != null)
							fis.close();
						
					} 
					catch (IOException e1) 
					{
						
						errorStage.show();
						e1.printStackTrace();
						
					}
				
			}
			
		});

		MenuItem saveFile = new MenuItem("Save");
		KeyCombination saveKeyCombi = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		saveFile.setAccelerator(saveKeyCombi);
		saveFile.setOnAction(e -> 
		{
			
			
		});

		MenuItem saveAs = new MenuItem("Save As...");
		KeyCombination saveAsKeyCombi = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
				KeyCombination.SHIFT_DOWN);
		saveAs.setAccelerator(saveAsKeyCombi);
		saveAs.setOnAction(e -> 
		{
			
			FileChooser.ExtensionFilter extFilterDat = new FileChooser.ExtensionFilter("DAT files", "*.dat");

			FileChooser saveFileChooser = new FileChooser();
			saveFileChooser.getExtensionFilters().add(extFilterDat);
			
			File file = saveFileChooser.showSaveDialog(primaryStage);
			System.out.println(file.getAbsolutePath());
			
			FileOutputStream fos = null;
        	ObjectOutputStream oos = null;
        	
        	ArrayList<Object> saveThis = new ArrayList<Object>();
        	saveThis.add(sizeMenu.getValue());
        	saveThis.add(adjacencyMatrix.getAdjacencyMatrix());
        	
        	try 
			{
			
				fos = new FileOutputStream(file);
	        	oos = new ObjectOutputStream(fos);
	     
	        	
	        	oos.writeObject(saveThis);
	 
			} 
		
			catch (IOException ex) 
			{
			
				errorLabel.setText("File could not be saved.");
				errorStage.show();
			
				ex.printStackTrace();
	    	
	    	}
			
			finally
			{
					try 
					{
						
						if(oos != null)
							oos.close();
						
						if(fos != null)
							fos.close();
						
					} 
					catch (IOException e1) 
					{
						
						errorStage.show();
						e1.printStackTrace();
						
					}
				
			}
			
		});

		MenuItem saveAll = new MenuItem("Save All");
		MenuItem exit = new MenuItem("Exit");

		//Setting edit menu
		Menu editMenu = new Menu("_Edit");

		//Setting items and shortcuts for edit menu
		//Undo feature
		MenuItem undoTyping = new MenuItem("Undo Typing");
		KeyCombination undoTypingKeyCombi = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
		undoTyping.setAccelerator(undoTypingKeyCombi);
		undoTyping.setOnAction(e ->
		{
			try 
			{
				
			swapButtonsDigit(editTyped.getUndoButton());
			
			}
			catch(Exception exception)
			{

				errorLabel.setText("No steps left to undo.");
				if(errorStage.isShowing() == false)
				{
					
					errorStage.show();
					
				}

			}

		});
		
		//Redo feature
		MenuItem redoTyping = new MenuItem("Redo Typing");
		KeyCombination redoTypingKeyCombi = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
		redoTyping.setAccelerator(redoTypingKeyCombi);
		redoTyping.setOnAction(e ->
		{
			try 
			{
				
			swapButtonsDigit(editTyped.getRedoButton());

			}
			catch(Exception exception)
			{

				errorLabel.setText("No steps left to redo.");
				if(errorStage.isShowing() == false)
				{
					
					errorStage.show();
					
				}
			}

		});

		//Copy feature
		MenuItem copy = new MenuItem("Copy");
		KeyCombination copyKeyCombi = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
		copy.setAccelerator(copyKeyCombi);
		copy.setOnAction(e -> System.out.println("Matrix copied to clipboard."));

		//Paste feature
		MenuItem paste = new MenuItem("Paste");
		KeyCombination pasteKeyCombi = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
		paste.setAccelerator(pasteKeyCombi);
		paste.setOnAction(e -> System.out.println("Matrix from clipboard copied to screen."));

		//Tooltips feature
		MenuItem hideTooltips = new MenuItem("Hide Tooltip Description");

		//Setting view menu
		Menu viewMenu = new Menu("_View");

		//Setting items and shortcuts for view menu.
		MenuItem fontSize = new MenuItem("Font Size");
		MenuItem changeDesign = new MenuItem("Change Design");

		MenuItem clean = new MenuItem("Clean");
		KeyCombination cleanKeyCombi = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN,
				KeyCombination.SHIFT_DOWN);
		clean.setAccelerator(cleanKeyCombi);
		clean.setOnAction(e -> System.out.println("Adjacency Matrix cleared."));

		MenuItem resolution = new MenuItem("Resolution");

		//Setting help menu
		Menu helpMenu = new Menu("Help");

		//Setting items for help menu
		MenuItem programGuide = new MenuItem("Program Guide");

		//Adding items to corresponding menus
		fileMenu.getItems().addAll(newProject, newFile, loadProject, loadFile, new SeparatorMenuItem(), saveFile, saveAs,
				saveAll, new SeparatorMenuItem(), exit);
		editMenu.getItems().addAll(undoTyping, redoTyping, new SeparatorMenuItem(), copy, paste, new SeparatorMenuItem(),
				hideTooltips);
		viewMenu.getItems().addAll(fontSize, changeDesign, clean, resolution);
		helpMenu.getItems().add(programGuide);

		//Setting menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefSize(1280, 20);
		
		//Adding menus
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);
		
		
		//Setting HBox for size menu
		HBox innerTopHBox = new HBox();
		innerTopHBox.setFillHeight(true);
		innerTopHBox.setPadding(new Insets(15));
		innerTopHBox.setSpacing(15);
		innerTopHBox.setAlignment(Pos.CENTER_LEFT);
		
		//Setting children
		Label sizeMenuLabel = new Label("Choose the size of the matrix :");
		sizeMenuLabel.setFont(font);
		
//		Button calculate = new Button("Calculate");
//		calculate.setFont(font);
//		calculate.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: white");
//		calculate.setOnAction(e -> 
//		{
//			
//			adjacencyMatrix = new AdjacencyMatrix(matrixSize);
//			adjacencyMatrix.setAdjacencyMatrix(buttons);
//			
//			distanceMatrix = new DistanceMatrix(adjacencyMatrix.getAdjacencyMatrix());
//			pathMatrix = new PathMatrix(matrixSize, distanceMatrix);
//			
//			
//			//Setting left screen layout
//			VBox vBoxLeft = prepVBox(distanceMatrix.getDistanceMatrix(), "Distance-Matrix");
//			
//			//Setting left screen layout
//			VBox vBoxRight = prepVBox(pathMatrix.getPathMatrix(), "Path-Matrix");
//			
//			//Adding path- and distanceMatrix-layouts to main layout
//			pane.setLeft(vBoxLeft);
//			pane.setRight(vBoxRight);
//			
//		});
		
		//Setting combobox for sizeMenu
		sizeMenu = new ComboBox<String>();
		sizeMenu.setValue(""+matrixSize);
		sizeMenu.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: white");
		sizeMenu.setOnAction(e -> {
				
			matrixSize = Integer.valueOf(sizeMenu.getValue());
			
			vBoxCenter.getChildren().remove(1);
			addChildArrayToVBox(vBoxCenter, addButtonMatrix());
			
			adjacencyMatrix = new AdjacencyMatrix(matrixSize);
			DistanceMatrix distanceMatrix = new DistanceMatrix(adjacencyMatrix.getAdjacencyMatrix());
			PathMatrix pathMatrix = new PathMatrix(matrixSize, distanceMatrix);
			
			//Setting left screen layout
			VBox vBoxLeft = prepVBox(distanceMatrix.getDistanceMatrix(), "Distance-Matrix");
			
			//Setting left screen layout
			VBox vBoxRight = prepVBox(pathMatrix.getPathMatrix(), "Path-Matrix");
			
			//Adding path- and distanceMatrix-layouts to main layout
			pane.setLeft(vBoxLeft);
			pane.setRight(vBoxRight);
				
		});
		
		fillSizeMenu(sizeMenu);
		
		//Adding children
		innerTopHBox.getChildren().addAll(sizeMenuLabel, sizeMenu);

		//Setting VBox for BorderPane top region
		VBox vBoxTop = new VBox();
		vBoxTop.setPrefWidth(sceneWidth);
		vBoxTop.setPrefHeight(768/5);
		vBoxTop.setStyle("-fx-background-color:#696969");
		
		//Setting children
		Label graphCalcLabel = new Label("GraphsAreFun");
		graphCalcLabel.setFont(new Font(44));
		
		//Adding children
		vBoxTop.getChildren().addAll(graphCalcLabel, innerTopHBox);
		vBoxTop.setAlignment(Pos.BASELINE_CENTER);
		
		HBox hBoxBottom = new HBox(); 
		hBoxBottom.setPrefHeight(768/5);
		hBoxBottom.setStyle("-fx-background-color: #696969;");
		
		//Setting VBox for BorderPane center region
		vBoxCenter= new VBox();
		vBoxCenter.setPrefHeight(10000);
		vBoxCenter.setStyle("-fx-background-color: #8d8d8d;");
		vBoxCenter.setAlignment(Pos.CENTER);
		
		//Setting children
		Label adjacencyMatrixLabel = new Label("Adjacency Matrix:");
		adjacencyMatrixLabel.setFont(new Font(27));
		
		
		//Adding children
		vBoxCenter.getChildren().add(adjacencyMatrixLabel);
		addChildArrayToVBox(vBoxCenter, addButtonMatrix());
		
		
		//Setting left and right screen layout
		VBox vBoxLeft;
		VBox vBoxRight;
		
		
			
		vBoxLeft = prepVBox(new int[matrixSize][matrixSize], "Distance-Matrix");
		vBoxRight = prepVBox(new int[matrixSize][matrixSize], "Path-Matrix");
			
		pane.setLeft(vBoxLeft);
		pane.setRight(vBoxRight);
			
		

		//Embedding layouts in parent layout
		pane.setTop(vBoxTop);
		pane.setBottom(hBoxBottom);
		pane.setCenter(vBoxCenter);
		
		//Setting Tabpane
		TabPane tabPane = new TabPane();
		
		//Setting Tab_1
		Tab tab_1 = new Tab("Tab_1", pane);
		
		//Setting "Add-Tab"-Button
		Tab addTab = new Tab("+");
		addTab.setStyle("-fx-font-size:19; -fx-padding: -3 4 5 3;");
		
		//Making the Add-Tab button open a new Tab on click
		addTab.setOnSelectionChanged(e->{
			
			
			Tab newTab = new Tab("newTab", callDefaultScreen());
			BorderPane bp = (BorderPane)newTab.getContent();
			VBox vboxTest = (VBox)bp.getChildren().get(0);
			newTab.setContent(vboxTest);
			tabPane.getTabs().add(newTab);
	        tabPane.getSelectionModel().selectLast();
	        
	        int addTabIndex = tabPane.getTabs().indexOf(addTab);
	        int tabCount = tabPane.getTabs().size();
	        
	        	tabPane.getTabs().remove(addTab);
	        	tabPane.getTabs().add(addTabIndex + 1, addTab);
	        

			
		});
		
		//Adding Tabs to tabPane
		tabPane.getTabs().addAll(tab_1,addTab);
		
		//Setting main-Pane
		VBox mainPane = new VBox();
		mainPane.getChildren().addAll(menuBar, tabPane);

		//Setting main scene
		Scene scene = new Scene(mainPane, sceneWidth, 768);
		
		//Setting primary Stage
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}




	//Comment
	public VBox prepVBox(int [][] matrix, String labelText) 
	{

		VBox vBox = new VBox();
		vBox.setPrefWidth(sceneWidth/3);
		vBox.setStyle("-fx-background-color: #808080;");
		Label label = new Label(labelText);
		label.setFont(new Font(27));
		
		ArrayList<ArrayList<Label>> labels = new ArrayList<ArrayList<Label>>();
		
		try
		{
		
			for(int i=0 ; i<matrix.length ; i++) 
			{
			
				labels.add(new ArrayList<Label>());
			
				for(int j=0 ; j<matrix.length ; j++) 
				{
				
				
					Label digitLabel = new Label(""+matrix[i][j]);
					digitLabel.setFont(font);
					labels.get(i).add(digitLabel);
				
				}	
			}
		} 
	
		catch (NullPointerException e) 
		{
			
			e.printStackTrace();
			
		}
		
		vBox.setAlignment(Pos.TOP_CENTER);
		vBox.getChildren().add(label);
		vBox.setAlignment(Pos.CENTER);
		addChildArrayToVBox(vBox, labels);
		
		return vBox;
	}

	//Returns a button-matrix
	public ArrayList<ArrayList<Button>> addButtonMatrix()
	{
		buttons = new ArrayList<ArrayList<Button>>(matrixSize);
		editTyped = new EditTyped();
		for(int i=0 ; i<matrixSize ; i++) 
		{
			
			buttons.add(new ArrayList<Button>());
			for(int j=0 ; j<matrixSize ; j++) 
			{
				
				if (j!=i) 
				{
					
					final int i2=i;
					final int j2=j;
					Button matrixFieldButton = new Button("0");
					buttons.get(i).add(matrixFieldButton);
					
					//Removes the standard glow from a pressed button and colors it by CSS
					buttons.get(i).get(j).setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: white");
					buttons.get(i).get(j).setOnMouseClicked(e -> 
					{
						
						swapButtonsDigit(buttons.get(i2).get(j2));
						editTyped.addEvent(e, 1);
						
					});

				}
				
				else 
				{
					
					Button matrixDiagonalButton = new Button("0");
					matrixDiagonalButton.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: #a9a9a9");
					buttons.get(i).add(matrixDiagonalButton);
					
				}
			}	
		}
		
		return buttons;
	}
	

	
	//Changes a button's and it's diagonal counterpart's text within a button-matrix
	public void swapButtonsDigit(Button button1) throws NullPointerException
	{
		
		try
		{	
			
			Button button2;
			
			breakHere:
			for(int i = 0 ; i < buttons.size() ; i++)
			{
				
				for(int j = 0 ; j < buttons.size() ; j++)
				{
					
					button2 = buttons.get(j).get(i);
					
					if(buttons.get(i).get(j) == button1) 
					{
							
						if(buttons.get(i).get(j).getText() == "1")
						{
						
							button1.setText("0");
							button2.setText("0");
														
						}
					
						else 
						{
									
							button1.setText("1");
							button2.setText("1");
									
						}
						
						adjacencyMatrix.diagAdjacencyMatrixDigits(buttons, i, j);
						break breakHere;
					}
				}
			}		
		}	
			
		catch(Exception exception)
		{
				
			throw new NullPointerException();
				
		}
		
		refreshMatrices();
		
	}
	
	//Forming a labelmatrix from a 2D-ArrayList and adding it to a layout
	public void addChildArrayToVBox(VBox vBox, ArrayList<ArrayList<Label>> labelMatrix)
	  {
		
		HBox rowHBox = new HBox();
		rowHBox.setPadding(new Insets(10));
		rowHBox.setAlignment(Pos.CENTER);

		
		for(int i=0 ; i<labelMatrix.size() ; i++) 
		{
				
			VBox columnVBox = new VBox();
			columnVBox.setPadding(new Insets(11));
			columnVBox.setSpacing(5);
			columnVBox.setAlignment(Pos.CENTER);
				
			for (int j=0 ; j<labelMatrix.size() ; j++) 
			{
				try 
				{
					
					columnVBox.getChildren().add(labelMatrix.get(i).get(j));
				
				}
					
				catch (NullPointerException e) 
				{
					
					e.printStackTrace();	
						
				}	
			}
				
			rowHBox.getChildren().add(columnVBox);
				
		}
			
		vBox.getChildren().add(rowHBox);
		 
		
		
	}
	
	//Forming a buttonmatrix from a 2D-ArrayList and adding it to a layout
	public void addChildArrayToVBox(VBox vBox, List<ArrayList<Button>> buttonMatrix)
	  {
		
		HBox rowHBox = new HBox();
		rowHBox.setPadding(new Insets(10));
		rowHBox.setAlignment(Pos.CENTER);

		
		for(int i=0 ; i<buttonMatrix.size() ; i++) 
		{
				
			VBox columnVBox = new VBox();
			columnVBox.setPadding(new Insets(5));
			columnVBox.setSpacing(5);
			columnVBox.setAlignment(Pos.CENTER);
				
			for (int j=0 ; j<buttonMatrix.size() ; j++) 
			{
				try 
				{
					
				columnVBox.getChildren().add(buttonMatrix.get(i).get(j));
					
				}
					
				catch (NullPointerException e) 
				{

					e.printStackTrace();
						
				}
					
			}
				
			rowHBox.getChildren().add(columnVBox);
			
		} 
		
		vBox.getChildren().add(rowHBox);

	}
	
	public void refreshButtonMatrix(int[][] loadedMatrix)
	{
		
		for (int i=0 ; i<loadedMatrix.length ; i++)
		{
			
			for (int j=0 ; j<loadedMatrix.length ; j++)
			{
				
				buttons.get(i).get(j).setText(""+loadedMatrix[i][j]);
				
			}	
			
		}
		
	}
	
//	public int[][] parseMatrixFromButtons() throws NumberFormatException
//	{
//		int[][] adjacencyMatrix = new int[buttons.size()][buttons.size()];
//		for(int i=0 ; i<buttons.size() ; i++ ) 
//		{
//
//			for(int j=0 ; j<buttons.size() ; j++ ) 
//			{
//				
//				try 
//				{
//					
//					Integer integer = Integer.parseInt(buttons.get(i).get(j).getText());
//					adjacencyMatrix[i][j] = integer.intValue();
//					
//					return adjacencyMatrix;
//				} 
//				
//				catch (NumberFormatException e) 
//				{
//					
//					e.printStackTrace();
//					throw new NumberFormatException();
//					
//				}		
//				
//			}
//		}
//		return null;
//	}
		
	
	//filling a combobox with options 1-10 to choose
	public  void fillSizeMenu(ComboBox<String> sizeMenu) 
	{
	
		for (int i = 0; i < 10; i++) 
		{
			
			sizeMenu.getItems().add(""+(i+1));
	
		}
		
	}
	
//	public void cloneIntArray(int[][] copiedArray, int[][] originalArray)
//	{
//		
//		for (int i = 0; i < originalArray.length; i++) 
//		{
//			
//			for (int j = 0; j < originalArray.length; j++) 
//			{
//				
//				copiedArray[i][j] = originalArray[i][j];
//				
//			}
//		}
//	}
	
	
	public static void main(String[] args) 
	{
		
		launch(args);
		
	}
	

	public void printMatrix(int[][] matrix)
	{
		
		for (int i = 0; i < matrix.length; i++) 
		{
			
			for (int j = 0; j < matrix.length; j++) 
			{
				
				System.out.print(matrix[i][j]);
		
			}
			
			System.out.println();
			
		}
	}
	
	//Refreshes Path-, Distance- and Adjacencymatrix
	public void refreshMatrices()
	{
		
		DistanceMatrix distanceMatrix = new DistanceMatrix(adjacencyMatrix.getAdjacencyMatrix());
		PathMatrix pathMatrix = new PathMatrix(adjacencyMatrix.getAdjacencyMatrix().length, distanceMatrix);
		
		
		//Setting left screen layout
		VBox vBoxLeft = prepVBox(distanceMatrix.getDistanceMatrix(), "Distance-Matrix");
		
		//Setting left screen layout
		VBox vBoxRight = prepVBox(pathMatrix.getPathMatrix(), "Path-Matrix");
		
		//Adding path- and distanceMatrix-layouts to main layout
		pane.setLeft(vBoxLeft);
		pane.setRight(vBoxRight);
		
	}
	
	public BorderPane callDefaultScreen() {
		
		matrixSize = 5;
		sizeMenu.setValue("5");
		
		VBox vBoxCenter = new VBox();
		//vBoxCenter.getChildren().remove(1);
		addChildArrayToVBox(vBoxCenter, addButtonMatrix());
		
		adjacencyMatrix = new AdjacencyMatrix(matrixSize);
		DistanceMatrix distanceMatrix = new DistanceMatrix(adjacencyMatrix.getAdjacencyMatrix());
		PathMatrix pathMatrix = new PathMatrix(matrixSize, distanceMatrix);
		
		//Setting left screen layout
		VBox vBoxLeft = prepVBox(distanceMatrix.getDistanceMatrix(), "Distance-Matrix");
		
		//Setting left screen layout
		VBox vBoxRight = prepVBox(pathMatrix.getPathMatrix(), "Path-Matrix");
		
		//Adding path- and distanceMatrix-layouts to layout
		BorderPane pane = new BorderPane();
		pane.setLeft(vBoxLeft);
		pane.setRight(vBoxRight);
		
		return pane;
		
	}
	
	
	

}
