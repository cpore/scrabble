package com.d09e.scrabble;

import java.util.HashSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.d09e.scrabble.exception.InvalidPlacementException;

public class Board implements Jsonizable{

	public static final String FIRST_MOVE = "firstMove";

	public static final int ROWS = 15;
	public static final int COLS = 15;
	public boolean firstMove;
	private Square[][] squares = new Square[ROWS][COLS];

	public Board() {
		//initialize squares
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				squares[i][j] = new Square(i, j);
			}
		}
		firstMove = true;
	}

	//Copy ctor
	public Board(Board board) {
		this.firstMove = board.firstMove;
		//initialize squares
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				squares[i][j] = board.squares[i][j].copy();
			}
		}
	}

	public Board(JSONObject jo) {
		this.firstMove = jo.getBoolean(FIRST_MOVE);
		//initialize squares
		for(int i=0; i<ROWS; i++){
			JSONArray row = jo.getJSONArray(String.valueOf(i));
			for(int j=0; j<COLS; j++){
				squares[i][j] = new Square(row.getJSONObject(j));
			}
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put(FIRST_MOVE, firstMove);

		for(int i=0; i<ROWS; i++){
			JSONArray row = new JSONArray();
			for(int j=0; j<COLS; j++){
				row.put(squares[i][j].toJson());
			}
			jo.put(String.valueOf(i), row);
		}
		return jo;
	}

	public Board copy(){
		return new Board(this);
	}

	public boolean isEmpty(){
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				if(squares[i][j].hasTile()) return false;
			}
		}
		return true;
	}

	public Tile getTile(int row, int col){
		return squares[row][col].getTile();
	}

	public boolean isStar(int row, int col){
		return squares[row][col].isStar;
	}

	public int getTileMultiplier(int row, int col){
		return squares[row][col].getTileMultiplier();
	}

	public int getWordMultiplier(int row, int col){
		return squares[row][col].getWordMultiplier();
	}

	public HashSet<Slot> getPossibleSlots(Rack rack){
		HashSet<Slot> possibleSlots = new HashSet<Slot>();
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++){
				if(!squareIsUsed(i, j)) continue;

				Slot hSlot = new Slot(D.HORIZONTAL, i, j, getTile(i, j));
				Slot vSlot = new Slot(D.VERTICAL, i, j, getTile(i, j));

				int rackCounter = 0;
				int tileCounter = 0;
				//get west path
				while(rackCounter < rack.size() && inBoardBounds(i, j-rackCounter-tileCounter)){
					if(hasWestNeighbor(i, j-rackCounter-tileCounter)){
						while(hasWestNeighbor(i, j-rackCounter-tileCounter)){
							tileCounter++;
							hSlot.addNode(getTile(i, j-rackCounter-tileCounter), i, j-rackCounter-tileCounter);
						}
					}else{
						rackCounter++;
					}

				}
				// if there wasn't any room to add tiles from the rack don't set the start index
				if(rackCounter > 0)	hSlot.setStart(j-rackCounter-tileCounter);
				// get east path
				rackCounter = 0;
				tileCounter = 0;
				while(rackCounter < rack.size() && inBoardBounds(i, j+rackCounter+tileCounter)){
					if(hasEastNeighbor(i, j+rackCounter+tileCounter)){
						while(hasEastNeighbor(i, j+rackCounter+tileCounter)){
							tileCounter++;
							hSlot.addNode(getTile(i, j+rackCounter+tileCounter), i, j+rackCounter+tileCounter);
						}
					}else{
						rackCounter++;
					}

				}
				if(rackCounter > 0) hSlot.setEnd(j+rackCounter+tileCounter);
				
				if(hSlot.hasPath()){
					possibleSlots.add(hSlot);
				}

				rackCounter = 0;
				tileCounter = 0;
				//get north path
				while(rackCounter < rack.size() && inBoardBounds(i-rackCounter-tileCounter, j)){
					if(hasNorthNeighbor(i-rackCounter-tileCounter, j)){
						while(hasNorthNeighbor(i-rackCounter-tileCounter, j)){
							tileCounter++;
							hSlot.addNode(getTile(i-rackCounter-tileCounter, j), i-rackCounter-tileCounter, j);
						}
					}else{
						rackCounter++;
					}

				}
				if(rackCounter > 0) vSlot.setStart(i-rackCounter-tileCounter);
				
				rackCounter = 0;
				tileCounter = 0;
				//get south path
				while(rackCounter < rack.size() && inBoardBounds(i+rackCounter+tileCounter, j)){
					if(hasSouthNeighbor(i+rackCounter+tileCounter, j)){
						while(hasSouthNeighbor(i+rackCounter+tileCounter, j)){
							tileCounter++;
							hSlot.addNode(getTile(i+rackCounter+tileCounter, j), i+rackCounter+tileCounter, j);
						}
					}else{
						rackCounter++;
					}

				}
				if(rackCounter > 0) vSlot.setEnd(i+rackCounter+tileCounter);
				
				if(vSlot.hasPath()) possibleSlots.add(vSlot);
			}
		}
		return possibleSlots;
	}

	public void placeWord(Move move) throws InvalidPlacementException{
		int dir = move.getDir();
		int i = move.getRow();
		int j = move.getCol();
		Tile[] wordTiles = move.getWordTiles();

		if(dir == D.HORIZONTAL){
			int col = j;
			for(Tile t: wordTiles){
				//jump over used squares
				while(squares[i][col].hasTile()){
					col++;
				}
				squares[i][col++].placeTile(t);
			}
		}else if(dir == D.VERTICAL){
			int row = i;
			for(Tile t: wordTiles){
				//jump over used squares
				while(squares[row][j].hasTile()){
					row++;
				}
				squares[row++][j].placeTile(t);
			}
		}

	}

	public boolean squareIsUsed(int row, int col){
		return squares[row][col].hasTile();
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
		System.out
		.println(  "     0    1    2    3    4    5    6    7    8    9    10   11   12   13   14");

	}

	public static boolean inBoardBounds(int i, int j){
		return i >=0 && i < Board.ROWS && j >=0 && j < Board.COLS;
	}

	public boolean hasNorthNeighbor(int row, int col){
		if(inBoardBounds(row-1, col)){
			return squares[row-1][col].getTile() != null;
		}
		return false;
	}

	public boolean hasSouthNeighbor(int row, int col){
		if(inBoardBounds(row+1, col)){
			return squares[row+1][col].getTile() != null;
		}
		return false;
	}

	public boolean hasEastNeighbor(int row, int col){
		if(inBoardBounds(row, col+1)){
			return squares[row][col+1].getTile() != null;
		}
		return false;
	}

	public boolean hasWestNeighbor(int row, int col){
		if(inBoardBounds(row, col-1)){
			return squares[row][col-1].getTile() != null;
		}
		return false;
	}

	/**
	 * A Square on the Board onto which Tiles are placed.
	 * @author cpore
	 *
	 */
	private static class Square implements Jsonizable{
		public static final String IS_DBL_LTR = "isDoubleLetter";
		public static final String IS_DBL_WRD = "isDoubleWord";
		public static final String IS_TPL_LTR = "isTripleLetter";
		public static final String IS_TPL_WRD = "isTripleWord";
		public static final String IS_STAR = "isStar";
		public static final String TILE = "tile";
		public static final String I = "i";
		public static final String J = "j";

		boolean isDoubleLetter = false;
		boolean isDoubleWord = false;
		boolean isTripleLetter = false;
		boolean isTripleWord = false;
		boolean isStar = false;

		private Tile tile = null;

		private int i;
		private int j;

		public Square(int i, int j){
			isDoubleLetter = Util.isDoubleLetter(i, j);
			isDoubleWord = Util.isDoubleWord(i, j);
			isTripleLetter = Util.isTripleLetter(i, j);
			isTripleWord = Util.isTripleWord(i, j);
			isStar = Util.isStar(i, j);
			this.i = i;
			this.j = j;
		}

		public Square(int i, int j, Tile tile){
			this.tile = tile;
			isDoubleLetter = Util.isDoubleLetter(i, j);
			isDoubleWord = Util.isDoubleWord(i, j);
			isTripleLetter = Util.isTripleLetter(i, j);
			isTripleWord = Util.isTripleWord(i, j);
			isStar = Util.isStar(i, j);
			this.i = i;
			this.j = j;
		}

		public Square(JSONObject jo){
			isDoubleLetter = jo.getBoolean(IS_DBL_LTR);
			isDoubleWord = jo.getBoolean(IS_DBL_WRD);
			isTripleLetter = jo.getBoolean(IS_TPL_LTR);
			isTripleWord = jo.getBoolean(IS_TPL_WRD);
			isStar = jo.getBoolean(IS_STAR);
			if(jo.has(TILE)) tile = new Tile(jo.getJSONObject(TILE));
			i = jo.getInt(I);
			j = jo.getInt(J);
		}

		@Override
		public JSONObject toJson() {
			JSONObject jo = new JSONObject();
			jo.put(IS_DBL_LTR, isDoubleLetter);
			jo.put(IS_DBL_WRD, isDoubleWord);
			jo.put(IS_TPL_LTR, isTripleLetter);
			jo.put(IS_TPL_WRD, isTripleWord);
			jo.put(IS_STAR, isStar);
			if(tile != null) jo.put(TILE, tile.toJson());
			jo.put(I, i);
			jo.put(J, j);
			return jo;
		}

		public Tile getTile(){
			return tile;
		}

		public int getTileMultiplier(){
			int multiplier = 1;
			if(isDoubleLetter)
				multiplier = 2;
			else if(isTripleLetter)
				multiplier = 3;

			return multiplier;
		}

		public int getWordMultiplier(){
			int multiplier = 1;
			if(isDoubleWord || isStar)
				multiplier = 2;
			else if(isTripleWord)
				multiplier = 3;

			return multiplier;
		}

		public Square copy(){
			Tile t = null;
			if(tile != null)
				t = tile.copy();
			return new Square(i, j, t);
		}

		public boolean hasTile(){
			if(tile == null) return false;

			//System.out.println("Square has tile: " + tile.getPlacedLetter());
			return true;
		}

		public void placeTile(Tile tile) throws InvalidPlacementException{
			if(this.tile != null) throw new InvalidPlacementException("Square not empty, contains:" + this.tile.getLetter());
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

			if(tile.getValue() == 10){
				return " " + String.valueOf(tile.getPlacedLetter()) + String.valueOf(tile.getValue());
			}
			return " " + String.valueOf(tile.getPlacedLetter()) + String.valueOf(tile.getValue()) + " ";

		}

		@Override
		public String toString() {
			return "Square [tile=" + tile + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + i;
			result = prime * result + j;
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
			Square other = (Square) obj;
			if (i != other.i)
				return false;
			if (j != other.j)
				return false;
			return true;
		}
	}

}


