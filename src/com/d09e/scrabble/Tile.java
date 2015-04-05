package com.d09e.scrabble;

public class Tile {

	private int id; // used to differentiate between tiles of same letter
	private char letter;
	private int value;
	
	public Tile(int id, char letter, int value){
		this.letter = letter;
		this.value = value;
	}
	
	public Tile copy(){
		return new Tile(id, letter, value);
	}
	
	public char getLetter() {
		return letter;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Tile [letter=" + letter + ", value=" + value + "]";
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

	
	
	
}
