package application;

import java.awt.Event;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private Stage primaryStage;
	private int chosenFontSize = 17;
	private int matrixSize = 10;
	private final int sceneWidth = 1380;
	private VBox vBoxCenter;
	private EditTyped editTyped = new EditTyped();
	private List<ArrayList<Button>> buttons = new ArrayList<ArrayList<Button>>(matrixSize);


	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("GraphsAreFun");
		mainWindow();
		
	}

	@SuppressWarnings("static-access")
	public void mainWindow() {

		//Set Children for error layout
		Label errorLabel = new Label("Something went wrong.");
		errorLabel.setFont(new Font(chosenFontSize));
		
		Button errorOkButton = new Button("Ok");
		errorOkButton.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: white");
		errorOkButton.setOnAction(event ->
		{
			
			Stage stage = (Stage) errorOkButton.getScene().getWindow();
			stage.close();
			
		});
		
		//Set error layout
		VBox errorPane = new VBox();
		errorPane.setAlignment(Pos.CENTER);
		errorPane.setStyle("-fx-background-color:a9a9a9");
		errorPane.setSpacing(10);
		errorPane.getChildren().addAll(errorLabel, errorOkButton);
		
		//Set main-layout
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(0));
		
		//Set error scene
		final int errorSceneWidth = 300;
		Scene errorScene = new Scene(errorPane, errorSceneWidth, 100);
		
		//Set error stage
		Stage errorStage = new Stage();
		errorStage.setTitle("Error");
		errorStage.setResizable(false);
		errorStage.initOwner(primaryStage);
		errorStage.initModality(Modality.APPLICATION_MODAL);
		errorStage.setScene(errorScene);

		//Set file menu
		Menu fileMenu = new Menu("_File");

		//Setting items and shortcuts for file menu
		MenuItem newProject = new MenuItem("New Project...");
		MenuItem newFile = new MenuItem("New File...");
		MenuItem openProject = new MenuItem("Open Project...");
		MenuItem openFile = new MenuItem("Open File...");

		MenuItem save = new MenuItem("Save");
		KeyCombination saveKeyCombi = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		save.setAccelerator(saveKeyCombi);
		save.setMnemonicParsing(true);
		save.setOnAction(e -> {
			System.out.println("File saved.");
			
		});

		MenuItem saveAs = new MenuItem("Save As...");
		KeyCombination saveAsKeyCombi = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
				KeyCombination.SHIFT_DOWN);
		saveAs.setAccelerator(saveAsKeyCombi);
		saveAs.setOnAction(e -> System.out.println("File saved as ..."));

		MenuItem saveAll = new MenuItem("Save All");
		MenuItem exit = new MenuItem("Exit");

		//Set edit menu
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
			System.out.println("Ctrl+Z - Typing undone.");
			}
			catch(Exception exception)
			{

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
			System.out.println("Ctrl+Y - Typing redone.");
			}
			catch(Exception exception)
			{

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

		//Set view menu
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

		//Set help menu
		Menu helpMenu = new Menu("Help");

		//Set items for help menu
		MenuItem programGuide = new MenuItem("Program Guide");

		//Add items to corresponding menus
		fileMenu.getItems().addAll(newProject, newFile, openProject, openFile, new SeparatorMenuItem(), save, saveAs,
				saveAll, new SeparatorMenuItem(), exit);
		editMenu.getItems().addAll(undoTyping, redoTyping, new SeparatorMenuItem(), copy, paste, new SeparatorMenuItem(),
				hideTooltips);
		viewMenu.getItems().addAll(fontSize, changeDesign, clean, resolution);
		helpMenu.getItems().add(programGuide);

		//Set menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefSize(1280, 20);
		
		//Add menus
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);
		
		//
		
		//Set HBox for size menu
		HBox innerTopHBox = new HBox();
		innerTopHBox.setFillHeight(true);
		innerTopHBox.setPadding(new Insets(15));
		innerTopHBox.setSpacing(15);
		innerTopHBox.setAlignment(Pos.CENTER_LEFT);
		
		//Set children
		Label sizeMenuLabel = new Label("Choose the size of the matrix :");
		sizeMenuLabel.setFont(new Font(chosenFontSize));
		
		ComboBox<String> sizeMenu = prepSizeMenu();
		
		//Add children
		innerTopHBox.getChildren().addAll(sizeMenuLabel, sizeMenu);

		//Setting VBox for BorderPane top region
		VBox vBoxTop = new VBox();
		vBoxTop.setPrefWidth(sceneWidth);
		vBoxTop.setPrefHeight(140);
		vBoxTop.setStyle("-fx-background-color:#696969");
		
		//Set children
		Label graphCalcLabel = new Label("GraphsAreFun");
		graphCalcLabel.setFont(new Font(44));
				
		vBoxTop.getChildren().addAll(menuBar, graphCalcLabel, innerTopHBox);
		vBoxTop.setAlignment(Pos.BASELINE_CENTER);
		
		HBox hBoxBottom = new HBox(); 
		hBoxBottom.setPrefHeight(120);
		hBoxBottom.setStyle("-fx-background-color: #696969;");
		
		//Setting VBox for BorderPane center region
		vBoxCenter= new VBox();
		vBoxCenter.setStyle("-fx-background-color: #8d8d8d;");
		vBoxCenter.setAlignment(Pos.TOP_CENTER);
		vBoxCenter.setPadding(new Insets(10, 0, 0, 0));
		vBoxCenter.setSpacing(30);
		
		//Set children
		Label adjacencyMatrixLabel = new Label("Adjacency Matrix:");
		adjacencyMatrixLabel.setFont(new Font(27));
		
		//Add children
		vBoxCenter.getChildren().add(adjacencyMatrixLabel);
		addChildArrayToVBox(vBoxCenter, addButtonMatrix());
		
		
		
		VBox vBoxLeft = prepVBox();
		VBox vBoxRight = prepVBox();

		//Embedding layouts in parent layout
		pane.setTop(vBoxTop);
		pane.setBottom(hBoxBottom);
		pane.setLeft(vBoxLeft);
		pane.setRight(vBoxRight);
		pane.setCenter(vBoxCenter);
		
		//Set main scene
		Scene scene = new Scene(pane, sceneWidth, 768);
		
		//Set primary Stage
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) 
	{
		
		launch(args);
		
	}


	//Returns a colored VBox
	public VBox prepVBox() 
	{

		VBox vBox = new VBox();
		vBox.setPrefWidth(sceneWidth/3);
		vBox.setStyle("-fx-background-color: #808080;");

		return vBox;
	}

	//Returns a button-matrix
	public List<ArrayList<Button>> addButtonMatrix()
	{
		
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
					buttons.get(i).get(j).setOnAction(e -> 
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
	
			
		if(button1 != null)
		{	
			
			Button button2;
			
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
					}
				}
			}
		}	
			
		else
		{
				
			throw new NullPointerException();
				
		}
	}
	

	//Adding button-matrix or label-matrix to vBox
	public void addChildArrayToVBox(VBox vBox, List<ArrayList<Button>> labelOrButtonMatrix) 
	  {
		
		HBox rowHBox = new HBox();
		rowHBox.setPadding(new Insets(10));
		rowHBox.setAlignment(Pos.CENTER);

		for(int i=0 ; i<labelOrButtonMatrix.size() ; i++) 
		{
			
			VBox columnVBox = new VBox();
			columnVBox.setPadding(new Insets(5));
			columnVBox.setSpacing(5);
			columnVBox.setAlignment(Pos.CENTER);
			
			for (int j=0 ; j<labelOrButtonMatrix.size() ; j++) 
			{
				
				columnVBox.getChildren().add(labelOrButtonMatrix.get(i).get(j));
				
			}
			
			rowHBox.getChildren().add(columnVBox);
			
		}
		
		vBox.getChildren().add(rowHBox);
		
	}
	
	//Creating comboBox for size menu 
	public  ComboBox<String> prepSizeMenu() {

		ComboBox<String> sizeMenu = new ComboBox<String>();
		sizeMenu.getSelectionModel().select(""+matrixSize);
		sizeMenu.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		sizeMenu.setOnAction(e -> {
				
			matrixSize = Integer.valueOf(sizeMenu.getSelectionModel().getSelectedItem());
			vBoxCenter.getChildren().remove(1);
			addChildArrayToVBox(vBoxCenter, addButtonMatrix());
				
		});
			
		for (int i = 0; i < 10; i++) 
		{
			
			sizeMenu.getItems().add(""+(i+1));
	
		}

		return sizeMenu;
	}

}
