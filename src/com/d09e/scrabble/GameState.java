package com.d09e.scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameState implements Jsonizable{
	public static final String BOARD = "board";
	public static final String BAG = "bag";
	public static final String PLAYERS = "players";
	public static final String CURRENT_PLAYER = "currentPlayer";

	private Bag bag;
	private Board board;

	public static boolean gameOver = false;

	private ArrayList<Player> players;

	private Player currentPlayer;
	private int playerIdx;

	public GameState(){
		players = new ArrayList<Player>();
		players.add(new Player("ROBOT Player 1", false));
		players.add(new Player("ROBOT Player 2", false));
		bag = new Bag();
		board = new Board();
		
		playerIdx = 0;

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
		playerIdx = players.indexOf(currentPlayer);
	}

	public GameState(Bag bag, Board board, ArrayList<Player> players, Player currentPlayer){
		this.bag = bag;
		this.board = board;
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.playerIdx = players.indexOf(currentPlayer);
	}

	public GameState copy(){
		ArrayList<Player> playersCopy = new ArrayList<Player>();
		for(Player p: players) playersCopy.add(p.copy());
		return new GameState(bag.copy(), board.copy(), playersCopy, currentPlayer.copy());
	}

	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	public void setHuman(){
		for(Player p: players)
			p.setIsHuman(true);
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
			scores.append(p.getName() + ": " + p.getScore() + ", ");
		}
		System.out.println(scores.substring(0, scores.length()-2));
		;		
	}

	public void start(){
	
		while(!gameOver()){
			currentPlayer = players.get(playerIdx);
			currentPlayer.getMove(GameState.this);
			
			playerIdx = (playerIdx + 1) % players.size();
		}
	}

	private boolean gameOver() {
		
		for(Player p: players){
			if(!bag.isEmpty() || !p.getRack().isEmpty()){
				return false;
			}
		}
		return true;
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
		File stateFile  = new File(saveFile + ".state");
		int ctr = 0;
		while(stateFile.exists()){
			saveFile += String.valueOf(ctr++);
			stateFile = new File(saveFile + ".state");
		}

		try {
			FileWriter fw = new FileWriter(stateFile);
			fw.write(toJson().toString(2));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static GameState loadGameState(String stateFile) throws JSONException, FileNotFoundException, IOException{
		JSONObject gameState = new JSONObject(IOUtils.toString(new FileReader(stateFile)));
		return new GameState(gameState);
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

	public void printRacks() {
		for(Player p: players){
			System.out.print(p.getName() + " ");
			p.getRack().printRack();
		}

	}

}
