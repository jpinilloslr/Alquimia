package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import view.Alchemy;

public class ElementsManager {
	private ArrayList<Element> elements;
	private static ElementsManager singleton;
	private int cluesCount;
	private int pointsForNextClue;
	
	private final int MAX_POINTS_FOR_CLUE;
	
	private ElementsManager() {
		elements = new ArrayList<Element>();
		setCluesCount(10);
		MAX_POINTS_FOR_CLUE = 3;
		pointsForNextClue = MAX_POINTS_FOR_CLUE;
	}
	
	public void addPoint() {
		if(pointsForNextClue > 0)
			pointsForNextClue--;
		else {
			if(cluesCount < 10)
				cluesCount++;
			pointsForNextClue = MAX_POINTS_FOR_CLUE;
		}
			
	}
	
	public static ElementsManager getSingleton() {
		if(null == singleton)
			singleton = new ElementsManager();
		
		return singleton;
	}
	
	public ArrayList<Element> getElements() {
		return elements;
	}
	
	public Element getElementByName(String name) {
		Element element = null;
		int i=0;
		
		while((null == element) && (i < elements.size())) {
			if(elements.get(i).getName().equals(name))
				element = elements.get(i); 
			
			i++;
		}
		
		return element;
	}
	
	public void loadElements() {
		ElementsLoader loader = new ElementsLoader(elements);
		loader.loadElements();
		loadState();
		getElementByName("Fuego").setVisible(true);
		getElementByName("Tierra").setVisible(true);
		getElementByName("Aire").setVisible(true);
		getElementByName("Agua").setVisible(true);
		orderByName();
	}
	
	public int getDiscovered() {
		int n = 0;
		for(int i=0; i<elements.size(); i++)
			if(elements.get(i).isVisible())
				n++;
		return n;
	}
	
	public int getCount() {
		return elements.size();
	}
	
	public void saveState() {
		String data = String.valueOf(cluesCount) + "," + String.valueOf(pointsForNextClue);
		
		for(int i=0; i<elements.size(); i++) {
			if(elements.get(i).isVisible()) {
				if(data.length() > 0)
					data += "," + elements.get(i).getName();
				else
					data += elements.get(i).getName();
			}
		}
		
		data = Cypher.encode(data);		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("state.dat"));
			writer.write(data);
			writer.close( );
		} catch (IOException e) {}
	}
	
	public void cypherData() {
		String data = "";
		StringBuffer buffer = new StringBuffer();
		try {
	        InputStreamReader isr = new InputStreamReader(Alchemy.class.getResourceAsStream("data/elements.dat"), "UTF8");
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
		
		data = Cypher.encode(data);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("elements.dat"));
			writer.write(data);
			writer.close( );
		} catch (IOException e) {}
	}
	
	public void loadState() {
		String data = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("state.dat"));
			data = reader.readLine();
			reader.close();
		} catch (IOException e) {
		}
		
		if(data.length() > 0) {
			int n = 0;
			data = Cypher.decode(data);			
			String[] resultsArray = data.split(",");	
			
			for (String currentResult : resultsArray) {
				currentResult = currentResult.trim();
				if(n >= 2)
					getElementByName(currentResult).setVisible(true);
				else 
				if(n == 0){
					try {
						cluesCount = Integer.valueOf(currentResult);
					} catch(Exception exc) {
						cluesCount = 10;
					}
				} else 
				if(n == 1){
					try {
						pointsForNextClue = Integer.valueOf(currentResult);
					} catch(Exception exc) {
						pointsForNextClue = MAX_POINTS_FOR_CLUE;
					}
				}
				n++;
			}		
		}
	}
	
	public void reset() {
		for(int i=0; i<elements.size(); i++) {
			elements.get(i).setVisible(false);
		}
		getElementByName("Fuego").setVisible(true);
		getElementByName("Tierra").setVisible(true);
		getElementByName("Aire").setVisible(true);
		getElementByName("Agua").setVisible(true);
		setCluesCount(10);
		pointsForNextClue = MAX_POINTS_FOR_CLUE;
		saveState();
	}
	
	private boolean rightStringIsMinor(String left, String right) {
		
		int size = left.length();
		if(size > right.length())
			size = right.length();
		int i = 0;
		
		while(i<size) {
			if((int)right.toLowerCase().charAt(i) < (int)left.toLowerCase().charAt(i))
				return true;
			else
			if((int)right.toLowerCase().charAt(i) > (int)left.toLowerCase().charAt(i))
				return false;
			i++;
		}
		
		return false;
	}
	
	private void orderByName() {	
		boolean completed = false;
		
		do {
			completed = true;
			
			for(int j=0; j<elements.size()-1; j++) {
				if(rightStringIsMinor(elements.get(j).getName(), elements.get(j+1).getName())) {
					Element element1 = elements.get(j);
					Element element2 = elements.get(j+1);
					
					elements.set(j, element2);
					elements.set(j+1, element1);
					
					completed = false;
				}
			}
		} while(!completed);
	}
	
	public String[] getClue()  {
		String clue[] = new String[2];
		
		if(getCluesCount() <= 0) {
			clue[0] = "NONE";
			clue[1] = "";
			return clue;
		}			
				
		int i, j;
		Element a, b;
		boolean clueFounded = false;
		ArrayList<String> results = new ArrayList<String>();
		
		i = 0;
		j = 0;
		a = null;
		b = null;
		
		clue[0] = "";
		clue[1] = "";
				
		while((i<elements.size()) && (!clueFounded)) {
			
			j = 0;			
			while((j<elements.size()) && (!clueFounded)) {
				a = elements.get(i);
				b = elements.get(j);
				if((a.isVisible()) && (b.isVisible())) {
					results = a.combine(b);
					//System.out.println(a.getFilename() + " + " + b.getFilename());
					if(null != results) {
						for (String result : results) {
							if(!getElementByName(result).isVisible())
								clueFounded = true;
						}
					}
				}
				j++;
			}
			i++;
		}
	
		if((null != a) && (null != b) && (clueFounded)) {
			clue[0] = a.getName();
			clue[1] = b.getName();
			cluesCount--;
			pointsForNextClue = MAX_POINTS_FOR_CLUE;
		}
		
		return clue;
	}

	public int getCluesCount() {
		return cluesCount;
	}

	public void setCluesCount(int cluesCount) {
		this.cluesCount = cluesCount;
	}

}
