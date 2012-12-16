package org.personalfinance;

public class HighScore implements Comparable<HighScore> {
	
	private String name;
	private int score;
	
	//Don't remove this contructor
	public HighScore(){
		
	}
	
	public HighScore(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(HighScore arg0) {

		if(this.score < arg0.getScore()){
			return -1;
		} else if (this.score > arg0.getScore()){
			return 1;
		} else {
			return 0;
		}
		
	}
	
	

}
