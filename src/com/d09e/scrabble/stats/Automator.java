package com.d09e.scrabble.stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.d09e.scrabble.Scrabble;

public class Automator {

	private static final int ITERATIONS = 100;
	private static final ArrayList<StatsCollector> p1Stats = new ArrayList<StatsCollector>();
	private static final ArrayList<StatsCollector> p2Stats = new ArrayList<StatsCollector>();
	
	private static String p1Name = "ROBOT 1";
	private static String p2Name = "ROBOT 2";
	public static String scenario = "exhaustive-search";
	private Automator(){
		
	}
	
	public static void go(){
		for(int i=1; i<=ITERATIONS; i++){
			Scrabble.play(p1Name, p2Name);
			p1Stats.add(Scrabble.stats.get(0));
			p2Stats.add(Scrabble.stats.get(1));
		}
		
		PrintWriter pw1 = null;
		PrintWriter pw2 = null;
		try {
			pw1 = new PrintWriter(new FileWriter("stats/" + scenario + "_" + p1Name, false));
			pw1.println(StatsCollector.header);
			pw2 = new PrintWriter(new FileWriter("stats/" + scenario + "_" + p2Name, false));
			pw2.println(StatsCollector.header);
			for(StatsCollector sc: p1Stats){
				//System.out.println(sc.getStats());
				pw1.println(sc.getStats());
			}
			for(StatsCollector sc: p2Stats){
				//System.out.println(sc.getStats());
				pw2.println(sc.getStats());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			pw1.close();
			pw2.close();
		}
		
		
	}
}
