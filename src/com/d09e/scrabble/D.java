package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Arrays;

import com.d09e.scrabble.exception.InvalidPlacementException;

public class D {
	private static boolean DEBUG = false;

	//TODO deal with bad rack, swap?

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private D(){ }



	public static boolean isValidMove(Board board, Move move){

		if(!isFirstMoveOK(board, move)){
			if(DEBUG) System.out.println("First play must be on star.");
			return false;
		}

		if(!wordInBoardBounds(board, move)){
			if(DEBUG) System.out.println(move.getTileString() + ": Word not in bounds.");
			return false;
		}

		if(!placementOk(board, move)){
			if(DEBUG) System.out.println("Placement is invalid");
			return false;
		}

		return true;
	}

	/**
	 * Checks that a word is placed within board bounds.
	 * @param board
	 * @param dir
	 * @param row
	 * @param col
	 * @param word
	 * @return
	 */
	private static boolean wordInBoardBounds(Board board, Move move){
		int dir = move.getDir();
		int row = move.getRow();
		int col = move.getCol();
		int len = move.getWordTiles().length;

		int sq = 0;
		
		if(dir == HORIZONTAL){
			for(int i=col; i<(col+len); i++){
				if(i<0 || i+sq>=Board.COLS) return false;
				//jump over used squares
				while(board.squareIsUsed(row, i+sq)){ 
					sq++;
					if(i<0 || i+sq>=Board.COLS) return false;
				}
			}
		}else if(dir == VERTICAL){
			for(int j=row; j<(row+len); j++){
				if(j<0 || j+sq>=Board.ROWS) return false;
				//jump over used squares
				while(board.squareIsUsed(j+sq, col)){
					sq++; 
					if(j<0 || j+sq>=Board.ROWS) return false;
				}
			}
		}

		return true;
	}

	private static boolean isFirstMoveOK(Board board, Move move){
		if(!board.firstMove) return true;

		int dir = move.getDir();
		int row = move.getRow();
		int col = move.getCol();
		int len = move.getWordTiles().length;

		if(dir == HORIZONTAL){
			return row == 7 && col <= 7 && col+len-1 >= 7;
		}else if(dir == VERTICAL){
			return col == 7 && row <= 7 && row+len-1 >= 7;
		}

		return false;

	}

	private static boolean placementOk(Board board, Move move) {
		Board tempBoard = board.copy();

		int dir = move.getDir();
		int row = move.getRow();
		int col = move.getCol();
		Tile[] wordTiles = move.getWordTiles();


		try {
			tempBoard.placeWord(move, move.getTileString().contains("?"));
			//tempBoard.placeWord(move, false);
		} catch (InvalidPlacementException e) {
			System.out.println("Word out of bounds");
			return false;
		}

		if(dir == HORIZONTAL){
			return isHWordConnected(tempBoard, row, col, wordTiles)
					&& hPlacementOk(tempBoard, row, col);
		}else if(dir == VERTICAL){
			return isVWordConnected(tempBoard, row, col, wordTiles)
					&& vPlacementOk(tempBoard, row, col);
		}

		return false;
	}

	private static boolean hPlacementOk(Board board, int row, int col){
		// check that this horizontally placed word is valid
		if(!isHWordOk(board, row, col)){
			if(DEBUG) System.out.println("Bad Horizontal word");
			return false;
		}

		// Go to the westmost position of vertical cluster
		int startCol = col;
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		//check all the vertical words that cross this horizontal word
		do{
			if(board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)){
				if(!isVWordOk(board, row, startCol)){
					return false;
				}
			}
		}while(board.hasEastNeighbor(row, startCol++));

		return true;
	}

	private static boolean vPlacementOk(Board board, int row, int col){
		// check that this vertically placed word is valid
		if(!isVWordOk(board, row, col)){
			if(DEBUG) System.out.println("Bad Vertical word");
			return false;
		}

		// Go to the northmost position of vertical cluster
		int startRow = row;
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}


		//check all the horizontal words that cross this vertical word
		do{
			if(board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)){
				if(!isHWordOk(board, startRow, col)){
					return false;
				}
			}
		}while(board.hasSouthNeighbor(startRow++, col));

		return true;
	}

	private static boolean isVWordOk(Board board, int startRow, int col){
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		String word = "";

		do{
			//TODO getLetter or getPlacedLetter ?
			word += board.getTile(startRow, col).getPlacedLetter();
		}while(board.hasSouthNeighbor(startRow++, col));

		if(DEBUG) System.out.println("isVWordOk: " + word);
		return Scrabble.isValidWord(word);

	}

	private static boolean isHWordOk(Board board, int row, int startCol){
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		String word = "";

		do{
			//TODO getLetter or getPlacedLetter ?
			word += board.getTile(row, startCol).getPlacedLetter();
		}while(board.hasEastNeighbor(row, startCol++));

		if(DEBUG) System.out.println("isHWordOK: " + word);
		return Scrabble.isValidWord(word);

	}

	private static boolean isVWordConnected(Board board, int startRow, int col, Tile[] wordTiles){
		if(board.firstMove) return true;
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}
		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));
		Tile t = null;

		do{
			t = board.getTile(startRow, col);
			if(!tiles.contains(t) || board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)){
				return true;
			}
		}while(board.hasSouthNeighbor(startRow++, col));

		if(DEBUG) System.out.println("Vertical Word not connected to a cluster.");
		return false;

	}

	private static boolean isHWordConnected(Board board, int row, int startCol, Tile[] wordTiles){
		if(board.firstMove){
			return true;
		}
		// Go to the westmost position of vertical cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}
		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));

		Tile t = null;

		do{
			t = board.getTile(row, startCol);
			if(!tiles.contains(t) || board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)){
				return true;
			}
		}while(board.hasEastNeighbor(row, startCol++));

		if(DEBUG) System.out.println("Horizontal Word not connected to a cluster.");
		return false;

	}


}
