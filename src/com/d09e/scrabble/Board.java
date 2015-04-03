package com.d09e.scrabble;

import com.d09e.scrabble.exception.InvalidPlacementException;


public class Board {
	public static final int ROWS = 15;
	public static final int COLS = 15;
	private Square[][] squares = new Square[ROWS][COLS];

	public Board() {
		//initialize squares
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				squares[i][j] = new Square(i, j);
			}
		}
	}

	public void placeTile(int i, int j, Tile t){
		try {
			squares[i][j].placeTile(t);
		} catch (InvalidPlacementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void placeWord(String word, Square start, Square End){

	}

	public void printBoard(){
		System.out
		.println(  "     0    1    2    3    4    5    6    7    8    9    10   11   12   13   14");

		System.out
		.println("   +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+");
		for(int i =0; i<ROWS; i++){

			if(i<10){
				System.out.print(i + "  |");
			}else{
				System.out.print(i + " |");
			}
			for(int j=0; j<COLS; j++){
				System.out.print(squares[i][j].topOfSquare() + "|");
			}
			System.out.println();
			System.out.print("   |");
			for(int j=0; j<COLS; j++){
				System.out.print(squares[i][j].bottomOfSquare() + "|");
			}
			System.out.println();
			System.out
			.println("   +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+");
		}
	}

	/**
	 * A Square on the Board onto which Tiles are placed.
	 * @author cpore
	 *
	 */
	private static class Square {
		boolean isDoubleLetter = false;
		boolean isDoubleWord = false;
		boolean isTripleLetter = false;
		boolean isTripleWord = false;
		boolean isStar = false;

		private Tile tile = null;

		public Square(int i, int j){
			isDoubleLetter = Util.isDoubleLetter(i, j);
			isDoubleWord = Util.isDoubleWord(i, j);
			isTripleLetter = Util.isTripleLetter(i, j);
			isTripleWord = Util.isTripleWord(i, j);
			isStar = Util.isStar(i, j);
		}

		public Square(int i, int j, Tile tile){
			this.tile = tile;
			isDoubleLetter = Util.isDoubleLetter(i, j);
			isDoubleWord = Util.isDoubleWord(i, j);
			isTripleLetter = Util.isTripleLetter(i, j);
			isTripleWord = Util.isTripleWord(i, j);
			isStar = Util.isStar(i, j);
		}

		public void placeTile(Tile tile) throws InvalidPlacementException{
			if(this.tile != null) throw new InvalidPlacementException("Square not empty, contains:" + tile.getLetter());
			this.tile = tile;
		}

		public String topOfSquare(){
			if(isStar){
				return " *  ";
			}if(isDoubleLetter){
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

		public boolean isStar(){
			return isStar;
		}

		public boolean isDoubleLetter(){
			return isDoubleLetter;
		}

		public boolean isDoubleWord(){
			return isDoubleWord;
		}

		public boolean isTripleLetter(){
			return isTripleLetter;
		}

		public boolean isTripleWord(){
			return isTripleWord;
		}


		@Override
		public String toString() {
			return "Square [tile=" + tile + "]";
		}



	}
}


