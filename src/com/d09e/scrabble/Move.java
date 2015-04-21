package com.d09e.scrabble;

public class Move implements Comparable<Move>{
	
	private int dir;
	private int row;
	private int col;
	private Tile[] wordTiles;
	private int score = 0;

	public Move(int dir, int row, int col, Tile[] wordTiles){
		this.dir = dir;
		this.row = row;
		this. col = col;
		this.wordTiles = wordTiles;
	}
	
	public Move(Slot s, Tile[] wordTiles){
		this.dir = s.getDir();
		this.row = s.getRow();
		this.col = s.getCol();
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
	
	public int getScore(Board board){
		score = Score.getScore(board, Move.this);
		return score;
	}

	@Override
	public int compareTo(Move o) {
		if(score > o.score) return 1;
		else if(score < o.score) return -1;
		return 0;
	}
}
