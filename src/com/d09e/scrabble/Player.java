package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.d09e.scrabble.exception.InvalidPlacementException;
import com.icantrap.collections.dawg.Dawg.Result;

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

	public void getMove(GameState gameState) {
		if(isHuman){
			promptForWord(gameState);
		}else{
			findBestWord(gameState);
		}

	}

	private String findBestWord(GameState gameState) {
		// TODO
		return "";

	}

	private void promptForWord(GameState gameState) {

		Scanner scanner = new Scanner(System.in);
		boolean moveOk = false;
		while(!moveOk){
			System.out.print(name + ": " + score + " ");
			printRack();
			System.out.println("enter h or v and start square and letters to play(in order)\nor c for hints\nor s for scores\nor press enter to print board:  ");
			String move = scanner.nextLine();
			Scanner lineScanner = new Scanner(move);
			String cmd = null;

			try{
				cmd = lineScanner.next();
			}catch(NoSuchElementException nsee){
				gameState.printScores();
				gameState.printBoard();
				continue;
			}
			int dir = -1;
			if(cmd.equalsIgnoreCase("h")){
				dir = D.HORIZONTAL;
			}else if(cmd.equalsIgnoreCase("v")){
				dir = D.VERTICAL;
			}else if(cmd.equalsIgnoreCase("c")){
				for(Result r: D.getSubwords(getRackString())){
					System.out.println(r.word);
				}
				continue;
			}else if(cmd.equalsIgnoreCase("s")){
				gameState.printScores();
				continue;
			}

			int row;
			int col;
			String word;
			try{
				row = lineScanner.nextInt();
				col = lineScanner.nextInt();
				word = lineScanner.next();
			}catch(InputMismatchException ime){
				System.out.println("Bad Command. Please Try again.");
				continue;
			}catch(NoSuchElementException nsee){
				System.out.println("Bad Command. Please Try again.");
				continue;
			}

			word = word.toUpperCase();

			System.out.println(String.valueOf(row) + " " + String.valueOf(col) + " " + word);

			Board board = gameState.getBoard();
			Tile[] playedTiles = getWordTiles(word);
			if(D.isValidMove(board, dir, row, col, word, playedTiles)){

				try {
					placeWord(board, gameState.getBag(), dir, row, col, playedTiles);
					score += Score.getScore(board, dir, row, col, playedTiles);
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
	
	private void placeWord(Board board, Bag bag, int dir, int row, int col, Tile[] wordTiles) throws InvalidPlacementException{
		board.placeWord(dir, row, col, wordTiles);
		
		for(int i=0; i<wordTiles.length; i++){
			rack.remove(wordTiles[i]);
			if(!bag.isEmpty()) rack.add(bag.drawTile());
		}
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
	
	public String getRackString(){
		String rackString = "";
		for(Tile t: rack){
			rackString += t.getLetter();
		}
		return rackString;
	}



}
