package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class TileTurnoverPlayer extends Player{

	public TileTurnoverPlayer(String name) {
		super(name);
	}

	public TileTurnoverPlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public TileTurnoverPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public TileTurnoverPlayer() {
		super("TileTurnoverPlayer");
	}

	@Override
	public Player copy(){
		return new TileTurnoverPlayer(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.TILE_TURNOVER;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return tileTurnover(gameState, move);
	}

}
