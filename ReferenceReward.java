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
	private double refReward = 0.1;
	
	private double alpha = 0.1;
	
	public ReferenceReward()
	{
	}
	
	public boolean checkExploitation(double reward)
	{
		double tempRefReward = refReward;
		
		updateRefReward(refReward + alpha * (reward-refReward));
		
		if(tempRefReward > reward)
		{
			return false;
		}	
		return true;
	}
	
	private void updateRefReward(double newRefReward)
	{
		refReward = newRefReward;
	}


}







