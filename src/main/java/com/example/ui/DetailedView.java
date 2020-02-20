package com.example.ui;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.example.parser.NodeHandler;
import com.example.parser.NodeInfo;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.PollEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("details")

@CssImport("./styles/shared-styles.css")
public class DetailedView extends VerticalLayout {

	  private Grid<NodeInfo> grid = new Grid<>(NodeInfo.class);
	 
	  private Div content = new Div();
	private NodeInfoForm form;
	private LoginService service;
	 private TextField filterText = new TextField();
	
	
	Text text = new Text("");
	/**
	 * The field stores the selected NodeInfo while polling the update of the view to set selection again
	 */
	private NodeInfo selectedNodeInfo;
	
	/**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public DetailedView(LoginService service) { 
    	this.service = service;
    	this.setSizeFull();
    	addClassName("detail-view");
    	configureGrid();
    	form = new NodeInfoForm();
    	
        form.addListener(NodeInfoForm.OnEvent.class, this::powerOnNode);
        form.addListener(NodeInfoForm.OffEvent.class, this::powerOffNode);
        form.addListener(NodeInfoForm.ResetEvent.class, this::resetNode);
    	
        content.add(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        
        text.setText(String.format("NODE Information for '%s'", this.service.getHostName()));
        
        add(text, getToolBar(), content);
          updateList();   	
          if(!LoginService.isLoggedIn()) {
          	this.setVisible(false);
          	navigateToMainPage();
          	}
          	else {
          		this.setVisible(true);
          	  UI.getCurrent().setPollInterval(1000);
              UI.getCurrent().addPollListener(new ComponentEventListener<PollEvent>() {
    			@Override
    			public void onComponentEvent(PollEvent event) {
    				updateList();
    				editNodeInfo(selectedNodeInfo);
    				
    			}
    		});
          	}
        
    } // end constructor

	private void updateList() {
		Unirest.setTimeouts(0, 0);
    	if (service.getHostName() != null)
		try {
			HttpResponse<String> response = Unirest
				.get(String.format("http://%s:80/REST/node", service.getHostName()))
			  .basicAuth(service.getName(), service.getPassword())
			  .header("Content-Type", "application/json")
			  //.header("Authorization", "Basic YWRtaW46YWRtaW4=")
			  .asString();
			    SAXParserFactory factory = SAXParserFactory.newInstance();
		         SAXParser saxParser = factory.newSAXParser();
		         NodeHandler nodehandler = new NodeHandler();
		         saxParser.parse(response.getRawBody(), nodehandler);   
		         List<NodeInfo> fullList = nodehandler.getNodeInfoList();
		         List<NodeInfo> partList = fullList.stream().filter(n -> n.getId().contains(filterText.getValue())).collect(Collectors.toList());
		         grid.setItems(partList);
		         grid.select(selectedNodeInfo);
		         
		         
		} catch (UnirestException | SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		} 
	}

	private void configureGrid() {
		grid.addClassName("nodeinfo-grid");
		 grid.setSizeFull();
		 // grid.removeColumnByKey("company");
		 grid.setColumns("id", "health", "baseBoardId", "baseBoardPosition","state");
		 grid.getColumnByKey("baseBoardPosition").setHeader("baseBoardPos");
	       grid.getColumns().forEach(col -> col.setAutoWidth(true));
	       grid.asSingleSelect().addValueChangeListener(evt -> {
	    	   selectedNodeInfo = evt.getValue();
	    	   editNodeInfo(selectedNodeInfo); 
	       });
		
	}
	
	 private void editNodeInfo(NodeInfo nodeInfo) {
	        if (nodeInfo == null) {
	            // closeEditor();
	        } else {
	            form.setNodeInfo(nodeInfo);
	            form.setVisible(true);
	            addClassName("editing");
	        }
	    }
	 
	  private void closeEditor() {
	        form.setNodeInfo(null);
	        form.setVisible(false);
	        removeClassName("editing");
	    }

	private void navigateToMainPage() {
		Notification.show("You are not logged in and NOT authorized!!! Log in! ",3000, Position.MIDDLE );
		UI.getCurrent().navigate("");
		
		
	}
	
	 private void resetNode(NodeInfoForm.ResetEvent evt) {
		 Unirest.setTimeouts(0, 0);
	    	if (service.getHostName() != null)
			try {
				HttpResponse<String> response = 
						Unirest.post(String.format("http://%s:80/REST/node/%s/manage/reset", 
								service.getHostName(), evt.getNodeInfo().getId()))
				  .basicAuth(service.getName(), service.getPassword())
				  .header("Content-Type", "application/json")
				  //.header("Authorization", "Basic YWRtaW46YWRtaW4=")
				  .asString();
				Notification.show(String.format("%s", response.getBody()));
			} catch (UnirestException  e) {
				e.printStackTrace();
			} 
	        closeEditor();
	        editNodeInfo(evt.getNodeInfo());
		 
	 }
	
	 private void powerOffNode(NodeInfoForm.OffEvent evt) {
		 Unirest.setTimeouts(0, 0);
	    	if (service.getHostName() != null)
			try {
				HttpResponse<String> response = 
						Unirest.post(String.format("http://%s:80/REST/node/%s/manage/power_off", 
								service.getHostName(), evt.getNodeInfo().getId()))
				  .basicAuth(service.getName(), service.getPassword())
				  .header("Content-Type", "application/json")
				  //.header("Authorization", "Basic YWRtaW46YWRtaW4=")
				  .asString();
				Notification.show(String.format("%s", response.getBody()));
			} catch (UnirestException  e) {
				e.printStackTrace();
			} 
	        closeEditor();
	        editNodeInfo(evt.getNodeInfo());
	    }
		 
	 
	
	 private void powerOnNode(NodeInfoForm.OnEvent evt) {
		 Unirest.setTimeouts(0, 0);
	    	if (service.getHostName() != null)
			try {
				HttpResponse<String> response = 
						Unirest.post(String.format("http://%s:80/REST/node/%s/manage/power_on", 
								service.getHostName(), evt.getNodeInfo().getId()))
				  .basicAuth(service.getName(), service.getPassword())
				  .header("Content-Type", "application/json")
				  //.header("Authorization", "Basic YWRtaW46YWRtaW4=")
				  .asString();
				Notification.show(String.format("%s", response.getBody()));
			} catch (UnirestException  e) {
				e.printStackTrace();
			} 
	        closeEditor();
	        editNodeInfo(evt.getNodeInfo());
	    }
	 
	 private HorizontalLayout getToolBar() {
	        filterText.setPlaceholder("Filter by id contains ...");
	        filterText.setClearButtonVisible(true);
	        filterText.setValueChangeMode(ValueChangeMode.EAGER);
	        filterText.addValueChangeListener(e -> updateList());

	        HorizontalLayout toolbar = new HorizontalLayout(filterText);
	        toolbar.addClassName("toolbar");
	        return toolbar;
	    }
	 
	 
}
