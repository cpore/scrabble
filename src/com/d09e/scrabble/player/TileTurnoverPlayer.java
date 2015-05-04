package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public class TileTurnoverPlayer extends UseQPlayer{
	
	public TileTurnoverPlayer(String name) {
		super(name);
	}
	
	public TileTurnoverPlayer(JSONObject jo) {
		super(jo);
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
