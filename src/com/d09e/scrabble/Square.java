package com.d09e.scrabble;

/**
 * A Square on the Board onto which Tiles are placed.
 * @author cpore
 *
 */
public class Square {
	boolean isDoubleLetter = false;
	boolean isDoubleWord = false;
	boolean isTripleLetter = false;
	boolean isTripleWord = false;
	boolean isStar = false;

	private Tile tile = null;

	public Square(int i, int j){
		isDoubleLetter = isDoubleLetter(i, j);
		isDoubleWord = isDoubleWord(i, j);
		isTripleLetter = isTripleLetter(i, j);
		isTripleWord = isTripleWord(i, j);
	}

	public Square(int i, int j, Tile tile){
		this.tile = tile;
		isDoubleLetter = isDoubleLetter(i, j);
		isDoubleWord = isDoubleWord(i, j);
		isTripleLetter = isTripleLetter(i, j);
		isTripleWord = isTripleWord(i, j);
	}

	public void placeTile(Tile tile) throws InvalidPlacementException{
		if(this.tile != null) throw new InvalidPlacementException("Square not empty, contains:" + tile.getLetter());
		this.tile = tile;
	}

	public String topOfSquare(){
		if(isDoubleLetter){
			return " DL ";
		}else if(isDoubleWord){
			return " DW ";
		}else if(isTripleLetter){
			return " TL ";
		}else if(isTripleWord){
			return " TW ";
		}

		return "    ";
	}

	public String bottomOfSquare(){
		if(tile == null) return "    ";

		return " " + String.valueOf(tile.getLetter()) + String.valueOf(tile.getValue());

	}


	@Override
	public String toString() {
		return "Square [tile=" + tile + "]";
	}

	public boolean isStar(int i, int j){
		if(i == 7 && j == 7) return true;
		return false;
	}

	public boolean isDoubleLetter(int i ,int j){
		if(i == 0 && j == 3) return true;
		if(i == 0 && j == 11) return true;
		if(i == 2 && j == 6) return true;
		if(i == 2 && j == 8) return true;
		if(i == 3 && j == 0) return true;
		if(i == 3 && j == 7) return true;
		if(i == 3 && j == 14) return true;
		if(i == 6 && j == 2) return true;
		if(i == 6 && j == 6) return true;
		if(i == 6 && j == 8) return true;
		if(i == 6 && j == 12) return true;
		if(i == 7 && j == 3) return true;
		if(i == 7 && j == 11) return true;
		if(i == 8 && j == 2) return true;
		if(i == 8 && j == 6) return true;
		if(i == 8 && j == 8) return true;
		if(i == 8 && j == 12) return true;
		if(i == 11 && j == 0) return true;
		if(i == 11 && j == 7) return true;
		if(i == 11 && j == 14) return true;
		if(i == 12 && j == 6) return true;
		if(i == 12 && j == 8) return true;
		if(i == 14 && j == 3) return true;
		if(i == 14 && j == 11) return true;
		return false;
	}

	public boolean isTripleLetter(int i ,int j){
		if(i == 1 && j == 5) return true;
		if(i == 1 && j == 9) return true;
		if(i == 5 && j == 1) return true;
		if(i == 5 && j == 5) return true;
		if(i == 5 && j == 9) return true;
		if(i == 5 && j == 13) return true;
		if(i == 9 && j == 1) return true;
		if(i == 9 && j == 5) return true;
		if(i == 9 && j == 9) return true;
		if(i == 9 && j == 13) return true;
		if(i == 13 && j == 5) return true;
		if(i == 13 && j == 9) return true;
		return false;
	}

	public boolean isDoubleWord(int i ,int j){
		if(i == 1 && j == 1) return true;
		if(i == 2 && j == 2) return true;
		if(i == 3 && j == 3) return true;
		if(i == 4 && j == 4) return true;
		if(i == 1 && j == 13) return true;
		if(i == 2 && j == 12) return true;
		if(i == 3 && j == 11) return true;
		if(i == 4 && j == 10) return true;
		if(i == 10 && j == 4) return true;
		if(i == 11 && j == 3) return true;
		if(i == 12 && j == 2) return true;
		if(i == 13 && j == 1) return true;
		if(i == 10 && j == 10) return true;
		if(i == 11 && j == 11) return true;
		if(i == 12 && j == 12) return true;
		if(i == 13 && j == 13) return true;
		return false;
	}

	public boolean isTripleWord(int i ,int j){
		if(i == 0 && j == 0) return true;
		if(i == 0 && j == 7) return true;
		if(i == 0 && j == 14) return true;
		if(i == 7 && j == 0) return true;
		if(i == 14 && j == 0) return true;
		if(i == 7 && j == 14) return true;
		if(i == 14 && j == 7) return true;
		if(i == 14 && j == 14) return true;
		return false;
	}

	public class InvalidPlacementException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = -437777505153066782L;
		public InvalidPlacementException(String message) {
			super(message);
		}
	}

}
