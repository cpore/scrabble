package com.d09e.scrabble;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

public class Slot{

	private int dir;
	private int row;
	private int col;
	private int start = 0;
	private int end = 0;
	private PathTile anchor;
	private ArrayList<PathTile> pathTiles = new ArrayList<PathTile>();

	public Slot(int dir, int row, int col, Tile tile){
		this.dir = dir;
		this.row = row;
		this.col = col;
		this.anchor = new PathTile(tile, row, col);
	}
	
	public boolean hasPath(){
		return end - start > 0;
		
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Tile getAnchorTile() {
		return anchor.tile;
	}

	public int getDir() {
		return dir;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public void addNode(Tile t, int row, int col){
		pathTiles.add(new PathTile(t, row, col));
	}
	
	public String getPathLetters(Rack rack){
		String pathString = rack.getRackString();
		pathString += anchor.tile.getPlacedLetter();
		for(PathTile n: pathTiles){
			pathString += n.tile.getPlacedLetter();
		}
		return pathString;
	}
	
	public int[] getNodeDistances(int maxDistance){
		ArrayList<Integer> ind = new ArrayList<Integer>();
		for(int i=0; i< pathTiles.size(); i++){
			int distance = pathTiles.get(i).distanceFromAnchor(dir, dir == D.HORIZONTAL ? col : row);
			if(distance > maxDistance) continue;
			ind.add(distance);
		}
		
		return ArrayUtils.toPrimitive(ind.toArray(new Integer[0]));
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anchor == null) ? 0 : anchor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slot other = (Slot) obj;
		if (anchor == null) {
			if (other.anchor != null)
				return false;
		} else if (!anchor.tile.equals(other.anchor.tile))
			return false;
		return true;
	}



	private class PathTile{
		
		public Tile tile;
		public int row;
		public int col;
		
		public PathTile(Tile t, int row, int col){
			this.tile = t;
			this.row = row;
			this.col = col;
		}
		
		public int distanceFromAnchor(int dir, int origin){
			if(dir == D.HORIZONTAL){
				return Math.abs(origin - col);
			}else{
				return Math.abs(origin - row);
			}
		}
		
	}

}
