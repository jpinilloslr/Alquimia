package model;

import java.util.ArrayList;

public class Combination {
	private String pair;
	private ArrayList<String> results;
	
	public Combination() {
		results = new ArrayList<String>();
	}
	
	public Combination(String pair, ArrayList<String> results) {
		this.pair = pair;
		this.results = results;		
	}
	
	public void setResult(ArrayList<String> results) {
		this.results = results;
	}
	
	public ArrayList<String> getResults() {
		return results;
	}
	
	public void setPair(String pair) {
		this.pair = pair;
	}
	
	public String getPair() {
		return pair;
	}
}
