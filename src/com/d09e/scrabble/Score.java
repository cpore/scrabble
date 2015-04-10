package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Arrays;


public class Score {


	public static int getScore(Board board, int dir, int row, int col, Tile[] wordTiles) {
		int score = 0;
		if(dir == D.HORIZONTAL){
			score = getHWordScore(board, row, col, wordTiles);
		}else if(dir == D.VERTICAL){
			score = getVWordScore(board, row, col, wordTiles);
		}

		return score;
	}

	private static int getHWordScore(Board board, int row, int col, Tile[] wordTiles){
		int score = 0;
		// Go to the westmost position of vertical cluster
		int startCol = col;
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		score += getMainHWordScore(board, row, startCol, wordTiles);

		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));

		Tile t = null;

		do{
			t = board.getTile(row, startCol);
			if((board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)) && tiles.contains(t)){
				score += getVCrossScore(board, row, startCol);
			}
		}while(board.hasEastNeighbor(row, startCol++));

		return score;
	}

	private static int getVWordScore(Board board, int row, int col, Tile[] wordTiles){
		int score = 0;

		// Go to the northmost position of vertical cluster
		int startRow = row;
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		score += getMainVWordScore(board, startRow, col, wordTiles);

		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));

		Tile t = null;

		do{
			t = board.getTile(startRow, col);
			if((board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)) && tiles.contains(t)){
				score += getHCrossWordScore(board, startRow, col);
			}
		}while(board.hasSouthNeighbor(startRow++, col));

		return score;
	}

	private static int getVCrossScore(Board board, int startRow, int col){
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		int score = 0;

		do{
			score += board.getTile(startRow, col).getValue() * board.getTileMultiplier(startRow, col);
		}while(board.hasSouthNeighbor(startRow++, col));

		return score;

	}

	private static int getHCrossWordScore(Board board, int row, int startCol){
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		int score = 0;

		do{
			score += board.getTile(row, startCol).getValue() * board.getTileMultiplier(row, startCol);
		}while(board.hasEastNeighbor(row, startCol++));

		return score;

	}

	private static int getMainHWordScore(Board board, int row, int startCol, Tile[] wordTiles){
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		int score = 0;
		int wordMultiplier = 1;

		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));

		Tile t = null;

		do{
			t = board.getTile(row, startCol);

			if(tiles.contains(t)){
				score += t.getValue() * board.getTileMultiplier(row, startCol);
				wordMultiplier *= board.getWordMultiplier(row, startCol);
			}else {
				score += t.getValue();
			}
		}while(board.hasEastNeighbor(row, startCol++));

		return score * wordMultiplier;

	}

	private static int getMainVWordScore(Board board, int startRow, int col, Tile[] wordTiles){
		// Go to the westmost position of horizontal cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		int score = 0;
		int wordMultiplier = 1;

		ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(wordTiles));

		Tile t = null;

		do{
			t = board.getTile(startRow, col);

			if(tiles.contains(t)){
				score += t.getValue() * board.getTileMultiplier(startRow, col);
				wordMultiplier *= board.getWordMultiplier(startRow, col);
			}else {
				score += t.getValue();
			}
		}while (board.hasSouthNeighbor(startRow++, col));

		return score * wordMultiplier;

	}
}
