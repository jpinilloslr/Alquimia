package model;

import java.util.ArrayList;

public class Element {
	private boolean visible;
	private String name;
	private ArrayList<Combination> combinations;
	
	private Combination getCombinationByPairName(String name) {
		Combination comb = null;		
		int i = 0;
		
		if(name.length() > 0) {
			while((null == comb) && (i < combinations.size())) {
				if(combinations.get(i).getPair().equals(name)) {
					comb = combinations.get(i);
				}
				i++;
			}
		}
		return comb;
	}
	
	public Element() {
		combinations = new ArrayList<Combination>();
		visible = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.length() > 0)
			this.name = name;
	}
	
	public void addCombination(String pair, ArrayList<String> results) {
		if((pair.length() > 0) && (results.size() > 0)) {
			if(null == getCombinationByPairName(pair))
				combinations.add(new Combination(pair, results));
		}
	}
	
	public boolean isCompatible(Element element) {
		return (null != getCombinationByPairName(element.getName()));			
	}
	
	public ArrayList<Combination> getCombinations() {
		return combinations;
	}
	
	public ArrayList<String> combine(Element element) {
		Combination comb = getCombinationByPairName(element.getName());
		ArrayList<String> results = null;
		
		if(null != comb) {
			results = comb.getResults();
		} 
		
		return results;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public String getFilename() {
		String filename = name;
		filename = filename.replaceAll(" ", "_");
		
		filename = filename.replace("á", "a");
		filename = filename.replace("é", "e");
		filename = filename.replace("í", "i");
		filename = filename.replace("ó", "o");
		filename = filename.replace("ú", "u");

		filename = filename.replace("Á", "A");
		filename = filename.replace("É", "E");
		filename = filename.replace("Í", "I");
		filename = filename.replace("Ó", "O");
		filename = filename.replace("Ú", "U");
		
		
		filename = filename.toLowerCase() + ".png";
		return filename;
	}
}
