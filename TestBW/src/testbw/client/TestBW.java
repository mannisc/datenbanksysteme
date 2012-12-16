package testbw.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import testbw.client.SetupStaticDBService;
import testbw.client.SetupStaticDBServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestBW implements EntryPoint {

  // GUI elements
  private VerticalPanel vpanel = new VerticalPanel();
  private HorizontalPanel setupPanel = new HorizontalPanel();
  private Button setupDBButton = new Button("setupDB");
  private TextBox inputBox = new TextBox();
  private Label resultLabel = new Label();
  
  private SetupStaticDBServiceAsync setupSvc = GWT.create(SetupStaticDBService.class);
  

  /**
   * Entry point method.
   */
  public void onModuleLoad() {
	  
	  // Build GUI
	  setupPanel.add(inputBox);
	  setupPanel.add(setupDBButton);
	  vpanel.add(setupPanel);
	  vpanel.add(resultLabel);
	  RootPanel.get("setupDB").add(vpanel);
	  inputBox.setFocus(true);
	  inputBox.setText("Enter <DBName>;<username>;<password>");
	  setupPanel.addStyleName("setupPanel");
	  inputBox.addStyleName("textBox");
	  
	  
	  // Handles - listen for mouse events on the SetupDB button.
	    setupDBButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        setupDB();
	      }
	    }); 
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

	 // Make the call to the stock price service.
	 String input = inputBox.getText();
	 ((SetupStaticDBServiceAsync) setupSvc).setupStaticDB(input, callback);
	  
  }
}