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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
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
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
