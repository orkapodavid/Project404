package ui.comp3111;

import javafx.geometry.Insets;
import java.util.ArrayList;
import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.SampleDataGenerator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The Main class of this GUI application
 * 
 * @author cspeter
 *
 */
public class Main extends Application {

	private ArrayList<DataTable> DataSets = new ArrayList<DataTable>();
	private int DataSetCount = 0;

	// Attributes: Scene and Stage
	private static final int SCENE_NUM = 5;
	private static final int SCENE_MAIN_SCREEN = 0;
	private static final int SCENE_CREATE_CHART = 1;
	private static final int SCENE_SPLIT_DATA = 2;
	private static final int SCENE_FILTER_DATA = 3;
	private static final int SCENE_SHOW_CHART = 4;
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - [Project404]", "Create Chart", "Split Data",
			"Filter Data" ,"Chart"};
	private Stage stage = null;
	private Scene[] scenes = null;

	// To keep this application more structural
	// The following UI components are used to keep references after invoking
	// createScene()

	// Screen 1: paneMainScreen
	private Button importButton, exportButton, filterButton, chartButton, showChartButton, splitButton;
	private ListView<String> dataList;
	private ListView<String> chartList;

	// Screen 2: paneCreateChartScreen
	private Label chartHeader = null;
	private Button chartCancel = null;
	private Button chartComfirm = null;
	private Label chartSelectType= null;
	private ComboBox<String> chartTypes = null;
	private Label chartSelectXaxisLabel= null;
	private ComboBox<String> chartSelectXaxis = null;
	private Label chartSelectYaxisLabel= null;
	private ComboBox<String> chartSelectYaxis = null;
	private Label chartSelectTextLabel= null;
	private ComboBox<String> chartSelectTextCol = null;
	private Label chartSelectNumLabel= null;
	private ComboBox<String> chartSelectNumCol = null;
	// Screen 3: paneSplitDataScreen
	private Label splitHeader = null;
	private Button splitCancel = null;
	private Button splitComfirm = null;
	// Screen 4: paneFilterDataScreen
	private Label filterHeader = null;
	private Button filterCancel = null;
	private Button filterComfirm = null;
	//Screen 5: paneShowChartScreen
	private Label showChartHeader = null;
	private Button showChartComfirm = null;

	/**
	 * create all scenes in this application
	 */
	private void initScenes() {
		scenes = new Scene[SCENE_NUM];
		scenes[SCENE_MAIN_SCREEN] = new Scene(paneMainScreen(), 520, 500);
		scenes[SCENE_CREATE_CHART] = new Scene(paneCreateChartScreen(), 520, 500);
		scenes[SCENE_SPLIT_DATA] = new Scene(paneSplitDataScreen(), 520, 500);
		scenes[SCENE_FILTER_DATA] = new Scene(paneFilterDataScreen(), 520, 500);
		scenes[SCENE_SHOW_CHART] = new Scene(paneShowChartScreen(),520,500);
		for (Scene s : scenes) {
			if (s != null)
				// Assumption: all scenes share the same stylesheet
				s.getStylesheets().add("Main.css");
		}
	}

	/**
	 * This method will be invoked after createScenes(). In this stage, all UI
	 * components will be created with a non-NULL references for the UI components
	 * that requires interaction (e.g. button click, or others).
	 */
	private void initEventHandlers() {
		initMainScreenHandlers();
		initSubScreenHandlers();
	}

	/**
	 * Initialize event handlers of the main screen
	 */
	private void initMainScreenHandlers() {
		importButton.setOnAction(e -> {
			testadd();
		});
		chartButton.setOnAction(e -> {
			chartHeader.setText(checkSelectedDataSet());
			putSceneOnStage(SCENE_CREATE_CHART);
		});
		filterButton.setOnAction(e -> {
			filterHeader.setText(checkSelectedDataSet());
			putSceneOnStage(SCENE_FILTER_DATA);
		});
		splitButton.setOnAction(e -> {
			splitHeader.setText(checkSelectedDataSet());
			putSceneOnStage(SCENE_SPLIT_DATA);
		});
		showChartButton.setOnAction(e->{
			putSceneOnStage(SCENE_SHOW_CHART);
		});
	}

	/**
	 * Initialize event handlers of the sub screen
	 */
	private void initSubScreenHandlers() {
		//create chart screen
		chartCancel.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
		//split data screen
		splitCancel.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
		//filter data screen
		filterCancel.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
		//show chart screen
		showChartComfirm.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
	}

	/**
	 * Create the create chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneCreateChartScreen() {
		
		Font labelFont = new Font(20);
		chartHeader = new Label();
		chartHeader.getStyleClass().add("Header");
		chartCancel = new Button("Cancel");
		chartComfirm = new Button("Comfirm");
		
		chartSelectType = new Label("Create: ");
		chartSelectType.setFont(labelFont);
		chartTypes = new ComboBox<>();
		chartTypes.getItems().addAll("Line Chart","Pie Chart");
		chartTypes.setValue("Line Chart");
		
		chartSelectXaxis = new ComboBox<>();
		chartSelectXaxis.getItems().addAll("sample1","sample2");
		chartSelectYaxis = new ComboBox<>();
		chartSelectYaxis.getItems().addAll("sample1","sample2");
		chartSelectTextCol = new ComboBox<>();
		chartSelectTextCol.getItems().addAll("sample1","sample2");
		chartSelectNumCol = new ComboBox<>();
		chartSelectNumCol.getItems().addAll("sample1","sample2");
		
		chartSelectXaxisLabel = new Label("X-axis: ");
		chartSelectXaxisLabel.setFont(labelFont);
		chartSelectYaxisLabel = new Label("Y-axis: ");
		chartSelectYaxisLabel.setFont(labelFont);
		chartSelectTextLabel = new Label("Text Label: ");
		chartSelectTextLabel.setFont(labelFont);
		chartSelectNumLabel = new Label("Numerical Column: ");
		chartSelectNumLabel.setFont(labelFont);
		
		HBox actionButtons = new HBox();
		actionButtons.setSpacing(10);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().addAll(chartCancel,chartComfirm);
		
		HBox selectionBoxes = new HBox();
		selectionBoxes.setSpacing(10); 
		selectionBoxes.setAlignment(Pos.TOP_LEFT);
		selectionBoxes.getChildren().addAll(chartSelectType,chartTypes);
		
		HBox lineChartXSelectionBoxes = new HBox();
		lineChartXSelectionBoxes.setSpacing(10); 
		lineChartXSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		lineChartXSelectionBoxes.getChildren().addAll(chartSelectXaxisLabel,chartSelectXaxis);
		
		HBox lineChartYSelectionBoxes = new HBox();
		lineChartYSelectionBoxes.setSpacing(10); 
		lineChartYSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		lineChartYSelectionBoxes.getChildren().addAll(chartSelectYaxisLabel,chartSelectYaxis);
		
		HBox pieChartTSelectionBoxes = new HBox();
		pieChartTSelectionBoxes.setSpacing(10); 
		pieChartTSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		pieChartTSelectionBoxes.getChildren().addAll(chartSelectTextLabel,chartSelectTextCol);
		
		HBox pieChartNSelectionBoxes = new HBox();
		pieChartNSelectionBoxes.setSpacing(10); 
		pieChartNSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		pieChartNSelectionBoxes.getChildren().addAll(chartSelectNumLabel,chartSelectNumCol);
		
		boolean h = true;
		if(!h) {
			chartSelectTextLabel.getStyleClass().add("combo-box-base:disabled");
			chartSelectTextLabel.setDisable(true);
			chartSelectTextCol.getStyleClass().add("combo-box-base:disabled");
			chartSelectTextCol.setDisable(true);
			chartSelectNumLabel.getStyleClass().add("combo-box-base:disabled");
			chartSelectNumLabel.setDisable(true);
			chartSelectNumCol.getStyleClass().add("combo-box-base:disabled");
			chartSelectNumCol.setDisable(true);
		}else {
			chartSelectXaxis.getStyleClass().add("combo-box-base:disabled");
			chartSelectXaxis.setDisable(true);
			chartSelectYaxis.getStyleClass().add("combo-box-base:disabled");
			chartSelectYaxis.setDisable(true);
			chartSelectXaxisLabel.getStyleClass().add("combo-box-base:disabled");
			chartSelectXaxisLabel.setDisable(true);
			chartSelectYaxisLabel.getStyleClass().add("combo-box-base:disabled");
			chartSelectYaxisLabel.setDisable(true);
		}
			
		VBox container = new VBox();
		container.setSpacing(10); 
		container.setAlignment(Pos.TOP_LEFT);
		container.getChildren().addAll(selectionBoxes,lineChartXSelectionBoxes,lineChartYSelectionBoxes,pieChartTSelectionBoxes,pieChartNSelectionBoxes);
		
		BorderPane pane= new BorderPane();
		pane.setPadding(new Insets(30, 30, 10, 20));
		pane.setTop(container);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");
		
		return pane;
	}

	/**
	 * Create the Filter Data screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneFilterDataScreen() {

		filterHeader = new Label();
		filterHeader.getStyleClass().add("Header");
		filterComfirm = new Button("Comfirm");
		filterCancel = new Button("Cancel");

		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().addAll(filterCancel,filterComfirm);

		BorderPane pane = new BorderPane();
		pane.setTop(filterHeader);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * Create the Split Data screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneSplitDataScreen() {

		splitHeader = new Label();
		splitHeader.getStyleClass().add("Header");
		splitComfirm = new Button("Comfirm");
		splitCancel = new Button("Cancel");

		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().addAll(splitCancel,splitComfirm);
		
		BorderPane pane = new BorderPane();
		pane.setTop(splitHeader);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}
	
	/**
	 * Create the show chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneShowChartScreen() {

		showChartHeader = new Label();
		showChartHeader.getStyleClass().add("Header");
		showChartComfirm = new Button("Back");

		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().add(showChartComfirm);

		BorderPane pane = new BorderPane();
		pane.setTop(showChartHeader);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}


	/**
	 * Creates the main screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneMainScreen() {

		MenuBar menuBar = new MenuBar();
		Menu FileIO = new Menu("File");
		MenuItem Save = new MenuItem("Save");
		MenuItem Load = new MenuItem("Load");
		FileIO.getItems().addAll(Save, Load);
		menuBar.getMenus().add(FileIO);

		dataList = new ListView<String>();
		chartList = new ListView<String>();
		chartList.getItems().addAll("Chart1", "Chart2");

		importButton = new Button("Import");
		exportButton = new Button("Export");
		filterButton = new Button("Filter Data");
		splitButton = new Button("Split Data");
		chartButton = new Button("Create Chart");
		showChartButton = new Button("Show Chart");

		// Layout the UI components

		HBox hc = new HBox(20);
		hc.setAlignment(Pos.CENTER);
		hc.getChildren().addAll(dataList, chartList);

		HBox buttons = new HBox(20);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(importButton, exportButton, filterButton, splitButton, chartButton,
				showChartButton);

		VBox container = new VBox(20);
		container.getChildren().addAll(hc, new Separator(), buttons);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setTop(menuBar);
		pane.setCenter(container);

		// Apply style to the GUI components
		// btSampleLineChart.getStyleClass().add("comfirm-button");
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * This method is used to pick anyone of the scene on the stage. It handles the
	 * hide and show order. In this application, only one active scene should be
	 * displayed on stage.
	 * 
	 * @param sceneID
	 *            - The sceneID defined above (see SCENE_XXX)
	 */
	private void putSceneOnStage(int sceneID) {

		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
			return;

		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.setResizable(true);
		stage.show();
	}

	/**
	 * All JavaFx GUI application needs to override the start method You can treat
	 * it as the main method (i.e. the entry point) of the GUI application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			stage = primaryStage; // keep a stage reference as an attribute
			initScenes(); // initialize the scenes
			initEventHandlers(); // link up the event handlers
			putSceneOnStage(SCENE_MAIN_SCREEN); // show the main screen

		} catch (Exception e) {

			e.printStackTrace(); // exception handling: print the error message on the console
		}
	}

	/**
	 * main method - only use if running via command line
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public void testadd() {
		DataTable t = new DataTable();
		DataSets.add(t);
		String name = "DataSet" + DataSetCount++;
		dataList.getItems().add(name);
	}

	private String checkSelectedDataSet() {
		return "Selected DataSet: DataSet" + dataList.getSelectionModel().getSelectedIndex();
	}
}
