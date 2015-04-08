package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Arrays;

import com.d09e.scrabble.exception.InvalidPlacementException;
import com.icantrap.collections.dawg.Dawg.Result;

public class D {

	//TODO deal with bad rack, swap?
	
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private D(){ }

	public static boolean isValidWord(String word){

		if(word.contains("?")){
			String pattern = word.replace("?", ".");
			for(Result r: Scrabble.dawg.subwords(word, word)){
				if(r.word.matches(pattern)){
					//System.out.println(r.word);
					return true;
				}
			}
			return false;
		}
		return Scrabble.dawg.contains(word);
	}

	public static Result[] getSubwords(String rack){
		return Scrabble.dawg.subwords(rack, "");
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
	public static boolean wordInBoardBounds(Board board, int dir, int row, int col, String word){
		if(dir == HORIZONTAL){
			for(int i=col; i<(word.length() + col); i++){
				if(i<0 || i>=Board.COLS) return false;
				//jump over used squares
				while(board.squareIsUsed(row, i)) i++;
			}
		}else if(dir == VERTICAL){
			for(int j=row; j<(word.length() + row); j++){
				if(j<0 || j>=Board.ROWS) return false;
				//jump over used squares
				while(board.squareIsUsed(j, col)) j++;
			}
		}

		return true;
	}

	public static boolean isValidMove(Board board, int dir, int row, int col, String word,
			Tile[] wordTiles){

		if(!isFirstMoveOK(board, dir, row, col, word.length())){
			System.out.println("First play must be on star.");
			return false;
		}
		
		if(!wordInBoardBounds(board, dir, row, col, word)){
			System.out.println("Word not in bounds or square already used.");
			return false;
		}

		if(!placementOk(board, dir, row, col, wordTiles)){
			System.out.println("Placement is invalid");
			return false;
		}

		board.firstMove = false;
		
		return true;
	}
	
	private static boolean isFirstMoveOK(Board board, int dir, int row, int col, int len){
		if(!board.firstMove) return true;
		
		if(dir == HORIZONTAL){
			return (row == 7 && col+len >= 7);
		}else if(dir == VERTICAL){
			return (col == 7 && row+len >= 7);
		}
		
		return false;
		
	}

	private static boolean placementOk(Board board, int dir, int row, int col,
			Tile[] wordTiles) {
		Board tempBoard = board.copy();

		try {
			tempBoard.placeWord(dir, row, col, wordTiles);
		} catch (InvalidPlacementException e) {
			// TODO Auto-generated catch block
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
			System.out.println("Bad Horizontal word");
			return false;
		}

		// Go to the westmost position of vertical cluster
		int startCol = col;
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}
		
		
		//check all the vertical words that cross this horizontal word
		if(board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)){
			if(!isVWordOk(board, row, startCol)){
				return false;
			}
		}

		while(board.hasSouthNeighbor(row, startCol++)){
			if(board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)){
				if(!isVWordOk(board, row, startCol)){
					return false;
				}
			}
		}

		return true;
	}

	private static boolean vPlacementOk(Board board, int row, int col){
		// check that this vertically placed word is valid
		if(!isVWordOk(board, row, col)){
			System.out.println("Bad Vertical word");
			return false;
		}

		// Go to the northmost position of vertical cluster
		int startRow = row;
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		
		//check all the horizontal words that cross this vertical word
		if(board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)){
			if(!isHWordOk(board, startRow, col)){
				return false;
			}
		}

		while(board.hasSouthNeighbor(startRow++, col)){
			if(board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)){
				if(!isHWordOk(board, startRow, col)){
					return false;
				}
			}
		}

		return true;
	}

	private static boolean isVWordOk(Board board, int startRow, int col){
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		String word = "";

		word += board.getTile(startRow, col).getLetter();

		while(board.hasSouthNeighbor(startRow++, col)){
			word += board.getTile(startRow, col).getLetter();
		}

		System.out.println(word);
		return isValidWord(word);

	}
	
	private static boolean isVWordConnected(Board board, int startRow, int col, Tile[] wordTiles){
		if(board.firstMove) return true;
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}
		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));
		System.out.print("Tiles: ");
		for(Tile t: tiles)
			System.out.print(t);
		Tile t = board.getTile(startRow, col);
		String word = "";
		word += t.getLetter();
		if(!tiles.contains(t)){
			return true;
		}
		
		while(board.hasSouthNeighbor(startRow++, col)){
			t = board.getTile(startRow, col);
			word += t.getLetter();
			if(!tiles.contains(t)){
				System.out.print(word);
				return true;
			}
		}

		System.out.println("isVWordConnected: " + word);
		System.out.println("Vertical Word not connected to a cluster.");
		return false;

	}
	
	private static boolean isHWordConnected(Board board, int row, int startCol, Tile[] wordTiles){
		if(board.firstMove) return true;
		// Go to the westmost position of vertical cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}
		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));
		
		String word = "";
		Tile t = board.getTile(row, startCol);
		word += t.getLetter();
		if(!tiles.contains(t)){
			return true;
		}
		
		while(board.hasEastNeighbor(row, startCol++)){
			t = board.getTile(row, startCol);
			word += t.getLetter();
			if(!tiles.contains(t)){
				return true;
			}
		}

		System.out.println("isHWordConnected: " + word);
		System.out.println("Horizontal Word not connected to a cluster.");
		return false;

	}

	private static boolean isHWordOk(Board board, int row, int startCol){
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		String word = "";

		word += board.getTile(row, startCol).getLetter();

		while(board.hasEastNeighbor(row, startCol++)){
			word += board.getTile(row, startCol).getLetter();
		}

		System.out.println("isHWordOK: " + word);
		return isValidWord(word);

	}


}
