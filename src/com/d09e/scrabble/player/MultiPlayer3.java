package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class MultiPlayer3 extends Player{

	public MultiPlayer3(String name) {
		super(name);
	}

	public MultiPlayer3(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public MultiPlayer3(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public MultiPlayer3() {
		super("MultiPlayer3");
	}

	@Override
	public Player copy(){
		return new MultiPlayer3(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.MULTI3;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return useQ(gameState, move) + vowelConsonant(gameState, move);
	}

}
