package com.d09e.scrabble;

import java.util.ArrayList;

public class Player {

	private String name;
	private int score;

	private ArrayList<Tile> rack = new ArrayList<Tile>();

	private boolean isHuman;

	public Player(String name, boolean isHuman){
		this.name = name;
		this.score = 0;
		this.isHuman = isHuman;
	}

	public int getScore() {
		return score;
	}
	
	public String getNamr() {
		return name;
	}

	public void setScore(int delta) {
		score += delta;
	}

	public void drawTile(Bag bag){
		if(rack.size() > 7){
			throw new IndexOutOfBoundsException();
		}
		rack.add(bag.drawTile());
	}

	public void addTile(Tile tile) {
		if(rack.size() > 7){
			throw new IndexOutOfBoundsException();
		}
		rack.add(tile);
	}

	public void removeTile(Tile tile) {
		rack.remove(tile);
	}
	
	public ArrayList<Tile> getRack(){
		return rack;
	}

	public void printPlayerLetters() {
		for (Tile tile: rack) {
			System.out.print(tile.getLetter() + "   ");
		}
		System.out.println();
	}

	public int getNumberOfLetters() {
		return rack.size();
	}

	public boolean isHuman() {
		return isHuman;
	}


}
