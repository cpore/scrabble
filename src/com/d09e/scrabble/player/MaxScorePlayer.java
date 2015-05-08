package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class MaxScorePlayer extends Player{

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

	public MaxScorePlayer() {
		super("MaxScorePlayer");
	}

	@Override
	public Player copy(){
		return new MaxScorePlayer(name, score, rack.copy());
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
