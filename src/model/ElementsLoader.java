package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import view.Alchemy;

public class ElementsLoader {
	private ArrayList<Element> elements;
	
	public ElementsLoader(ArrayList<Element> elements) {
		this.elements = elements;
	}
	
	public String loadFile() {
		String data = "";
		StringBuffer buffer = new StringBuffer();
		try {
	        InputStreamReader isr = new InputStreamReader(Alchemy.class.getResourceAsStream("data/elements.dat"));
	        Reader in = new BufferedReader(isr);
	        int ch;

	        while ((ch = in.read()) > -1) {
	            buffer.append((char)ch);
	        }
	        in.close();
	        data = buffer.toString();
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	    }
		return data;
	}
	
	public void loadElements() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	
		Document dom = null;
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			//String data = loadFile();
			//ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
			//dom = db.parse(stream);
			dom = db.parse(Alchemy.class.getResourceAsStream("data/elements.dat"));		
		}catch(Exception exc) {
			exc.printStackTrace();
		}
		
		if(null != dom) {
			org.w3c.dom.Element root = dom.getDocumentElement();			
			NodeList nl = root.getElementsByTagName("Element");
			
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {
					org.w3c.dom.Element el = (org.w3c.dom.Element)nl.item(i);
					parseElement(el);
				}
			}
		}
	}
	
	private void parseElement(org.w3c.dom.Element el) {
		String name = el.getAttribute("name");			

		NodeList comb = el.getElementsByTagName("Combination");

		if(comb != null && comb.getLength() > 0) {
			for(int i = 0 ; i < comb.getLength();i++) {
				org.w3c.dom.Element combElement = (org.w3c.dom.Element)comb.item(i);
				String pair = combElement.getAttribute("pair");

				NodeList resultNode = combElement.getElementsByTagName("Results");
				if(resultNode != null && resultNode.getLength() > 0) {
					String result = resultNode.item(0).getTextContent();
					loadCombination(name, pair, result);
				}
			}
		} else {
			Element element = null;
			element = ElementsManager.getSingleton().getElementByName(name);
			
			if(null == element) {
				element = new Element();
				element.setName(name);
				elements.add(element);
			}
		}
	}
	
	private void loadCombination(String name, String pair, String result) {
		ArrayList<String> results = new ArrayList<String>();
		String[] resultsArray = result.split(",");	
		
		for (String currentResult : resultsArray) {
			results.add(currentResult.trim());
		}		
		
		Element element = null;
		element = ElementsManager.getSingleton().getElementByName(name);
		
		if(null == element) {
			element = new Element();
			element.setName(name);
			element.addCombination(pair, results);
			elements.add(element);
		} else {
			element.addCombination(pair, results);
		}

		element = null;
		element = ElementsManager.getSingleton().getElementByName(pair);
		
		if(null == element) {
			element = new Element();
			element.setName(pair);
			element.addCombination(name, results);
			elements.add(element);
		} else {
			element.addCombination(name, results);
		}
	}
}
