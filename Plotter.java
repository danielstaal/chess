
/*
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

public class Plotter extends ConsoleProgram
{
	private double numberOfGames;
	private int writePlotPerNumber; 

	private double[] checkMateArray;
	private double[] staleMateArray;
	private double[] remisArray;

	public Plotter(double n, int w)
	{
		numberOfGames = n;
		writePlotPerNumber = w;
		
		checkMateArray = new double[((int)numberOfGames/writePlotPerNumber)+1];
		staleMateArray = new double[((int)numberOfGames/writePlotPerNumber)+1];
		remisArray = new double[((int)numberOfGames/writePlotPerNumber)+1];
	}
	
	public void plotValues()
	{
		System.out.println("");System.out.println("");
		System.out.println("Checkmates");
		for(int i=0;i<checkMateArray.length;i++)
		{
			System.out.print(i);System.out.print(": ");
			for(int j=0;j<checkMateArray[i];j++)
			{
				System.out.print("+");
			}
			System.out.println("");
		}
		
		System.out.println("Stalemates");
		for(int i=0;i<staleMateArray.length;i++)
		{
			System.out.print(i);System.out.print(": ");
			for(int j=0;j<staleMateArray[i];j++)
			{
				System.out.print("+");
			}
			System.out.println("");
		}
		
		System.out.println("Remis");
		for(int i=0;i<remisArray.length;i++)
		{
			System.out.print(i);System.out.print(": ");
			for(int j=0;j<remisArray[i];j++)
			{
				System.out.print("+");
			}
			System.out.println("");
		}
	}
	
	public void setCheckMateArray(int value, int index)
	{
		checkMateArray[index] = value;
	}
	public void setStaleMateArray(int value, int index)
	{
		staleMateArray[index] = value;
	}
	public void setRemisArray(int value, int index)
	{
		remisArray[index] = value;
	}
}
