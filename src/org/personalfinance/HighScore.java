package org.personalfinance;

import android.os.Parcel;
import android.os.Parcelable;

public class HighScore implements Comparable<HighScore>, Parcelable {

	private String name;
	private int score;

	// Don't remove this contructor
	public HighScore() {

	}

	public HighScore(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public HighScore(Parcel in){
		super();
		this.score = in.readInt();
		this.name = in.readString();

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

		if (this.score < arg0.getScore()) {
			return -1;
		} else if (this.score > arg0.getScore()) {
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(this.score);
		dest.writeString(this.name);

	}

	public static final Parcelable.Creator<HighScore> CREATOR = new Parcelable.Creator<HighScore>() {
		public HighScore createFromParcel(Parcel in) {
			return new HighScore(in);
		}

		public HighScore[] newArray(int size) {
			return new HighScore[size];
		}
	};

}
