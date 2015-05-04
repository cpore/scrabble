package com.d09e.scrabble.stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.d09e.scrabble.Scrabble;
import com.d09e.scrabble.player.MaxScorePlayer;
import com.d09e.scrabble.player.Player;
import com.d09e.scrabble.player.TileTurnoverPlayer;
import com.d09e.scrabble.player.UseQPlayer;

public class Automator {

	private static final ArrayList<StatsCollector> p1Stats = new ArrayList<StatsCollector>();
	private static final ArrayList<StatsCollector> p2Stats = new ArrayList<StatsCollector>();
	public static String scenario;
	
	public Automator(String scenario){
		Automator.scenario = scenario;
	}
	
	public void go(int iterations){
		String p1Name = "TileTurnoverPlayer";
		String p2Name = "MaxScorePlayer";
		
		for(int i=1; i<=iterations; i++){
			Player p1 = new TileTurnoverPlayer(p1Name);
			Player p2 = new MaxScorePlayer(p2Name);
			Scrabble.play(p1, p2);
			if(p1 instanceof UseQPlayer && !p1.usedQ) continue;
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
