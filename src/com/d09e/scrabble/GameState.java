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

import com.d09e.scrabble.player.Player;
import com.d09e.scrabble.player.PlayerFactory;
import com.d09e.scrabble.player.RobotPlayer;
import com.d09e.scrabble.stats.StatsCollector;

public class GameState implements Jsonizable{
	//private static final boolean DEBUG = false;
	
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

	public GameState(String p1Name, String p2Name){
		players = new ArrayList<Player>();
		Player p1 = new RobotPlayer(p1Name);
		Player p2 = new RobotPlayer(p2Name);
		players.add(p1);
		players.add(p2);
		
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
			players.add(PlayerFactory.makePlayer(playerArray.getJSONObject(i)));
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
		
		long startTime = System.currentTimeMillis();
		for(int i=0; i<players.size(); i++){
			StatsCollector sc = new StatsCollector(players.get(i).getName());
			sc.start(startTime);
			Scrabble.stats.add(sc);
		}
		
		while(!gameOver()){
			currentPlayer = players.get(playerIdx);
			StatsCollector sc = Scrabble.stats.get(playerIdx);
			playerIdx = (playerIdx + 1) % players.size();
			
			long startMove = System.currentTimeMillis();
			Move move = currentPlayer.getMove(GameState.this);
			
			if(move == null) continue;
			sc.addMove(System.currentTimeMillis()-startMove, move.getScore(), move.getWord().length());
			
		}
		
		long endTime = System.currentTimeMillis();
		for(int i=0; i<players.size(); i++){
			Player p = players.get(i);
			StatsCollector sc = Scrabble.stats.get(i);
			sc.finish(endTime, p.getScore(), isWinner(p));
			//System.out.println(sc.getStats());
		}
		
		
	}
	
	private boolean isWinner(Player p){
		int pScore = p.getScore();
		for(Player other: players){
			if(!other.equals(p) && other.getScore() > pScore) return false;
		}
		return true;
	}

	private boolean gameOver() {
		
		for(Player p: players){
			if((!bag.isEmpty() || !p.getRack().isEmpty()) && allPlayersPass()){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean allPlayersPass(){
		ArrayList<Boolean> passList = new ArrayList<Boolean>();
		for(Player p: players){
			if(p.getPasses() > 1){
				passList.add(true);
			}else{
				passList.add(false);
			}
		}
		return passList.contains(false);
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
