package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class VowelConsonantPlayer extends Player{

	public VowelConsonantPlayer(String name) {
		super(name);
	}

	public VowelConsonantPlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public VowelConsonantPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public VowelConsonantPlayer() {
		super("VowelConsonantPlayer");
	}

	@Override
	public Player copy(){
		return new VowelConsonantPlayer(name, score, rack.copy());
	}

	@Override
	protected int type() {
		return PlayerFactory.VOWEL_CONSONANT;
	}

	@Override
	public float getUtility(GameState gameState, Move move) {
		return vowelConsonant(gameState, move);
	}

}
