package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class UseBonusSquaresPlayer extends Player{

	public UseBonusSquaresPlayer(String name) {
		super(name);
	}

	public UseBonusSquaresPlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public UseBonusSquaresPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public UseBonusSquaresPlayer() {
		super("UseBonusSquaresPlayer");
	}

	@Override
	public Player copy(){
		return new UseBonusSquaresPlayer(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.USE_BONUS_SQUARES;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return useBonusSquares(gameState, move);
	}

}
