package com.d09e.scrabble;

public class Slot {

	private int row, col;
	private boolean openNorth, openSouth, openWest, openEast;

	public Slot(int row, int col, boolean openNorth, boolean openSouth,
			boolean openWest, boolean openEast) {
		this.row = row;
		this.col = col;
		this.openNorth = openNorth;
		this.openSouth = openSouth;
		this.openWest = openWest;
		this.openEast = openEast;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isOpenNorth() {
		return openNorth;
	}

	public boolean isOpenSouth() {
		return openSouth;
	}

	public boolean isOpenWest() {
		return openWest;
	}

	public boolean isOpenEast() {
		return openEast;
	}
}
