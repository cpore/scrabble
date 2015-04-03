package com.d09e.scrabble;

import java.util.ArrayList;

public class Game {
	private Bag bag;
	private Board board;
	
	private ArrayList<Player> players;
	
	private Player currentPlayer;
	
	public Game(){
		players = new ArrayList<Player>();
		currentPlayer = new Player("Player 1", true);
		players.add(currentPlayer);
		players.add(new Player("Player 2", true));
		bag = new Bag();
		board = new Board();
		
		playersDrawTiles();
	}
	
	public void printBoard(){
		board.printBoard();
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
}
