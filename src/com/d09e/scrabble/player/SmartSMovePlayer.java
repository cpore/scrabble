package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class SmartSMovePlayer extends Player{

	public SmartSMovePlayer(String name) {
		super(name);
	}

	public SmartSMovePlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public SmartSMovePlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public SmartSMovePlayer() {
		super("SmartSMovePlayer");
	}

	@Override
	public Player copy(){
		return new SmartSMovePlayer(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.SMART_S_MOVE;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return smartSMove(gameState, move);
	}

}
