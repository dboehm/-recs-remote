package com.example.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NodeHandler extends DefaultHandler {

	private String id;
	private String health;
	private String baseBoardId;
	private String baseBoardPosition;
	private String state;
	
	private List<NodeInfo> nodeInfoList = new ArrayList<>();
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		 if (qName.equalsIgnoreCase("node")) {
			 	 
	         id = attributes.getValue("id");
	         health = attributes.getValue("health");
	         baseBoardId = attributes.getValue("baseBoardId");
			 baseBoardPosition = attributes.getValue("baseBoardPosition");
			 state = attributes.getValue("state");
	         
	      } 
	   }
	

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		 if (qName.equalsIgnoreCase("node")) {
	        NodeInfo nodeInfo = new NodeInfo(id, health, baseBoardId, baseBoardPosition, state);
	        nodeInfoList.add(nodeInfo);
	      }
	}


	public List<NodeInfo> getNodeInfoList() {
		return nodeInfoList;
	}
	
	

}
