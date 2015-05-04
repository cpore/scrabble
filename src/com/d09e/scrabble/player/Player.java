package com.d09e.scrabble.player;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import com.d09e.scrabble.Bag;
import com.d09e.scrabble.Board;
import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Jsonizable;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;
import com.d09e.scrabble.Tile;
import com.d09e.scrabble.exception.InvalidPlacementException;

public abstract class Player implements Jsonizable, Evaluator{
	private static final boolean DEBUG = false;

	public static final String NAME = "name";
	public static final String SCORE = "score";
	public static final String RACK = "rack";
	public static final String PASSES = "passes";
	public static final String TYPE = "type";

	protected int passes = 0;

	protected String name;
	protected int score;
	protected Rack rack;

	//heuristc variables
	public boolean usedQ = false;

	public Player(String name){
		this.name = name;
		this.score = 0;
		this.rack = new Rack();
	}

	// copy ctor
	public Player(String name, int score, Rack rack){
		this.name = name;
		this.score = score;
		this.rack = rack;
	}

	public Player(JSONObject jo){
		this.name = jo.getString(NAME);
		this.score = jo.getInt(SCORE);
		this.rack = new Rack(jo.getJSONObject(RACK));
	}

	public abstract Player copy();

	protected abstract int type();

	public abstract Move getMove(GameState gameState);

	protected abstract void handleNoMove(GameState gameState);

	protected abstract boolean swapTiles(GameState gameState);

	@Override
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put(NAME, name);
		jo.put(SCORE, score);
		jo.put(RACK, rack.toJson());
		jo.put(TYPE, type());
		return jo;
	}

	public int getScore() {
		return score;
	}

	public String getName() {
		return name;
	}

	public Rack getRack() {
		return rack;
	}

	public int getPasses(){
		return passes;
	}

	public void setScore(int delta) {
		score += delta;
	}

	public void drawTile(Bag bag){
		if(rack.size() > 7){
			throw new IndexOutOfBoundsException();
		}
		rack.addTile(bag.drawTile());
	}


	public int getNumberOfLetters() {
		return rack.size();
	}

	protected void placeWord(GameState gameState, Move move) throws InvalidPlacementException{
		Board board = gameState.getBoard();
		Bag bag = gameState.getBag();
		board.placeWord(move, true);

		this.score += move.getScore();

		Tile[] wordTiles = move.getWordTiles();
		for(Tile t: wordTiles){
			if(!rack.remove(t)){
				if(DEBUG){ 
					System.out.println("TILE NOT IN RACK: " + t.toString());
					for(Tile tile: wordTiles){
						System.out.println(tile.toString());
					}
					System.exit(0);
				}
			}else{
				if(DEBUG) System.out.println("REMOVED: " + t.toString());
				if(!bag.isEmpty()) rack.addTile(bag.drawTile());
			}

		}

		System.out.println(gameState.getCurrentPlayer().getName() + " placed:" + move.getWord());

		board.firstMove = false;
	}

	protected boolean swapTilesTemp(GameState gameState){
		Bag bag = gameState.getBag();
		Rack rack = gameState.getCurrentPlayer().getRack();

		// can't swap if there are less than 7 tiles left in bag
		if(bag.size() < 7){
			return false;
		}

		int succesfulSwaps = 0;
		// swap a random number of tiles
		int rand = new Random().nextInt(rack.size());

		for(int i=0; i<rand; i++){

			Tile bagTile = bag.drawTile();

			Tile rackTile = rack.removeRandomTile();

			if(rackTile.getLetter() == '?'){
				continue;
			}else{
				rack.addTile(bagTile);
				succesfulSwaps++;
			}
		}

		return succesfulSwaps > 0;
	}

	// Heuristics
	protected float useQ(GameState gameState, Move move) {
		if(move.getTileString().contains("Q")){
			usedQ = true;
			return 200f;
		}
		return 0f;
	}

	protected float uWithQUnseen(GameState gameState, Move move){
		if(move.getTileString().contains("U") && move.getTileString().contains("Q")){
			usedQ = true;
			return 200f;
		}

		if(gameState.getBoard().qUnseen()
				&& move.getTileString().contains("U")){
			return -12f;
		}
		return 0f;
	}

	protected float saveCommon(GameState gameState, Move move){
		float delta = 0f;
		Rack rackCopy = gameState.getCurrentPlayer().getRack().copy();
		for(Tile t: move.getWordTiles()){
			rackCopy.remove(t);
		}
		for(Tile t: rackCopy.getRackTiles()){
			if(t.getLetter() == 'A'){
				delta += 5f;
			}else if(t.getLetter() == 'E'){
				delta += 5f;
			}else if(t.getLetter() == 'I'){
				delta += 5f;
			}else if(t.getLetter() == 'N'){
				delta += 5f;
			}else if(t.getLetter() == 'R'){
				delta += 5f;
			}else if(t.getLetter() == 'S'){
				delta += 5f;
			}
		}

		return delta;
	}

	protected float smartSMove(GameState gameState, Move move){
		return 0;
	}

	protected float useBonusSquares(GameState gameState, Move move){

		return 0f;
	}

	protected float tileTurnover(GameState gameState, Move move){
		float averageTileValue = getAverageTileValue(gameState.getBoard().getTiles());
		float delta = 0f;
		float rackValue = 0f;

		Rack rackCopy = gameState.getCurrentPlayer().getRack().copy();

		for(Tile t: rackCopy.getRackTiles()){
			rackValue += t.getValue();
		}

		for(Tile t: move.getWordTiles()){
			rackCopy.remove(t);
		}
		for(Tile t: rackCopy.getRackTiles()){
			delta += t.getValue();
		}
		if(gameState.getBag().size() >= 7){
			for(int i=0; i<7-rackCopy.getRackTiles().size(); i++){
				delta += averageTileValue;
			}
		}

		return delta > rackValue ? delta : rackValue;

	}

	private float getAverageTileValue(ArrayList<Tile> boardTiles){
		Bag bag = new Bag();
		bag.removeTiles(boardTiles);

		float total = 0f;
		for(Tile t: bag.getTiles()){
			total += t.getValue();
		}

		return total/bag.getTiles().size();
	}
}