package model;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import com.sun.javafx.css.parser.*;


public class Window extends Application
{
	private int matrixDefaultSize=10;
	private GUIComponent g;
	private GridPane layout;
	private Scene scene;
	private Label[][] wegMatrix= new Label[matrixDefaultSize][matrixDefaultSize];
	private Label[][] distanzMatrix=new Label[matrixDefaultSize][matrixDefaultSize];
	private Label knotenGrade;
	private Label exzentrizitaeten;
	private Label zentrumRadiusDurchmesser;
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		
		
		layout = new GridPane();
		g = new GUIComponent(matrixDefaultSize);
		ComboBox<Integer> dropDownMenue = new ComboBox<Integer>();
		g.fillDropDownMenue(dropDownMenue);
		Button calculate = new Button("Rechne");
		calculate.setTranslateX(700);
		calculate.setTranslateY(900);
		
		
		
		
		g.nodesToLayout(layout, g.getButtonMatrix(), 635, 100);
		layout.getChildren().add(dropDownMenue);
		layout.getChildren().add(calculate);
		scene = new Scene(layout, 1800, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();
		

		
		calculate.setOnAction(e ->{
			layout.getChildren().removeIf(n-> (n instanceof Label));
			
			Matrix matrix = new Matrix(g.getAdjazenzMatrix());
			matrix.printDistanz(matrix.getDistanzMatrix(matrix.adjazenzMatrixOrigin));
			
			g.cloneLabelArray(distanzMatrix, g.matrixToLabel(matrix.getDistanzMatrix(matrix.adjazenzMatrixOrigin), false));
			g.cloneLabelArray(wegMatrix, g.matrixToLabel(matrix.getWegMatrix(matrix.getDistanzMatrix(matrix.adjazenzMatrixOrigin)),true));
			g.nodesToLayout(layout, distanzMatrix, 28, 100);
			g.nodesToLayout(layout,wegMatrix, 1250, 100);
			
			knotenGrade=new Label(matrix.getKnotengrade());
			knotenGrade.setTranslateX(100);
			knotenGrade.setTranslateY(600);
			layout.getChildren().add(knotenGrade);
			
			exzentrizitaeten=new Label(matrix.getExzentrizitaeten());
			exzentrizitaeten.setTranslateX(250);
			exzentrizitaeten.setTranslateY(600);
			layout.getChildren().add(exzentrizitaeten);
			
			String s=""+matrix.getZentrum()+"\n\n"+matrix.getRadius()+"\n\n"+matrix.getDurchmesser();
			zentrumRadiusDurchmesser=new Label(s);
			zentrumRadiusDurchmesser.setTranslateX(400);
			zentrumRadiusDurchmesser.setTranslateY(600);
			layout.getChildren().add(zentrumRadiusDurchmesser);
			
			int[][] buffybuffer=matrix.getWegMatrix(matrix.getDistanzMatrix(matrix.adjazenzMatrixOrigin));
			matrix.leseKomps(buffybuffer, true);
			System.out.println(matrix.artikulationenString());
			System.out.println(matrix.brueckenString());
			
			//System.out.println(matrix.komponentenToString());
		
			

		});
		
		dropDownMenue.setOnAction(e -> {
		int chosenSize = dropDownMenue.getSelectionModel().getSelectedItem();
		wegMatrix = new Label[chosenSize][chosenSize];
		distanzMatrix = new Label[chosenSize][chosenSize];
		layout = new GridPane();
		g = new GUIComponent((int)dropDownMenue.getValue());
		g.nodesToLayout(layout, g.getButtonMatrix(), 635, 100);
		layout.getChildren().add(dropDownMenue);
		layout.getChildren().add(calculate);
		scene = new Scene(layout, 1800, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();
		});
	}
	
	

}
