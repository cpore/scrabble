package com.d09e.scrabble;


public class Score {

	
	public static int getScore(Board board, int dir, int row, int col) {
		int score = 0;
		if(dir == D.HORIZONTAL){
			score = getHWordScore(board, row, col);
		}else if(dir == D.VERTICAL){
			score = getVWordScore(board, row, col);
		}

		return score;
	}

	private static int getHWordScore(Board board, int row, int col){
		int score = 0;
		// Go to the westmost position of vertical cluster
		int startCol = col;
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}
		
		score += getMainHWordScore(board, row, startCol);

		if(board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)){
			score += getVCrossScore(board, row, startCol);
		}

		while(board.hasSouthNeighbor(row, startCol++)){
			if(board.hasNorthNeighbor(row, startCol) || board.hasSouthNeighbor(row, startCol)){
				score += getVCrossScore(board, row, startCol);
			}
		}

		return score;
	}

	private static int getVWordScore(Board board, int row, int col){
		int score = 0;

		// Go to the northmost position of vertical cluster
		int startRow = row;
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}
		
		score += getMainVWordScore(board, startRow, col);

		if(board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)){
			score += getHCrossWordScore(board, startRow, col);
		}

		while(board.hasSouthNeighbor(startRow++, col)){

			if(board.hasEastNeighbor(startRow, col) || board.hasWestNeighbor(startRow, col)){
				score += getHCrossWordScore(board, startRow, col);
			}
		}

		return score;
	}

	private static int getVCrossScore(Board board, int startRow, int col){
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		int score = 0;

		score += board.getTile(startRow, col).getValue();

		while(board.hasSouthNeighbor(startRow++, col)){
			score += board.getTile(startRow, col).getValue();
		}

		return score;

	}
	
	private static int getHCrossWordScore(Board board, int row, int startCol){
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		int score = 0;

		score += board.getTile(row, startCol).getValue();

		while(board.hasEastNeighbor(row, startCol++)){
			score += board.getTile(row, startCol).getValue();
		}

		return score;

	}
	
	private static int getMainHWordScore(Board board, int row, int startCol){
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		int score = 0;
		int wordMultiplier = 1;

		score += board.getTile(row, startCol).getValue()
				* board.getTileMultiplier(row, startCol);
		wordMultiplier *= board.getWordMultiplier(row, startCol);

		while (board.hasEastNeighbor(row, startCol++)) {
			score += board.getTile(row, startCol).getValue()
					* board.getTileMultiplier(row, startCol);
			
			wordMultiplier *= board.getWordMultiplier(row, startCol);
		}

		return score * wordMultiplier;

	}
	
	private static int getMainVWordScore(Board board, int startRow, int col){
		// Go to the westmost position of horizontal cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		int score = 0;
		int wordMultiplier = 1;

		score += board.getTile(startRow, col).getValue()
				* board.getTileMultiplier(startRow, col);
		wordMultiplier *= board.getWordMultiplier(startRow, col);

		while (board.hasSouthNeighbor(startRow, col++)) {
			score += board.getTile(startRow, col).getValue()
					* board.getTileMultiplier(startRow, col);
			
			wordMultiplier *= board.getWordMultiplier(startRow, col);
		}

		return score * wordMultiplier;

	}
}
