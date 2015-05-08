package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class MultiPlayer4 extends Player{

	public MultiPlayer4(String name) {
		super(name);
	}

	public MultiPlayer4(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public MultiPlayer4(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public MultiPlayer4() {
		super("MultiPlayer4");
	}

	@Override
	public Player copy(){
		return new MultiPlayer4(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.MULTI4;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return useQ(gameState, move)+ tileTurnover(gameState, move);
	}

}
