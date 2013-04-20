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
		
		filename = filename.replace("�", "a");
		filename = filename.replace("�", "e");
		filename = filename.replace("�", "i");
		filename = filename.replace("�", "o");
		filename = filename.replace("�", "u");

		filename = filename.replace("�", "A");
		filename = filename.replace("�", "E");
		filename = filename.replace("�", "I");
		filename = filename.replace("�", "O");
		filename = filename.replace("�", "U");
		
		
		filename = filename.toLowerCase() + ".png";
		return filename;
	}
}
