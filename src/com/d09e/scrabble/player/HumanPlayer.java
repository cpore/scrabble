package com.d09e.scrabble.player;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.json.JSONObject;

import com.d09e.scrabble.D;
import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;
import com.d09e.scrabble.Search;
import com.d09e.scrabble.exception.InvalidPlacementException;
import com.icantrap.collections.dawg.Dawg.Result;

public class HumanPlayer extends Player{

	private static final boolean DEBUG = false;

	public HumanPlayer(String name){
		super(name);
	}

	// copy ctor
	public HumanPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public HumanPlayer(JSONObject jo){
		super(jo);
	}

	public HumanPlayer() {
		super("HumanPlayer");
	}

	@Override
	public Player copy(){
		return new HumanPlayer(name, score, rack.copy());
	}

	@Override
	public Move getMove(GameState gameState) {
		return promptForWord(gameState);
	}

	private Move promptForWord(GameState gameState) {

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		boolean moveOk = false;
		Move move = null;
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
				move = Search.findBestMove(gameState);
				if(D.isValidMove(gameState.getBoard(), move)){

					try {
						placeWord(gameState, move);
					} catch (InvalidPlacementException e) {
						e.printStackTrace();
						continue;
					}
					moveOk = true;
					lineScanner.close();
					return move;
				}else{
					System.out.println("Move not OK");
					continue;
				}
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

			if(DEBUG) System.out.println(String.valueOf(row) + " " + String.valueOf(col) + " " + word);

			move = new Move(dir, row, col, rack.getWordTiles(word));

			if(D.isValidMove(gameState.getBoard(), move)){

				try {
					placeWord(gameState, move);
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
		return move;
		//scanner.close();
	}

	protected void handleNoMove(GameState gameState){
		if(swapTiles(gameState)){
			return;
		}
		
		// handle case where a swap is not possible
		// and a round of not swaps mean the game is over
		passes += 1;
	}

	protected boolean swapTiles(GameState gameState){
		return swapTilesTemp(gameState);

	}

	@Override
	protected int type() {
		return PlayerFactory.HUMAN;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return 0;
	}

}
