package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.d09e.scrabble.exception.InvalidPlacementException;

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

	public void printRack() {
		System.out.print("Rack: ");
		for (Tile tile: rack) {
			System.out.print(tile.getLetter() + " ");
		}
		System.out.println();
	}

	public int getNumberOfLetters() {
		return rack.size();
	}

	public boolean isHuman() {
		return isHuman;
	}

	public void getMove(Board board) {
		if(isHuman){
			promptForWord(board);
		}else{
			findBestWord(board);
		}

	}

	private String findBestWord(Board board) {
		// TODO
		return "";

	}

	private void promptForWord(Board board) {

		Scanner scanner = new Scanner(System.in);
		boolean moveOk = false;
		while(!moveOk){
			System.out.print(name + " ");
			printRack();
			System.out.print("enter h or v and start square and word or press enter to print board:  ");
			String move = scanner.nextLine();
			Scanner lineScanner = new Scanner(move);
			String cmd = null;
			
			try{
				cmd = lineScanner.next();
			}catch(NoSuchElementException nsee){
				board.printBoard();
				continue;
			}
			int dir = -1;
			if(cmd.equalsIgnoreCase("h")){
				dir = D.HORIZONTAL;
			}else if(cmd.equalsIgnoreCase("v")){
				dir = D.VERTICAL;
			}
			int row = lineScanner.nextInt();
			int col = lineScanner.nextInt();
			String word = lineScanner.next();
			word = word.toUpperCase();
			
			System.out.println(String.valueOf(row) + " " + String.valueOf(col) + " " + word);
			
			// TODO do checks, MOAR CHECKS!
			if(D.inBoardBounds(row, col) && rackContains(word)){
				
				try {
					board.placeWord(dir, row, col, getWordTiles(word));
				} catch (InvalidPlacementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				moveOk = true;
				lineScanner.close();
			}else{
				System.out.println("Move not OK");
			}
			
		}
		//scanner.close();
	}
	
	private Tile[] getWordTiles(String word){
		Tile[] tiles = new Tile[word.length()];
		int tileidx = 0;
		for(char c: word.toCharArray()){
			for(Tile t: rack){
				if(t.getLetter() == c){
					tiles[tileidx++] = t;
					break;
				}
			}
		}
		return tiles;
	}
	
	public boolean rackContains(String word){
		for(char c: word.toCharArray()){
			if(!rackContains(c)) return false;
		}
		return true;
	}
	
	private boolean rackContains(char c){
		for(Tile t: rack){
			if(t.getLetter() == c){
				return true;
			}
		}
		return false;
	}



}
