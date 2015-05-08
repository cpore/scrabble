package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class UseQPlayer extends Player{

	public UseQPlayer(String name) {
		super(name);
	}

	public UseQPlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public UseQPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public UseQPlayer() {
		super("UseQPlayer");
	}

	@Override
	protected int type() {
		return PlayerFactory.USE_Q;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return useQ(gameState, move);
	}

	@Override
	public Player copy() {
		return new UseQPlayer(name, score, rack.copy());
	}

}
