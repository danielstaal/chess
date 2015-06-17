/*
 * File: WriteToFile.java
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


public class WriteToFile extends ConsoleProgram
{

	/* To distinguish datafiles */
	private static int numberOfDataFile = 1;
	
	ArrayList<Double> parVector;
	
	public WriteToFile(ArrayList<Double> pV)
	{
		parVector = pV;
	}

	public void writeFileParVector()
	{
		// FEATURE: num pos squares
		double[] writeParVector = new double[parVector.size()+1];
		for(int i=0; i<parVector.size();i++)
		{
			writeParVector[i] = parVector.get(i);
		}
		fileWriting(writeParVector);
	}
	
	// to write the parameter vector to a file
	private void fileWriting(double[] content)
	{
		String fileName = "testData" + numberOfDataFile + ".txt";
		// to increment the name of the datafile
		numberOfDataFile++;
		
		String output = "";
		for(int i=0;i<content.length;i++)
		{
			output += content[i] + ",";
		}
		
		try {
 
			File file = new File("/home/jharvard/java/chess/data/" + fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(output);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// to read in the parameter vector (weights) from an earlier test
	public void readFile(int index)
	{
		try {
				String inFile = "data/testData" + index + ".txt";
		
		        BufferedReader in = new BufferedReader(new FileReader(inFile));
		        String str;
		        str = in.readLine();
		        
		        String[] ar=str.split(",");
		        
		        for(int i=0;i<parVector.size();i++)
		        {
		        	parVector.set(i, Double.parseDouble(ar[i])); 
		        	System.out.println(parVector.get(i));
		      	}
		        
		        in.close();
		}
		catch (IOException e)
		{
		        System.out.println("File Read Error");
		}
	}
}
