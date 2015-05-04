package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.icantrap.collections.dawg.Dawg.Result;

public class Rack implements Jsonizable{
	private static final boolean DEBUG = false;

	public static final String RACK_ARRAY = "rackArray";
	private ArrayList<Tile> rack = new ArrayList<Tile>();

	public Rack(){
		rack = new ArrayList<Tile>();
	}

	public Rack(JSONObject jo){
		this.rack = new ArrayList<Tile>();
		JSONArray rackArray = jo.getJSONArray(RACK_ARRAY);
		for(int i=0; i<rackArray.length(); i++){
			this.rack.add(new Tile(rackArray.getJSONObject(i)));
		}
	}

	public Rack(ArrayList<Tile> rack){
		this.rack = rack;
	}

	public Rack copy(){
		ArrayList<Tile> rackCopy = new ArrayList<Tile>();
		for(Tile t: rack) rackCopy.add(t.copy());
		return new Rack(rackCopy);
	}

	public int size(){
		return rack.size();
	}

	public void addTile(Tile tile) {
		if(DEBUG) System.out.println("ADDING TILE: " + tile.toString());
		if(rack.size() > 7){
			throw new IndexOutOfBoundsException();
		}
		rack.add(tile);
	}

	public ArrayList<Tile> getRackTiles(){
		return rack;
	}

	public void printRack() {
		System.out.print("Rack: ");
		for (Tile tile: rack) {
			System.out.print(tile.getLetter() + " ");
		}
		System.out.println();
	}

	public Tile removeRandomTile(){
		Tile t = rack.get(new Random().nextInt(rack.size()));
		rack.remove(t);
		return t;
	}

	@Override
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		JSONArray rackArray = new JSONArray();
		for(Tile t: rack) rackArray.put(t.toJson());
		jo.put(RACK_ARRAY, rackArray);
		return jo;
	}

	public boolean remove(Tile tile) {
		return rack.remove(tile);
	}

	public Tile[] getWordTiles(String word){
		Tile[] tiles = new Tile[word.length()];
		char[] charArray = word.toCharArray();
		int tileIdx = 0;

		ArrayList<Integer> skipList = new ArrayList<Integer>();

		for(int i=0; i < charArray.length; i++){
			for(int j = 0; j< rack.size(); j++){
				if(!skipList.contains(j) && rack.get(j).getLetter() == charArray[i]){
					tiles[tileIdx++] = rack.get(j);
					skipList.add(j);
					if(tileIdx == tiles.length) return tiles;
					break;
				}
			}

		}

		return tiles;
	}

	/*public Tile[] getWordTiles(String wordWithWC, String word){
		Tile[] tiles = new Tile[wordWithWC.length()];
		int tileIdx = 0;
		char[] wordArray = wordWithWC.toCharArray();

		ArrayList<Integer> skipList = new ArrayList<Integer>();
		
		for(int i=0; i<wordArray.length; i++ ){
			for(int j = 0; j< rack.size(); j++){
				if(rack.get(j).getLetter() == wordArray[i] && !skipList.contains(j)){
					if(wordArray[i] == '?') rack.get(j).setPlacedLetter(word.charAt(i));
					tiles[tileIdx++] = rack.get(j);
					skipList.add(j);
					if(tileIdx == tiles.length) return tiles;
					break;
				}
			}
		}
		return tiles;
	}*/
/*
	public void setPlacedTiles(String wordWithWC, String word){
		char[] wordArray = wordWithWC.toCharArray();
		for( int i=0; i<wordArray.length; i++ ){
			for(Tile t: rack){
				if(t.getLetter() == wordArray[i]){
					if(wordArray[i] == '?') t.setPlacedLetter(word.charAt(i));
					break;
				}
			}
		}
	}*/

	/*	public boolean rackContains(String word){
		for(char c: word.toCharArray()){
			if(!rackContains(c)) return false;
		}
		return true;
	}

	private boolean rackContains(char c){
		for(Tile t: rack){
			if(t.getLetter() == c){
				return true;
			}
		}
		return false;
	}*/

	public String getRackString(){
		String rackString = "";
		for(Tile t: rack){
			rackString += t.getLetter();
		}
		return rackString;
	}

	public Result[] getPossibleWords(String extras){
		String searchString = getRackString();
		if(extras != null){
			searchString += extras;
		}
		return Scrabble.getSubwords(searchString);
	}

	public boolean isEmpty() {
		return rack.isEmpty();
	}
}
