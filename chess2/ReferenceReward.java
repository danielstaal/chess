/*
 * File: ReferenceReward.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This file will eventually implement the game of Chess.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class ReferenceReward extends ConsoleProgram
{
	private Extra extra = new Extra();	
		
	public ReferenceReward()
	{
	}
	
	public boolean checkExploitation(int checkMates, int remis)
	{
		// for simple Exploration exploitation
		double difference = ((double)checkMates+1)/(remis+1);
		int chance = (int)(100*difference);
	
		int rand = extra.randInt(0,chance);
		//System.out.println(rand);
		
		int randCheck = extra.randInt(0,1);
		
		if(checkMates < remis && rand < 50 && randCheck == 1)
		{
			return false;
		}
		return true;
	}
}















