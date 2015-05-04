package com.d09e.scrabble;

import java.util.ArrayList;

import com.icantrap.collections.dawg.Dawg.Result;


public class Move implements Comparable<Move>{
	private static final boolean DEBUG = false;

	private int dir;
	private int row;
	private int col;
	private Tile[] wordTiles;
	private int score = 0;
	private float utility = 0f;
	private String placedWord;

	public Move(int dir, int row, int col, Tile[] wordTiles){
		this.dir = dir;
		this.row = row;
		this. col = col;
		this.wordTiles = wordTiles;
	}

	public Move copy(){
		Tile[] newTile = new Tile[wordTiles.length];
		for(int i=0; i< wordTiles.length; i++){
			newTile[i] = wordTiles[i].copy();
		}
		return new Move(dir, row, col, newTile);
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

	public void setUtility(float delta){
		this.utility = score + delta;
	}
	
	public float getUtility(){
		if(utility == 0) return score;
		return utility;
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
			Result[] results = Scrabble.dawg.subwords(word, word);
			if(results == null){
				System.out.println("results null with word: " + word);
				System.exit(0);
				return;
			}
			for(Result r: results){
				if(r.word.matches(pattern)){
					if(DEBUG) System.out.println("SET TILE PATTERN: " + r.word + ":" + word);
					setPlacedTiles(word, r.word);
					this.placedWord = r.word;
					return;
				}
			}
			if(DEBUG) System.out.println("NO MATCHES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
	}

	private void setPlacedTiles(String wordWithWC, String word){
		char[] charArray = wordWithWC.toCharArray();

		ArrayList<Integer> skipList = new ArrayList<Integer>();

		for(int i=0; i<charArray.length; i++){
			for(int j = 0; j< wordTiles.length; j++){
				if(!skipList.contains(j) && wordTiles[j].getLetter() == charArray[i] && charArray[i] == '?'){
					wordTiles[j].setPlacedLetter(word.charAt(i));
					skipList.add(j);
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
			word += board.getTile(row, startCol).getPlacedLetter();
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
			word += board.getTile(startRow, col).getPlacedLetter();
		}while(board.hasSouthNeighbor(startRow++, col));

		return word;
	}

	@Override
	public int compareTo(Move o) {
		//sorts in descending order
		if(getUtility() > o.getUtility()) return -1;
		else if(getUtility() < o.getUtility()) return 1;
		return 0;
	}
}
