package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class MultiPlayer2 extends Player{

	public MultiPlayer2(String name) {
		super(name);
	}

	public MultiPlayer2(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public MultiPlayer2(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public MultiPlayer2() {
		super("MultiPlayer2");
	}

	@Override
	public Player copy(){
		return new MultiPlayer2(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.MULTI2;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return vowelConsonant(gameState, move)
				+ tileTurnover(gameState, move);
	}

}
