package com.d09e.scrabble;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameState implements Jsonizable{
	public static final String BOARD = "board";
	public static final String BAG = "bag";
	public static final String PLAYERS = "players";
	public static final String CURRENT_PLAYER = "currentPlayer";
	
	private Bag bag;
	private Board board;
	
	private ArrayList<Player> players;
	
	private Player currentPlayer;
	
	public GameState(){
		players = new ArrayList<Player>();
		players.add(new Player("Player 1", true));
		players.add(new Player("Player 2", true));
		bag = new Bag();
		board = new Board();
		
		playersDrawTiles();
	}
	
	public GameState(JSONObject jo){
		bag = new Bag(jo.getJSONObject(BAG));
		board = new Board(jo.getJSONObject(BOARD));
		JSONArray playerArray = jo.getJSONArray(PLAYERS);
		players = new ArrayList<Player>();
		for(int i=0; i<playerArray.length(); i++){
			players.add(new Player(playerArray.getJSONObject(i)));
		}
		currentPlayer = players.get(jo.getInt(CURRENT_PLAYER));
	}
	
	
	public Board getBoard(){
		return board;
	}
	
	public Bag getBag(){
		return bag;
	}
	
	public void printBoard(){
		board.printBoard();
	}
	
	public void printScores(){
		StringBuilder scores = new StringBuilder();
		for(Player p: players){
			scores.append(p.getNamr() + ": " + p.getScore() + ", ");
		}
		System.out.println(scores.substring(0, scores.length()-2));
;		
	}
	
	public void start(){
		int i = 0;
		while(!gameOver()){
			currentPlayer = players.get(i);
			
			getPlayerInput();
			
			
			if(i == players.size()-1){
				i = 0;
				continue;
			}
			i++;
		}
	}
	
	private void getPlayerInput() {
		currentPlayer.getMove(GameState.this);
		
	}

	private boolean gameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Draw 7 tiles for each player
	 */
	private void playersDrawTiles(){
		for(Player player: players){
			for(int i=0; i<7; i++){
				player.drawTile(bag);
			}
		}
	}
	
	public void saveGameState(String saveFile){
		File stateFile  = new File(saveFile);
		try {
			FileWriter fw = new FileWriter(stateFile);
			fw.write(toJson().toString(2));
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put(BOARD, board.toJson());
		jo.put(BAG,  bag.toJson());
		JSONArray playerArray = new JSONArray();
		for(Player p: players) playerArray.put(p.toJson());
		jo.put(PLAYERS, playerArray);
		jo.put(CURRENT_PLAYER, players.indexOf(currentPlayer));
		return jo;
	}
	
}
