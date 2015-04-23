package com.d09e.scrabble;

import org.json.JSONObject;

public class Tile implements Jsonizable{
	
	public static final String ID = "id";
	public static final String LETTER = "letter";
	public static final String BLANK_LETTER = "blankLetter";
	public static final String VALUE = "value";

	public int id; // used to differentiate between tiles of same letter
	private char letter;
	private char placedLetter = '?';
	private int value;
	
	public Tile(int id, char letter, int value){
		this.id = id;
		this.letter = letter;
		this.value = value;
	}
	
	public Tile(int id, char letter, char blankLetter, int value){
		this.id = id;
		this.letter = letter;
		this.placedLetter = blankLetter;
		this.value = value;
	}
	
	public Tile(JSONObject jsonObject) {
		this.id = jsonObject.getInt(ID);
		this.letter = jsonObject.getString(LETTER).charAt(0);
		this.placedLetter = jsonObject.getString(BLANK_LETTER).charAt(0);
		this.value = jsonObject.getInt(VALUE);
	}

	public Tile copy(){
		return new Tile(id, letter, placedLetter, value);
	}
	
	public char getPlacedLetter() {
		if(letter == '?') return placedLetter;
		return letter;
	}
	
	public char getLetter() {
		return letter;
	}
	
	public void setPlacedLetter(char letter){
		if(this.letter != '?') return;
		this.placedLetter = letter;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Tile [id=" + id + ", letter=" + letter + ", placedLetter=" + placedLetter + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put(ID, id);
		jo.put(LETTER, Character.toString(letter));
		jo.put(BLANK_LETTER, Character.toString(placedLetter));
		jo.put(VALUE, value);
		return jo;
	}

	
	
}
