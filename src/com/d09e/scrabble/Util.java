package com.d09e.scrabble;

public class Util {

	public static boolean isStar(int i, int j){
		if(i == 7 && j == 7) return true;
		return false;
	}

	public static boolean isDoubleLetter(int i ,int j){
		if(i == 0 && j == 3) return true;
		if(i == 0 && j == 11) return true;
		if(i == 2 && j == 6) return true;
		if(i == 2 && j == 8) return true;
		if(i == 3 && j == 0) return true;
		if(i == 3 && j == 7) return true;
		if(i == 3 && j == 14) return true;
		if(i == 6 && j == 2) return true;
		if(i == 6 && j == 6) return true;
		if(i == 6 && j == 8) return true;
		if(i == 6 && j == 12) return true;
		if(i == 7 && j == 3) return true;
		if(i == 7 && j == 11) return true;
		if(i == 8 && j == 2) return true;
		if(i == 8 && j == 6) return true;
		if(i == 8 && j == 8) return true;
		if(i == 8 && j == 12) return true;
		if(i == 11 && j == 0) return true;
		if(i == 11 && j == 7) return true;
		if(i == 11 && j == 14) return true;
		if(i == 12 && j == 6) return true;
		if(i == 12 && j == 8) return true;
		if(i == 14 && j == 3) return true;
		if(i == 14 && j == 11) return true;
		return false;
	}

	public static boolean isTripleLetter(int i ,int j){
		if(i == 1 && j == 5) return true;
		if(i == 1 && j == 9) return true;
		if(i == 5 && j == 1) return true;
		if(i == 5 && j == 5) return true;
		if(i == 5 && j == 9) return true;
		if(i == 5 && j == 13) return true;
		if(i == 9 && j == 1) return true;
		if(i == 9 && j == 5) return true;
		if(i == 9 && j == 9) return true;
		if(i == 9 && j == 13) return true;
		if(i == 13 && j == 5) return true;
		if(i == 13 && j == 9) return true;
		return false;
	}

	public static boolean isDoubleWord(int i ,int j){
		if(i == 1 && j == 1) return true;
		if(i == 2 && j == 2) return true;
		if(i == 3 && j == 3) return true;
		if(i == 4 && j == 4) return true;
		if(i == 1 && j == 13) return true;
		if(i == 2 && j == 12) return true;
		if(i == 3 && j == 11) return true;
		if(i == 4 && j == 10) return true;
		if(i == 10 && j == 4) return true;
		if(i == 11 && j == 3) return true;
		if(i == 12 && j == 2) return true;
		if(i == 13 && j == 1) return true;
		if(i == 10 && j == 10) return true;
		if(i == 11 && j == 11) return true;
		if(i == 12 && j == 12) return true;
		if(i == 13 && j == 13) return true;
		return false;
	}

	public static boolean isTripleWord(int i ,int j){
		if(i == 0 && j == 0) return true;
		if(i == 0 && j == 7) return true;
		if(i == 0 && j == 14) return true;
		if(i == 7 && j == 0) return true;
		if(i == 14 && j == 0) return true;
		if(i == 7 && j == 14) return true;
		if(i == 14 && j == 7) return true;
		if(i == 14 && j == 14) return true;
		return false;
	}
}
