package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Bag {
	
	private ArrayList<Tile> tiles;
	
	public Bag(){
		tiles = new ArrayList<Tile>();
		int id = 0;
		// initialize and add tiles to bag
		for(int i=0; i<12; i++){
			tiles.add(new Tile(id++, 'E', 1));
		}
		
		for(int i=0; i<9; i++){
			tiles.add(new Tile(id++, 'A', 1));
			tiles.add(new Tile(id++, 'I', 1));
		}
			
		for(int i=0; i<8; i++){
			tiles.add(new Tile(id++, 'O', 1));
		}
		
		for(int i=0; i<6; i++){
			tiles.add(new Tile(id++, 'N', 1));
			tiles.add(new Tile(id++, 'R', 1));
			tiles.add(new Tile(id++, 'T', 1));
		}
			
		for(int i=0; i<4; i++){
			tiles.add(new Tile(id++, 'L', 1));
			tiles.add(new Tile(id++, 'S', 1));
			tiles.add(new Tile(id++, 'U', 1));
			tiles.add(new Tile(id++, 'D', 2));
		}
		
		for(int i=0; i<3; i++){
			tiles.add(new Tile(id++, 'G', 2));
		}
		
		for(int i=0; i<2; i++){
			tiles.add(new Tile(id++, ' ', 0));
			tiles.add(new Tile(id++, ' ', 0));
			tiles.add(new Tile(id++, 'B', 3));
			tiles.add(new Tile(id++, 'C', 3));
			tiles.add(new Tile(id++, 'M', 3));
			tiles.add(new Tile(id++, 'P', 3));
			tiles.add(new Tile(id++, 'F', 4));
			tiles.add(new Tile(id++, 'H', 4));
			tiles.add(new Tile(id++, 'V', 4));
			tiles.add(new Tile(id++, 'W', 4));
			tiles.add(new Tile(id++, 'Y', 4));
		}
	
		tiles.add(new Tile(id++, 'K', 5));
		tiles.add(new Tile(id++, 'J', 8));
		tiles.add(new Tile(id++, 'X', 8));
		tiles.add(new Tile(id++, 'Q', 10));
		tiles.add(new Tile(id++, 'Z', 10));

		// shuffle the bag for good measure
		Collections.shuffle(tiles);
	}
	
	public Tile drawTile(){
		return tiles.remove(new Random().nextInt(tiles.size()));
	}
	
	/**
	 * Given a board (state) remove tiles from the bag that are already on the board.
	 * @param board
	 */
	public void adjustBag(Board board){
		
	}
	
}
