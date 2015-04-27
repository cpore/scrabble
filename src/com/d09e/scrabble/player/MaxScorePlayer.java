package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.D;
import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;
import com.d09e.scrabble.Search;
import com.d09e.scrabble.exception.InvalidPlacementException;

public class MaxScorePlayer extends Player implements Evaluator{

	private static final boolean DEBUG = false;

	public MaxScorePlayer(String name){
		super(name);
	}

	// copy ctor
	public MaxScorePlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public MaxScorePlayer(JSONObject jo){
		super(jo);
	}

	@Override
	public Player copy(){
		return new MaxScorePlayer(name, score, rack.copy());
	}

	@Override
	public Move getMove(GameState gameState){
		Move move = Search.findBestMove(gameState);
		// TODO handle no moves found
		if(move == null){
			if(DEBUG){ 
				System.out.println("NO VALID MOVE!!!!!");
				gameState.printRacks();
				gameState.printScores();
				gameState.printBoard();
			}
			handleNoMove(gameState);
			return move;
		}
		//should already be valid move based on search
		//but just in case...
		if(D.isValidMove(gameState.getBoard().copy(), move, null)){

			try {
				placeWord(gameState, move);
				passes = 0;
			} catch (InvalidPlacementException e) {
				e.printStackTrace();
			}
		}else{
			if(DEBUG) System.out.println("Searched Move not OK");
			//TODO handle passing

		}
		
		return move;
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
		return PlayerFactory.MAX_SCORE;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return move.getUtility();
	}
}
