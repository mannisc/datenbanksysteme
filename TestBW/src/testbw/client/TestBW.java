package testbw.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

import testbw.client.SetupStaticDBService;
import testbw.client.SetupStaticDBServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;



public class TestBW implements EntryPoint {

  // GUI elements
  private VerticalPanel vpanel = new VerticalPanel();
  private HorizontalPanel setupPanel = new HorizontalPanel();
  private Button setupDBButton = new Button("SetupDB");
  private Button analysisButton = new Button("Analyze");
  private TextBox inputBox = new TextBox();
  private Label resultLabel = new Label();
  private DialogBox dialogBox = new DialogBox();
  private Button closeButton = new Button("Close");
  private VerticalPanel dialogVPanel = new VerticalPanel();
  private SimpleLayoutPanel layoutPanel;
  private PieChart pieChart;
  
  private ArrayList<String> fromServer = new ArrayList<String>();


  
  // Services
  private SetupStaticDBServiceAsync setupSvc = GWT.create(SetupStaticDBService.class);
  private AnalysisServiceAsync analysisSvc = GWT.create(AnalysisService.class);
  

  /**
   * Entry point method.
   */
  public void onModuleLoad() {
	  
	  // Build GUI
	  setupPanel.add(inputBox);
	  setupPanel.add(setupDBButton);
	  setupPanel.add(analysisButton);
	  vpanel.add(setupPanel);
	  vpanel.add(resultLabel);
	  RootPanel.get("setupDB").add(vpanel);
	  inputBox.setFocus(true);
	  inputBox.setText("Enter <DBName>;<username>;<password>");
	  setupPanel.addStyleName("setupPanel");
	  inputBox.addStyleName("textBox");
	  
	  
	  
	  
	  // Create the popup dialog box
	  dialogBox.addStyleName("dialogBox");
	  dialogBox.setText("Analysis Results");
	  dialogBox.setAnimationEnabled(true);
	  closeButton.getElement().setId("closeButton");
	  dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	  dialogVPanel.addStyleName("dialogVPanel");
	  dialogBox.setWidget(dialogVPanel);
	  
	  
	  
		// Add a handler to close the DialogBox
	  closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
	  
	  
	  // Handles - listen for mouse events on the SetupDB button.
	  setupDBButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        setupDB();
	      }
	  });
	  
	  // Handles - listen for mouse events on the analysis button.
	  analysisButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        getAnalysis();
	      }
	  });
	  
	  

      // Create the API Loader
      ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
      chartLoader.loadApi(new Runnable() {

              @Override
              public void run() {
                      getSimpleLayoutPanel().setWidget(getPieChart());
                      drawPieChart();
              }
      });

  }
  
  private SimpleLayoutPanel getSimpleLayoutPanel() {
      if (layoutPanel == null) {
              layoutPanel = new SimpleLayoutPanel();
      }
      layoutPanel.setSize("500px", "900px");
      return layoutPanel;
  }

  private Widget getPieChart() {
      if (pieChart == null) {
              pieChart = new PieChart();
      }
      pieChart.addStyleName("pieChart");
      pieChart.setSize("500px", "900px");
      return pieChart;
  }
  
  private void drawPieChart() {

      DataTable dataTable = DataTable.create();
      dataTable.addColumn(ColumnType.STRING, "Partei");
      dataTable.addColumn(ColumnType.NUMBER, "Anteil");
      
      
      dataTable.addRows(fromServer.size());
      for (int i = 0; i < fromServer.size(); i=i+2)
      {
    	  System.out.println(fromServer.get(i));
    	  dataTable.setValue(i, 0, fromServer.get(i));
    	  dataTable.setValue(i, 1, new Double (fromServer.get(i+1)).doubleValue());
      }
      
      pieChart.draw(dataTable);
}


  
  private void setupDB(){
	  
	  // Initialize the service proxy.
	  if (setupSvc == null) {
		  setupSvc = (SetupStaticDBServiceAsync) GWT.create(SetupStaticDBService.class);
	      ServiceDefTarget target = (ServiceDefTarget) setupSvc;
	      target.setServiceEntryPoint(GWT.getModuleBaseURL() + "setupStaticDB");
	    }
	  
	  // Set up the callback object.
	  AsyncCallback<String> callback = new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {

	          resultLabel.setText("Error: " + caught.getMessage());
	          resultLabel.setVisible(true);
	      }

	      public void onSuccess(String s) {
	    	  resultLabel.setText(s);
	          resultLabel.setVisible(true);
	      }
	 };

	 // Make the call to the setupDB service.
	 String input = inputBox.getText();
	 ((SetupStaticDBServiceAsync) setupSvc).setupStaticDB(input, callback);
	  
  }
  
  
  private void getAnalysis(){
	  
	  // Initialize the service proxy.
	  if (analysisSvc == null) {
		  analysisSvc = (AnalysisServiceAsync) GWT.create(AnalysisService.class);
	      ServiceDefTarget target = (ServiceDefTarget) analysisSvc;
	      target.setServiceEntryPoint(GWT.getModuleBaseURL() + "analysis");
	    }
	  
	  
	  // Set up the callback object.
	  AsyncCallback< ArrayList<String> > callback = new AsyncCallback< ArrayList<String> >() {
	      public void onFailure(Throwable caught) {

	          resultLabel.setText("Error: " + caught.getMessage());
	          resultLabel.setVisible(true);
	      }

	      public void onSuccess(ArrayList<String> s) {
	    	  
	    	  fromServer = s;
	    	  
	    	  resultLabel.setText("Analysis complete.");
	          resultLabel.setVisible(true);
	          
	          
	    	  dialogBox.setText("Analysis Results: Last update: " +  DateTimeFormat.getMediumDateFormat().format(new Date()));
	    	  dialogVPanel.add(pieChart);
	    	  dialogBox.center();
	    	  drawPieChart();
	    	  dialogVPanel.add(closeButton);
	    	  closeButton.setFocus(true);
	    	  dialogBox.center();
	    	  

	      }
	 };
	 
	 // Make the call to the setupDB service.
	 String input = inputBox.getText();
	 ((AnalysisServiceAsync) analysisSvc).getAnalysis(input, callback);
  }
}