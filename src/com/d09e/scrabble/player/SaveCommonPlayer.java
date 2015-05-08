package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class SaveCommonPlayer extends Player{

	public SaveCommonPlayer(String name) {
		super(name);
	}

	public SaveCommonPlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public SaveCommonPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}
	
	public SaveCommonPlayer() {
		super("SaveCommonPlayer");
	}

	@Override
	public Player copy(){
		return new SaveCommonPlayer(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.SAVE_COMMON;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return saveCommon(gameState, move);
	}

}
