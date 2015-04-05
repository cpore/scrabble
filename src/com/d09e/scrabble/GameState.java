package com.d09e.scrabble;

import java.util.ArrayList;

public class GameState {
	
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
		
		start();
	}
	
	public void printBoard(){
		board.printBoard();
	}
	
	private void start(){
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
		currentPlayer.getMove(board);
		
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
	
}
