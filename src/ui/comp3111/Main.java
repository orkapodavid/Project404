package ui.comp3111;

import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.SampleDataGenerator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	private Map<String, DataTable> DataSets = new HashMap<String, DataTable>();
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
	private Alert noDatasetAlert = null;
	// Screen 2: paneCreateChartScreen
	private Boolean isLineChart = null;
	private String chartXaxisName = null;
	private String chartYaxisName = null;
	private String chartNumColName = null;
	private String chartTextColName = null;
	private String currentDatasetName = null;
	private DataTable currentDataTable = null;
	private Button chartCancel = null;
	private Button chartComfirm = null;
	private Label chartSelectDataset= null;
	private Label chartSelect= null;
	private ComboBox<String> chartTypes = null;
	private Label chartSelectXaxisLabel= null;
	private ComboBox<String> chartSelectXaxis = null;
	private Label chartSelectYaxisLabel= null;
	private ComboBox<String> chartSelectYaxis = null;
	private Label chartSelectTextLabel= null;
	private ComboBox<String> chartSelectTextCol = null;
	private Label chartSelectNumLabel= null;
	private ComboBox<String> chartSelectNumCol = null;
	private Alert noSelectedColAlert = null;
	// Screen 3: paneSplitDataScreen
	private Label splitHeader = null;
	private Button splitCancel = null;
	private Button splitComfirm = null;
	// Screen 4: paneFilterDataScreen
	private Label filterHeader = null;
	private Button filterCancel = null;
	private Button filterComfirm = null;
	// Screen 5: paneShowChartScreen
	private LineChart<Number, Number> lineChart = null;
	private NumberAxis xAxis = null;
	private NumberAxis yAxis = null;
	private Label showChartHeader = null;
	private Button showChartBack = null;
	
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
		initCreateChartHandlers();
		initShowChartHandlers();
		initAlertMsg();
	}
	
	private void initAlertMsg() {
		noDatasetAlert = new Alert(AlertType.INFORMATION);
		noDatasetAlert.setTitle("Reminder Dialog");
		noDatasetAlert.setHeaderText(null);
		noDatasetAlert.setContentText("No dataset is available. Please import a dataset.");
		
		noSelectedColAlert = new Alert(AlertType.WARNING);
		noSelectedColAlert.setTitle("Warning Dialog");
		noSelectedColAlert.setHeaderText(null);
		noSelectedColAlert.setContentText("Incomplete selection of data columns. Please complete your slection");
	}
	/**
	 * Initialize event handlers of the main screen
	 */
	private void initMainScreenHandlers() {
		importButton.setOnAction(e -> {
			testadd();
		});
		
		// Add ChangeListener to the ListView dataList
		dataList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> ov, final String oldValue, final String newValue)
			{
				currentDatasetName = newValue;
				
			}
		});
		
		chartButton.setOnAction(e -> {
			currentDatasetName = null;
			currentDatasetName = dataList.getSelectionModel().getSelectedItem();
			if(currentDatasetName != null) {
				chartSelectDataset.setText("Selected Dataset: " + currentDatasetName);
				currentDataTable = DataSets.get(currentDatasetName);
				chartSelectXaxis.getItems().addAll(currentDataTable.getAllNumColName());
				chartSelectYaxis.getItems().addAll(currentDataTable.getAllNumColName());
				chartSelectTextCol.getItems().addAll(currentDataTable.getAllTextColName());
				chartSelectNumCol.getItems().addAll(currentDataTable.getAllNumColName());
				putSceneOnStage(SCENE_CREATE_CHART);
			}else {
				noDatasetAlert.showAndWait();
			}
			
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
	}
	
	/**
	 * Initialize event handlers of the sub screen - SCENE_CREATE_CHART
	 */
	private void initCreateChartHandlers() {
		//create chart screen
		chartCancel.setOnAction(e -> {
			chartSelectXaxis.getItems().clear();
			chartSelectYaxis.getItems().clear();
			chartSelectNumCol.getItems().clear();
			chartSelectTextCol.getItems().clear();
			putSceneOnStage(SCENE_MAIN_SCREEN);		
		});
		
		// Add a ChangeListener to the Button chartComfirm
		chartComfirm.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	        	if(isLineChart) {
	        		chartXaxisName = null;
	        		chartYaxisName = null;
	        		chartXaxisName = chartSelectXaxis.getValue();
	        		chartYaxisName = chartSelectYaxis.getValue();
	        		if(chartXaxisName != null && chartYaxisName != null) {
	        			chartSelectXaxis.getItems().clear();
		    			chartSelectYaxis.getItems().clear();
		        		initLineChart();
	        		}else {
	        			noSelectedColAlert.showAndWait();
	        			return;
	        		}
	        		
	        	}else {
	        		chartNumColName = chartSelectNumCol.getValue();
	        		chartTextColName = chartSelectTextCol.getValue();
	        		chartSelectNumCol.getItems().clear();
	    			chartSelectTextCol.getItems().clear();
	        	}
	        	
	        	putSceneOnStage(SCENE_SHOW_CHART);
	        }
	    });
		
		// Add a ChangeListener to the ComboBox chartTypes
		chartTypes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener <String>()
		{
			public void changed(ObservableValue<? extends String> ov, final String oldValue, final String newValue){
				if(newValue == "Line Chart") {
					isLineChart = true;
					chartSelectTextLabel.getStyleClass().add("combo-box-base:disabled");
					chartSelectTextLabel.setDisable(true);
					chartSelectTextCol.getStyleClass().add("combo-box-base:disabled");
					chartSelectTextCol.setDisable(true);
					chartSelectNumLabel.getStyleClass().add("combo-box-base:disabled");
					chartSelectNumLabel.setDisable(true);
					chartSelectNumCol.getStyleClass().add("combo-box-base:disabled");
					chartSelectNumCol.setDisable(true);
					
					chartSelectXaxis.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectXaxis.setDisable(false);
					chartSelectYaxis.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectYaxis.setDisable(false);
					chartSelectXaxisLabel.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectXaxisLabel.setDisable(false);
					chartSelectYaxisLabel.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectYaxisLabel.setDisable(false);
				}else {
					isLineChart = false;
					chartSelectXaxis.getStyleClass().add("combo-box-base:disabled");
					chartSelectXaxis.setDisable(true);
					chartSelectYaxis.getStyleClass().add("combo-box-base:disabled");
					chartSelectYaxis.setDisable(true);
					chartSelectXaxisLabel.getStyleClass().add("combo-box-base:disabled");
					chartSelectXaxisLabel.setDisable(true);
					chartSelectYaxisLabel.getStyleClass().add("combo-box-base:disabled");
					chartSelectYaxisLabel.setDisable(true);
					
					chartSelectTextLabel.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectTextLabel.setDisable(false);
					chartSelectTextCol.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectTextCol.setDisable(false);
					chartSelectNumLabel.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectNumLabel.setDisable(false);
					chartSelectNumCol.getStyleClass().removeAll("combo-box-base:disabled");
					chartSelectNumCol.setDisable(false);
				}	
			}
		});
				
	}
	
	/**
	 * Initialize event handlers of the sub screen - SCENE_SHOW_CHART
	 */
	private void initShowChartHandlers() {
		//show chart screen
		showChartBack.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	        	// clear all previous series
	    		lineChart.getData().clear();
	    		putSceneOnStage(SCENE_MAIN_SCREEN);
	        }
	    });
	}

	/**
	 * Create the create chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneCreateChartScreen() {
		
		Font labelFont = new Font(20);
		chartCancel = new Button("Cancel");
		chartComfirm = new Button("Comfirm");
		
		chartSelectDataset = new Label();
		chartSelectDataset.getStyleClass().add("Header");
		chartSelect = new Label("Select type of chart");
		chartSelect.setFont(labelFont);
		chartTypes = new ComboBox<String>();
		chartTypes.getItems().addAll("Line Chart","Pie Chart");
		
		chartSelectXaxis = new ComboBox<>();
		chartSelectYaxis = new ComboBox<>();
		chartSelectTextCol = new ComboBox<>();
		chartSelectNumCol = new ComboBox<>();
		
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
		selectionBoxes.getChildren().addAll(chartSelect,chartTypes);
		
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
			
		VBox container = new VBox();
		container.setSpacing(10); 
		container.setAlignment(Pos.TOP_LEFT);
		container.getChildren().addAll(chartSelectDataset, selectionBoxes,lineChartXSelectionBoxes,lineChartYSelectionBoxes,pieChartTSelectionBoxes,pieChartNSelectionBoxes);
		
		// Default choice is "Line Chart"
		chartTypes.getSelectionModel().selectFirst();
		isLineChart = true;
		chartSelectTextLabel.getStyleClass().add("combo-box-base:disabled");
		chartSelectTextLabel.setDisable(true);
		chartSelectTextCol.getStyleClass().add("combo-box-base:disabled");
		chartSelectTextCol.setDisable(true);
		chartSelectNumLabel.getStyleClass().add("combo-box-base:disabled");
		chartSelectNumLabel.setDisable(true);
		chartSelectNumCol.getStyleClass().add("combo-box-base:disabled");
		chartSelectNumCol.setDisable(true);
		
		BorderPane pane= new BorderPane();
		pane.setPadding(new Insets(30, 30, 10, 20));
		pane.setTop(container);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");
		
		return pane;
	}
	/**
	 * Populate sample data table values to the chart view
	 */
	private void initLineChart() {

		
		if(currentDatasetName == null) {
			return;
		}
		
		// Get 2 columns
		DataColumn xCol = currentDataTable.getCol(chartXaxisName);
		DataColumn yCol = currentDataTable.getCol(chartYaxisName);

		// Ensure both columns exist and the type is number
		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_NUMBER)
				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {

			lineChart.setTitle("Sample Line Chart");
			xAxis.setLabel(chartXaxisName);
			yAxis.setLabel(chartYaxisName);

			// defining a series
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

			series.setName(currentDatasetName);

			// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			Number[] xValues = (Number[]) xCol.getData();
			Number[] yValues = (Number[]) yCol.getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				series.getData().add( new XYChart.Data<Number, Number>(xValues[i], yValues[i]));
			}
			
			// add the new series as the only one series for this line chart
			lineChart.getData().add(series);
		}
	}

	/**
	 * Create the show chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneShowChartScreen() {

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		// Layout the UI components
		
		showChartHeader = new Label();
		showChartHeader.getStyleClass().add("Header");
		showChartBack = new Button("Back");
		
		VBox chartContainer = new VBox(20);
		chartContainer.getChildren().addAll(lineChart);
		chartContainer.setAlignment(Pos.CENTER);
		
		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().add(showChartBack);
		
		BorderPane pane = new BorderPane();
		pane.setTop(showChartHeader);
		pane.setCenter(chartContainer);
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
		dataList.getSelectionModel().selectFirst();
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
		DataTable t = SampleDataGenerator.generateSampleLineData();
		String name = "DataSet" + DataSetCount++;
		DataSets.put(name, t);
		dataList.getItems().add(name);
	}

	private String checkSelectedDataSet() {
		return "Selected DataSet: DataSet" + dataList.getSelectionModel().getSelectedIndex();
	}
}
