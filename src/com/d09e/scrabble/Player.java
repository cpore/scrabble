package com.d09e.scrabble;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.json.JSONObject;

import com.d09e.scrabble.exception.InvalidPlacementException;
import com.icantrap.collections.dawg.Dawg.Result;

public class Player implements Jsonizable{
	
	public static final String NAME = "name";
	public static final String SCORE = "score";
	public static final String RACK = "rack";
	public static final String IS_HUMAN = "isHuman";

	private String name;
	private int score;

	private Rack rack;

	private boolean isHuman;

	public Player(String name, boolean isHuman){
		this.name = name;
		this.score = 0;
		this.isHuman = isHuman;
		this.rack = new Rack();
	}
	
	// copy ctor
	public Player(String name, boolean isHuman, int score, Rack rack){
		this.name = name;
		this.score = score;
		this.isHuman = isHuman;
		this.rack = rack;
	}
	
	public Player(JSONObject jo){
		this.name = jo.getString(NAME);
		this.score = jo.getInt(SCORE);
		this.rack = new Rack(jo.getJSONObject(RACK));
		this.isHuman = jo.getBoolean(IS_HUMAN);
	}
	
	public Player copy(){
		return new Player(name, isHuman, score, rack.copy());
	}

	@Override
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put(NAME, name);
		jo.put(SCORE, score);
		jo.put(RACK, rack.toJson());
		jo.put(IS_HUMAN, isHuman);
		return jo;
	}

	public int getScore() {
		return score;
	}

	public String getName() {
		return name;
	}
	
	public Rack getRack() {
		return rack;
	}

	public void setScore(int delta) {
		score += delta;
	}

	public void drawTile(Bag bag){
		if(rack.size() > 7){
			throw new IndexOutOfBoundsException();
		}
		rack.addTile(bag.drawTile());
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
			makeMove(gameState);
		}

	}
	
	private void makeMove(GameState gameState){
		Move move = Search.findBestMove(gameState);
		if(move == null){
			System.out.println("NO VALID MOVE!!!!!");
			gameState.printRacks();
			gameState.printScores();
			gameState.printBoard();
			new Scanner(System.in).next();
			return;
		}
		//should already be valid move based on search
		//but just in case...
		if(D.isValidMove(gameState.getBoard().copy(), move)){

			try {
				placeWord(gameState.getBoard(), gameState.getBag(), move);
			} catch (InvalidPlacementException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Searched Move not OK");
			//TODO handle passing
			promptForWord(gameState);
		}
		
	}

	private void promptForWord(GameState gameState) {

		Scanner scanner = new Scanner(System.in);
		boolean moveOk = false;
		while(!moveOk){
			System.out.print(name + ": " + score + " ");
			rack.printRack();
			System.out.println("enter h or v and start square and letters to play(in order)\nor c for hints\nor p for scores\nor press enter to print board:  ");
			String playedMove = scanner.nextLine();
			Scanner lineScanner = new Scanner(playedMove);
			String cmd = null;

			try{
				cmd = lineScanner.next();
			}catch(NoSuchElementException nsee){
				gameState.printRacks();
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
				for(Result r: rack.getPossibleWords("")){
					System.out.println(r.word);
				}
				continue;
			}else if(cmd.equalsIgnoreCase("p")){
				gameState.printScores();
				continue;
			}else if(cmd.equalsIgnoreCase("s")){
				gameState.saveGameState("savestates/savestate");
				continue;
			}else if(cmd.equalsIgnoreCase("f")){
				makeMove(gameState);
				lineScanner.close();
				return;
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
			Move move = new Move(dir, row, col, rack.getWordTiles(word));
			
			if(D.isValidMove(board, move)){

				try {
					placeWord(board, gameState.getBag(), move);
				} catch (InvalidPlacementException e) {
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
	
	private void placeWord(Board board, Bag bag, Move move) throws InvalidPlacementException{
		board.placeWord(move, true);
		
		this.score += move.getScore();
		
		Tile[] wordTiles = move.getWordTiles();
		for(Tile t: wordTiles){
			if(!rack.remove(t)){
				System.out.println("TILE NOT IN RACK: " + t.toString());
				for(Tile tile: wordTiles){
					System.out.println(tile.toString());
				}
				System.exit(0);
			}
			System.out.println("REMOVED: " + t.toString());
		}
		
		for(Tile t: wordTiles){
			if(!bag.isEmpty()) rack.addTile(bag.drawTile());
		}
		
		board.firstMove = false;
	}

	public void setIsHuman(boolean b) {
		isHuman = b;
	}

}
