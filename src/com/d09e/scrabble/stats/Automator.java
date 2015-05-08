package com.d09e.scrabble.stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.d09e.scrabble.Scrabble;
import com.d09e.scrabble.player.Player;
import com.d09e.scrabble.player.PlayerFactory;
import com.d09e.scrabble.player.UseQPlayer;

public class Automator {

	private final ArrayList<StatsCollector> p1Stats = new ArrayList<StatsCollector>();
	private final ArrayList<StatsCollector> p2Stats = new ArrayList<StatsCollector>();
	
	public Automator(){ }
	
	public void go(int iterations, int p1Type, int p2Type){
		Player p1 = PlayerFactory.makePlayer(p1Type);
		Player p2 = PlayerFactory.makePlayer(p2Type);
		
		String scenario = p1.getName().replace("Player", "") + "-vs-" + p2.getName().replace("Player", "");
		
		for(int i=1; i<=iterations; i++){
			p1 = PlayerFactory.makePlayer(p1Type);
			p2 = PlayerFactory.makePlayer(p2Type);
			Scrabble.play(p1, p2);
			if(p1 instanceof UseQPlayer && !p1.usedQ) continue;
			p1Stats.add(Scrabble.stats.get(0));
			p2Stats.add(Scrabble.stats.get(1));
		}
		
		PrintWriter pw1 = null;
		PrintWriter pw2 = null;
		try {
			pw1 = new PrintWriter(new FileWriter("stats/" + scenario + "_" + p1.getName(), false));
			pw1.println(StatsCollector.header);
			pw2 = new PrintWriter(new FileWriter("stats/" + scenario + "_" + p2.getName(), false));
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
