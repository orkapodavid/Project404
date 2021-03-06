package ui.comp3111;

import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.Environment;
import core.comp3111.LineChartClass;
import core.comp3111.PieChartClass;
import core.comp3111.ImportExportCSV;
import core.comp3111.DataTableException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

/**
 * The Main class of this GUI application.
 * 
 * @author cspeter
 * @author kwchiuab
 * @author kpor
 * @author kwaleung
 */
public class Main extends Application {

	private ImportExportCSV importexporter = null;
	private FileChooser ImportChooser, ExportChooser;
	private Alert noFileChosen, emptyTable, tableAdded, wrongFileType, noFileExported, replacedAlert, noDataSets, emptyCSV, noExport, noImport;
	private ChoiceDialog<String> chooseReplaceOption;
	private static final String replaceWithZeros = "Replace with zeros";
	private static final String replaceWithMean = "Replace with column mean";
	private static final String replaceWithMedian = "Replace with column median";
	
	private Environment environment = null;
	private Alert saved, loaded, noFileSaved, noFileLoaded;
	private FileChooser SaveChooser, LoadChooser; 

	// Attributes: Scene and Stage
	private static final int SCENE_NUM = 6;
	private static final int SCENE_MAIN_SCREEN = 0;
	private static final int SCENE_CREATE_CHART = 1;
	private static final int SCENE_SPLIT_DATA = 2;
	private static final int SCENE_FILTER_DATA = 3;
	private static final int SCENE_SHOW_LINECHART = 4;
	private static final int SCENE_SHOW_PIECHART = 5;
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - [Project404]", "Create Chart", "Split Data",
			"Filter Data", "Line Chart", "Pie Chart" };
	private Stage stage = null;
	private Scene[] scenes = null;

	// To keep this application more structural
	// The following UI components are used to keep references after invoking
	// createScene()

	// Screen 1: paneMainScreen
	private Button importButton, exportButton, filterButton, chartButton, showChartButton, splitButton;
	private MenuItem Save, Load;
	private ListView<String> dataList;
	private ListView<String> chartList;
	private Alert noDatasetAlert = null;
	private Alert noChartAlert = null;
	
	// Screen 2: paneCreateChartScreen
	private Boolean isLineChart = null;
	private CheckBox setAnimation = null;
	private String chartXaxisName = null;
	private String chartYaxisName = null;
	private String chartNumColName = null;
	private String chartTextColName = null;
	private String currentDatasetName = null;
	private DataTable currentDataTable = null;
	private Button chartCancel = null;
	private Button chartComfirm = null;
	private Label chartSelectDataset = null;
	private Label chartSelect = null;
	private ComboBox<String> chartTypes = null;
	private Label chartSelectXaxisLabel = null;
	private ComboBox<String> chartSelectXaxis = null;
	private Label chartSelectYaxisLabel = null;
	private ComboBox<String> chartSelectYaxis = null;
	private Label chartSelectTextLabel = null;
	private ComboBox<String> chartSelectTextCol = null;
	private Label chartSelectNumLabel = null;
	private ComboBox<String> chartSelectNumCol = null;
	private Alert noSelectedColAlert = null;
	
	// Screen 3: paneSplitDataScreen
	private Label splitHeader = null;
	private Button splitCancel = null;
	private Button splitComfirm = null;
	private Label splitActionLabel = null;
	private ComboBox<String> splitAction = null;
	private Label splitPercentage = null;
	private Label splitSliderLabel = null;
	private Slider splitSlider = null;
	private Alert noRowsReplaceAlert = null;
	private Alert noRowsOneSuccessAlert = null;
	private Alert goodReplaceAlert = null;
	
	// Screen 4: paneFilterDataScreen
	private Label filterHeader = null;
	private Label filterSelectNumLabel = null;
	private Label filterSelectOperatorLabel = null;
	private Label fliterActionLabel = null;
	private Button filterCancel = null;
	private Button filterComfirm = null;
	private TextField filterTextField = null;
	private ComboBox<String> filterAction = null;
	private ComboBox<String> filterSelectNumCol = null;
	private ComboBox<String> filterSelectOperator = null;
	private Alert notNum = null;
	private Alert notEnoughInput = null;
	private Alert noNumericalCol = null;
	private Alert negValueInCol = null;
	private Alert allRowsFilteredOut = null;
	
	// Screen 5: paneShowLineChartScreen
	private LineChart<Number, Number> lineChart = null;
	private NumberAxis xAxis = null;
	private NumberAxis yAxis = null;
	private Label showLineChartHeader = null;
	private Button showLineChartBack = null;
	private Timeline tl = null;
	private LineChartClass current = null;
	
	// Screen 6: paneShowPieChartScreen
	private PieChart pieChart = null;
	private Button showPieChartBack = null;
	private Label showPieChartHeader = null;

	/**
	 * Create all scenes in this application.
	 */
	private void initScenes() {
		scenes = new Scene[SCENE_NUM];
		scenes[SCENE_MAIN_SCREEN] = new Scene(paneMainScreen(), 520, 500);
		scenes[SCENE_CREATE_CHART] = new Scene(paneCreateChartScreen(), 520, 500);
		scenes[SCENE_SPLIT_DATA] = new Scene(paneSplitDataScreen(), 520, 500);
		scenes[SCENE_FILTER_DATA] = new Scene(paneFilterDataScreen(), 520, 500);
		scenes[SCENE_SHOW_LINECHART] = new Scene(paneShowLineChartScreen(), 520, 500);
		scenes[SCENE_SHOW_PIECHART] = new Scene(paneShowPieChartScreen(), 520, 500);
		for (Scene s : scenes) {
			if (s != null)
				// Assumption: all scenes share the same stylesheet
				s.getStylesheets().add("Main.css");
		}
	}

	/**
	 * This method will be invoked after createScenes(). 
	 * <p>In this stage, all UI
	 * components will be created with a non-NULL references for the UI components
	 * that requires interaction (e.g. button click, or others).
	 */
	private void initEventHandlers() {
		importexporter = new ImportExportCSV();
		environment = new Environment();
		initMainScreenHandlers();
		initSubScreenHandlers();
		initCreateChartHandlers();
		initFliterDataHandlers();
		initSplitDataHandler();
		initTimer();
		initAlertMsg();
	}
	
	/**
	 * <p>In this stage, all UI Alert
	 * components will be created with a non-NULL references containing the
	 * respective alert message.
	 */
	private void initAlertMsg() {
		// for Main Scene
		noDatasetAlert = new Alert(AlertType.INFORMATION);
		noDatasetAlert.setTitle("Reminder Dialog");
		noDatasetAlert.setHeaderText(null);
		noDatasetAlert.setContentText("No dataset is selected. Please select a dataset.");

		noSelectedColAlert = new Alert(AlertType.WARNING);
		noSelectedColAlert.setTitle("Warning Dialog");
		noSelectedColAlert.setHeaderText(null);
		noSelectedColAlert.setContentText("Incomplete selection of data columns. Please complete your selection");
		
		noChartAlert = new Alert(AlertType.INFORMATION);
		noChartAlert.setTitle("Reminder Dialog");
		noChartAlert.setHeaderText(null);
		noChartAlert.setContentText("No chart is selected. Please select a chart.");
		
		// for SCENE SPLIT DATA
		goodReplaceAlert = new Alert(AlertType.INFORMATION);
		goodReplaceAlert.setTitle("Successful Operation");
		goodReplaceAlert.setHeaderText(null);
		
		noRowsReplaceAlert = new Alert(AlertType.ERROR);
		noRowsReplaceAlert.setTitle("Error Message: Empty Dataset");
		noRowsReplaceAlert.setHeaderText("One of the newly created Dataset is empty.");
		noRowsReplaceAlert.setContentText("Replacement cannot be done. The environment remains unchanged.");
		
		noRowsOneSuccessAlert = new Alert(AlertType.ERROR);
		noRowsOneSuccessAlert.setTitle("Error Message: Empty Dataset");
		noRowsOneSuccessAlert.setHeaderText("One of the newly created Dataset is empty.");
		noRowsOneSuccessAlert.setContentText("Only one new dataset will be created. ");
		
		//for SCENE_CREATE_CHART
		negValueInCol = new Alert(AlertType.ERROR);
		negValueInCol.setTitle("Error Message: Negative value is not allowed");
		negValueInCol.setHeaderText(null);
		negValueInCol.setContentText("Selected column has negative value(s). Please select another column.");
		
		// for SCENE_Fliter_DATA
		noNumericalCol = new Alert(AlertType.INFORMATION);
		noNumericalCol.setTitle("Reminder Dialog");
		noNumericalCol.setHeaderText(null);
		noNumericalCol.setContentText("Selected dataset has no numerical column. Please select another dataset");
		
		notEnoughInput = new Alert(AlertType.INFORMATION);
		notEnoughInput.setTitle("Reminder Dialog");
		notEnoughInput.setHeaderText(null);
		notEnoughInput.setContentText("Incomplete information. Please fill out this form.");
		
		notNum = new Alert(AlertType.WARNING);
		notNum.setTitle("Warning Dialog");
		notNum.setHeaderText(null);
		notNum.setContentText("Please input a valid number for filtering");
		
		allRowsFilteredOut = new Alert(AlertType.ERROR);
		allRowsFilteredOut.setTitle("Error Message: Empty Dataset");
		allRowsFilteredOut.setHeaderText("No row in the selected dataset meets the filtering requirement.");
		allRowsFilteredOut.setContentText("The environment remains unchanged.");
		
		//For Import and Export
		ImportChooser = new FileChooser();
		ExportChooser = new FileChooser();
		noFileChosen = new Alert(AlertType.ERROR);
		emptyTable = new Alert(AlertType.ERROR);
		tableAdded = new Alert(AlertType.INFORMATION);
		wrongFileType = new Alert(AlertType.ERROR);
		noFileExported = new Alert(AlertType.ERROR);
		replacedAlert = new Alert(AlertType.INFORMATION);
		emptyCSV = new Alert(AlertType.ERROR);
		noDataSets = new Alert(AlertType.ERROR);
		noExport = new Alert(AlertType.INFORMATION);
		noImport = new Alert(AlertType.INFORMATION);
		
		ImportChooser.setTitle("Import CSV");
		ExtensionFilter CSVfilter = new ExtensionFilter("CSV Files", "*.csv");
		ImportChooser.getExtensionFilters().add(CSVfilter);
		ImportChooser.setSelectedExtensionFilter(CSVfilter);
		
		ExportChooser.setTitle("Export CSV");
		ExportChooser.getExtensionFilters().add(CSVfilter);
		ExportChooser.setSelectedExtensionFilter(CSVfilter);
		
		noFileChosen.setTitle("Error");
		noFileChosen.setHeaderText("No file selected");
		noFileChosen.setContentText("Operation cancelled");
		
		emptyTable.setTitle("Error");
		emptyTable.setHeaderText("Selected CSV File is empty");
		emptyTable.setContentText("Operation cancelled");
		
		tableAdded.setTitle("Success");
		tableAdded.setHeaderText("File Imported");
		tableAdded.setContentText("CSV file successfully imported into data sets");
		
		wrongFileType.setTitle("ERROR");
		wrongFileType.setHeaderText("Invalid file type");
		wrongFileType.setContentText("Operation cancelled");
		
		noFileExported.setTitle("ERROR");
		noFileExported.setHeaderText("File not saved");
		noFileExported.setContentText("Operation cancelled");
		
		noDataSets.setTitle("ERROR");
		noDataSets.setHeaderText("No data set");
		noDataSets.setContentText("No data set available for export");
		
		emptyCSV.setTitle("ERROR");
		emptyCSV.setHeaderText("Empty CSV");
		emptyCSV.setContentText("Selected CSV file is empty");
		
		noExport.setTitle("Cancelled");
		noExport.setHeaderText("Export Cancelled");
		noExport.setContentText("Returning to main window.");
		
		noImport.setTitle("Cancelled");
		noImport.setHeaderText("Import Cancelled");
		noImport.setContentText("Returning to main window.");
		
		replacedAlert.setTitle("Information");
		replacedAlert.setHeaderText("Replaced missing data");
		
		Collection<String> replaceOptions = new ArrayList<String>();
		replaceOptions.add(replaceWithZeros);
		replaceOptions.add(replaceWithMean);
		replaceOptions.add(replaceWithMedian);
		
		chooseReplaceOption = new ChoiceDialog<String>(replaceWithZeros, replaceOptions);
		chooseReplaceOption.setTitle("Replace Option");
		chooseReplaceOption.setHeaderText("Please choose");
		
		//For Save and Load
		SaveChooser = new FileChooser();
		LoadChooser = new FileChooser();
		LoadChooser.setTitle("Load Environment");
		ExtensionFilter SaveLoadFilter = new ExtensionFilter("comp3111 files", "*.comp3111");
		LoadChooser.getExtensionFilters().add(SaveLoadFilter);
		LoadChooser.setSelectedExtensionFilter(SaveLoadFilter);
		SaveChooser.setTitle("Save Environment");
		SaveChooser.getExtensionFilters().add(SaveLoadFilter);
		SaveChooser.setSelectedExtensionFilter(SaveLoadFilter);
		
		saved = new Alert(AlertType.INFORMATION);
		loaded = new Alert(AlertType.INFORMATION);
		noFileSaved = new Alert(AlertType.ERROR);
		noFileLoaded = new Alert(AlertType.ERROR);
		saved.setTitle("Saved");
		saved.setHeaderText("Success");
		loaded.setTitle("Loaded");
		loaded.setHeaderText("Success");
		noFileSaved.setTitle("ERROR");
		noFileSaved.setHeaderText("File not saved");
		noFileSaved.setContentText("Operation cancelled as no directory has been specified.");
		noFileLoaded.setTitle("ERROR");
		noFileLoaded.setHeaderText("File not selected");
		noFileLoaded.setContentText("Operation cancelled as no file selected.");
	}

	/**
	 * Initialize event handlers of the main screen
	 */
	private void initMainScreenHandlers() {
		Save.setOnAction(e -> {
			SaveChooser.setInitialFileName("envparams.comp3111");
			File selectedFile = SaveChooser.showSaveDialog(null);
			if (selectedFile != null) {
				String filePath = null;
				try {
					filePath = environment.saveEnvironment(selectedFile);
				} catch (Exception e1) {
					System.out.println("saveEnv: Exception");
					//e1.printStackTrace();
				}
				if (!filePath.equals(null)) {
					saved.setContentText("Environment has been saved to: " + filePath);
					saved.showAndWait();
				}
			} else {
				System.out.println("saveEnv: No file saved.");
				noFileSaved.showAndWait();
			}
		});
		
		Load.setOnAction(e -> {
			File selectedFile = LoadChooser.showOpenDialog(null);
			if (selectedFile != null) {
				String filePath = null;
				try {
					filePath = environment.loadEnvironment(selectedFile);
				} catch (Exception e1) {
					System.out.println("loadEnv: Exception");
					e1.printStackTrace();
				}
				dataList.getItems().remove(0, dataList.getItems().size());
				chartList.getItems().remove(0, chartList.getItems().size());	
				//sort
				List<String> dataSortList = new ArrayList<String>(environment.getEnviornmentLineCharts().size());
				for (String datakey:environment.getEnvironmentDataTables().keySet()) {
					dataSortList.add(datakey);
				}

				// sort the ArrayList of datasets
				Collections.sort(dataSortList);
				// add the sorted ArrayList of datasets into ListView of dataList
				dataList.getItems().addAll(dataSortList);
				List<String> chartSortList = new ArrayList<String>(environment.getEnviornmentLineCharts().size());
				for (String chartkey:environment.getEnviornmentLineCharts().keySet()) {
					chartSortList.add(chartkey);
				}
				for (String chartkey:environment.getEnviornmentPieCharts().keySet()) {
					chartSortList.add(chartkey);
				}
				// sort the ArrayList of charts
				Collections.sort(chartSortList);
				// add the sorted ArrayList of charts into ListView of chartList
				chartList.getItems().addAll(chartSortList);

				if (!loaded.equals(null)) {
					loaded.setContentText("Environment has been loaded from: " + filePath);
					loaded.showAndWait();
				}
			} else {
				System.out.println("loadEnv: No file loaded.");
				if (!noFileLoaded.equals(null))	{
					noFileLoaded.showAndWait();
				}
			}			
		});
		
		importButton.setOnAction(e -> {
			String name = "DataSet" + (environment.getEnvironmentDataTables().size() + 1);
			File selectedFile = ImportChooser.showOpenDialog(null);
			DataTable importedTable = null, finalImportedTable = new DataTable();
			boolean isCancelled = false;
			
			if (selectedFile != null) {
				try {
					importedTable = importexporter.importCSV(selectedFile);
					if (importedTable != null) {
						int colCount = importedTable.getNumCol();
						int rowCount = importedTable.getNumRow();
						String[] importTableColNames = importedTable.getAllColName();
						for (int i=0; i<colCount; i++) {
							Object[] currColElements = importedTable.getCol(importTableColNames[i]).getData();
							String currColTypeName = importedTable.getCol(importTableColNames[i]).getTypeName();
							boolean isMissing = importexporter.checkMissingData(currColElements);
							if (isMissing) {
								//currColElements = replaceEmptyElements(currColElements, currColTypeName, importTableColNames[i]);
								if(currColTypeName.equals(DataType.TYPE_NUMBER)) {
									Number mean = importexporter.calculateMean(currColElements);
									Number median = importexporter.calculateMedian(currColElements);
									
									chooseReplaceOption.setContentText("Replace missing data in column: " + importTableColNames[i]);
									
									Optional<String> returnedReplaceOption = chooseReplaceOption.showAndWait();
									String selectedReplaceOption = null;
									if (returnedReplaceOption.isPresent()) {
										selectedReplaceOption = returnedReplaceOption.get();
									} else {
										isCancelled = true;
										break;
									}
									for (int j=0; j<currColElements.length; j++) {
										if (currColElements[j].equals("")) {
											currColElements[j] = importexporter.replaceEmptyElement(selectedReplaceOption, mean, median);
										}
									}
									DataColumn replaceDataColumn = new DataColumn(currColTypeName, currColElements);
									finalImportedTable.addCol(importTableColNames[i], replaceDataColumn);
									
									switch (selectedReplaceOption) {
									case replaceWithZeros: replacedAlert.setContentText("Replaced missing data with zeros in column: " + importTableColNames[i]);
									break;
									case replaceWithMean: replacedAlert.setContentText("Replaced missing data with mean value in column: " + importTableColNames[i]);
									break;
									case replaceWithMedian: replacedAlert.setContentText("Replaced missing data with median value in column: " + importTableColNames[i]);
									break;
									default: replacedAlert.setContentText("Replaced missing data with zeros in column: " + importTableColNames[i]);
									break;
									}
									replacedAlert.showAndWait();
								} else {
									finalImportedTable.addCol(importTableColNames[i], importedTable.getCol(importTableColNames[i]));
									replacedAlert.setContentText("Replaced missing data with empty string in column: " + importTableColNames[i]);
									replacedAlert.showAndWait();
								}
							} else {
								finalImportedTable.addCol(importTableColNames[i], importedTable.getCol(importTableColNames[i]));
							}
						}
						if (isCancelled) {
							System.out.println("importCSV: Import Cancelled");
							noImport.showAndWait();
						} else {
							environment.getEnvironmentDataTables().put(name,finalImportedTable);
							dataList.getItems().add(name);
							System.out.println("importCSV: Imported table added");
							tableAdded.showAndWait();
						}
					} else {
						System.out.println("importCSV: Empty CSV File");
						emptyCSV.showAndWait();
					}
				} catch (Exception e1) {
					System.out.println("importCSV: Exception");
					e1.printStackTrace();
				}
			} else {
				System.out.println("importCSV: No file selected");
				noFileChosen.showAndWait();
			}
		});
		
		exportButton.setOnAction(e -> {
			List<String> exportOptions = new ArrayList<String>();
			if (environment.getEnvironmentDataTables().size() != 0) {
				List<String> exportSortList = new ArrayList<String>(environment.getEnvironmentDataTables().size());
				for (String key: environment.getEnvironmentDataTables().keySet()) {
					exportSortList.add(key);
				}
				Collections.sort(exportSortList);
				exportOptions.addAll(exportSortList);
				ChoiceDialog<String> chooseExportDataSet = new ChoiceDialog<String>(exportOptions.get(0), exportOptions);
				chooseExportDataSet.setTitle("Export");
				chooseExportDataSet.setHeaderText("Please choose table to export");
				Optional<String> returnedDataSetOption = chooseExportDataSet.showAndWait();
				
				String selectedDataSetName;
				
				if (returnedDataSetOption.isPresent()) {
					System.out.println("exportCSV: Selected tabled: " + returnedDataSetOption.get());
					selectedDataSetName = returnedDataSetOption.get();
					ExportChooser.setInitialFileName(selectedDataSetName + ".csv");
					System.out.println("exportCSV: Initial file name: " + ExportChooser.getInitialFileName());
					
					File selectedFile = ExportChooser.showSaveDialog(null);
					
					if (selectedFile != null) {
						try {
							importexporter.exportCSV(selectedFile, selectedDataSetName, environment.getEnvironmentDataTables());
						} catch (Exception e1) {
							System.out.println("exportCSV: Exception");
							Path currFilePath = selectedFile.toPath();
							try {
								Files.deleteIfExists(currFilePath);
							} catch (IOException e2) {
								//e2.printStackTrace();
							}
							//e1.printStackTrace();
						}
					} else {
						System.out.println("exportCSV: No file saved");
						noFileExported.showAndWait();
					}
				} else {
					System.out.println("exportCSV: No data selected");
					noExport.showAndWait();
				}
			} else {
				System.out.println("exportCSV: No data sets");
				noDataSets.showAndWait();
			}
		});

		chartButton.setOnAction(e -> {
			currentDatasetName = dataList.getSelectionModel().getSelectedItem();
			if (currentDatasetName != null) {
				chartSelectDataset.setText("Selected Dataset: " + currentDatasetName);
				currentDataTable = environment.getEnvironmentDataTables().get(currentDatasetName);
				if(currentDataTable.getNumOfNumCol() <= 0) {
					noNumericalCol.showAndWait();
				}else {
					chartSelectXaxis.getItems().addAll(currentDataTable.getAllNumColName());
					chartSelectYaxis.getItems().addAll(currentDataTable.getAllNumColName());
					chartSelectTextCol.getItems().addAll(currentDataTable.getAllTextColName());
					chartSelectNumCol.getItems().addAll(currentDataTable.getAllNumColName());
					putSceneOnStage(SCENE_CREATE_CHART);
				}
			} else {
				noDatasetAlert.showAndWait();
			}

		});

		filterButton.setOnAction(e -> {
			currentDatasetName = dataList.getSelectionModel().getSelectedItem();
			if(currentDatasetName != null) {
				filterHeader.setText("Selected Dataset: " + currentDatasetName);
				currentDataTable = environment.getEnvironmentDataTables().get(currentDatasetName);
				// check if selected dataset contains at least one numerical column
				System.out.println("currentDataTable.getNumOfNumCol() = "+currentDataTable.getNumOfNumCol());
				if(currentDataTable.getNumOfNumCol() <= 0) {
					noNumericalCol.showAndWait();
				}else {
					filterSelectNumCol.getItems().addAll(currentDataTable.getAllNumColName());
					putSceneOnStage(SCENE_FILTER_DATA);
				}
			}else {
				noDatasetAlert.showAndWait();
			}
			
		});
		
		splitButton.setOnAction(e -> {
			currentDatasetName = dataList.getSelectionModel().getSelectedItem();
			if(currentDatasetName != null) {
				splitHeader.setText("Selected Dataset: " + currentDatasetName);
				currentDataTable = environment.getEnvironmentDataTables().get(currentDatasetName);
				putSceneOnStage(SCENE_SPLIT_DATA);
			}else {
				noDatasetAlert.showAndWait();
			}
		});
		
		showChartButton.setOnAction(e -> {
			String name = chartList.getSelectionModel().getSelectedItem();
			if (name != null) {
				String checking = name.substring(0, 9);
				System.out.println(checking);
				if (checking.equals(new String("LineChart"))) {
					//if an animated chart is selected, enable the timer
					if (environment.getEnviornmentLineCharts().get(name).get_animate()) {
						XYChart.Series<Number, Number> temp = new XYChart.Series<Number, Number>();
						temp.setName(environment.getEnviornmentLineCharts().get(name).getSeries().getName());
						current = environment.getEnviornmentLineCharts().get(name);
						if(lineChart.getData().size() != 0)
							lineChart.getData().set(0, temp);
						else 
							lineChart.getData().add(temp);
						tl.play();
					} else if (lineChart.getData().get(0) != environment.getEnviornmentLineCharts().get(name).getSeries()) {
						lineChart.getData().clear();
						lineChart.getData().add(environment.getEnviornmentLineCharts().get(name).getSeries());
					}
					lineChart.setTitle(environment.getEnviornmentLineCharts().get(name).getTitle());
					xAxis.setLabel(environment.getEnviornmentLineCharts().get(name).getXAxisName());
					yAxis.setLabel(environment.getEnviornmentLineCharts().get(name).getYAxisName());
					putSceneOnStage(SCENE_SHOW_LINECHART);
				} else {
					PieChartClass newPieChartClass = environment.getEnviornmentPieCharts().get(name);
					ObservableList<PieChart.Data> newOList = newPieChartClass.getObserList();
					
					if(pieChart.getData() != newOList) {
						pieChart.setData(newOList);
					}
					pieChart.setTitle(newPieChartClass.getTitle());
					putSceneOnStage(SCENE_SHOW_PIECHART);
				}
			} else {
				noChartAlert.showAndWait();
			}

		});
	}

	/**
	 * Initialize event handlers of all the return button.
	 */
	private void initSubScreenHandlers() {
		// create chart screen
		chartCancel.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
		
		
		showLineChartBack.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
			tl.stop();
		});
		showPieChartBack.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
	}
	/**
	 * Initialize the timer for animated chart
	 */
	private void initTimer() {
		tl = new Timeline();
		tl.getKeyFrames().add(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				int size = lineChart.getData().get(0).getData().size();
				if (size == current.getSeries().getData().size()) 
					lineChart.getData().get(0).getData().remove(1, size);
				else
					lineChart.getData().get(0).getData().add(current.getSeries().getData().get(size));
			}
		}));
		tl.setCycleCount(Animation.INDEFINITE);
	}

	/**
	 * Initialize event handlers of the sub screen - SCENE_CREATE_CHART.
	 */
	private void initCreateChartHandlers() {
		// create chart screen
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
				if (isLineChart) {
					chartXaxisName = chartSelectXaxis.getValue();
					chartYaxisName = chartSelectYaxis.getValue();
					if (chartXaxisName != null && chartYaxisName != null) {
						chartSelectXaxis.getItems().clear();
						chartSelectYaxis.getItems().clear();
						chartSelectNumCol.getItems().clear();
						chartSelectTextCol.getItems().clear();
						LineChartClass t = new LineChartClass(currentDataTable, chartXaxisName, chartYaxisName, currentDatasetName, setAnimation.isSelected());
						String name = "LineChart" + (environment.getEnviornmentLineCharts().size() + 1);
						environment.getEnviornmentLineCharts().put(name, t);
						if (t.get_animate()) {
							XYChart.Series<Number, Number> temp = new XYChart.Series<Number, Number>();
							temp.setName(t.getSeries().getName());
							if (lineChart.getData().size() == 0)
								lineChart.getData().add(temp);
							else
								lineChart.getData().set(0, temp);
							current = environment.getEnviornmentLineCharts().get(name);
							tl.play();
						}
						else {
							if (lineChart.getData().size() == 0)
								lineChart.getData().add(t.getSeries());

							else
								lineChart.getData().set(0, t.getSeries());
						}
						chartList.getItems().add(name);
						putSceneOnStage(SCENE_SHOW_LINECHART);
					} else {
						noSelectedColAlert.showAndWait();
						return;
					}

				} else {
					// Initialize PieChart Screen
					chartNumColName = chartSelectNumCol.getValue();
					chartTextColName = chartSelectTextCol.getValue();
					if(chartNumColName != null && chartTextColName != null) {
						// Detect negative value
						for(Object val :currentDataTable.getCol(chartNumColName).getData()) {
							if(((Number)val).doubleValue() < 0.0) {
								negValueInCol.showAndWait();
								return;
							}
						}
						chartSelectXaxis.getItems().clear();
						chartSelectYaxis.getItems().clear();
						chartSelectNumCol.getItems().clear();
						chartSelectTextCol.getItems().clear();
						PieChartClass t = new PieChartClass(currentDataTable,chartNumColName,chartTextColName,currentDatasetName);
						String name = "PieChart" + (environment.getEnviornmentPieCharts().size() + 1);
						environment.getEnviornmentPieCharts().put(name, t);
						chartList.getItems().add(name);
						pieChart.setTitle("Pie Chart of " + currentDatasetName);
						pieChart.setLegendSide(Side.LEFT);
						pieChart.setData(t.getObserList());		
						putSceneOnStage(SCENE_SHOW_PIECHART);
					}else {
						noSelectedColAlert.showAndWait();
						return;
					}	
				}

			}
		});

		// Add a ChangeListener to the ComboBox chartTypes
		chartTypes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (newValue == "Line Chart") {
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
					setAnimation.getStyleClass().removeAll("combo-box-base:disabled");
					setAnimation.setDisable(false);
				} else {
					isLineChart = false;
					chartSelectXaxis.getStyleClass().add("combo-box-base:disabled");
					chartSelectXaxis.setDisable(true);
					chartSelectYaxis.getStyleClass().add("combo-box-base:disabled");
					chartSelectYaxis.setDisable(true);
					chartSelectXaxisLabel.getStyleClass().add("combo-box-base:disabled");
					chartSelectXaxisLabel.setDisable(true);
					chartSelectYaxisLabel.getStyleClass().add("combo-box-base:disabled");
					chartSelectYaxisLabel.setDisable(true);
					setAnimation.getStyleClass().add("combo-box-base:disabled");
					setAnimation.setDisable(true);

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
	 * Initialize event handlers of the sub screen - SCENE_FILTER_DATA.
	 */
	private void initFliterDataHandlers() {
		filterCancel.setOnAction(e -> {
			filterAction.getSelectionModel().clearSelection();
			filterSelectNumCol.getItems().clear();
			filterSelectOperator.getSelectionModel().clearSelection();
			filterTextField.clear();
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
		
		filterComfirm.setOnAction(e -> {
			String filterOption = filterAction.getValue();
			String filterNumColName = filterSelectNumCol.getValue();
			String filterOperator = filterSelectOperator.getValue();
			String filterThreshold = filterTextField.getText();
			double threshold;
			// Ensure all inputs are well-received
			if(filterNumColName == null || filterOperator == null || filterThreshold == null || filterOption == null) {
				notEnoughInput.showAndWait();
				return;
			}
			
			// Ensure the input threshold is a valid double
			try {
				threshold = Double.parseDouble(filterThreshold);
			} catch(NullPointerException | NumberFormatException ex) {
				notNum.showAndWait();
				return;
			}
			
			System.out.println("---------Original DataTable---------");
			environment.getEnvironmentDataTables().get(currentDatasetName).print();
			
			// filter the data 
			String newDataTableName = null;
			if(filterOption == "Replacing the current dataset") {
				try {
					newDataTableName = environment.filterDatasetByNum(currentDatasetName, filterNumColName, filterOperator, threshold, true);
				} catch (DataTableException e1) {
					e1.printStackTrace();
				}
			}else {
				try {
					newDataTableName = environment.filterDatasetByNum(currentDatasetName, filterNumColName, filterOperator, threshold, false);
				} catch (DataTableException e1) {
					e1.printStackTrace();
				}	
			}
			
			if(newDataTableName == "Empty DataTable") {
				// Empty DataTable created after filtering
				// handle the exception of all rows are filtered out and filterDataTable has no rows
				allRowsFilteredOut.showAndWait();
			}else {
				// for debugging:
				if(newDataTableName == null) {
					System.out.println("---------Replaced DataTable---------");
					environment.getEnvironmentDataTables().get(currentDatasetName).print();
					goodReplaceAlert.setContentText(currentDatasetName + " has been replaced.");
				}else {
					// add the new DataTable onto the dataList
					dataList.getItems().add(newDataTableName);
					goodReplaceAlert.setContentText("A new dataset: "+newDataTableName + " has been created.");
					System.out.println("---------New DataTable---------");
					environment.getEnvironmentDataTables().get(newDataTableName).print();
				}
				goodReplaceAlert.showAndWait();
			}

			// clear all input informations
			filterAction.getSelectionModel().clearSelection();
			filterSelectNumCol.getItems().clear();
			filterSelectOperator.getSelectionModel().clearSelection();
			filterTextField.clear();
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
	}
	
	/**
	 * Initialize event handlers of the sub screen - SCENE_SPLIT_DATA.
	 */
	private void initSplitDataHandler() {
		splitSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	splitPercentage.setText(String.format("%.0f%%", new_val));
            }
        });
		
		// split data screen
		splitCancel.setOnAction(e -> {
			splitAction.getSelectionModel().clearSelection();
			splitSlider.setValue(50);
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});

		splitComfirm.setOnAction(e -> {
			int splitRatio;
			String splitOption = splitAction.getValue();
			if(splitOption == null) {
				notEnoughInput.showAndWait();
				return;
			}else {
				// Get the input split ratio
				splitRatio = ((Number)splitSlider.getValue()).intValue();
				System.out.println("splitRatio: " + splitRatio);
				String[] newDatasetName = null;
				// debug:
				System.out.println("---------Original DataTable---------");
				environment.getEnvironmentDataTables().get(currentDatasetName).print();
				
				// split the data set
				if(splitOption == "Replacing the current dataset") {
					System.out.println("Replacing the current dataset");
					try {
						newDatasetName = environment.randSplitDatasetByNum(currentDatasetName, splitRatio, true);
					} catch (DataTableException e1) {
						e1.printStackTrace();
					}
					if(newDatasetName[0].equals(currentDatasetName)) {
						// replacement is successful
						goodReplaceAlert.setContentText(currentDatasetName + " has been replaced.");
						// add the new DataTable onto the dataList
						dataList.getItems().add(newDatasetName[1]);
						goodReplaceAlert.showAndWait();
					}else if(newDatasetName[0].equals("")) {
						// replacement is unsuccessful because one of the splited dataset is empty
						noRowsReplaceAlert.showAndWait();
					}
				}else {
					try {
						newDatasetName = environment.randSplitDatasetByNum(currentDatasetName, splitRatio, false);
					} catch (DataTableException e1) {
						e1.printStackTrace();
					}
					if(!newDatasetName[0].equals("") && newDatasetName[1].equals("")) {
						// only one dataset is not empty
						dataList.getItems().add(newDatasetName[0]);
						goodReplaceAlert.setContentText("Only one dataset: "+newDatasetName[0] + " has been created.");
						noRowsOneSuccessAlert.showAndWait();
					}else if (newDatasetName[0].equals("") && !newDatasetName[1].equals("")) {
						// only one dataset is not empty
						dataList.getItems().add(newDatasetName[1]);
						goodReplaceAlert.setContentText("Only one dataset: "+newDatasetName[1] + " has been created.");
						noRowsOneSuccessAlert.showAndWait();
					}else if (!newDatasetName[0].equals("") && !newDatasetName[1].equals("")){
						// two new datasets are not empty
						dataList.getItems().add(newDatasetName[0]);
						dataList.getItems().add(newDatasetName[1]);
						goodReplaceAlert.setContentText("Two datasets: "+newDatasetName[0] + " & "+ newDatasetName[1]+ " has been created.");
					}
					goodReplaceAlert.showAndWait();
				}
				
				// debug:
				if(!newDatasetName[0].equals("")) {
					System.out.println("---------"+newDatasetName[0] +"---------");
					environment.getEnvironmentDataTables().get(newDatasetName[0]).print();
				}
				if(!newDatasetName[1].equals("")) {
					System.out.println("---------"+newDatasetName[1] +"---------");
					environment.getEnvironmentDataTables().get(newDatasetName[1]).print();
				}
				
				// clear all input informations
				splitAction.getSelectionModel().clearSelection();
				splitSlider.setValue(50);
				
				putSceneOnStage(SCENE_MAIN_SCREEN);
			}
			
		});
		
	}
	
	/**
	 * Create the create chart screen and layout its UI components.
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
		chartTypes.getItems().addAll("Line Chart", "Pie Chart");

		chartSelectXaxis = new ComboBox<>();
		chartSelectYaxis = new ComboBox<>();
		chartSelectTextCol = new ComboBox<>();
		chartSelectNumCol = new ComboBox<>();
		setAnimation = new CheckBox("Animated LineChart");

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
		actionButtons.getChildren().addAll(chartCancel, chartComfirm);

		HBox selectionBoxes = new HBox();
		selectionBoxes.setSpacing(10);
		selectionBoxes.setAlignment(Pos.TOP_LEFT);
		selectionBoxes.getChildren().addAll(chartSelect, chartTypes);

		HBox lineChartXSelectionBoxes = new HBox();
		lineChartXSelectionBoxes.setSpacing(10);
		lineChartXSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		lineChartXSelectionBoxes.getChildren().addAll(chartSelectXaxisLabel, chartSelectXaxis);

		HBox lineChartYSelectionBoxes = new HBox();
		lineChartYSelectionBoxes.setSpacing(10);
		lineChartYSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		lineChartYSelectionBoxes.getChildren().addAll(chartSelectYaxisLabel, chartSelectYaxis);

		HBox pieChartTSelectionBoxes = new HBox();
		pieChartTSelectionBoxes.setSpacing(10);
		pieChartTSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		pieChartTSelectionBoxes.getChildren().addAll(chartSelectTextLabel, chartSelectTextCol);

		HBox pieChartNSelectionBoxes = new HBox();
		pieChartNSelectionBoxes.setSpacing(10);
		pieChartNSelectionBoxes.setAlignment(Pos.TOP_LEFT);
		pieChartNSelectionBoxes.getChildren().addAll(chartSelectNumLabel, chartSelectNumCol);

		VBox container = new VBox();
		container.setSpacing(10);
		container.setAlignment(Pos.TOP_LEFT);
		container.getChildren().addAll(chartSelectDataset, selectionBoxes, lineChartXSelectionBoxes,
				lineChartYSelectionBoxes, setAnimation, pieChartTSelectionBoxes, pieChartNSelectionBoxes);

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

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(30, 30, 10, 20));
		pane.setTop(container);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}


	/**

	 * Create the show pie chart screen and layout its UI components.
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneShowPieChartScreen() {
		pieChart = new PieChart();
		
		// Layout the UI components
		showPieChartBack = new Button("Back");
		showPieChartHeader = new Label();
		showPieChartHeader.getStyleClass().add("Header");
		
		VBox chartContainer = new VBox(20);
		chartContainer.getChildren().addAll(pieChart);
		chartContainer.setAlignment(Pos.CENTER);
		
		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().add(showPieChartBack);
		
		BorderPane pane = new BorderPane();
		pane.setTop(showPieChartHeader);
		pane.setCenter(chartContainer);
		pane.setBottom(actionButtons);
		
		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");
				
		return pane;
	}

	/**
	 * Create the show line chart screen and layout its UI components.
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneShowLineChartScreen() {

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		lineChart.setAnimated(false);

		// Layout the UI components

		showLineChartHeader = new Label();
		showLineChartHeader.getStyleClass().add("Header");
		showLineChartBack = new Button("Back");

		VBox chartContainer = new VBox(20);
		chartContainer.getChildren().addAll(lineChart);
		chartContainer.setAlignment(Pos.CENTER);

		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().add(showLineChartBack);

		BorderPane pane = new BorderPane();
		pane.setTop(showLineChartHeader);
		pane.setCenter(chartContainer);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * Create the Filter Data screen and layout its UI components.
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneFilterDataScreen() {
		Font labelFont = new Font(20);
		filterHeader = new Label();
		filterHeader.getStyleClass().add("Header");
		filterComfirm = new Button("Comfirm");
		filterCancel = new Button("Cancel");
		
		filterSelectNumLabel = new Label("Numerical Column:");
		filterSelectNumLabel.setFont(labelFont);
		filterTextField = new TextField();
		filterSelectOperatorLabel = new Label("Filter Data");
		filterSelectOperatorLabel.setFont(labelFont);
		fliterActionLabel = new Label("Select an effect after filtering");
		fliterActionLabel.setFont(labelFont);
		
		filterAction = new ComboBox<String>();
		filterAction.getItems().addAll("Replacing the current dataset", "Creating a new dataset");
		filterSelectNumCol = new ComboBox<String>();
		filterSelectOperator = new ComboBox<String>();
		filterSelectOperator.getItems().addAll(">","<", ">=","<=", "==", "!=");
		
		
		HBox selectionBoxes = new HBox(20);
		selectionBoxes.setSpacing(10);
		selectionBoxes.setAlignment(Pos.TOP_LEFT);
		selectionBoxes.getChildren().addAll(filterSelectNumLabel, filterSelectNumCol);
		
		HBox selectionBoxes2 = new HBox(20);
		selectionBoxes2.setSpacing(10);
		selectionBoxes2.setAlignment(Pos.TOP_LEFT);
		selectionBoxes2.getChildren().addAll(filterSelectOperatorLabel, filterSelectOperator, filterTextField);
		
		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().addAll(filterCancel, filterComfirm);

		VBox container = new VBox();
		container.setSpacing(10);
		container.setAlignment(Pos.TOP_LEFT);
		container.getChildren().addAll(filterHeader, fliterActionLabel, filterAction, selectionBoxes, selectionBoxes2);
				
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(30, 30, 10, 20));
		pane.setTop(container);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * Create the Split Data screen and layout its UI components.
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneSplitDataScreen() {
		Font labelFont = new Font(20);
		splitHeader = new Label();
		splitHeader.getStyleClass().add("Header");
		splitComfirm = new Button("Comfirm");
		splitCancel = new Button("Cancel");
		
		splitActionLabel = new Label("Select an effect after spliting");
		splitActionLabel.setFont(labelFont);
		splitSliderLabel = new Label("Percentage of random split: ");
		splitSliderLabel.setFont(labelFont);
		
		splitAction  = new ComboBox<String>();
		splitAction.getItems().addAll("Replacing the current dataset", "Creating a new dataset");
		
		splitSlider = new Slider();
		splitSlider.setMin(1);
		splitSlider.setMax(99);
		splitSlider.setValue(50);
		splitSlider.setShowTickLabels(true);
		splitSlider.setShowTickMarks(true);
		splitSlider.setSnapToTicks(true);
		splitSlider.setMajorTickUnit(10);
		splitSlider.setMinorTickCount(5);
		splitSlider.setBlockIncrement(10);
		splitPercentage = new Label(((Number)splitSlider.getValue()).intValue()+"%");
		splitPercentage.setFont(labelFont);
		
		HBox splitBar = new HBox(20);
		splitBar.setAlignment(Pos.TOP_LEFT);
		splitBar.getChildren().addAll(splitSliderLabel, splitPercentage);
				
		VBox container = new VBox(10);
		container.setAlignment(Pos.TOP_LEFT);
		container.getChildren().addAll(splitHeader, splitActionLabel,splitAction,splitBar,splitSlider);
		
		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.BOTTOM_RIGHT);
		actionButtons.getChildren().addAll(splitCancel, splitComfirm);

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(30, 30, 10, 20));
		pane.setTop(container);
		pane.setBottom(actionButtons);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * Creates the main screen and layout its UI components.
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneMainScreen() {
		
		MenuBar menuBar = new MenuBar();
		Menu FileIO = new Menu("File");
		Save = new MenuItem("Save");
		Load = new MenuItem("Load");
		FileIO.getItems().addAll(Save, Load);
		menuBar.getMenus().add(FileIO);

		dataList = new ListView<String>();
		dataList.getSelectionModel().selectFirst();
		chartList = new ListView<String>();
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
	 * This method is used to pick anyone of the scene on the stage. 
	 * <p>It handles the
	 * hide and show order. In this application, only one active scene should be
	 * displayed on stage.
	 * 
	 * @param sceneID The sceneID defined above (see SCENE_XXX)
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
	 * All JavaFx GUI application needs to override the start method.
	 * <p>You can treat
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
	 * Main method only use if running via command line.
	 * 
	 * @param args default command-line argument
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
