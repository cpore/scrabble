package com.d09e.scrabble;

import com.icantrap.collections.dawg.Dawg.Result;


public class Move implements Comparable<Move>{

	private int dir;
	private int row;
	private int col;
	private Tile[] wordTiles;
	private int score = 0;
	private String placedWord;

	public Move(int dir, int row, int col, Tile[] wordTiles){
		this.dir = dir;
		this.row = row;
		this. col = col;
		this.wordTiles = wordTiles;
	}

	public int getDir() {
		return dir;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Tile[] getWordTiles() {
		return wordTiles;
	}
	
	/*public Tile[] getPlacedWordTiles() {
		//not all the tiles may have been used after they have been placed? 
		//TODO
	}*/

	public String getTileString(){
		String tileString = "";
		for(Tile t: wordTiles){
			tileString += t.getLetter();
		}
		return tileString;
	}

	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return score;
	}

	public int getScore(Board board){
		score = Score.getScore(board, Move.this);
		return score;
	}

	/**
	 *  Get word as placed on the given board.
	 *  Precondition: move has been placed, and no moves have been placed
	 *  after this that may affect the word extracted from the board.
	 * @param board
	 * @return
	 */
	public void setPlacedWord(Board board){
		
		if(dir == D.HORIZONTAL){
			placedWord = getMainHWord(board);
		}else{
			placedWord = getMainVWord(board);
		}
		
		setPlacedWordTiles(placedWord);

	}
	
	public void setPlacedWordTiles(String word){
		if(word.contains("?")){
			String pattern = word.replace("?", ".");
			for(Result r: Scrabble.dawg.subwords(word, word)){
				if(r.word.matches(pattern)){
					System.out.println("PATTERN: " + r.word + ":" + word);
					setPlacedTiles(word, r.word);
					this.placedWord = r.word;
					return;
				}
			}
		}
	}
	
	//XXX
	private void setPlacedTiles(String wordWithWC, String word){
		char[] wordArray = wordWithWC.toCharArray();
		for( int i=0; i<wordArray.length;i++ ){
			for(Tile t: wordTiles){
				if(t.getLetter() == wordArray[i] && wordArray[i] == '?'){
					t.setPlacedLetter(word.charAt(i));
					break;
				}
			}
		}
	}
	
	public String getWord(){
		return placedWord;
	}

	private String getMainHWord(Board board){
		String word = "";
		int startCol = col;
		// Go to the westmost position of horizontal cluster
		while(board.hasWestNeighbor(row, startCol)){
			startCol--;
		}

		do{
			word += board.getTile(row, startCol).getLetter();
		}while(board.hasEastNeighbor(row, startCol++));

		return word;

	}

	private String getMainVWord(Board board){
		String word = "";
		int startRow = row;
		// Go to the northmost position of vertical cluster
		while(board.hasNorthNeighbor(startRow, col)){
			startRow--;
		}

		do{
			//TODO getLetter or getPlacedLetter ?
			word += board.getTile(startRow, col).getLetter();
		}while(board.hasSouthNeighbor(startRow++, col));

		System.out.println("isVWordOk: " + word);
		return word;
	}
	
	@Override
	public int compareTo(Move o) {
		if(score > o.score) return 1;
		else if(score < o.score) return -1;
		return 0;
	}
}
