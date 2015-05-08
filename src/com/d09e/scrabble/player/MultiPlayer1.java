package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class MultiPlayer1 extends Player{

	public MultiPlayer1(String name) {
		super(name);
	}

	public MultiPlayer1(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public MultiPlayer1(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public MultiPlayer1() {
		super("MultiPlayer1");
	}

	@Override
	public Player copy(){
		return new MultiPlayer1(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.MULTI1;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return useQ(gameState, move) + vowelConsonant(gameState, move)
				+ tileTurnover(gameState, move);
	}

}
